package com.tcgpocket.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.ScriptedRandom;
import com.tcgpocket.TestBoard;
import com.tcgpocket.action.Action;
import com.tcgpocket.action.AttachEnergyAction;
import com.tcgpocket.action.EndTurnAction;
import com.tcgpocket.action.EvolveAction;
import com.tcgpocket.action.PlayCardAction;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.ICard;
import com.tcgpocket.card.ITrainerCard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DrawCard;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.player.Decision;
import com.tcgpocket.player.IPlayer;
import com.tcgpocket.player.RandomPlayer;
import com.tcgpocket.pool.CardPool;
import com.tcgpocket.resolve.SeededRandom;
import com.tcgpocket.state.ActiveModifier;
import com.tcgpocket.state.Battle;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.ModifierKind;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.Knockout;
import com.tcgpocket.trigger.Trigger;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TurnEngineTest {

    private static final PokemonCard PIKACHU = PokemonCard.basic(
            "pikachu", "Pikachu", 60, Type.LIGHTNING, 1,
            List.of(new Action("Gnaw", EnergyCost.of(Type.LIGHTNING, 1),
                    new Attempt(new DealDamage(new Literal(20), new OpponentActive())))));
    private static final PokemonCard SNORLAX = TestBoard.card("Snorlax", 150, Type.COLORLESS);
    private static final PokemonCard RAICHU = PokemonCard.evolution(
            "raichu", "Raichu", 1, "Pikachu", 100, Type.LIGHTNING, 2, List.of());

    /** Takes the last option every time: EndTurnAction on a turn, the last Basic in setup. */
    private static final class Passive implements IPlayer {
        @Override
        public String name() {
            return "passive";
        }

        @Override
        public <T> T choose(Decision<T> decision) {
            return decision.options().getLast();
        }
    }

    private static TestBoard passiveBoard() {
        return new TestBoard(new Passive(), new Passive());
    }

    private static void knockOut(PokemonInPlay pokemon) {
        pokemon.takeDamage(pokemon.maxHp());
    }

    @Nested
    @DisplayName("Knockouts — score, announce, discard, promote")
    class Knockouts {

        private final TestBoard board = new TestBoard();
        private final TurnEngine engine = new TurnEngine(board.battle);
        private final PokemonInPlay yours = board.active(board.you, SNORLAX);
        private final PokemonInPlay theirs = board.active(board.them, PIKACHU);
        private final PokemonInPlay theirBench = board.bench(board.them, SNORLAX);

        @Test
        @DisplayName("a knockout scores 1, discards the Pokemon, and its owner promotes from the bench")
        void ordinaryKnockout() {
            knockOut(theirs);

            engine.checkKnockouts(Optional.of(yours));

            assertEquals(1, board.you.points());
            assertEquals("Pikachu", board.them.discardPile().getFirst().definition().name());
            assertSame(theirBench, board.them.active().orElseThrow());
            assertTrue(board.them.bench().isEmpty());
            assertFalse(engine.isOver());
        }

        @Test
        @DisplayName("an EX scores 2, a Mega EX 3")
        void exScoresMore() {
            assertEquals(1, TurnEngine.pointsFor(PIKACHU));
            assertEquals(2, TurnEngine.pointsFor(PIKACHU.withTags(CardTag.EX)));
            assertEquals(3, TurnEngine.pointsFor(PIKACHU.withTags(CardTag.MEGA_EX)));
        }

        @Test
        @DisplayName("Knockout fires while the Pokemon still holds its Tool, and the Tool is discarded after")
        void knockoutFiresBeforeLeavingPlay() {
            ToolCard scarf = ToolCard.of("scarf", "Scarf", new Trigger(
                    Knockout.class, new Attempt(new DrawCard(new Literal(1), new SelfSide()))));
            theirs.attachTool(board.loose(board.them, scarf));
            board.inDeck(board.them, SNORLAX);
            knockOut(theirs);

            engine.checkKnockouts(Optional.empty());

            assertEquals(1, board.them.hand().size(), "the Tool's trigger fired");
            assertEquals(2, board.them.discardPile().size(), "the Pokemon and its Tool");
        }

        @Test
        @DisplayName("an evolved Pokemon discards every card in its stack, each under its own id")
        void evolvedKnockoutDiscardsTheWholeStack() {
            CardInstance raichu = board.inHand(board.them, RAICHU);
            board.them.removeFromHand(raichu);
            theirs.evolveInto(raichu, 1);
            knockOut(theirs);

            engine.checkKnockouts(Optional.empty());

            List<CardInstance> discarded = board.them.discardPile();
            assertEquals(List.of("Pikachu", "Raichu"),
                    discarded.stream().map(card -> card.definition().name()).toList());
            assertEquals(theirs.instanceId(), discarded.get(0).instanceId());
            assertSame(raichu, discarded.get(1));
            assertEquals(Zone.DISCARD, discarded.get(0).zone());
            assertEquals(Zone.DISCARD, raichu.zone());
        }

        @Test
        void nothingKnockedOutDoesNothing() {
            engine.checkKnockouts(Optional.empty());

            assertEquals(0, board.you.points());
            assertSame(theirs, board.them.active().orElseThrow());
        }
    }

    @Nested
    @DisplayName("Winning — most win conditions wins, equal is a tie")
    class Winning {

        private final TestBoard board = new TestBoard();
        private final TurnEngine engine = new TurnEngine(board.battle);
        private final PokemonInPlay yours = board.active(board.you, SNORLAX);
        private final PokemonInPlay theirs = board.active(board.them, PIKACHU);

        @Test
        void threePointsWins() {
            board.bench(board.them, SNORLAX);
            board.you.awardPoints(2);
            knockOut(theirs);

            engine.checkKnockouts(Optional.empty());

            assertTrue(engine.isOver());
            assertSame(board.you, engine.winner().orElseThrow());
        }

        @Test
        @DisplayName("being the only side with Pokemon in play wins")
        void emptyBoardLoses() {
            knockOut(theirs);

            engine.checkKnockouts(Optional.empty());

            assertSame(board.you, engine.winner().orElseThrow());
        }

        @Test
        @DisplayName("a double knockout of points against bench is a tie")
        void pointsAgainstBenchIsATie() {
            board.bench(board.them, SNORLAX);
            board.you.awardPoints(2);
            knockOut(yours);
            knockOut(theirs);

            engine.checkKnockouts(Optional.empty());

            assertTrue(engine.isOver());
            assertTrue(engine.winner().isEmpty());
        }

        @Test
        @DisplayName("both on three points, but only one can promote: that one wins")
        void pointsAndBenchBeatsPoints() {
            board.bench(board.you, SNORLAX);
            board.you.awardPoints(2);
            board.them.awardPoints(2);
            knockOut(yours);
            knockOut(theirs);

            engine.checkKnockouts(Optional.empty());

            assertSame(board.you, engine.winner().orElseThrow());
        }

        @Test
        @DisplayName("after " + TurnEngine.MAX_TURNS + " turns the game is a tie")
        void turnLimitIsATie() {
            TestBoard passive = passiveBoard();
            for (Side side : List.of(passive.you, passive.them)) {
                for (int i = 0; i < 20; i++) {
                    passive.inDeck(side, SNORLAX);
                }
            }

            Optional<Side> result = new TurnEngine(passive.battle).playGame();

            assertTrue(result.isEmpty());
            assertEquals(TurnEngine.MAX_TURNS + 1, passive.battle.turn());
        }
    }

    @Test
    @DisplayName("The battle's own coin decides who goes first, and is the same coin the game flips")
    void coinFlipDecidesFirstPlayer() {
        Side a = new Side("a", new Passive());
        Side b = new Side("b", new Passive());
        ScriptedRandom tails = ScriptedRandom.alwaysTails();
        Battle battle = Battle.flipForFirst(a, b, tails);
        assertSame(b, battle.attacker());
        assertSame(tails, battle.rng());
        assertSame(a, Battle.flipForFirst(a, b, ScriptedRandom.alwaysHeads()).attacker());
    }

    @Nested
    @DisplayName("A turn")
    class Turns {

        private final TestBoard board = passiveBoard();
        private final TurnEngine engine = new TurnEngine(board.battle);
        private final PokemonInPlay yours = board.active(board.you, PIKACHU);
        private final PokemonInPlay theirs = board.active(board.them, PIKACHU);

        @Test
        @DisplayName("the first player gets no energy on turn 1; the second player does on turn 2")
        void noEnergyOnTurnOne() {
            board.you.registerTypes(Type.LIGHTNING);
            board.them.registerTypes(Type.LIGHTNING);

            engine.playTurn();
            assertTrue(board.you.currentEnergy().isEmpty());

            engine.playTurn();
            assertTrue(board.them.currentEnergy().isPresent());
        }

        @Test
        @DisplayName("draws a card and hands the turn over")
        void drawsAndSwitches() {
            board.inDeck(board.you, SNORLAX);

            engine.playTurn();

            assertEquals(1, board.you.hand().size());
            assertSame(board.them, board.battle.attacker());
            assertEquals(2, board.battle.turn());
        }

        @Test
        @DisplayName("TurnEnd is announced, so Poison ticks and can knock out between turns")
        void poisonKnocksOutAtTurnEnd() {
            board.bench(board.them, SNORLAX);
            theirs.takeDamage(theirs.maxHp() - PoisonStatus.DAMAGE);
            theirs.addStatus(new PoisonStatus());

            engine.playTurn();

            assertEquals(1, board.you.points());
            assertEquals("Snorlax", board.them.active().orElseThrow().definition().name());
        }

        @Test
        @DisplayName("expired modifiers and per-turn flags are cleared between turns")
        void betweenTurnsCleanup() {
            yours.addModifier(new ActiveModifier(ModifierKind.CANNOT_ATTACK, 0, board.battle.turn()));
            board.you.markRetreated();

            engine.playTurn();

            assertTrue(yours.modifiers().isEmpty());
            assertFalse(board.you.retreatedThisTurn());
        }
    }

    @Nested
    @DisplayName("legalActions — every move on offer")
    class LegalActions {

        private final TestBoard board = new TestBoard();
        private final TurnEngine engine = new TurnEngine(board.battle);
        private final PokemonInPlay yours = board.active(board.you, PIKACHU);

        @Test
        @DisplayName("always offers ending the turn, and an attack only once it is paid for")
        void endTurnAlwaysAttackWhenPaid() {
            assertInstanceOf(EndTurnAction.class, engine.legalActions().getLast());
            assertTrue(engine.legalActions().stream().noneMatch(Action.class::isInstance));

            yours.attachEnergy(Type.LIGHTNING, 1);

            assertTrue(engine.legalActions().stream().anyMatch(Action.class::isInstance));
        }

        @Test
        @DisplayName("nobody evolves on their first turn")
        void noEvolvingOnFirstTurns() {
            board.inHand(board.you, RAICHU);

            assertFalse(offersEvolution(), "turn 1");

            board.battle.switchSides();
            board.battle.switchSides();

            assertTrue(offersEvolution(), "turn 3");
        }

        @Test
        @DisplayName("the second player cannot evolve on turn 2 either")
        void secondPlayerFirstTurn() {
            board.active(board.them, PIKACHU);
            board.inHand(board.them, RAICHU);

            board.battle.switchSides();
            assertFalse(offersEvolution(), "turn 2");

            board.battle.switchSides();
            board.battle.switchSides();
            assertTrue(offersEvolution(), "turn 4");
        }

        private boolean offersEvolution() {
            return engine.legalActions().stream().anyMatch(EvolveAction.class::isInstance);
        }
    }

    @Nested
    @DisplayName("Self-play — two random players finish a real game")
    class SelfPlay {

        @ParameterizedTest
        @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
        void randomGameFinishes(long seed) {
            TestBoard board = new TestBoard(
                    new RandomPlayer("you", new SeededRandom(seed)),
                    new RandomPlayer("them", new SeededRandom(seed + 100)),
                    new SeededRandom(seed));
            deal(board, board.you, Type.LIGHTNING);
            deal(board, board.them, Type.FIRE);
            TurnEngine engine = new TurnEngine(board.battle);

            engine.playGame();

            assertTrue(engine.isOver());
            assertTrue(board.battle.turn() <= TurnEngine.MAX_TURNS + 1);
        }

        /** Random play mostly runs out the clock, so this one plays to win and must produce a winner. */
        @ParameterizedTest
        @ValueSource(longs = {1, 2, 3})
        void greedyGameHasAWinner(long seed) {
            TestBoard board = new TestBoard(new Greedy(seed), new Greedy(seed + 100), new SeededRandom(seed));
            deal(board, board.you, Type.LIGHTNING);
            deal(board, board.them, Type.FIRE);

            assertTrue(new TurnEngine(board.battle).playGame().isPresent());
        }

        /** Attacks when it can, then attaches, plays, evolves and ends the turn; random for anything else. */
        private static final class Greedy implements IPlayer {
            private static final List<Class<?>> PREFERENCE = List.of(
                    Action.class, AttachEnergyAction.class, PlayCardAction.class,
                    EvolveAction.class, EndTurnAction.class);

            private final RandomPlayer fallback;

            Greedy(long seed) {
                this.fallback = new RandomPlayer("greedy", new SeededRandom(seed));
            }

            @Override
            public String name() {
                return "greedy";
            }

            @Override
            public <T> T choose(Decision<T> decision) {
                for (Class<?> kind : PREFERENCE) {
                    for (T option : decision.options()) {
                        if (kind.isInstance(option)) {
                            return option;
                        }
                    }
                }
                return fallback.choose(decision);
            }
        }

        /** Twenty cards: every Pokemon of the type and every Trainer, cycled. */
        private static void deal(TestBoard board, Side side, Type type) {
            List<ICard> cards = CardPool.all().stream()
                    .filter(card -> card instanceof ITrainerCard
                            || card instanceof PokemonCard pokemon && pokemon.types().contains(type))
                    .toList();
            for (int i = 0; i < 20; i++) {
                board.inDeck(side, cards.get(i % cards.size()));
            }
            side.registerTypes(type);
        }
    }
}
