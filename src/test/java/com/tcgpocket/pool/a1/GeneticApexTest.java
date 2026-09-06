package com.tcgpocket.pool.a1;

import com.tcgpocket.ScriptedRandom;
import com.tcgpocket.TestBoard;
import com.tcgpocket.action.IAction;
import com.tcgpocket.action.UseAbilityAction;
import com.tcgpocket.card.IAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.trigger.TriggerDispatcher;
import com.tcgpocket.trigger.TurnEnd;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * One assertion per card that the tree does what the printed text says.
 *
 * <p>This is the point of writing cards as data: the expected value of the test
 * <em>is</em> the card text, so a wrong tree shows up as a wrong number rather
 * than as a subtly wrong game three hundred turns later.
 */
class GeneticApexTest {

    /** A wall to attack into, so nothing dies mid-test and confuses the numbers. */
    private static final PokemonCard WALL = TestBoard.card("Snorlax", 500, Type.COLORLESS);

    private static IAction attack(PokemonCard card, String name) {
        return card.actions().stream()
                .filter(action -> action instanceof com.tcgpocket.action.Action attackAction
                        && attackAction.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError(card.name() + " has no attack " + name));
    }

    @Nested
    @DisplayName("Grass — the Bulbasaur line")
    class GrassLine {

        private final TestBoard board = new TestBoard();

        @Test
        void vineWhipDoes40() {
            PokemonInPlay bulbasaur = board.active(board.you, Grass.BULBASAUR);
            PokemonInPlay wall = board.active(board.them, WALL);

            attack(Grass.BULBASAUR, "Vine Whip").execute(board.contextFor(bulbasaur));

            assertEquals(40, wall.damage());
        }

        @Test
        @DisplayName("Giant Bloom does 100 and heals 30 from itself")
        void giantBloomHealsAfterwards() {
            PokemonInPlay venusaur = board.active(board.you, Grass.VENUSAUR_EX);
            PokemonInPlay wall = board.active(board.them, WALL);
            venusaur.takeDamage(80);

            attack(Grass.VENUSAUR_EX, "Giant Bloom").execute(board.contextFor(venusaur));

            assertEquals(100, wall.damage());
            assertEquals(50, venusaur.damage(), "80 taken, 30 healed");
        }

        @Test
        @DisplayName("healing nothing does not spoil the attack")
        void healingAtFullHpIsANoOp() {
            PokemonInPlay venusaur = board.active(board.you, Grass.VENUSAUR_EX);
            PokemonInPlay wall = board.active(board.them, WALL);

            AttemptResult result =
                    attack(Grass.VENUSAUR_EX, "Giant Bloom").execute(board.contextFor(venusaur));

            assertTrue(result.succeeded());
            assertEquals(100, wall.damage());
            assertEquals(0, venusaur.damage());
        }

        @Test
        @DisplayName("Find a Friend pulls a Grass Pokemon out of the deck")
        void findAFriendSearchesTheDeck() {
            PokemonInPlay caterpie = board.active(board.you, Grass.CATERPIE);
            board.active(board.them, WALL);
            board.inDeck(board.you, TestBoard.card("Charmander", 60, Type.FIRE));
            board.inDeck(board.you, Grass.BULBASAUR);

            attack(Grass.CATERPIE, "Find a Friend").execute(board.contextFor(caterpie));

            assertEquals(1, board.you.hand().size());
            assertEquals("Bulbasaur", board.you.hand().get(0).definition().name(),
                    "the Charmander was passed over");
            assertEquals(1, board.you.deck().size());
        }

        @Test
        @DisplayName("no Grass Pokemon in the deck is a legal nothing-happened")
        void findAFriendWithNothingToFind() {
            PokemonInPlay caterpie = board.active(board.you, Grass.CATERPIE);
            board.active(board.them, WALL);
            board.inDeck(board.you, TestBoard.card("Charmander", 60, Type.FIRE));

            AttemptResult result =
                    attack(Grass.CATERPIE, "Find a Friend").execute(board.contextFor(caterpie));

            assertTrue(result.succeeded(), "an empty search must not fail the attack");
            assertTrue(board.you.hand().isEmpty());
        }

        @Test
        void theLineEvolvesInOrder() {
            assertTrue(Grass.BULBASAUR.isBasic());
            assertEquals("Bulbasaur", Grass.IVYSAUR.evolvesFrom().orElseThrow());
            assertEquals("Ivysaur", Grass.VENUSAUR.evolvesFrom().orElseThrow());
            assertEquals("Ivysaur", Grass.VENUSAUR_EX.evolvesFrom().orElseThrow());
            assertEquals(2, Grass.VENUSAUR_EX.stage());
        }
    }

