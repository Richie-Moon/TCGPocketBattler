package com.tcgpocket.server;

import com.tcgpocket.energy.Type;
import com.tcgpocket.engine.OpeningPlacement;
import com.tcgpocket.engine.TurnEngine;
import com.tcgpocket.player.Decision;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * One game between two browsers.
 *
 * <p>Before every question it sends each player their own
 * {@link BoardView}, so the player who is waiting still sees the board change.
 * Setup is the one time both players are asked at once; see {@link #placeTogether}.
 */
final class Game {

    private static final Logger LOG = LoggerFactory.getLogger(Game.class);

    // ponytail: fixed decks until there is deck building; TurnEngine.setup runs DeckValidator on whatever is dealt.
    static final List<String> LIGHTNING_DECK = List.of(
            "A1-094", "A1-094", "A1-095", "A1-095", "A1-096", "A1-096", "A1-097", "A1-097", "A1-098", "A1-098",
            "A1-103", "A1-103", "A1-226", "A1-226", "P-A-001", "P-A-001", "P-A-005", "P-A-005", "P-A-007", "P-A-007");
    static final List<String> FIRE_DECK = List.of(
            "A1-033", "A1-033", "A1-034", "A1-034", "A1-036", "A1-036", "A1-039", "A1-039", "A1-040", "A1-040",
            "A1-046", "A1-046", "A1-221", "A1-221", "P-A-001", "P-A-001", "P-A-005", "P-A-005", "P-A-007", "P-A-007");

    record StateMessage(String type, BoardView board) {
        StateMessage(BoardView board) {
            this("state", board);
        }
    }

    record OverMessage(String type, BoardView board, String result) {
        OverMessage(BoardView board, String result) {
            this("over", board, result);
        }
    }

    private record Seat(RemotePlayer player, Side side, Consumer<Object> send) {
    }

    private final List<Seat> seats;
    private final Battle battle;
    private int nextInstanceId;

    Game(Consumer<Object> sendFirst, Consumer<Object> sendSecond, RandomSource rng) {
        RemotePlayer first = new RemotePlayer("Player 1", sendFirst, this::broadcast);
        RemotePlayer second = new RemotePlayer("Player 2", sendSecond, this::broadcast);
        Side firstSide = deal(new Side(first.name(), first), LIGHTNING_DECK, Type.LIGHTNING);
        Side secondSide = deal(new Side(second.name(), second), FIRE_DECK, Type.FIRE);

        this.seats = List.of(new Seat(first, firstSide, sendFirst), new Seat(second, secondSide, sendSecond));
        this.battle = Battle.flipForFirst(firstSide, secondSide, rng);
    }

    List<RemotePlayer> players() {
        return seats.stream().map(Seat::player).toList();
    }

    /** Plays to the end, then tells both players the result. Never throws. */
    void run() {
        String result;
        try {
            TurnEngine engine = new TurnEngine(battle);
            engine.dealOpeningHands();
            placeTogether(engine);
            result = engine.playOut()
                    .map(winner -> winner.name() + " wins")
                    .orElse("Tie");
        } catch (RemotePlayer.Left e) {
            result = "A player left";
        } catch (RuntimeException e) {
            LOG.error("game crashed", e);
            result = "The game crashed";
        }
        for (Seat seat : seats) {
            seat.send().accept(new OverMessage(BoardView.of(battle, seat.side()), result));
        }
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
                RemotePlayer player = seats.get(i).player();
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
        seats.forEach(seat -> seat.player().leave());
    }

    private void broadcast() {
        for (Seat seat : seats) {
            seat.send().accept(new StateMessage(BoardView.of(battle, seat.side())));
        }
    }

    private Side deal(Side side, List<String> deck, Type energy) {
        for (String id : deck) {
            side.addToDeck(new CardInstance(nextInstanceId++, CardPool.get(id), side, Zone.DECK));
        }
        side.registerTypes(energy);
        return side;
    }
}
