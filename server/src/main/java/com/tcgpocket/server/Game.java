package com.tcgpocket.server;

import com.tcgpocket.energy.Type;
import com.tcgpocket.engine.OpeningPlacement;
import com.tcgpocket.engine.TurnEngine;
import com.tcgpocket.player.Decision;
import com.tcgpocket.player.IPlayer;
import com.tcgpocket.pool.CardPool;
import com.tcgpocket.resolve.RandomSource;
import com.tcgpocket.state.Battle;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.Zone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * One game between two browsers, or one browser and a bot.
 *
 * <p>Before every question it sends each player their own
 * {@link BoardView}, so the player who is waiting still sees the board change.
 * Setup is the one time both players are asked at once; see {@link #placeTogether}.
 */
final class Game {

    private static final Logger LOG = LoggerFactory.getLogger(Game.class);

    // The decks dealt to a player who is signed out or has none selected (and always to the bot).
    static final List<String> LIGHTNING_DECK = List.of(
            "A1-094", "A1-094", "A1-095", "A1-095", "A1-096", "A1-096", "A1-097", "A1-097", "A1-098", "A1-098",
            "A1-103", "A1-103", "A1-226", "A1-226", "P-A-001", "P-A-001", "P-A-005", "P-A-005", "P-A-007", "P-A-007");
    static final List<String> FIRE_DECK = List.of(
            "A1-033", "A1-033", "A1-034", "A1-034", "A1-036", "A1-036", "A1-039", "A1-039", "A1-040", "A1-040",
            "A1-046", "A1-046", "A1-221", "A1-221", "P-A-001", "P-A-001", "P-A-005", "P-A-005", "P-A-007", "P-A-007");

    /** {@code flips} are the coins flipped since the last board, in order, {@code true} for heads. */
    record StateMessage(String type, BoardView board, List<Boolean> flips) {
        StateMessage(BoardView board, List<Boolean> flips) {
            this("state", board, flips);
        }
    }

    record OverMessage(String type, BoardView board, List<Boolean> flips, String result) {
        OverMessage(BoardView board, List<Boolean> flips, String result) {
            this("over", board, flips, result);
        }
    }

    /**
     * What a player chose, sent to everyone else so their log shows the other side's moves. Worded for the
     * receiver ("your Pikachu"); {@code yourTurn} is too, so the log knows whose turn it falls in. {@code kind},
     * {@code card} and {@code target} are the choice's {@link OptionView} fields, so the browser can show the move
     * on the board; a chosen card's id is left out with its name. {@code shown} is the {@link OptionView}'s too.
     */
    record LogMessage(String type, int turn, boolean yourTurn, String text, String kind, Integer card, Integer target,
                      String shown) {
        LogMessage(int turn, boolean yourTurn, String text, String kind, Integer card, Integer target, String shown) {
            this("log", turn, yourTurn, text, kind, card, target, shown);
        }
    }

    private record Seat(IPlayer player, Side side, Consumer<Object> send, BoardView.Cosmetics cosmetics) {
    }

    private final List<Seat> seats;
    private final Battle battle;
    private final Flips flips;
    private int nextInstanceId;

    /** A deck to deal: printed card ids, the energy types its Energy Zone generates, and how it looks. */
    record Deal(List<String> cards, Set<Type> energy, BoardView.Cosmetics cosmetics) {
        static final Deal LIGHTNING = new Deal(LIGHTNING_DECK, Set.of(Type.LIGHTNING), BoardView.Cosmetics.DEFAULT);
        static final Deal FIRE = new Deal(FIRE_DECK, Set.of(Type.FIRE), BoardView.Cosmetics.DEFAULT);
    }

    /** Decks are not checked here; {@code TurnEngine.dealOpeningHands} rejects an illegal one. */
    Game(Consumer<Object> sendFirst, Deal firstDeck, Consumer<Object> sendSecond, Deal secondDeck, RandomSource rng) {
        this(sendFirst, firstDeck, sendSecond, secondDeck, null, rng);
    }

    /** One browser, playing {@code deck}, against {@code bot} with {@link Deal#FIRE}; the bot is sent nothing. */
    Game(Consumer<Object> send, Deal deck, IPlayer bot, RandomSource rng) {
        this(send, deck, message -> { }, Deal.FIRE, bot, rng);
    }

    private Game(Consumer<Object> sendFirst, Deal firstDeck, Consumer<Object> sendSecond, Deal secondDeck,
                 IPlayer bot, RandomSource rng) {
        RemotePlayer first = new RemotePlayer("Player 1", sendFirst, this::broadcast);
        IPlayer second = bot != null ? bot : new RemotePlayer("Player 2", sendSecond, this::broadcast);
        Side firstSide = deal(new Side(first.name(), new Logged(first)), firstDeck);
        Side secondSide = deal(new Side(second.name(), new Logged(second)), secondDeck);

        this.seats = List.of(new Seat(first, firstSide, sendFirst, firstDeck.cosmetics()),
                new Seat(second, secondSide, sendSecond, secondDeck.cosmetics()));
        this.flips = new Flips(rng);
        this.battle = Battle.flipForFirst(firstSide, secondSide, flips);
        flips.take(); // who goes first is not shown as a coin
    }

    /** The browsers, first player first; a bot is not one. */
    List<RemotePlayer> players() {
        return seats.stream().map(Seat::player)
                .filter(RemotePlayer.class::isInstance).map(RemotePlayer.class::cast).toList();
    }

    /**
     * Plays to the end, then tells both players the result. Never throws.
     *
     * @return the first player's score (1 win, 0.5 tie, 0 loss), or empty when the game did not finish
     */
    OptionalDouble run() {
        String result;
        OptionalDouble score = OptionalDouble.empty();
        try {
            TurnEngine engine = new TurnEngine(battle);
            engine.dealOpeningHands();
            placeTogether(engine);
            Optional<Side> winner = engine.playOut();
            result = winner.map(side -> side.name() + " wins").orElse("Tie");
            score = OptionalDouble.of(winner.map(side -> side == seats.getFirst().side() ? 1.0 : 0.0).orElse(0.5));
        } catch (RemotePlayer.Left e) {
            result = "A player left";
        } catch (RuntimeException e) {
            LOG.error("game crashed", e);
            result = "The game crashed";
        }
        List<Boolean> flipped = flips.take();
        for (Seat seat : seats) {
            seat.send().accept(new OverMessage(view(seat), flipped, result));
        }
        return score;
    }

    /**
     * Asks both players for their opening board at the same time, as Pocket does, and places neither
     * until both have confirmed, so neither sees the other's before choosing their own.
     *
     * <p>The two questions wait on their own threads, but only this (the game) thread changes the board.
     */
    private void placeTogether(TurnEngine engine) {
        List<Decision<OpeningPlacement>> decisions = seats.stream()
                .map(seat -> engine.openingDecision(seat.side()))
                .toList();
        List<OpeningPlacement> placements = new ArrayList<>();
        try (ExecutorService asking = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<OpeningPlacement>> answers = new ArrayList<>();
            for (int i = 0; i < seats.size(); i++) {
                IPlayer player = seats.get(i).player();
                Decision<OpeningPlacement> decision = decisions.get(i);
                answers.add(asking.submit(() -> player.choose(decision)));
            }
            for (Future<OpeningPlacement> answer : answers) {
                placements.add(join(answer));
            }
        }
        for (int i = 0; i < seats.size(); i++) {
            engine.place(seats.get(i).side(), placements.get(i));
        }
    }

    /** A player who left ends both questions: {@link #abandon} wakes every player. */
    private static <T> T join(Future<T> answer) {
        try {
            return answer.get();
        } catch (ExecutionException e) {
            throw e.getCause() instanceof RuntimeException cause ? cause : new IllegalStateException(e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RemotePlayer.Left();
        }
    }

    /** Ends the game at the next question, whoever it is for. */
    void abandon() {
        players().forEach(RemotePlayer::leave);
    }

    private void broadcast() {
        List<Boolean> flipped = flips.take();
        for (Seat seat : seats) {
            seat.send().accept(new StateMessage(view(seat), flipped));
        }
    }

    /** The game's chance, noting each coin flip for the browsers to show. Setup broadcasts from two threads. */
    private static final class Flips implements RandomSource {
        private final RandomSource rng;
        private final List<Boolean> since = new ArrayList<>();

        Flips(RandomSource rng) {
            this.rng = rng;
        }

        @Override
        public int nextInt(int bound) {
            return rng.nextInt(bound);
        }

        @Override
        public synchronized boolean nextBoolean() {
            boolean heads = rng.nextBoolean();
            since.add(heads);
            return heads;
        }

        @Override
        public void shuffle(List<?> list) {
            rng.shuffle(list);
        }

        /** The flips since the last call. */
        synchronized List<Boolean> take() {
            List<Boolean> taken = List.copyOf(since);
            since.clear();
            return taken;
        }
    }

    /** Passes every choice through to {@link #tellOthers}, so the engine never needs to know about the log. */
    private final class Logged implements IPlayer {
        private final IPlayer player;

        Logged(IPlayer player) {
            this.player = player;
        }

        @Override
        public String name() {
            return player.name();
        }

        @Override
        public <T> T choose(Decision<T> decision) {
            // A RemotePlayer sends the board itself; a bot's moves need one each too, or its whole turn lands at once.
            if (!(player instanceof RemotePlayer)) {
                broadcast();
            }
            T choice = player.choose(decision);
            tellOthers(decision, choice);
            return choice;
        }
    }

    /**
     * Setup is left out, since both boards are revealed together, and a chosen card is not named: it may have
     * come from a hand or deck. Called on the game thread, or on a setup thread, where it sends nothing.
     */
    private void tellOthers(Decision<?> decision, Object choice) {
        for (Seat seat : seats) {
            if (seat.side() == decision.chooser()) {
                continue;
            }
            // Labelled as if the receiver were choosing, so "your" and "opponent's" are theirs.
            OptionView view = OptionView.of(choice,
                    new Decision<>(decision.prompt(), decision.options(), seat.side(), decision.context()));
            boolean hidden = view.kind().equals("card");
            if (!view.kind().equals("setup")) {
                seat.send().accept(new LogMessage(battle.turn(), battle.attacker() == seat.side(),
                        hidden ? "Chose a card" : view.label(), view.kind(), hidden ? null : view.card(), view.target(),
                        view.shown()));
            }
        }
    }

    private BoardView view(Seat viewer) {
        return BoardView.of(battle, viewer.side(), side -> seats.stream()
                .filter(seat -> seat.side() == side).findFirst().orElseThrow().cosmetics());
    }

    private Side deal(Side side, Deal deck) {
        for (String id : deck.cards()) {
            side.addToDeck(new CardInstance(nextInstanceId++, CardPool.get(id), side, Zone.DECK));
        }
        side.registerTypes(deck.energy().toArray(Type[]::new));
        return side;
    }
}
