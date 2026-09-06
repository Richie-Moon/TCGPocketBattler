package com.tcgpocket.effect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.TestBoard;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.condition.IsType;
import com.tcgpocket.condition.Not;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.AttackerAll;
import com.tcgpocket.target.AttackerBenchSpecific;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentBench;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.Self;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class IEffectTest {

    private static final PokemonCard PIKACHU = TestBoard.card("Pikachu", 60, Type.LIGHTNING);
    private static final PokemonCard ODDISH = TestBoard.card("Oddish", 60, Type.GRASS);
    private static final PokemonCard SNORLAX = TestBoard.card("Snorlax", 150, Type.COLORLESS);

    /** Weak to Lightning. */
    private static final PokemonCard MAROWAK = PokemonCard.basic("marowak", "Marowak", 120, Type.FIGHTING, 2)
            .withWeakness(Type.LIGHTNING);

    @Nested
    @DisplayName("outcome semantics")
    class Outcomes {

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("an unresolvable target is a failure, which aborts an attempt")
        void unresolvedTargetFails() {
            ResolutionContext context = board.contextWithoutSource();

            assertEquals(EffectOutcome.FAILED,
                    new DealDamage(new Literal(30), new OpponentActive()).apply(context));
            assertEquals(EffectOutcome.FAILED,
                    new HealDamage(new Literal(30), new Self()).apply(context));
        }

        @Test
        @DisplayName("nothing to do is a no-op, which does not abort")
        void nothingToDoIsANoOp() {
            PokemonInPlay pokemon = board.active(board.you, PIKACHU);
            ResolutionContext context = board.contextFor(pokemon);

            assertEquals(EffectOutcome.NO_OP,
                    new HealDamage(new Literal(30), new Self()).apply(context),
                    "healing a Pokemon at full HP");
            assertEquals(EffectOutcome.NO_OP,
                    new RemoveStatus(new Self(), new PoisonStatus()).apply(context),
                    "removing a status it does not have");
            assertEquals(EffectOutcome.NO_OP, new NoEffect().apply(context));
        }

        @Test
        void anEmptyGroupIsANoOpRatherThanAFailure() {
            board.active(board.you, PIKACHU);
            ResolutionContext context = board.contextWithoutSource();

            assertEquals(EffectOutcome.NO_OP,
                    new DamageEach(new Literal(20), new OpponentBench()).apply(context));
        }
    }

    @Nested
    @DisplayName("costs are all-or-nothing")
    class Costs {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay pokemon = board.active(board.you, PIKACHU);
        private final ResolutionContext context = board.contextFor(pokemon);

        @Test
        @DisplayName("too little energy discards nothing and fails")
        void randomEnergyCostIsNotPartiallyPaid() {
            pokemon.attachEnergy(Type.LIGHTNING, 1);

            assertEquals(EffectOutcome.FAILED,
                    new DiscardRandomEnergy(new Literal(2), new Self()).apply(context));
            assertEquals(1, pokemon.totalEnergy(), "the one energy is still attached");
        }

        @Test
        void typedEnergyCostIsNotPartiallyPaid() {
            pokemon.attachEnergy(Type.LIGHTNING, 1);
            pokemon.attachEnergy(Type.WATER, 3);

            assertEquals(EffectOutcome.FAILED,
                    new DiscardTypeEnergy(Type.LIGHTNING, new Literal(2), new Self()).apply(context));
            assertEquals(4, pokemon.totalEnergy());
        }

        @Test
        void payableCostsGoThrough() {
            pokemon.attachEnergy(Type.LIGHTNING, 2);

            assertEquals(EffectOutcome.APPLIED,
                    new DiscardTypeEnergy(Type.LIGHTNING, new Literal(2), new Self()).apply(context));
            assertEquals(0, pokemon.totalEnergy());
        }

        @Test
        void handCostIsNotPartiallyPaid() {
            board.inHand(board.you, PIKACHU);

            assertEquals(EffectOutcome.FAILED,
                    new DiscardFromHand(new AttackerSide(), new Literal(2)).apply(context));
            assertEquals(1, board.you.hand().size());
            assertEquals(0, board.you.discardPile().size());
        }

        @Test
        void handCostCanBeNarrowedByACondition() {
            board.inHand(board.you, PIKACHU);
            board.inHand(board.you, SNORLAX);
            board.inHand(board.you, PIKACHU);

            EffectOutcome outcome = new DiscardFromHand(
                    new AttackerSide(), new Literal(2),
                    Optional.of(new IsSpecies("Pikachu"))).apply(context);

            assertEquals(EffectOutcome.APPLIED, outcome);
            assertEquals(1, board.you.hand().size());
            assertEquals("Snorlax", board.you.hand().get(0).definition().name());
        }
    }

    @Nested
    @DisplayName("damage")
    class Damage {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay attacker = board.active(board.you, PIKACHU);
        private final PokemonInPlay defender = board.active(board.them, MAROWAK);
        private final ResolutionContext context = board.contextFor(attacker);

        @Test
        @DisplayName("DealDamage runs the full pipeline, so weakness lands without the card saying so")
        void dealDamageAppliesWeakness() {
            assertEquals(EffectOutcome.APPLIED,
                    new DealDamage(new Literal(30), new OpponentActive()).apply(context));

            assertEquals(50, defender.damage());
        }

        @Test
        @DisplayName("bench damage skips weakness")
        void damageEachSkipsWeakness() {
            PokemonInPlay benched = board.bench(board.them, MAROWAK);

            new DamageEach(new Literal(30), new OpponentBench()).apply(context);

            assertEquals(30, benched.damage());
        }

        @Test
        @DisplayName("each hit runs the pipeline separately, so weakness applies per hit")
        void multiHitAppliesPerHit() {
            new MultiHit(new OpponentActive(), new Literal(10), new Literal(3)).apply(context);

            // Three hits of 10 + 20 weakness each, not (30 + 20).
            assertEquals(90, defender.damage());
        }

        @Test
        void spreadDamagePlacesOneCounterPerTen() {
            board.bench(board.them, SNORLAX);
            board.bench(board.them, SNORLAX);

            new SpreadDamage(new Literal(40), new OpponentBench()).apply(context);

            int total = board.them.bench().stream().mapToInt(PokemonInPlay::damage).sum();
            assertEquals(40, total);
        }

        @Test
        void spreadBelowOneCounterDoesNothing() {
            board.bench(board.them, SNORLAX);

            assertEquals(EffectOutcome.NO_OP,
                    new SpreadDamage(new Literal(5), new OpponentBench()).apply(context));
        }

        @Test
        void healingRemovesCounters() {
            defender.takeDamage(50);

            assertEquals(EffectOutcome.APPLIED,
                    new HealDamage(new Literal(30), new OpponentActive()).apply(context));
            assertEquals(20, defender.damage());
        }
    }

    @Nested
    @DisplayName("coin flips feed the numbers that follow them")
    class Flips {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay attacker = board.active(board.you, PIKACHU);
        private final PokemonInPlay defender = board.active(board.them, SNORLAX);
        private final ResolutionContext context = board.contextFor(attacker);

        @Test
        @DisplayName("FlipN records into the scope, where NumberHeads reads it")
        void flipThenDamagePerHeads() {
            new FlipN(new Literal(4)).apply(context);
            int heads = context.scope().lastFlip().orElseThrow().heads();

            new DealDamage(new Product(new NumberHeads(), new Literal(30)), new OpponentActive())
                    .apply(context);

            assertEquals(heads * 30, defender.damage());
            assertEquals(4, context.scope().lastFlip().orElseThrow().flips());
        }

        @Test
        void flippingZeroCoinsIsANoOp() {
            assertEquals(EffectOutcome.NO_OP, new FlipN(new Literal(0)).apply(context));
        }

        @Test
        @DisplayName("FlipUntilTails keeps the closing tails, so heads counts the run")
        void flipUntilTailsEndsOnTails() {
            new FlipUntilTails().apply(context);
            var result = context.scope().lastFlip().orElseThrow();

            assertFalse(result.lastWasHeads());
            assertEquals(result.flips() - 1, result.heads());
        }
    }

    @Nested
    @DisplayName("energy")
    class Energy {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay pokemon = board.active(board.you, PIKACHU);
        private final ResolutionContext context = board.contextFor(pokemon);

        @Test
        void attachFromTheZoneConsumesIt() {
            board.you.registerTypes(Type.LIGHTNING);
            board.you.generateEnergy(board.battle.rng());
            Type available = board.you.currentEnergy().orElseThrow();

            assertEquals(EffectOutcome.APPLIED,
                    new AttachFromEnergyZone(new Self()).apply(context));

            assertEquals(1, pokemon.energyOf(available));
            assertTrue(board.you.currentEnergy().isEmpty(), "the zone is now empty");
        }

        @Test
        void attachingFromAnEmptyZoneFails() {
            assertEquals(EffectOutcome.FAILED,
                    new AttachFromEnergyZone(new Self()).apply(context));
        }

        @Test
        void moveEnergyPreservesTypes() {
            PokemonInPlay benched = board.bench(board.you, SNORLAX);
            pokemon.attachEnergy(Type.LIGHTNING, 2);

            assertEquals(EffectOutcome.APPLIED,
                    new MoveEnergy(new Self(), new AttackerBenchSpecific(0), new Literal(1))
                            .apply(context));

            assertEquals(1, pokemon.totalEnergy());
            assertEquals(1, benched.energyOf(Type.LIGHTNING));
        }
    }

    @Nested
    @DisplayName("duration effects install modifiers")
    class Durations {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay pokemon = board.active(board.you, PIKACHU);
        private final ResolutionContext context = board.contextFor(pokemon);

        @Test
        void reductionLastsForItsDurationThenExpires() {
            new ReduceDamage(new Literal(20), new Self(), new Literal(1)).apply(context);

            assertEquals(1, pokemon.modifiers().size());
            assertTrue(pokemon.modifiers().get(0).isActiveOn(board.battle.turn()));
            assertTrue(pokemon.modifiers().get(0).isActiveOn(board.battle.turn() + 1));
            assertFalse(pokemon.modifiers().get(0).isActiveOn(board.battle.turn() + 2));
        }

        @Test
        void expiringDropsThem() {
            new IncreaseDamage(new Literal(10), new Self(), new Literal(0)).apply(context);
            assertEquals(1, pokemon.modifiers().size());

            pokemon.expireModifiers(board.battle.turn() + 1);

            assertEquals(0, pokemon.modifiers().size());
        }

        @Test
        void preventionInstallsItsOwnKind() {
            assertEquals(EffectOutcome.APPLIED,
                    new PreventDamage(new Self(), new Literal(1)).apply(context));
            assertEquals(com.tcgpocket.state.ModifierKind.PREVENT_DAMAGE,
                    pokemon.modifiers().get(0).kind());
        }
    }

    @Nested
    @DisplayName("board changes")
    class BoardChanges {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay active = board.active(board.them, PIKACHU);
        private final PokemonInPlay benched = board.bench(board.them, SNORLAX);
        private final ResolutionContext context = board.contextWithoutSource();

        @Test
        @DisplayName("switching sheds the outgoing Pokemon's statuses — the way out of Paralysis")
        void switchingClearsTemporaryState() {
            active.addStatus(new ParalysisStatus());

            assertEquals(EffectOutcome.APPLIED, new SwitchActive(new OpponentSide()).apply(context));

            assertSame(benched, board.them.active().orElseThrow());
            assertTrue(board.them.bench().contains(active));
            assertTrue(active.statuses().isEmpty(), "statuses come off when leaving the active spot");
        }

        @Test
        void switchingWithAnEmptyBenchFails() {
            board.you.setActive(board.active(board.you, PIKACHU));

            assertEquals(EffectOutcome.FAILED, new SwitchActive(new AttackerSide()).apply(context));
        }

        @Test
        void statusesGoOnAndComeOff() {
            assertEquals(EffectOutcome.APPLIED,
                    new AddStatus(new OpponentActive(), new SleepStatus()).apply(context));
            assertTrue(active.hasStatus(new SleepStatus()));

            assertEquals(EffectOutcome.NO_OP,
                    new AddStatus(new OpponentActive(), new SleepStatus()).apply(context),
                    "applying the same status twice changes nothing");

            assertEquals(EffectOutcome.APPLIED,
                    new RemoveStatus(new OpponentActive(), new SleepStatus()).apply(context));
            assertFalse(active.hasStatus(new SleepStatus()));
        }

        @Test
        void toolsComeFromHandAndOnlyOneFits() {
            ToolCard cape = ToolCard.named("giant-cape", "Giant Cape");
            board.inHand(board.them, cape);
            ResolutionContext theirCard = ResolutionContext.of(board.battle, board.them, active);

            assertEquals(EffectOutcome.APPLIED,
                    new AttachTool(new OpponentActive(), cape).apply(theirCard));
            assertTrue(active.hasTool());
            assertEquals(0, board.them.hand().size(), "the Tool left hand");

            assertEquals(EffectOutcome.FAILED,
                    new AttachTool(new OpponentActive(), cape).apply(theirCard),
                    "a Pokemon may hold only one Tool");
        }

        @Test
        void attachingAToolYouDoNotHoldFails() {
            ToolCard cape = ToolCard.named("giant-cape", "Giant Cape");
            ResolutionContext theirCard = ResolutionContext.of(board.battle, board.them, active);

            assertEquals(EffectOutcome.FAILED,
                    new AttachTool(new OpponentActive(), cape).apply(theirCard));
        }
    }

    @Nested
    @DisplayName("cards")
    class Cards {

        private final TestBoard board = new TestBoard();
        private final ResolutionContext context = board.contextWithoutSource();

        @Test
        void drawMovesCardsFromDeckToHand() {
            board.inDeck(board.you, PIKACHU);
            board.inDeck(board.you, SNORLAX);

            assertEquals(EffectOutcome.APPLIED,
                    new DrawCard(new Literal(2), new AttackerSide()).apply(context));
            assertEquals(2, board.you.hand().size());
            assertEquals(0, board.you.deck().size());
        }

        @Test
        @DisplayName("running out of deck is a no-op, since decking out is not a loss")
        void drawingFromAnEmptyDeckIsANoOp() {
            assertEquals(EffectOutcome.NO_OP,
                    new DrawCard(new Literal(3), new AttackerSide()).apply(context));
        }

        @Test
        void aShortDrawStillCounts() {
            board.inDeck(board.you, PIKACHU);

            assertEquals(EffectOutcome.APPLIED,
                    new DrawCard(new Literal(3), new AttackerSide()).apply(context));
            assertEquals(1, board.you.hand().size());
        }

        @Test
        void shufflingHandIntoDeckMovesEverything() {
            board.inHand(board.you, PIKACHU);
            board.inHand(board.you, SNORLAX);

            assertEquals(EffectOutcome.APPLIED,
                    new ShuffleHandIntoDeck(new AttackerSide()).apply(context));
            assertEquals(0, board.you.hand().size());
            assertEquals(2, board.you.deck().size());
        }
    }

    @Nested
    @DisplayName("composition")
    class Composition {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay attacker = board.active(board.you, PIKACHU);
        private final PokemonInPlay defender = board.active(board.them, SNORLAX);
        private final ResolutionContext context = board.contextFor(attacker);

        @Test
        void conditionalAppliesOnlyWhenTheConditionHolds() {
            IEffect hit = new DealDamage(new Literal(30), new OpponentActive());

            assertEquals(EffectOutcome.NO_OP,
                    new ConditionalEffect(new Not<>(new Always<>()), hit).apply(context));
            assertEquals(0, defender.damage());

            assertEquals(EffectOutcome.APPLIED,
                    new ConditionalEffect(new Always<>(), hit).apply(context));
            assertEquals(30, defender.damage());
        }

        @Test
        void repeatRunsItNTimes() {
            new RepeatEffect(new Literal(3), new DealDamage(new Literal(10), new OpponentActive()))
                    .apply(context);

            assertEquals(30, defender.damage());
        }

        @Test
        @DisplayName("repeat stops at the first failure rather than grinding on")
        void repeatShortCircuitsOnFailure() {
            attacker.attachEnergy(Type.LIGHTNING, 2);

            EffectOutcome outcome = new RepeatEffect(
                    new Literal(5),
                    new DiscardTypeEnergy(Type.LIGHTNING, new Literal(1), new Self()))
                    .apply(context);

            assertEquals(EffectOutcome.FAILED, outcome);
            assertEquals(0, attacker.totalEnergy(), "the two payable rounds went through");
        }

        @Test
        void repeatingZeroTimesIsANoOp() {
            assertEquals(EffectOutcome.NO_OP,
                    new RepeatEffect(new Literal(0), new NoEffect()).apply(context));
        }
    }

    @Nested
    @DisplayName("effects are values")
    class Values {

        @Test
        void structuralEquality() {
            assertEquals(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new DealDamage(new Literal(30), new OpponentActive()));
            assertEquals(new NoEffect(), new NoEffect());
        }

        @Test
        void cardsAndInstancesStayDistinct() {
            TestBoard board = new TestBoard();
            CardInstance first = board.inHand(board.you, PIKACHU);
            CardInstance second = board.inHand(board.you, PIKACHU);

            assertEquals(first.definition(), second.definition());
            assertFalse(first.instanceId() == second.instanceId());
        }
    }

    @Nested
    @DisplayName("SearchDeck — reaching into the deck")
    class Searching {

        private final TestBoard board = new TestBoard();
        private final ResolutionContext context = board.contextWithoutSource();

        private static final PokemonCard CHARMANDER =
                TestBoard.card("Charmander", 60, com.tcgpocket.energy.Type.FIRE);

        @Test
        @DisplayName("takes a matching card out of the deck and into hand")
        void findsWhatItIsLookingFor() {
            board.inDeck(board.you, CHARMANDER);
            CardInstance oddish = board.inDeck(board.you, ODDISH);

            assertEquals(EffectOutcome.APPLIED,
                    new SearchDeck(new AttackerSide(), new Literal(1),
                            new IsType(com.tcgpocket.energy.Type.GRASS)).apply(context));

            assertEquals(List.of(oddish), board.you.hand());
            assertEquals(1, board.you.deck().size(), "and it left the deck");
        }

        @Test
        @DisplayName("finding nothing is a no-op, so it cannot abort an attempt")
        void noMatchIsANoOp() {
            board.inDeck(board.you, CHARMANDER);

            assertEquals(EffectOutcome.NO_OP,
                    new SearchDeck(new AttackerSide(), new Literal(1),
                            new IsType(com.tcgpocket.energy.Type.GRASS)).apply(context));

            assertTrue(board.you.hand().isEmpty());
            assertEquals(1, board.you.deck().size());
        }

        @Test
        void anEmptyDeckIsANoOp() {
            assertEquals(EffectOutcome.NO_OP,
                    new SearchDeck(new AttackerSide(), new Literal(1),
                            new IsType(com.tcgpocket.energy.Type.GRASS)).apply(context));
        }

        @Test
        @DisplayName("asking for two never picks the same card twice")
        void takesDistinctCards() {
            board.inDeck(board.you, ODDISH);
            board.inDeck(board.you, ODDISH);
            board.inDeck(board.you, ODDISH);

            new SearchDeck(new AttackerSide(), new Literal(2),
                    new IsType(com.tcgpocket.energy.Type.GRASS)).apply(context);

            assertEquals(2, board.you.hand().size());
            assertEquals(2, board.you.hand().stream().distinct().count());
            assertEquals(1, board.you.deck().size());
        }

        @Test
        @DisplayName("finding fewer than asked for takes what is there")
        void takesWhatItCan() {
            board.inDeck(board.you, ODDISH);

            assertEquals(EffectOutcome.APPLIED,
                    new SearchDeck(new AttackerSide(), new Literal(3),
                            new IsType(com.tcgpocket.energy.Type.GRASS)).apply(context));

            assertEquals(1, board.you.hand().size());
        }

        @Test
        void noConditionMeansAnyCard() {
            board.inDeck(board.you, CHARMANDER);

            assertEquals(EffectOutcome.APPLIED,
                    new SearchDeck(new AttackerSide(), new Literal(1), Optional.empty())
                            .apply(context));

            assertEquals(1, board.you.hand().size());
        }

        @Test
        @DisplayName("a Trainer card has no type, so IsType passes it over")
        void trainersAreNotGrassPokemon() {
            CardInstance potion = board.inDeck(
                    board.you, ToolCard.named("giant-cape", "Giant Cape"));

            assertFalse(new IsType(com.tcgpocket.energy.Type.GRASS).evaluate(potion));

            assertEquals(EffectOutcome.NO_OP,
                    new SearchDeck(new AttackerSide(), new Literal(1),
                            new IsType(com.tcgpocket.energy.Type.GRASS)).apply(context));
        }
    }

    @Nested
    @DisplayName("HealEach — healing a group")
    class GroupHealing {

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("a group already at full HP is a no-op, not a failure")
        void nothingToHealIsANoOp() {
            board.active(board.you, PIKACHU);
            board.bench(board.you, PIKACHU);

            assertEquals(EffectOutcome.NO_OP,
                    new HealEach(new Literal(20), new AttackerAll())
                            .apply(board.contextWithoutSource()));
        }

        @Test
        @DisplayName("one Pokemon healing is enough to count as applied")
        void appliesWhenAnyoneHeals() {
            board.active(board.you, PIKACHU);
            PokemonInPlay benched = board.bench(board.you, PIKACHU);
            benched.takeDamage(30);

            assertEquals(EffectOutcome.APPLIED,
                    new HealEach(new Literal(20), new AttackerAll())
                            .apply(board.contextWithoutSource()));
            assertEquals(10, benched.damage());
        }

        @Test
        void anEmptyGroupIsANoOp() {
            assertEquals(EffectOutcome.NO_OP,
                    new HealEach(new Literal(20), new OpponentBench())
                            .apply(board.contextWithoutSource()));
        }
    }
}
