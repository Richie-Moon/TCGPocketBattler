package com.tcgpocket.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.TestBoard;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.ItemCard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DrawCard;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.effect.IAttempt;
import com.tcgpocket.effect.PreventAttack;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.player.ScriptedPlayer;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerBenchSpecific;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class IActionTest {

    private static final PokemonCard PIKACHU = TestBoard.card("Pikachu", 60, Type.LIGHTNING);
    private static final PokemonCard SNORLAX = TestBoard.card("Snorlax", 150, Type.COLORLESS);
    private static final PokemonCard RAICHU = PokemonCard.evolution(
            "raichu", "Raichu", 1, "Pikachu", 100, Type.LIGHTNING, 2, List.of());

    private static IAttempt strike(int damage) {
        return new Attempt(new DealDamage(new Literal(damage), new OpponentActive()));
    }

    @Nested
    @DisplayName("Action — an attack is gated on its energy cost")
    class Attacks {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay attacker = board.active(board.you, PIKACHU);
        private final PokemonInPlay defender = board.active(board.them, SNORLAX);
        private final ResolutionContext context = board.contextFor(attacker);

        private final Action thunderShock =
                new Action("Thunder Shock", EnergyCost.of(Type.LIGHTNING, 2), strike(30));

        @Test
        @DisplayName("illegal without the energy, which nothing in the model could express before")
        void costGatesTheAttack() {
            assertFalse(thunderShock.isLegal(context));

            attacker.attachEnergy(Type.LIGHTNING, 2);

            assertTrue(thunderShock.isLegal(context));
        }

        @Test
        void executingRunsTheAttempt() {
            attacker.attachEnergy(Type.LIGHTNING, 2);

            AttemptResult result = thunderShock.execute(context);

            assertTrue(result.succeeded());
            assertEquals(30, defender.damage());
        }

        @Test
        @DisplayName("Asleep and Paralyzed stop an attack even when the cost is paid")
        void specialConditionsForbidAttacking() {
            attacker.attachEnergy(Type.LIGHTNING, 2);

            attacker.addStatus(new SleepStatus());
            assertFalse(thunderShock.isLegal(context));

            attacker.addStatus(new ParalysisStatus());
            assertFalse(thunderShock.isLegal(context));
        }

        @Test
        @DisplayName("an attack lock stops attacking only, and lifts after the locked turn")
        void anAttackLockForbidsAttackingButNotRetreating() {
            attacker.attachEnergy(Type.LIGHTNING, 2);
            board.bench(board.you, SNORLAX);

            new PreventAttack(new Self(), new Literal(1)).apply(context);

            assertFalse(thunderShock.isLegal(context));
            assertTrue(new RetreatAction(new AttackerBenchSpecific(0))
                            .isLegal(board.contextWithoutSource()),
                    "retreating, attaching and Trainers all stay legal");

            board.battle.switchSides();
            assertFalse(thunderShock.isLegal(context), "still locked through the next turn");

            board.battle.switchSides();
            assertTrue(thunderShock.isLegal(context), "and no longer after it");
        }

        @Test
        void aBenchedPokemonCannotAttack() {
            PokemonInPlay benched = board.bench(board.you, PIKACHU);
            benched.attachEnergy(Type.LIGHTNING, 2);

            assertFalse(thunderShock.isLegal(board.contextFor(benched)));
        }

        @Test
        void aFreeAttackNeedsNoEnergy() {
            Action tackle = new Action("Tackle", EnergyCost.free(), strike(10));

            assertTrue(tackle.isLegal(context));
        }
    }

    @Nested
    @DisplayName("combinators")
    class Combinators {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay attacker = board.active(board.you, PIKACHU);
        private final PokemonInPlay defender = board.active(board.them, SNORLAX);
        private final ResolutionContext context = board.contextFor(attacker);

        @Test
        void preconditionGatesLegality() {
            ICondition<ResolutionContext> never = new Not<>(new Always<>());
            IAction gated = new WithPrecondition(
                    never, new Action("Tackle", EnergyCost.free(), strike(10)));

            assertFalse(gated.isLegal(context));
            assertTrue(gated.execute(context).failed());
            assertEquals(0, defender.damage());
        }

        @Test
        void preconditionPassesThroughWhenItHolds() {
            IAction gated = new WithPrecondition(
                    new Always<>(), new Action("Tackle", EnergyCost.free(), strike(10)));

            assertTrue(gated.isLegal(context));
            assertTrue(gated.execute(context).succeeded());
            assertEquals(10, defender.damage());
        }

        @Test
        @DisplayName("MultiAttempt keeps going after one attempt fails, unlike a single Attempt")
        void multiAttemptDoesNotShortCircuit() {
            // The first attempt fails on an unresolvable target; the second must still run.
            IAction action = new MultiAttempt(
                    new Attempt(new DealDamage(new Literal(10), new AttackerBenchSpecific(0))),
                    strike(20));

            AttemptResult result = action.execute(context);

            assertTrue(result.succeeded(), "the second attempt carried it");
            assertEquals(20, defender.damage());
        }

        @Test
        void multiAttemptFailsOnlyWhenEveryAttemptDoes() {
            IAction action = new MultiAttempt(
                    new Attempt(new DealDamage(new Literal(10), new AttackerBenchSpecific(0))),
                    new Attempt(new DealDamage(new Literal(10), new AttackerBenchSpecific(1))));

            assertTrue(action.execute(context).failed());
        }
    }

    @Nested
    @DisplayName("ChoiceAction asks a player")
    class Choices {

        @Test
        @DisplayName("the scripted branch is the one that runs")
        void theChooserPicks() {
            TestBoard board = new TestBoard(
                    new ScriptedPlayer("you", 1), new ScriptedPlayer("them"));
            PokemonInPlay attacker = board.active(board.you, PIKACHU);
            PokemonInPlay defender = board.active(board.them, SNORLAX);
            ResolutionContext context = board.contextFor(attacker);

            IAction choice = new ChoiceAction(
                    List.of(new Action("Weak", EnergyCost.free(), strike(10)),
                            new Action("Strong", EnergyCost.free(), strike(50))),
                    new AttackerSide(), "Choose one:");

            assertTrue(choice.execute(context).succeeded());
            assertEquals(50, defender.damage(), "option 1 was scripted");
        }

        @Test
        @DisplayName("only legal branches are offered, so the indices are into those")
        void illegalBranchesAreNotOffered() {
            TestBoard board = new TestBoard(
                    new ScriptedPlayer("you", 0), new ScriptedPlayer("them"));
            PokemonInPlay attacker = board.active(board.you, PIKACHU);
            PokemonInPlay defender = board.active(board.them, SNORLAX);
            ResolutionContext context = board.contextFor(attacker);

            // The first branch needs energy the attacker does not have.
            IAction choice = new ChoiceAction(
                    List.of(new Action("Costly", EnergyCost.of(Type.LIGHTNING, 3), strike(90)),
                            new Action("Free", EnergyCost.free(), strike(20))),
                    new AttackerSide(), "Choose one:");

            choice.execute(context);

            assertEquals(20, defender.damage(), "index 0 was the only legal branch");
        }

        @Test
        void aChoiceWithNoLegalBranchIsItselfIllegal() {
            TestBoard board = new TestBoard();
            PokemonInPlay attacker = board.active(board.you, PIKACHU);
            board.active(board.them, SNORLAX);
            ResolutionContext context = board.contextFor(attacker);

            IAction choice = new ChoiceAction(
                    List.of(new Action("Costly", EnergyCost.of(Type.LIGHTNING, 3), strike(90))),
                    new AttackerSide(), "Choose one:");

            assertFalse(choice.isLegal(context));
            assertTrue(choice.execute(context).failed());
        }

        @Test
        @DisplayName("a scripted player fails loudly on an unanticipated question")
        void scriptedPlayerRefusesToGuess() {
            TestBoard board = new TestBoard(
                    new ScriptedPlayer("you"), new ScriptedPlayer("them"));
            PokemonInPlay attacker = board.active(board.you, PIKACHU);
            board.active(board.them, SNORLAX);
            ResolutionContext context = board.contextFor(attacker);

            IAction choice = new ChoiceAction(
                    List.of(new Action("A", EnergyCost.free(), strike(10)),
                            new Action("B", EnergyCost.free(), strike(20))),
                    new AttackerSide(), "Choose one:");

            assertThrows(IllegalStateException.class, () -> choice.execute(context));
        }
    }

    @Nested
    @DisplayName("engine-generated: retreating")
    class Retreating {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay active = board.active(board.you, SNORLAX);
        private final PokemonInPlay benched = board.bench(board.you, PIKACHU);
        private final ResolutionContext context = board.contextWithoutSource();
        private final IAction retreat = new RetreatAction(new AttackerBenchSpecific(0));

        @Test
        void needsTheRetreatCostInEnergy() {
            assertFalse(retreat.isLegal(context), "Snorlax costs 1 and has none attached");

            active.attachEnergy(Type.WATER, 1);

            assertTrue(retreat.isLegal(context));
        }

        @Test
        @DisplayName("retreating swaps, pays, and sheds statuses")
        void retreatingClearsStatuses() {
            active.attachEnergy(Type.WATER, 1);
            active.addStatus(new ParalysisStatus());

            // Paralysis forbids retreating, so this is the illegal case first.
            assertFalse(retreat.isLegal(context));

            active.removeStatus(new ParalysisStatus());
            active.addStatus(new SleepStatus());
            assertFalse(retreat.isLegal(context), "Asleep also forbids it");

            active.removeStatus(new SleepStatus());
            assertTrue(retreat.execute(context).succeeded());

            assertSame(benched, board.you.active().orElseThrow());
            assertTrue(board.you.bench().contains(active));
            assertEquals(0, active.totalEnergy(), "the retreat cost was paid");
        }

        @Test
        void cannotRetreatWithAnEmptyBench() {
            TestBoard solo = new TestBoard();
            PokemonInPlay lone = solo.active(solo.you, SNORLAX);
            lone.attachEnergy(Type.WATER, 1);

            assertFalse(new RetreatAction(new AttackerBenchSpecific(0))
                    .isLegal(solo.contextWithoutSource()));
        }

        @Test
        void onlyOnceATurn() {
            active.attachEnergy(Type.WATER, 2);
            assertTrue(retreat.execute(context).succeeded());

            assertFalse(new RetreatAction(new AttackerBenchSpecific(0)).isLegal(context));
        }
    }

    @Nested
    @DisplayName("engine-generated: evolving")
    class Evolving {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay pikachu = board.active(board.you, PIKACHU);
        private final CardInstance raichu = board.inHand(board.you, RAICHU);
        private final ResolutionContext context = board.contextWithoutSource();
        private final IAction evolve = new EvolveAction(raichu, new AttackerActive());

        @Test
        @DisplayName("cannot evolve a Pokemon played this turn")
        void needsAPokemonPlayedEarlier() {
            pikachu.setTurnPlayed(board.battle.turn());
            assertFalse(evolve.isLegal(context));

            pikachu.setTurnPlayed(board.battle.turn() - 1);
            assertTrue(evolve.isLegal(context));
        }

        @Test
        void speciesMustMatch() {
            board.you.setActive(board.active(board.you, SNORLAX));
            board.you.active().orElseThrow().setTurnPlayed(0);

            assertFalse(evolve.isLegal(context), "Raichu does not evolve from Snorlax");
        }

        @Test
        @DisplayName("damage and energy carry over, statuses do not")
        void evolvingKeepsTheBodyButNotTheConditions() {
            pikachu.setTurnPlayed(0);
            pikachu.takeDamage(20);
            pikachu.attachEnergy(Type.LIGHTNING, 2);
            pikachu.addStatus(new SleepStatus());

            assertTrue(evolve.execute(context).succeeded());

            assertEquals("Raichu", pikachu.definition().name());
            assertEquals(100, pikachu.maxHp());
            assertEquals(20, pikachu.damage(), "damage carried over");
            assertEquals(2, pikachu.totalEnergy(), "energy carried over");
            assertTrue(pikachu.statuses().isEmpty(), "statuses did not");
            assertEquals(List.of(PIKACHU), pikachu.evolutionStack());
            assertEquals(0, board.you.hand().size());
        }
    }

    @Nested
    @DisplayName("engine-generated: playing cards and attaching energy")
    class TurnActions {

        private final TestBoard board = new TestBoard();
        private final ResolutionContext context = board.contextWithoutSource();

        @Test
        void aBasicGoesToTheBench() {
            CardInstance card = board.inHand(board.you, PIKACHU);

            assertTrue(new PlayCardAction(card).execute(context).succeeded());
            assertEquals(1, board.you.bench().size());
            assertEquals(0, board.you.hand().size());
        }

        @Test
        void anEvolutionCannotBePlayedStraightToTheBench() {
            CardInstance card = board.inHand(board.you, RAICHU);

            assertFalse(new PlayCardAction(card).isLegal(context));
        }

        @Test
        void theBenchHasALimit() {
            for (int i = 0; i < 3; i++) {
                board.bench(board.you, PIKACHU);
            }
            CardInstance card = board.inHand(board.you, PIKACHU);

            assertTrue(board.you.benchIsFull());
            assertFalse(new PlayCardAction(card).isLegal(context));
        }

        @Test
        @DisplayName("only one Supporter a turn, but Items are unlimited")
        void supporterLimit() {
            SupporterCard professor = SupporterCard.of("professor", "Professor",
                    new PlainAction("Draw 2", new Attempt(
                            new DrawCard(new Literal(2), new AttackerSide()))));
            ItemCard potion = ItemCard.of("potion", "Potion",
                    new PlainAction("Heal", new Attempt(
                            new HealDamage(new Literal(20), new Self()))));

            CardInstance first = board.inHand(board.you, professor);
            CardInstance second = board.inHand(board.you, professor);
            CardInstance item = board.inHand(board.you, potion);

            assertTrue(new PlayCardAction(first).execute(context).succeeded());
            assertFalse(new PlayCardAction(second).isLegal(context));
            assertTrue(new PlayCardAction(item).isLegal(context), "Items are not limited");
        }

        @Test
        @DisplayName("energy attaches once a turn — the limit lives on the action, not the effect")
        void energyAttachesOncePerTurn() {
            PokemonInPlay pokemon = board.active(board.you, PIKACHU);
            board.you.registerTypes(Type.LIGHTNING);
            board.you.generateEnergy(board.battle.rng());

            IAction attach = new AttachEnergyAction(new AttackerActive());

            assertTrue(attach.isLegal(context));
            assertTrue(attach.execute(context).succeeded());
            assertEquals(1, pokemon.totalEnergy());

            board.you.generateEnergy(board.battle.rng());
            assertFalse(attach.isLegal(context), "already attached this turn");
        }

        @Test
        void endTurnIsAlwaysAvailable() {
            assertTrue(new EndTurnAction().isLegal(context));
            assertTrue(new EndTurnAction().execute(context).succeeded());
        }
    }

    @Nested
    @DisplayName("engine-generated: abilities")
    class Abilities {

        @Test
        @DisplayName("once per turn is tracked per Pokemon, so two copies each get a use")
        void oncePerTurnPerPokemon() {
            ActivatedAbility heal = new ActivatedAbility(
                    "Powder Heal",
                    new PlainAction("Heal 20", new Attempt(
                            new HealDamage(new Literal(20), new Self()))));
            PokemonCard butterfree = PokemonCard.basic(
                    "butterfree", "Butterfree", 120, Type.GRASS, 1).withAbility(heal);

            TestBoard board = new TestBoard();
            PokemonInPlay first = board.active(board.you, butterfree);
            PokemonInPlay second = board.bench(board.you, butterfree);
            first.takeDamage(50);
            second.takeDamage(50);
            ResolutionContext context = board.contextWithoutSource();

            IAction useFirst = new UseAbilityAction(first, heal);
            IAction useSecond = new UseAbilityAction(second, heal);

            assertTrue(useFirst.execute(context).succeeded());
            assertEquals(30, first.damage());
            assertFalse(useFirst.isLegal(context), "this one has had its turn");
            assertTrue(useSecond.isLegal(context), "the other copy has not");

            assertTrue(useSecond.execute(context).succeeded());
            assertEquals(30, second.damage());
        }

        @Test
        void anAbilityTheCardDoesNotHaveIsIllegal() {
            ActivatedAbility notMine = new ActivatedAbility(
                    "Borrowed", new PlainAction("", new Attempt()));

            TestBoard board = new TestBoard();
            PokemonInPlay pokemon = board.active(board.you, PIKACHU);

            assertFalse(new UseAbilityAction(pokemon, notMine)
                    .isLegal(board.contextWithoutSource()));
        }

        @Test
        @DisplayName("Self inside an ability means its holder, not whoever is attacking")
        void abilityResolvesWithItsHolderAsSource() {
            ActivatedAbility heal = new ActivatedAbility(
                    "Self Care",
                    new PlainAction("Heal 30", new Attempt(
                            new HealDamage(new Literal(30), new Self()))));
            PokemonCard healer = PokemonCard.basic("healer", "Healer", 90, Type.WATER, 1)
                    .withAbility(heal);

            TestBoard board = new TestBoard();
            PokemonInPlay active = board.active(board.you, PIKACHU);
            PokemonInPlay benchedHealer = board.bench(board.you, healer);
            benchedHealer.takeDamage(40);
            active.takeDamage(40);

            new UseAbilityAction(benchedHealer, heal).execute(board.contextFor(active));

            assertEquals(10, benchedHealer.damage(), "the ability healed its own holder");
            assertEquals(40, active.damage(), "not the active Pokemon");
        }
    }

    @Nested
    @DisplayName("actions are values")
    class Values {

        @Test
        void printedActionsCompareStructurally() {
            assertEquals(
                    new Action("Tackle", EnergyCost.free(), strike(10)),
                    new Action("Tackle", EnergyCost.free(), strike(10)));
            assertEquals(new EndTurnAction(), new EndTurnAction());
        }
    }
}
