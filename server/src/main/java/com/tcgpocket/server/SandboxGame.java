package com.tcgpocket.server;

import com.tcgpocket.action.EndTurnAction;
import com.tcgpocket.action.IAction;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ICard;
import com.tcgpocket.card.IPlayableCard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.card.StadiumCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.energy.Type;
import com.tcgpocket.engine.TurnEngine;
import com.tcgpocket.player.Decision;
import com.tcgpocket.player.IPlayer;
import com.tcgpocket.pool.CardPool;
import com.tcgpocket.resolve.RandomSource;
import com.tcgpocket.state.Battle;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.BurnStatus;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.status.IStatus;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.status.SleepStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * The owner's sandbox: one browser playing both sides of a game on a board it can edit, rated and recorded by
 * nothing. It starts empty on turn 1 and {@link TurnEngine#endless} runs it, so the rules are the real ones until
 * an edit steps outside them, except that it has no turn limit and no win ends it. Decks are never dealt, so
 * {@code DeckValidator} doesn't apply.
 *
 * <p>Only the game thread changes the board: {@link #submit} only queues, and {@link SandboxPlayer#choose} applies
 * what arrives. Board edits need the "Your move" question open: returning {@link #REFRESH} from it makes
 * {@code playTurn} check knockouts and ask again with fresh legal moves. A question in the middle of an effect
 * can't be rebuilt that way, so edits wait until it is answered.
 */
final class SandboxGame {

    private static final Logger LOG = LoggerFactory.getLogger(SandboxGame.class);

    private static final IAction REFRESH = new PlainAction("Sandbox edit", new Attempt());
    private static final Object LEFT = new Object();

    /** A {@link RemotePlayer.DecisionMessage}, and also who it's for, whether edits are open, and the coins queued. */
    record SandboxDecision(String type, int id, String prompt, List<OptionView> options,
                           boolean opponent, boolean editable, List<Boolean> coins) {
    }

    private final Consumer<Object> send;
    private final Side you;
    private final Side opponent;
    private final Battle battle;
    private final Game.Flips flips;
    private final Deque<Boolean> queuedCoins = new ArrayDeque<>();
    private final BlockingQueue<Object> inbox = new LinkedBlockingQueue<>();
    private int nextInstanceId;
    private int nextDecisionId = 1;

    SandboxGame(Consumer<Object> send, RandomSource rng) {
        this.send = send;
        IPlayer player = new SandboxPlayer();
        this.you = new Side("You", player);
        this.opponent = new Side("Opponent", player);
        // The types the panel can tick; no deck generates Dragon.
        Set<Type> types = EnumSet.complementOf(EnumSet.of(Type.DRAGON, Type.COLORLESS));
        you.setEnergyTypes(types);
        opponent.setEnergyTypes(types);
        this.flips = new Game.Flips(new QueuedCoins(rng));
        this.battle = new Battle(you, opponent, flips);
    }

    /** Plays until the browser leaves, then tells it so. Never throws. */
    void run() {
        String result;
        try {
            TurnEngine.endless(battle).playOut();
            throw new IllegalStateException("an endless game ended");
        } catch (RemotePlayer.Left e) {
            result = "Left the sandbox";
        } catch (RuntimeException e) {
            LOG.error("sandbox crashed", e);
            result = "The game crashed: " + e;
        }
        send.accept(new Game.OverMessage(BoardView.sandbox(battle, you), flips.take(), result));
    }

    /** Queues an {@link GameSocket.Answer} or a {@link SandboxCommand}. Called on a socket thread. */
    void submit(Object message) {
        inbox.add(message);
    }

    /** Ends the game at its next question. */
    void abandon() {
        inbox.add(LEFT);
    }

    /** Coins land as queued, then at random; the coin flips are what {@code nextBoolean} is documented to be. */
    private final class QueuedCoins implements RandomSource {
        private final RandomSource rng;

        QueuedCoins(RandomSource rng) {
            this.rng = rng;
        }

        @Override
        public int nextInt(int bound) {
            return rng.nextInt(bound);
        }

        @Override
        public boolean nextBoolean() {
            Boolean queued = queuedCoins.poll();
            return queued != null ? queued : rng.nextBoolean();
        }

        @Override
        public void shuffle(List<?> list) {
            rng.shuffle(list);
        }
    }

    /** The one browser, answering for both sides. Runs on the game thread, so it may apply edits. */
    private final class SandboxPlayer implements IPlayer {

        @Override
        public String name() {
            return "Sandbox";
        }

        @Override
        @SuppressWarnings("unchecked") // REFRESH only goes back to the turn question, whose T is IAction.
        public <T> T choose(Decision<T> decision) {
            boolean editable = decision.options().stream().anyMatch(EndTurnAction.class::isInstance);
            int id = nextDecisionId++;
            List<OptionView> options = decision.options().stream().map(option -> OptionView.of(option, decision)).toList();
            Runnable ask = () -> {
                send.accept(new Game.StateMessage(BoardView.sandbox(battle, you), flips.take()));
                send.accept(new SandboxDecision("decision", id, decision.prompt(), options,
                        decision.chooser() == opponent, editable, List.copyOf(queuedCoins)));
            };
            ask.run();
            while (true) {
                switch (take()) {
                    case GameSocket.Answer(int answered, int option) when answered != id ->
                            error("decision " + answered + " is not open");
                    case GameSocket.Answer(int answered, int option) when option < 0 || option >= options.size() ->
                            error("option " + option + " is out of range");
                    case GameSocket.Answer answer -> {
                        return decision.options().get(answer.option());
                    }
                    case SandboxCommand.QueueCoins(List<Boolean> coins) -> {
                        queuedCoins.addAll(coins);
                        ask.run();
                    }
                    case SandboxCommand.ClearCoins() -> {
                        queuedCoins.clear();
                        ask.run();
                    }
                    case SandboxCommand command when !editable -> error("Answer this question before editing the board");
                    case SandboxCommand command -> {
                        try {
                            apply(command);
                            return (T) REFRESH;
                        } catch (IllegalArgumentException | IllegalStateException e) {
                            error(e.getMessage());
                        }
                    }
                    default -> throw new RemotePlayer.Left();
                }
            }
        }

        private Object take() {
            try {
                return inbox.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RemotePlayer.Left();
            }
        }

        private void error(String message) {
            send.accept(new GameSocket.Error(message));
        }
    }

    /** Checks before it changes anything, so a rejected command leaves the board as it was. */
    private void apply(SandboxCommand command) {
        switch (command) {
            case SandboxCommand.AddCard add -> {
                Side side = side(add.opponent());
                ICard definition = printed(add.card());
                CardInstance card = new CardInstance(nextInstanceId, definition, side, Zone.HAND);
                checkRoom(card, side, add.zone(), add.position());
                nextInstanceId++;
                place(card, side, add.zone(), add.position());
            }
            case SandboxCommand.Move move -> {
                CardInstance card = card(move.card());
                checkRoom(card, card.owner(), move.zone(), move.position());
                takeOut(card);
                place(card, card.owner(), move.zone(), move.position());
            }
            case SandboxCommand.Remove remove -> takeOut(card(remove.card()));
            case SandboxCommand.Shuffle shuffle -> side(shuffle.opponent()).shuffleDeck(battle.rng());
            case SandboxCommand.SetDamage set -> {
                if (set.damage() < 0) {
                    throw new IllegalArgumentException("damage must not be negative");
                }
                PokemonInPlay pokemon = pokemon(set.pokemon());
                pokemon.heal(pokemon.damage());
                pokemon.takeDamage(set.damage());
            }
            case SandboxCommand.SetEnergy set -> {
                if (set.count() < 0) {
                    throw new IllegalArgumentException("energy must not be negative");
                }
                PokemonInPlay pokemon = pokemon(set.pokemon());
                pokemon.detachEnergy(set.type(), pokemon.energyOf(set.type()));
                pokemon.attachEnergy(set.type(), set.count());
            }
            case SandboxCommand.AddStatus add -> pokemon(add.pokemon()).addStatus(status(add.status()));
            case SandboxCommand.RemoveStatus remove -> pokemon(remove.pokemon()).removeStatus(status(remove.status()));
            case SandboxCommand.AttachTool attach -> {
                PokemonInPlay pokemon = pokemon(attach.pokemon());
                if (!(printed(attach.card()) instanceof ToolCard tool)) {
                    throw new IllegalArgumentException(attach.card() + " is not a Tool");
                }
                if (pokemon.hasTool()) {
                    throw new IllegalArgumentException(pokemon.definition().name() + " already holds a Tool");
                }
                pokemon.attachTool(new CardInstance(nextInstanceId++, tool, pokemon.owner(), Zone.ATTACHED));
            }
            case SandboxCommand.RemoveTool remove -> pokemon(remove.pokemon()).removeTool();
            case SandboxCommand.Evolve evolve -> {
                PokemonInPlay pokemon = pokemon(evolve.pokemon());
                if (!(printed(evolve.card()) instanceof PokemonCard evolution)) {
                    throw new IllegalArgumentException(evolve.card() + " is not a Pokemon");
                }
                pokemon.evolveInto(new CardInstance(nextInstanceId++, evolution, pokemon.owner(), pokemon.zone()), battle.turn());
            }
            case SandboxCommand.SetPlayedThisTurn set -> {
                PokemonInPlay pokemon = pokemon(set.pokemon());
                pokemon.setTurnPlayed(set.played() ? battle.turn() : 0);
                if (!set.played()) {
                    pokemon.owner().markTurnTaken();
                }
            }
            case SandboxCommand.SetPoints set -> side(set.opponent()).setPoints(set.points());
            case SandboxCommand.SetEnergyZone set -> {
                Side side = side(set.opponent());
                side.setEnergyTypes(set.types());
                side.setCurrentEnergy(set.current());
                side.setNextEnergy(set.next());
                // Attaching empties the zone, so Energy here now was put here to be attached.
                if (set.current() != null) {
                    side.clearEnergyAttached();
                }
            }
            case SandboxCommand.ResetTurn reset -> {
                Side side = side(reset.opponent());
                side.resetTurnFlags();
                side.clearLocks();
            }
            case SandboxCommand.SetTurn set -> {
                if (set.turn() < 1) {
                    throw new IllegalArgumentException("turn must be at least 1");
                }
                battle.setTurn(set.turn());
            }
            case SandboxCommand.SetStadium set -> {
                if (set.card() == null) {
                    battle.setStadium(null);
                } else if (printed(set.card()) instanceof StadiumCard stadium) {
                    battle.setStadium(new CardInstance(nextInstanceId++, stadium, you, Zone.ATTACHED));
                } else {
                    throw new IllegalArgumentException(set.card() + " is not a Stadium");
                }
            }
            // Handled in choose, since they don't need the turn question open.
            case SandboxCommand.QueueCoins queue -> throw new IllegalStateException();
            case SandboxCommand.ClearCoins clear -> throw new IllegalStateException();
        }
    }

    private Side side(boolean isOpponent) {
        return isOpponent ? opponent : you;
    }

    private static ICard printed(String id) {
        return CardPool.find(id).orElseThrow(() -> new IllegalArgumentException("no such card: " + id));
    }

    private CardInstance card(int id) {
        return Stream.of(you, opponent)
                .flatMap(side -> Stream.of(side.hand(), side.deck(), side.discardPile(), side.inPlay()))
                .flatMap(List::stream)
                .map(CardInstance.class::cast)
                .filter(card -> card.instanceId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no card " + id + " on the board"));
    }

    private PokemonInPlay pokemon(int id) {
        return Optional.of(card(id)).filter(PokemonInPlay.class::isInstance).map(PokemonInPlay.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("card " + id + " is not in play"));
    }

    private static IStatus status(String name) {
        return switch (name) {
            case "Poison" -> new PoisonStatus();
            case "Burn" -> new BurnStatus();
            case "Sleep" -> new SleepStatus();
            case "Paralysis" -> new ParalysisStatus();
            case "Confusion" -> new ConfusionStatus();
            default -> throw new IllegalArgumentException("no such status: " + name);
        };
    }

    private static void checkRoom(CardInstance card, Side side, Zone zone, Integer position) {
        if (zone == null) {
            throw new IllegalArgumentException("no zone given");
        }
        switch (zone) {
            case ACTIVE -> {
                requirePlayable(card);
                if (side.active().filter(active -> active != card).isPresent()) {
                    throw new IllegalArgumentException("the Active Spot is taken");
                }
            }
            case BENCH -> {
                requirePlayable(card);
                if (position == null ? side.benchIsFull() : !side.emptyBenchSlots().contains(position)) {
                    throw new IllegalArgumentException(position == null ? "the Bench is full" : "Bench slot " + position + " is not free");
                }
            }
            case DECK -> {
                if (position != null && position < 0) {
                    throw new IllegalArgumentException("deck position must not be negative");
                }
            }
            case HAND, DISCARD -> { }
            case ATTACHED -> throw new IllegalArgumentException("attach a Tool instead");
        }
    }

    private static void requirePlayable(CardInstance card) {
        if (!(card.definition() instanceof IPlayableCard)) {
            throw new IllegalArgumentException(card.definition().name() + " can't go into play");
        }
    }

    /** Takes a card off the board. A Pokemon leaving the Active Spot loses its statuses, as retreating does. */
    private static void takeOut(CardInstance card) {
        Side side = card.owner();
        switch (card.zone()) {
            case HAND -> side.removeFromHand(card);
            case DECK -> side.removeFromDeck(card);
            case DISCARD -> side.removeFromDiscard(card);
            case ACTIVE -> {
                side.setActive(null);
                ((PokemonInPlay) card).clearTemporaryState();
            }
            case BENCH -> side.removeFromBench((PokemonInPlay) card);
            case ATTACHED -> throw new IllegalStateException("not addressable: " + card);
        }
    }

    private static void place(CardInstance card, Side side, Zone zone, Integer position) {
        switch (zone) {
            case ACTIVE -> side.setActive(inPlay(card, side));
            case BENCH -> {
                int slot = position != null ? position : side.emptyBenchSlots().getFirst();
                side.addToBench(inPlay(card, side), slot);
            }
            case HAND, DECK, DISCARD -> {
                if (card instanceof PokemonInPlay pokemon) {
                    pokemon.removeTool().ifPresent(side::addToDiscard);
                    int offset = 0;
                    for (CardInstance physical : pokemon.cards()) {
                        pile(physical, side, zone, position == null ? null : position + offset++);
                    }
                } else {
                    pile(card, side, zone, position);
                }
            }
            case ATTACHED -> throw new IllegalStateException("checked by checkRoom");
        }
    }

    private static void pile(CardInstance card, Side side, Zone zone, Integer position) {
        switch (zone) {
            case HAND -> side.addToHand(card);
            case DISCARD -> side.addToDiscard(card);
            default -> side.addToDeck(card, position == null ? side.deck().size() : Math.min(position, side.deck().size()));
        }
    }

    /** Keeps a Pokemon already in play; a card from a pile becomes one, ready to evolve. Same id, as {@code PlayCardAction} does. */
    private static PokemonInPlay inPlay(CardInstance card, Side side) {
        return card instanceof PokemonInPlay pokemon
                ? pokemon
                : new PokemonInPlay(card.instanceId(), (IPlayableCard) card.definition(), side, Zone.BENCH, 0);
    }
}
