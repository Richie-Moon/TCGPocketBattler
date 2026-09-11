package com.tcgpocket.condition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.TestBoard;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CurrentHP;
import com.tcgpocket.number.Literal;
import com.tcgpocket.resolve.FlipResult;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.BurnStatus;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentBench;
import com.tcgpocket.target.Self;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class IConditionTest {

    private static final PokemonCard PIKACHU = TestBoard.card("Pikachu", 60, Type.LIGHTNING);
    private static final PokemonCard SNORLAX = TestBoard.card("Snorlax", 150, Type.COLORLESS);
    private static final PokemonCard CHARIZARD = TestBoard.card("Charizard", 180, Type.FIRE);

    private static final ICondition<ResolutionContext> TRUE = new Always<>();
    private static final ICondition<ResolutionContext> FALSE = new Not<>(new Always<>());

    @Nested
    @DisplayName("combinators are generic over any subject")
    class Combinators {

        private final TestBoard board = new TestBoard();
        private final ResolutionContext context = board.contextWithoutSource();

        @Test
        void andRequiresBoth() {
            assertTrue(new And<>(TRUE, TRUE).evaluate(context));
            assertFalse(new And<>(TRUE, FALSE).evaluate(context));
            assertFalse(new And<>(FALSE, TRUE).evaluate(context));
        }

        @Test
        void orAcceptsEither() {
            assertTrue(new Or<>(TRUE, FALSE).evaluate(context));
            assertTrue(new Or<>(FALSE, TRUE).evaluate(context));
            assertFalse(new Or<>(FALSE, FALSE).evaluate(context));
        }

        @Test
        void notInverts() {
            assertFalse(new Not<>(TRUE).evaluate(context));
            assertTrue(new Not<>(FALSE).evaluate(context));
        }

        @Test
        @DisplayName("All is vacuously true when empty, so it is a safe default")
        void allOverAList() {
            assertTrue(new All<ResolutionContext>(List.of()).evaluate(context));
            assertTrue(new All<>(List.of(TRUE, TRUE)).evaluate(context));
            assertFalse(new All<>(List.of(TRUE, FALSE)).evaluate(context));
        }

        @Test
        @DisplayName("Any is false when empty, mirroring All")
        void anyOverAList() {
            assertFalse(new Any<ResolutionContext>(List.of()).evaluate(context));
            assertTrue(new Any<>(List.of(FALSE, TRUE)).evaluate(context));
            assertFalse(new Any<>(List.of(FALSE, FALSE)).evaluate(context));
        }

        @Test
        @DisplayName("the same combinator works at the Pokemon level too")
        void combinatorsComposeAtAnyLevel() {
            PokemonInPlay pikachu = board.active(board.you, PIKACHU);
            pikachu.addStatus(new BurnStatus());

            ICondition<PokemonInPlay> burnedLightning =
                    new And<>(new IsBurned(), new HasType(Type.LIGHTNING));

            assertTrue(burnedLightning.evaluate(pikachu));
        }
    }

    @Nested
    @DisplayName("numeric comparisons")
    class Numeric {

        private final TestBoard board = new TestBoard();

        @Test
        void compareTwoNumbers() {
            PokemonInPlay theirs = board.active(board.them, SNORLAX);
            theirs.takeDamage(100);
            ResolutionContext context = board.contextWithoutSource();

            assertTrue(new LessThan(new CurrentHP(new OpponentActive()), new Literal(60))
                    .evaluate(context));
            assertFalse(new GreaterThan(new CurrentHP(new OpponentActive()), new Literal(60))
                    .evaluate(context));
            assertTrue(new EqualTo(new CurrentHP(new OpponentActive()), new Literal(50))
                    .evaluate(context));
        }

        @Test
        @DisplayName("strict, not inclusive")
        void boundariesAreExclusive() {
            ResolutionContext context = board.contextWithoutSource();

            assertFalse(new GreaterThan(new Literal(50), new Literal(50)).evaluate(context));
            assertFalse(new LessThan(new Literal(50), new Literal(50)).evaluate(context));
            assertTrue(new EqualTo(new Literal(50), new Literal(50)).evaluate(context));
        }
    }

    @Nested
    @DisplayName("For lifts a Pokemon condition to the game level")
    class Lifting {

        private final TestBoard board = new TestBoard();

        @Test
        void appliesTheConditionToTheResolvedTarget() {
            PokemonInPlay theirs = board.active(board.them, SNORLAX);
            theirs.addStatus(new PoisonStatus());
            ResolutionContext context = board.contextWithoutSource();

            assertTrue(new For(new IsPoisoned(), new OpponentActive()).evaluate(context));
            assertFalse(new For(new IsBurned(), new OpponentActive()).evaluate(context));
        }

        @Test
        @DisplayName("an unresolvable target is false — nothing satisfies a condition")
        void unresolvedTargetIsFalse() {
            ResolutionContext context = board.contextWithoutSource();

            assertFalse(new For(new IsActive(), new AttackerActive()).evaluate(context));
            assertFalse(new For(new IsActive(), new Self()).evaluate(context));
        }

        @Test
        void forAnyScansAGroup() {
            board.bench(board.them, PIKACHU);
            PokemonInPlay hurt = board.bench(board.them, SNORLAX);
            hurt.takeDamage(10);
            ResolutionContext context = board.contextWithoutSource();

            assertTrue(new ForAny(new IsDamaged(), new OpponentBench()).evaluate(context));
            assertFalse(new ForAny(new IsBurned(), new OpponentBench()).evaluate(context));
        }

        @Test
        void forAnyIsFalseForAnEmptyGroup() {
            assertFalse(new ForAny(new IsDamaged(), new AttackerBench())
                    .evaluate(board.contextWithoutSource()));
        }
    }

    @Nested
    @DisplayName("statuses")
    class Statuses {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay pokemon = board.active(board.you, PIKACHU);

        @Test
        void eachStatusHasItsOwnCheck() {
            pokemon.addStatus(new ConfusionStatus());

            assertTrue(new IsConfused().evaluate(pokemon));
            assertFalse(new IsAsleep().evaluate(pokemon));
            assertFalse(new IsParalyzed().evaluate(pokemon));
            assertFalse(new IsPoisoned().evaluate(pokemon));
            assertFalse(new IsBurned().evaluate(pokemon));
        }

        @Test
        @DisplayName("poison and burn stack with each other and with a special condition")
        void damageOverTimeStatusesCoexist() {
            pokemon.addStatus(new PoisonStatus());
            pokemon.addStatus(new BurnStatus());
            pokemon.addStatus(new SleepStatus());

            assertTrue(new IsPoisoned().evaluate(pokemon));
            assertTrue(new IsBurned().evaluate(pokemon));
            assertTrue(new IsAsleep().evaluate(pokemon));
            assertEquals(3, pokemon.statuses().size());
        }

        @Test
        @DisplayName("a second special condition replaces the first rather than stacking")
        void specialConditionsDoNotStack() {
            pokemon.addStatus(new PoisonStatus());
            pokemon.addStatus(new SleepStatus());
            pokemon.addStatus(new ParalysisStatus());

            assertFalse(new IsAsleep().evaluate(pokemon));
            assertTrue(new IsParalyzed().evaluate(pokemon));
            assertTrue(new IsPoisoned().evaluate(pokemon), "poison is unaffected");
            assertEquals(2, pokemon.statuses().size());
        }
    }

    @Nested
    @DisplayName("board position and card facts")
    class BoardFacts {

        private final TestBoard board = new TestBoard();

        @Test
        void positionComesFromTheInstance() {
            PokemonInPlay active = board.active(board.you, PIKACHU);
            PokemonInPlay benched = board.bench(board.you, SNORLAX);

            assertTrue(new IsActive().evaluate(active));
            assertFalse(new IsBenched().evaluate(active));
            assertTrue(new IsBenched().evaluate(benched));
        }

        @Test
        void damageAndTools() {
            PokemonInPlay pokemon = board.active(board.you, PIKACHU);

            assertFalse(new IsDamaged().evaluate(pokemon));
            assertFalse(new HasTool().evaluate(pokemon));

            pokemon.takeDamage(10);
            pokemon.attachTool(board.loose(board.you, SNORLAX));

            assertTrue(new IsDamaged().evaluate(pokemon));
            assertTrue(new HasTool().evaluate(pokemon));
        }

        @Test
        void typeComesFromTheDefinition() {
            PokemonInPlay pokemon = board.active(board.you, CHARIZARD);

            assertTrue(new HasType(Type.FIRE).evaluate(pokemon));
            assertFalse(new HasType(Type.WATER).evaluate(pokemon));
        }

        @Test
        @DisplayName("a dual-type Pokemon has both of its types, and no others")
        void dualTypeHasEitherType() {
            PokemonInPlay pokemon = board.active(board.you,
                    TestBoard.card("Steamvine", 90, Type.GRASS).withTypes(Type.GRASS, Type.WATER));

            assertTrue(new HasType(Type.GRASS).evaluate(pokemon));
            assertTrue(new HasType(Type.WATER).evaluate(pokemon));
            assertFalse(new HasType(Type.FIRE).evaluate(pokemon));
        }

        @Test
        @DisplayName("CardInstance-subjected conditions accept a Pokemon by subtyping")
        void cardLevelConditions() {
            CardInstance inHand = board.inHand(board.you, PIKACHU);
            PokemonInPlay onBench = board.bench(board.you, PIKACHU);

            assertTrue(new InZone(Zone.HAND).evaluate(inHand));
            assertFalse(new InZone(Zone.HAND).evaluate(onBench));
            assertTrue(new IsSpecies("Pikachu").evaluate(inHand));
            assertTrue(new IsSpecies("Pikachu").evaluate(onBench));
            assertFalse(new IsSpecies("Snorlax").evaluate(inHand));
        }

        @Test
        void tagsGateTheTwoPointRule() {
            PokemonCard pikachuEx = PokemonCard.basic("pikachu-ex", "Pikachu ex", 120, Type.LIGHTNING, 1)
                    .withTags(CardTag.EX);

            assertTrue(new HasTag(CardTag.EX).evaluate(board.inHand(board.you, pikachuEx)));
            assertFalse(new HasTag(CardTag.EX).evaluate(board.inHand(board.you, PIKACHU)));
        }

        @Test
        void stadiumIsMatchedById() {
            ResolutionContext context = board.contextWithoutSource();
            assertFalse(new StadiumInPlay("pikachu").evaluate(context));

            board.battle.setStadium(board.loose(board.you, PIKACHU));

            assertTrue(new StadiumInPlay("pikachu").evaluate(context));
            assertFalse(new StadiumInPlay("snorlax").evaluate(context));
        }
    }

    @Nested
    @DisplayName("HasEnergy — colorless is a wildcard")
    class EnergyGating {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay pokemon = board.active(board.you, PIKACHU);

        @Test
        void aFreeCostIsAlwaysPayable() {
            assertTrue(new HasEnergy(EnergyCost.free()).evaluate(pokemon));
        }

        @Test
        void typedCostNeedsThatType() {
            pokemon.attachEnergy(Type.LIGHTNING, 2);

            assertTrue(new HasEnergy(EnergyCost.of(Type.LIGHTNING, 2)).evaluate(pokemon));
            assertFalse(new HasEnergy(EnergyCost.of(Type.LIGHTNING, 3)).evaluate(pokemon));
            assertFalse(new HasEnergy(EnergyCost.of(Type.WATER, 1)).evaluate(pokemon));
        }

        @Test
        @DisplayName("colorless is paid by leftover energy of any type")
        void colorlessAcceptsAnything() {
            pokemon.attachEnergy(Type.WATER, 2);

            assertTrue(new HasEnergy(EnergyCost.of(Type.COLORLESS, 2)).evaluate(pokemon));
            assertFalse(new HasEnergy(EnergyCost.of(Type.COLORLESS, 3)).evaluate(pokemon));
        }

        @Test
        @DisplayName("colorless is paid only from what the typed part did not consume")
        void colorlessDoesNotDoubleCountTypedEnergy() {
            EnergyCost lightningPlusOne = EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 1);

            pokemon.attachEnergy(Type.LIGHTNING, 1);
            assertFalse(new HasEnergy(lightningPlusOne).evaluate(pokemon),
                    "one Lightning cannot pay both the Lightning and the Colorless");

            pokemon.attachEnergy(Type.WATER, 1);
            assertTrue(new HasEnergy(lightningPlusOne).evaluate(pokemon));
        }

        @Test
        void twoOfTheTypedElementAlsoPaysTheColorless() {
            pokemon.attachEnergy(Type.LIGHTNING, 2);

            assertTrue(new HasEnergy(EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 1))
                    .evaluate(pokemon));
        }

        @Test
        void wrongTypeCannotPayTheTypedPart() {
            pokemon.attachEnergy(Type.WATER, 5);

            assertFalse(new HasEnergy(EnergyCost.of(Type.LIGHTNING, 1)).evaluate(pokemon));
        }

        @Test
        void costsDropZeroEntriesAndReportTheirTotal() {
            EnergyCost cost = new EnergyCost(Map.of(Type.FIRE, 2, Type.WATER, 0));

            assertEquals(2, cost.total());
            assertEquals(0, cost.requirementFor(Type.WATER));
            assertFalse(cost.isFree());
        }
    }

    @Nested
    @DisplayName("coin flips and chance")
    class Chance {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay pokemon = board.active(board.you, PIKACHU);

        @Test
        void flipConditionsAreFalseBeforeAnyFlip() {
            ResolutionContext context = board.contextFor(pokemon);

            assertFalse(new LastCoinTossHeads().evaluate(context));
            assertFalse(new AllFlipsHeads().evaluate(context));
        }

        @Test
        void lastCoinTossReadsTheFinalFlip() {
            ResolutionContext context = board.contextFor(pokemon);

            context.scope().recordFlip(FlipResult.of(true, false));
            assertFalse(new LastCoinTossHeads().evaluate(context));

            context.scope().recordFlip(FlipResult.of(false, true));
            assertTrue(new LastCoinTossHeads().evaluate(context));
        }

        @Test
        void allFlipsHeadsNeedsEveryFlip() {
            ResolutionContext context = board.contextFor(pokemon);

            context.scope().recordFlip(FlipResult.of(true, true));
            assertTrue(new AllFlipsHeads().evaluate(context));

            context.scope().recordFlip(FlipResult.of(true, false));
            assertFalse(new AllFlipsHeads().evaluate(context));
        }

        @Test
        @DisplayName("an empty flip is not all-heads, despite vacuous truth")
        void anEmptyFlipIsNotAllHeads() {
            ResolutionContext context = board.contextFor(pokemon);
            context.scope().recordFlip(FlipResult.of());

            assertFalse(new AllFlipsHeads().evaluate(context));
        }

        @Test
        void probabilityBoundariesNeedNoRandomness() {
            ResolutionContext context = board.contextFor(pokemon);

            assertFalse(new Probability(new Literal(0)).evaluate(context));
            assertFalse(new Probability(new Literal(-10)).evaluate(context));
            assertTrue(new Probability(new Literal(100)).evaluate(context));
            assertTrue(new Probability(new Literal(150)).evaluate(context));
        }

        @Test
        @DisplayName("the same seed replays the same sequence")
        void probabilityIsReproducible() {
            List<Boolean> first = rollTwenty(new TestBoard(99L));
            List<Boolean> second = rollTwenty(new TestBoard(99L));
            List<Boolean> different = rollTwenty(new TestBoard(1234L));

            assertEquals(first, second);
            assertTrue(first.contains(true) && first.contains(false), "50% should vary");
            assertNotEquals(first, different);
        }

        private List<Boolean> rollTwenty(TestBoard fresh) {
            ResolutionContext context = fresh.contextWithoutSource();
            Probability coinish = new Probability(new Literal(50));
            return IntStream.range(0, 20)
                    .mapToObj(i -> coinish.evaluate(context))
                    .toList();
        }
    }

    @Nested
    @DisplayName("contract")
    class Contract {

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("conditions are values, so they compare structurally")
        void conditionsAreValues() {
            assertEquals(new HasType(Type.FIRE), new HasType(Type.FIRE));
            assertEquals(new IsBurned(), new IsBurned());
            assertEquals(
                    new For(new IsBurned(), new OpponentActive()),
                    new For(new IsBurned(), new OpponentActive()));
            assertNotEquals(new HasType(Type.FIRE), new HasType(Type.WATER));
        }

        @Test
        @DisplayName("evaluating does not mutate the board")
        void evaluationIsPure() {
            PokemonInPlay pokemon = board.active(board.you, PIKACHU);
            pokemon.addStatus(new BurnStatus());
            pokemon.takeDamage(20);

            ICondition<PokemonInPlay> condition = new And<>(new IsBurned(), new IsDamaged());

            assertTrue(condition.evaluate(pokemon));
            assertTrue(condition.evaluate(pokemon));
            assertEquals(20, pokemon.damage());
            assertEquals(1, pokemon.statuses().size());
        }
    }
}
