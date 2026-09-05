package com.tcgpocket.number;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tcgpocket.TestBoard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.FlipResult;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.DamageDealt;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class INumberTest {

    private static final PokemonCard PIKACHU = TestBoard.card("Pikachu", 60, Type.LIGHTNING);
    private static final PokemonCard SNORLAX = TestBoard.card("Snorlax", 150, Type.COLORLESS);

    @Nested
    @DisplayName("arithmetic")
    class Arithmetic {

        private final TestBoard board = new TestBoard();

        @Test
        void literalEvaluatesToItsValue() {
            assertEquals(30, new Literal(30).evaluate(board.contextWithoutSource()));
        }

        @Test
        void sumAndProductCompose() {
            INumber expression = new Product(new Sum(new Literal(2), new Literal(3)), new Literal(10));

            assertEquals(50, expression.evaluate(board.contextWithoutSource()));
        }

        @Test
        @DisplayName("difference may go negative — clamping is the damage pipeline's job")
        void differenceIsNotClamped() {
            INumber expression = new Difference(new Literal(20), new Literal(50));

            assertEquals(-30, expression.evaluate(board.contextWithoutSource()));
        }

        @Test
        @DisplayName("quotient truncates toward zero")
        void quotientTruncates() {
            assertEquals(2, new Quotient(new Literal(5), new Literal(2))
                    .evaluate(board.contextWithoutSource()));
            assertEquals(-2, new Quotient(new Literal(-5), new Literal(2))
                    .evaluate(board.contextWithoutSource()));
        }

        @Test
        @DisplayName("a zero divisor fails loudly rather than quietly yielding zero")
        void quotientRejectsZeroDivisor() {
            INumber expression = new Quotient(new Literal(50), new Literal(0));
            ResolutionContext context = board.contextWithoutSource();

            assertThrows(ArithmeticException.class, () -> expression.evaluate(context));
        }
    }

    @Nested
    @DisplayName("reading the board")
    class BoardReaders {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay mine = board.active(board.you, PIKACHU);
        private final PokemonInPlay theirs = board.active(board.them, SNORLAX);
        private final ResolutionContext context = board.contextFor(mine);

        @Test
        void maxHpReadsThePrintedCard() {
            assertEquals(60, new MaxHP(new AttackerActive()).evaluate(context));
            assertEquals(150, new MaxHP(new OpponentActive()).evaluate(context));
        }

        @Test
        void currentHpAndDamageAreComplementary() {
            theirs.takeDamage(40);

            assertEquals(110, new CurrentHP(new OpponentActive()).evaluate(context));
            assertEquals(40, new DamageOn(new OpponentActive()).evaluate(context));
        }

        @Test
        void stageReadsTheDefinition() {
            assertEquals(0, new Stage(new AttackerActive()).evaluate(context));
        }

        @Test
        @DisplayName("an unresolvable target contributes zero rather than throwing")
        void unresolvedTargetIsZero() {
            TestBoard empty = new TestBoard();
            // Neither side has an active Pokemon.
            ResolutionContext noBoard = empty.contextWithoutSource();

            assertEquals(0, new MaxHP(new AttackerActive()).evaluate(noBoard));
            assertEquals(0, new CurrentHP(new OpponentActive()).evaluate(noBoard));
            assertEquals(0, new Stage(new Self()).evaluate(noBoard));
        }

        @Test
        void energyOnCountsAllTypesOrOne() {
            mine.attachEnergy(Type.LIGHTNING, 2);
            mine.attachEnergy(Type.WATER, 1);

            assertEquals(3, new EnergyOn(new Self()).evaluate(context));
            assertEquals(2, new EnergyOn(new Self(), Type.LIGHTNING).evaluate(context));
            assertEquals(0, new EnergyOn(new Self(), Type.FIRE).evaluate(context));
        }

        @Test
        void pointsReadTheSide() {
            board.them.awardPoints(2);

            assertEquals(0, new Points(new AttackerSide()).evaluate(context));
            assertEquals(2, new Points(new OpponentSide()).evaluate(context));
        }
    }

    @Nested
    @DisplayName("Self is source-relative, not turn-relative")
    class SelfTargeting {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay mine = board.active(board.you, PIKACHU);
        private final PokemonInPlay theirs = board.active(board.them, SNORLAX);

        @Test
        @DisplayName("a defender's card resolving mid-attack still means its own Pokemon")
        void selfFollowsTheSourceNotTheTurn() {
            // The defending side's card is resolving during the attacker's turn,
            // which is exactly the case a bare Battle could not express.
            ResolutionContext defendersCard =
                    ResolutionContext.of(board.battle, board.them, theirs);

            assertEquals(150, new MaxHP(new Self()).evaluate(defendersCard));
            assertEquals(60, new MaxHP(new AttackerActive()).evaluate(defendersCard));
        }

        @Test
        void selfIsEmptyForACardWithNoSourcePokemon() {
            assertEquals(0, new MaxHP(new Self()).evaluate(board.contextWithoutSource()));
            assertEquals(60, new MaxHP(new AttackerActive()).evaluate(board.contextWithoutSource()));
        }

        @Test
        void selfSideFollowsTheController() {
            ResolutionContext defendersCard =
                    ResolutionContext.of(board.battle, board.them, theirs);
            board.them.awardPoints(1);

            assertEquals(1, new Points(new SelfSide()).evaluate(defendersCard));
            assertEquals(0, new Points(new AttackerSide()).evaluate(defendersCard));
        }
    }

    @Nested
    @DisplayName("CountCards")
    class Counting {

        private final TestBoard board = new TestBoard();

        @Test
        void countsAZoneForASide() {
            board.active(board.you, PIKACHU);
            board.bench(board.you, SNORLAX);
            board.bench(board.you, PIKACHU);
            board.inHand(board.you, PIKACHU);
            board.inDiscard(board.them, SNORLAX);

            ResolutionContext context = board.contextWithoutSource();

            assertEquals(2, new CountCards(new AttackerSide(), Zone.BENCH).evaluate(context));
            assertEquals(1, new CountCards(new AttackerSide(), Zone.ACTIVE).evaluate(context));
            assertEquals(1, new CountCards(new AttackerSide(), Zone.HAND).evaluate(context));
            assertEquals(0, new CountCards(new AttackerSide(), Zone.DISCARD).evaluate(context));
            assertEquals(1, new CountCards(new OpponentSide(), Zone.DISCARD).evaluate(context));
        }

        @Test
        void anEmptyZoneCountsZero() {
            assertEquals(0, new CountCards(new AttackerSide(), Zone.DECK)
                    .evaluate(board.contextWithoutSource()));
        }

        @Test
        void aFilterNarrowsTheCount() {
            board.inHand(board.you, PIKACHU);
            board.inHand(board.you, SNORLAX);
            board.inHand(board.you, PIKACHU);

            ICondition<CardInstance> isPikachu = new IsSpecies("Pikachu");
            INumber allCards = new CountCards(new AttackerSide(), Zone.HAND);
            INumber pikachus = new CountCards(new AttackerSide(), Zone.HAND, Optional.of(isPikachu));

            ResolutionContext context = board.contextWithoutSource();

            assertEquals(3, allCards.evaluate(context));
            assertEquals(2, pikachus.evaluate(context));
        }

        @Test
        void anAlwaysFilterMatchesEverything() {
            board.inHand(board.you, PIKACHU);
            board.inHand(board.you, SNORLAX);

            INumber counted = new CountCards(
                    new AttackerSide(), Zone.HAND, Optional.of(new Always<CardInstance>()));

            assertEquals(2, counted.evaluate(board.contextWithoutSource()));
        }
    }

    @Nested
    @DisplayName("Branch")
    class Branching {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay theirs = board.active(board.them, SNORLAX);

        /** 20 if they have taken any damage at all, 90 if they are past 100, else 0. */
        private INumber tieredDamage() {
            return new Branch(
                    List.of(
                            new Branch.Case(
                                    new GreaterThan(new DamageOn(new OpponentActive()), new Literal(100)),
                                    new Literal(90)),
                            new Branch.Case(
                                    new GreaterThan(new DamageOn(new OpponentActive()), new Literal(0)),
                                    new Literal(20))),
                    new Literal(0));
        }

        @Test
        void fallsThroughToOtherwiseWhenNothingMatches() {
            assertEquals(0, tieredDamage().evaluate(board.contextWithoutSource()));
        }

        @Test
        void takesTheFirstMatchingCase() {
            theirs.takeDamage(50);

            assertEquals(20, tieredDamage().evaluate(board.contextWithoutSource()));
        }

        @Test
        @DisplayName("case order decides the winner when several match")
        void earlierCasesWin() {
            theirs.takeDamage(120);

            // Both cases hold at 120 damage; the first listed one is taken.
            assertEquals(90, tieredDamage().evaluate(board.contextWithoutSource()));
        }

        @Test
        void anAlwaysCaseShortCircuitsTheRest() {
            INumber branch = new Branch(
                    List.of(new Branch.Case(new Always<>(), new Literal(10))),
                    new Literal(999));

            assertEquals(10, branch.evaluate(board.contextWithoutSource()));
        }
    }

    @Nested
    @DisplayName("resolution-scoped values")
    class ScopedValues {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay mine = board.active(board.you, PIKACHU);
        private final PokemonInPlay theirs = board.active(board.them, SNORLAX);

        @Test
        void numberHeadsIsZeroBeforeAnythingIsFlipped() {
            assertEquals(0, new NumberHeads().evaluate(board.contextFor(mine)));
        }

        @Test
        @DisplayName("\"30 damage for each heads\" is Product(NumberHeads, Literal(30))")
        void damagePerHeads() {
            ResolutionContext context = board.contextFor(mine);
            context.scope().recordFlip(FlipResult.of(true, false, true));

            INumber attack = new Product(new NumberHeads(), new Literal(30));

            assertEquals(60, attack.evaluate(context));
        }

        @Test
        void eventDamageIsZeroOutsideATrigger() {
            assertEquals(0, new EventDamage().evaluate(board.contextFor(mine)));
        }

        @Test
        @DisplayName("a trigger can read how much damage it just took")
        void eventDamageReadsTheEvent() {
            ResolutionContext triggered = board.contextFor(theirs)
                    .withEvent(new DamageDealt(Optional.of(mine), theirs, 40));

            assertEquals(40, new EventDamage().evaluate(triggered));
        }
    }

    @Nested
    @DisplayName("contract")
    class Contract {

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("evaluating is pure — the same expression twice gives the same answer")
        void evaluationDoesNotMutate() {
            PokemonInPlay theirs = board.active(board.them, SNORLAX);
            theirs.takeDamage(30);
            ResolutionContext context = board.contextWithoutSource();

            INumber expression = new Difference(
                    new MaxHP(new OpponentActive()), new DamageOn(new OpponentActive()));

            assertEquals(120, expression.evaluate(context));
            assertEquals(120, expression.evaluate(context));
            assertEquals(30, theirs.damage());
        }

        @Test
        @DisplayName("records compare structurally, so expressions can be asserted on directly")
        void expressionsAreValues() {
            assertEquals(
                    new Product(new NumberHeads(), new Literal(30)),
                    new Product(new NumberHeads(), new Literal(30)));
            assertNotEquals(
                    new Product(new NumberHeads(), new Literal(30)),
                    new Product(new NumberHeads(), new Literal(20)));
            assertEquals(new Self(), new Self());
        }
    }

}