    @Nested
    @DisplayName("Grass — Butterfree's Powder Heal")
    class PowderHeal {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay butterfree = board.active(board.you, Grass.BUTTERFREE);
        private final IAbility ability = Grass.BUTTERFREE.ability().orElseThrow();

        @Test
        @DisplayName("heals the Active and every benched Pokemon, 20 each")
        void healsTheWholeSide() {
            PokemonInPlay benchedOne = board.bench(board.you, WALL);
            PokemonInPlay benchedTwo = board.bench(board.you, WALL);
            butterfree.takeDamage(50);
            benchedOne.takeDamage(50);
            benchedTwo.takeDamage(10);

            AttemptResult result = new UseAbilityAction(butterfree, ability)
                    .execute(board.contextWithoutSource());

            assertTrue(result.succeeded());
            assertEquals(30, butterfree.damage(), "the Active heals too, not just the bench");
            assertEquals(30, benchedOne.damage());
            assertEquals(0, benchedTwo.damage(), "healing past full stops at full");
        }

        @Test
        @DisplayName("the opponent's Pokemon are not yours")
        void leavesTheOtherSideAlone() {
            PokemonInPlay theirs = board.active(board.them, WALL);
            theirs.takeDamage(50);
            butterfree.takeDamage(50);

            new UseAbilityAction(butterfree, ability).execute(board.contextWithoutSource());

            assertEquals(30, butterfree.damage());
            assertEquals(50, theirs.damage());
        }

        @Test
        void onceDuringYourTurn() {
            butterfree.takeDamage(50);
            IAction use = new UseAbilityAction(butterfree, ability);
            ResolutionContext context = board.contextWithoutSource();

            assertTrue(use.isLegal(context));
            use.execute(context);
            assertEquals(30, butterfree.damage());

            assertFalse(use.isLegal(context), "once per turn");
            assertTrue(use.execute(context).failed());
            assertEquals(30, butterfree.damage(), "and a second use changes nothing");
        }

        @Test
        @DisplayName("usable with nothing to heal, and still spent")
        void healingNothingIsStillAUse() {
            AttemptResult result = new UseAbilityAction(butterfree, ability)
                    .execute(board.contextWithoutSource());

            assertTrue(result.succeeded());
            assertFalse(result.changedTheBoard());
            assertTrue(butterfree.abilityUsedThisTurn());
        }
    }

    @Nested
    @DisplayName("Lightning — Pikachu ex")
    class PikachuEx {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay pikachu = board.active(board.you, Lightning.PIKACHU_EX);
        private final PokemonInPlay wall = board.active(board.them, WALL);
        private final ResolutionContext context = board.contextFor(pikachu);

        @Test
        @DisplayName("an empty bench means an empty attack")
        void thirtyTimesZero() {
            attack(Lightning.PIKACHU_EX, "Circle Circuit").execute(context);

            assertEquals(0, wall.damage());
        }

        @Test
        void thirtyForEachBenchedPokemon() {
            board.bench(board.you, WALL);
            board.bench(board.you, WALL);
            board.bench(board.you, WALL);

            attack(Lightning.PIKACHU_EX, "Circle Circuit").execute(context);

            assertEquals(90, wall.damage(), "a full bench of three");
        }

        @Test
        @DisplayName("the opponent's bench does not count")
        void countsYourOwnBenchOnly() {
            board.bench(board.you, WALL);
            board.bench(board.them, WALL);
            board.bench(board.them, WALL);

            attack(Lightning.PIKACHU_EX, "Circle Circuit").execute(context);

            assertEquals(30, wall.damage());
        }

        @Test
        void needsTwoLightningToBeLegal() {
            IAction circuit = attack(Lightning.PIKACHU_EX, "Circle Circuit");

            pikachu.attachEnergy(Type.LIGHTNING, 1);
            assertFalse(circuit.isLegal(context));

            pikachu.attachEnergy(Type.LIGHTNING, 1);
            assertTrue(circuit.isLegal(context));
        }
    }

    @Nested
    @DisplayName("Psychic — Mewtwo ex")
    class MewtwoEx {

