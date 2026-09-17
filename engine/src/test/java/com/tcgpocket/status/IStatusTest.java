package com.tcgpocket.status;

import com.tcgpocket.ScriptedRandom;
import com.tcgpocket.TestBoard;
import com.tcgpocket.action.Action;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.StatusApplied;
import com.tcgpocket.trigger.StatusRemoved;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TriggerDispatcher;
import com.tcgpocket.trigger.TurnEnd;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The five special conditions, exercised through the dispatcher rather than by
 * calling anything on them directly — because that is all the engine will ever
 * do to them.
 */
class IStatusTest {

    private static final PokemonCard PIKACHU = TestBoard.card("Pikachu", 60, Type.LIGHTNING);
    private static final PokemonCard SNORLAX = TestBoard.card("Snorlax", 150, Type.COLORLESS);

    @Nested
    @DisplayName("Poison")
    class Poison {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay poisoned = board.active(board.you, SNORLAX);

        @Test
        void ticksAtTheEndOfEveryTurn() {
            poisoned.addStatus(new PoisonStatus());

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));
            assertEquals(10, poisoned.damage());

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.them));
            assertEquals(20, poisoned.damage(), "the opponent's turn ending counts too");
        }

        @Test
        @DisplayName("never wears off on its own")
        void isPermanent() {
            poisoned.addStatus(new PoisonStatus());

            for (int turn = 0; turn < 5; turn++) {
                TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));
            }

            assertTrue(poisoned.hasStatus(new PoisonStatus()));
            assertEquals(50, poisoned.damage());
        }

        @Test
        @DisplayName("a benched Pokemon does not tick")
        void onlyTheActiveIsAffected() {
            PokemonInPlay benched = board.bench(board.you, SNORLAX);
            benched.addStatus(new PoisonStatus());

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));

            assertEquals(0, benched.damage());
        }
    }

    @Nested
    @DisplayName("Burn")
    class Burn {

        @Test
        @DisplayName("20 damage, then heads puts it out")
        void headsCuresIt() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysHeads());
            PokemonInPlay burned = board.active(board.you, SNORLAX);
            burned.addStatus(new BurnStatus());

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));

            assertEquals(20, burned.damage());
            assertFalse(burned.hasStatus(new BurnStatus()));
        }

        @Test
        void tailsKeepsBurning() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysTails());
            PokemonInPlay burned = board.active(board.you, SNORLAX);
            burned.addStatus(new BurnStatus());

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));
            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.them));

            assertEquals(40, burned.damage());
            assertTrue(burned.hasStatus(new BurnStatus()));
        }

        @Test
        @DisplayName("the damage lands before the flip, so a cure does not undo it")
        void damageComesFirst() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysHeads());
            PokemonInPlay burned = board.active(board.you, PIKACHU);
            burned.takeDamage(40);
            burned.addStatus(new BurnStatus());

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));

            assertTrue(burned.isKnockedOut(), "knocked out by the burn that then went out");
        }
    }

    @Nested
    @DisplayName("Sleep")
    class Sleep {

        @Test
        void headsWakesUpWithNoDamage() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysHeads());
            PokemonInPlay sleeper = board.active(board.you, SNORLAX);
            sleeper.addStatus(new SleepStatus());

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));

            assertFalse(sleeper.hasStatus(new SleepStatus()));
            assertEquals(0, sleeper.damage(), "sleep does no damage");
        }

        @Test
        void tailsStaysAsleep() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysTails());
            PokemonInPlay sleeper = board.active(board.you, SNORLAX);
            sleeper.addStatus(new SleepStatus());

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));

            assertTrue(sleeper.hasStatus(new SleepStatus()));
        }
    }

    @Nested
    @DisplayName("Paralysis")
    class Paralysis {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay victim = board.active(board.them, SNORLAX);

        @Test
        @DisplayName("survives the turn it was inflicted on")
        void theOpponentsTurnEndingDoesNotCureIt() {
            victim.addStatus(new ParalysisStatus());

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));

            assertTrue(victim.hasStatus(new ParalysisStatus()),
                    "curing it here would cost the victim nothing at all");
        }

        @Test
        void wearsOffAtTheEndOfItsVictimsOwnTurn() {
            victim.addStatus(new ParalysisStatus());

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));
            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.them));

            assertFalse(victim.hasStatus(new ParalysisStatus()));
        }

        @Test
        void doesNoDamage() {
            victim.addStatus(new ParalysisStatus());

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.them));

            assertEquals(0, victim.damage());
        }
    }

    @Nested
    @DisplayName("Confusion")
    class Confusion {

        private static final Action TACKLE = new Action(
                "Tackle",
                EnergyCost.free(),
                new Attempt(List.of(new DealDamage(new Literal(30), new OpponentActive()))));

        @Test
        void headsAndTheAttackGoesThrough() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysHeads());
            PokemonInPlay attacker = board.active(board.you, PIKACHU);
            PokemonInPlay defender = board.active(board.them, SNORLAX);
            attacker.addStatus(new ConfusionStatus());

            AttemptResult result = TACKLE.execute(board.contextFor(attacker));

            assertTrue(result.succeeded());
            assertEquals(30, defender.damage());
        }

        @Test
        @DisplayName("tails cancels the attack, and nothing after it runs")
        void tailsCancelsTheAttack() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysTails());
            PokemonInPlay attacker = board.active(board.you, PIKACHU);
            PokemonInPlay defender = board.active(board.them, SNORLAX);
            attacker.addStatus(new ConfusionStatus());

            AttemptResult result = TACKLE.execute(board.contextFor(attacker));

            assertTrue(result.failed());
            assertEquals(0, defender.damage());
            assertTrue(result.reason().contains("Tackle"));
        }

        @Test
        @DisplayName("a confused defender does not spoil the attacker's attack")
        void onlyTheAttackersConfusionCounts() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysTails());
            PokemonInPlay attacker = board.active(board.you, PIKACHU);
            PokemonInPlay defender = board.active(board.them, SNORLAX);
            defender.addStatus(new ConfusionStatus());

            AttemptResult result = TACKLE.execute(board.contextFor(attacker));

            assertTrue(result.succeeded());
            assertEquals(30, defender.damage());
        }
    }

    @Nested
    @DisplayName("Applying and removing a status is itself an event")
    class StatusEvents {

        private final TestBoard board = new TestBoard();

        @Test
        void bothEndsAreAnnounced() {
            PokemonInPlay mine = board.active(board.you, PIKACHU);
            PokemonInPlay theirs = board.active(board.them, SNORLAX);

            ToolCard alarm = ToolCard.of("alarm", "Alarm",
                    new Trigger(StatusApplied.class, new EventConcerns(new Self()),
                            new Attempt(List.of(new PlaceDamage(new Literal(10), new AttackerActive())))),
                    new Trigger(StatusRemoved.class, new EventConcerns(new Self()),
                            new Attempt(List.of(new PlaceDamage(new Literal(20), new AttackerActive())))));
            theirs.attachTool(board.loose(board.them, alarm));

            new com.tcgpocket.effect.AddStatus(new PoisonStatus(), new OpponentActive())
                    .apply(board.contextFor(mine));
            assertEquals(10, mine.damage());

            new com.tcgpocket.effect.RemoveStatus(new OpponentActive(), new PoisonStatus())
                    .apply(board.contextFor(mine));
            assertEquals(30, mine.damage());
        }
    }
}
