package com.tcgpocket.effect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.TestBoard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Note that every "step" in these tests is a real effect, not a stub: IEffect
 * is sealed, so a test double is not merely discouraged but impossible. The
 * costs are paid with real energy and the payoffs are observed as real damage.
 */
class IAttemptTest {

    private static final PokemonCard PIKACHU = TestBoard.card("Pikachu", 60, Type.LIGHTNING);
    private static final PokemonCard SNORLAX = TestBoard.card("Snorlax", 150, Type.COLORLESS);

    private final TestBoard board = new TestBoard();
    private final PokemonInPlay attacker = board.active(board.you, PIKACHU);
    private final PokemonInPlay defender = board.active(board.them, SNORLAX);
    private final ResolutionContext context = board.contextFor(attacker);

    /** The payoff, whose arrival or absence shows whether the attempt carried on. */
    private static IEffect payoff() {
        return new DealDamage(new Literal(30), new OpponentActive());
    }

    @Nested
    @DisplayName("running in order")
    class Sequencing {

        @Test
        @DisplayName("an empty attempt succeeds vacuously")
        void emptyAttemptSucceeds() {
            AttemptResult result = new Attempt().execute(context);

            assertTrue(result.succeeded());
            assertEquals(0, result.stepsRun());
            assertFalse(result.changedTheBoard());
        }

        @Test
        void everyStepRunsWhenNothingFails() {
            AttemptResult result = new Attempt(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new DealDamage(new Literal(20), new OpponentActive())).execute(context);

            assertTrue(result.succeeded());
            assertEquals(List.of(EffectOutcome.APPLIED, EffectOutcome.APPLIED), result.outcomes());
            assertEquals(30, defender.damage());
        }

        @Test
        @DisplayName("succeeding is not the same as changing anything")
        void anAttemptOfNoOpsSucceedsWithoutTouchingTheBoard() {
            AttemptResult result = new Attempt(
                    new HealDamage(new Literal(10), new Self()),
                    new RemoveStatus(new Self(), new PoisonStatus())).execute(context);

            assertTrue(result.succeeded());
            assertFalse(result.changedTheBoard());
            assertEquals(List.of(EffectOutcome.NO_OP, EffectOutcome.NO_OP), result.outcomes());
        }
    }

    @Nested
    @DisplayName("NO_OP carries on, FAILED stops")
    class ShortCircuit {

        @Test
        @DisplayName("a no-op does not swallow the rest of the card")
        void noOpDoesNotAbort() {
            // Healing a Pokemon already at full HP is a legal nothing-happened.
            AttemptResult result = new Attempt(
                    new HealDamage(new Literal(20), new Self()),
                    payoff()).execute(context);

            assertTrue(result.succeeded());
            assertEquals(List.of(EffectOutcome.NO_OP, EffectOutcome.APPLIED), result.outcomes());
            assertEquals(30, defender.damage(), "the payoff still landed");
        }

        @Test
        @DisplayName("a failure stops the attempt and the later effects never run")
        void failureAborts() {
            // No energy attached, so the cost cannot be paid.
            AttemptResult result = new Attempt(
                    new DiscardTypeEnergy(Type.LIGHTNING, new Literal(1), new Self()),
                    payoff()).execute(context);

            assertTrue(result.failed());
            assertEquals(0, defender.damage(), "the payoff never happened");
        }

        @Test
        @DisplayName("outcomes stop at the failure, so their count says how far it got")
        void outcomesRecordHowFarItGot() {
            AttemptResult result = new Attempt(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new DiscardTypeEnergy(Type.WATER, new Literal(1), new Self()),
                    payoff()).execute(context);

            assertEquals(2, result.stepsRun(), "three effects, but it stopped at the second");
            assertEquals(List.of(EffectOutcome.APPLIED, EffectOutcome.FAILED), result.outcomes());
            assertEquals(10, defender.damage(), "only the first effect landed");
        }

        @Test
        @DisplayName("the reason names the step that failed")
        void failureReasonIsInformative() {
            AttemptResult result = new Attempt(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new DiscardTypeEnergy(Type.WATER, new Literal(1), new Self())).execute(context);

            assertTrue(result.reason().contains("step 1"), result.reason());
            assertTrue(result.reason().contains("DiscardTypeEnergy"), result.reason());
        }

        @Test
        void aSucceedingAttemptHasNoReason() {
            AttemptResult result = new Attempt(payoff()).execute(context);

            assertEquals("", result.reason());
        }
    }

    @Nested
    @DisplayName("the card text this exists for")
    class IfYouDo {

        /** "Discard a Lightning Energy from this Pokemon. If you do, this attack does 30 damage." */
        private IAttempt discardThenStrike() {
            return new Attempt(
                    new DiscardTypeEnergy(Type.LIGHTNING, new Literal(1), new Self()),
                    payoff());
        }

        @Test
        void payTheCostAndTheBonusHappens() {
            attacker.attachEnergy(Type.LIGHTNING, 1);

            AttemptResult result = discardThenStrike().execute(context);

            assertTrue(result.succeeded());
            assertEquals(0, attacker.totalEnergy(), "the cost was paid");
            assertEquals(30, defender.damage());
        }

        @Test
        @DisplayName("an unpayable cost leaves the board exactly as it was")
        void cannotPayAndNothingHappensAtAll() {
            AttemptResult result = discardThenStrike().execute(context);

            assertTrue(result.failed());
            assertEquals(0, attacker.totalEnergy(), "nothing was deducted");
            assertEquals(0, defender.damage(), "and nothing was gained");
        }

        @Test
        @DisplayName("a flip earlier in the attempt feeds the damage later in it")
        void flipThenDamagePerHeads() {
            IAttempt attack = new Attempt(
                    new FlipN(new Literal(3)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(30)),
                            new OpponentActive()));

            AttemptResult result = attack.execute(context);
            int heads = context.scope().lastFlip().orElseThrow().heads();

            assertTrue(result.succeeded());
            assertEquals(heads * 30, defender.damage());
        }
    }

    @Nested
    @DisplayName("attempts are values")
    class Values {

        @Test
        void structuralEquality() {
            assertEquals(new Attempt(payoff()), new Attempt(payoff()));
            assertEquals(new Attempt(), new Attempt(List.of()));
        }

        @Test
        void theEffectListIsDefensivelyCopied() {
            List<IEffect> mutable = new java.util.ArrayList<>();
            mutable.add(payoff());

            Attempt attempt = new Attempt(mutable);
            mutable.clear();

            assertEquals(1, attempt.effects().size());
        }
    }
}