        private final TestBoard board = new TestBoard();
        private final PokemonInPlay mewtwo = board.active(board.you, Psychic.MEWTWO_EX);
        private final PokemonInPlay wall = board.active(board.them, WALL);

        @Test
        void psydriveDoes150AndDiscardsTwoPsychic() {
            mewtwo.attachEnergy(Type.PSYCHIC, 3);

            AttemptResult result =
                    attack(Psychic.MEWTWO_EX, "Psydrive").execute(board.contextFor(mewtwo));

            assertTrue(result.succeeded());
            assertEquals(150, wall.damage());
            assertEquals(1, mewtwo.energyOf(Type.PSYCHIC));
        }

        @Test
        @DisplayName("the damage lands before the discard, so an unpayable discard costs nothing")
        void orderWithinTheAttemptMatters() {
            mewtwo.attachEnergy(Type.PSYCHIC, 1);

            AttemptResult result =
                    attack(Psychic.MEWTWO_EX, "Psydrive").execute(board.contextFor(mewtwo));

            assertTrue(result.failed(), "the discard could not be paid");
            assertEquals(150, wall.damage(), "but the damage was already dealt");
            assertEquals(1, mewtwo.energyOf(Type.PSYCHIC), "and nothing was partially discarded");
        }

        @Test
        void psychicSphereIsTheCheapOption() {
            attack(Psychic.MEWTWO_EX, "Psychic Sphere").execute(board.contextFor(mewtwo));

            assertEquals(50, wall.damage());
        }
    }

    @Nested
    @DisplayName("Darkness — Koffing")
    class KoffingGas {

        @Test
        @DisplayName("heads poisons, and the poison then ticks on its own")
        void headsPoisons() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysHeads());
            PokemonInPlay koffing = board.active(board.you, Darkness.KOFFING);
            PokemonInPlay wall = board.active(board.them, WALL);

            attack(Darkness.KOFFING, "Gas").execute(board.contextFor(koffing));
            assertTrue(wall.hasStatus(new PoisonStatus()));

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));
            assertEquals(10, wall.damage(), "the card never mentions the 10");
        }

        @Test
        void tailsDoesNothingAndIsStillASuccess() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysTails());
            PokemonInPlay koffing = board.active(board.you, Darkness.KOFFING);
            PokemonInPlay wall = board.active(board.them, WALL);

            AttemptResult result = attack(Darkness.KOFFING, "Gas").execute(board.contextFor(koffing));

            assertTrue(result.succeeded());
            assertFalse(wall.hasStatus(new PoisonStatus()));
        }
    }

    @Nested
    @DisplayName("Trainers")
    class TrainerCards {

        private final TestBoard board = new TestBoard();

        @Test
        void potionHeals20() {
            PokemonInPlay pokemon = board.active(board.you, WALL);
            pokemon.takeDamage(50);

            Trainers.POTION.actions().get(0).execute(board.contextWithoutSource());

            assertEquals(30, pokemon.damage());
        }

        @Test
        void professorsResearchDrawsTwo() {
            board.inDeck(board.you, WALL);
            board.inDeck(board.you, WALL);
            board.inDeck(board.you, WALL);

            Trainers.PROFESSORS_RESEARCH.actions().get(0).execute(board.contextWithoutSource());

            assertEquals(2, board.you.hand().size());
            assertEquals(1, board.you.deck().size());
        }

        @Test
        @DisplayName("Rocky Helmet hits back at whoever attacked its wearer")
        void rockyHelmetRetaliates() {
            PokemonInPlay attacker = board.active(board.you, Grass.BULBASAUR);
            PokemonInPlay defender = board.active(board.them, WALL);
            defender.attachTool(board.loose(board.them, Trainers.ROCKY_HELMET));

            attack(Grass.BULBASAUR, "Vine Whip").execute(board.contextFor(attacker));

            assertEquals(40, defender.damage());
            assertEquals(20, attacker.damage(), "the Helmet answered");
        }

        @Test
        @DisplayName("a Helmet on the bench stays quiet when the Active is hit")
        void rockyHelmetOnlyAnswersForItsWearer() {
            PokemonInPlay attacker = board.active(board.you, Grass.BULBASAUR);
            board.active(board.them, WALL);
            PokemonInPlay benched = board.bench(board.them, WALL);
            benched.attachTool(board.loose(board.them, Trainers.ROCKY_HELMET));

            attack(Grass.BULBASAUR, "Vine Whip").execute(board.contextFor(attacker));

            assertEquals(0, attacker.damage());
        }
    }
}
