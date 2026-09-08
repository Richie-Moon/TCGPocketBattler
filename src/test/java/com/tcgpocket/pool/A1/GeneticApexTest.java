package com.tcgpocket.pool.A1;

import com.tcgpocket.ScriptedRandom;
import com.tcgpocket.TestBoard;
import com.tcgpocket.action.IAction;
import com.tcgpocket.action.UseAbilityAction;
import com.tcgpocket.card.IAbility;
import com.tcgpocket.player.ScriptedPlayer;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.SwitchActive;
import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.trigger.TriggerDispatcher;
import com.tcgpocket.trigger.TurnEnd;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
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
        @DisplayName("not offered with nothing to heal, so the use is not wasted")
        void healingNothingIsNotOffered() {
            IAction use = new UseAbilityAction(butterfree, ability);

            assertFalse(use.isLegal(board.contextWithoutSource()));
            assertTrue(use.execute(board.contextWithoutSource()).failed());
            assertFalse(butterfree.abilityUsedThisTurn(), "a refused use is not spent");
        }

        @Test
        @DisplayName("their damage is not yours: it does not unlock the ability")
        void onlyYourOwnDamageCounts() {
            PokemonInPlay theirs = board.active(board.them, WALL);
            theirs.takeDamage(50);

            assertFalse(new UseAbilityAction(butterfree, ability)
                    .isLegal(board.contextWithoutSource()));
        }
    }

    @Nested
    @DisplayName("Grass — Victreebel's Fragrance Trap")
    class FragranceTrap {

        /** You pick option 1 whenever you are asked; they are never asked. */
        private final ScriptedPlayer you = new ScriptedPlayer("you", 1);
        private final TestBoard board = new TestBoard(you, new ScriptedPlayer("them"));
        private final PokemonInPlay victreebel = board.active(board.you, Grass.VICTREEBEL);
        private final IAbility ability = Grass.VICTREEBEL.ability().orElseThrow();

        private static final PokemonCard EVOLVED = PokemonCard.evolution(
                "gloom", "Gloom", 1, "Oddish", 90, Type.GRASS, 2, List.of());

        @Test
        @DisplayName("you choose which Basic comes up, not your opponent")
        void dragsUpTheBasicYouPicked() {
            PokemonInPlay theirActive = board.active(board.them, WALL);
            board.bench(board.them, Grass.BULBASAUR);
            PokemonInPlay wanted = board.bench(board.them, Grass.CATERPIE);

            AttemptResult result = new UseAbilityAction(victreebel, ability)
                    .execute(board.contextWithoutSource());

            assertTrue(result.succeeded());
            assertSame(wanted, board.them.active().orElseThrow(), "your pick, option 1");
            assertTrue(board.them.bench().contains(theirActive), "the old Active went down");
        }

        @Test
        @DisplayName("evolutions on their bench are not offered")
        void onlyBasicsCanBeDragged() {
            board.active(board.them, WALL);
            PokemonInPlay onlyBasic = board.bench(board.them, Grass.BULBASAUR);
            board.bench(board.them, EVOLVED);

            new UseAbilityAction(victreebel, ability).execute(board.contextWithoutSource());

            assertSame(onlyBasic, board.them.active().orElseThrow());
            assertEquals(1, you.remaining(), "one candidate, so nothing was asked");
        }

        @Test
        @DisplayName("not offered at all when there is no Basic to drag up")
        void illegalWithNothingToDrag() {
            board.active(board.them, WALL);
            board.bench(board.them, EVOLVED);

            assertFalse(new UseAbilityAction(victreebel, ability)
                    .isLegal(board.contextWithoutSource()));
        }

        @Test
        @DisplayName("only works from the Active spot")
        void mustBeActive() {
            TestBoard benched = new TestBoard(new ScriptedPlayer("you", 0), new ScriptedPlayer("them"));
            PokemonInPlay onTheBench = benched.bench(benched.you, Grass.VICTREEBEL);
            benched.active(benched.you, WALL);
            benched.active(benched.them, WALL);
            benched.bench(benched.them, Grass.BULBASAUR);

            assertFalse(new UseAbilityAction(onTheBench, ability)
                    .isLegal(benched.contextWithoutSource()));
        }

        @Test
        void onceDuringYourTurn() {
            board.active(board.them, WALL);
            board.bench(board.them, Grass.BULBASAUR);
            IAction use = new UseAbilityAction(victreebel, ability);

            assertTrue(use.execute(board.contextWithoutSource()).succeeded());
            assertFalse(use.isLegal(board.contextWithoutSource()));
        }
    }

    @Nested
    @DisplayName("Grass — Exeggutor's Stomp")
    class Stomp {

        /** Weak to Grass, so one hit is 20 bigger than the printed number. */
        private static final PokemonCard STRAW_MAN =
                TestBoard.card("StrawMan", 500, Type.WATER).withWeakness(Type.GRASS);

        private int stomp(ScriptedRandom coin) {
            TestBoard board = new TestBoard(coin);
            PokemonInPlay exeggutor = board.active(board.you, Grass.EXEGGUTOR);
            PokemonInPlay wall = board.active(board.them, WALL);

            attack(Grass.EXEGGUTOR, "Stomp").execute(board.contextFor(exeggutor));
            return wall.damage();
        }

        @Test
        @DisplayName("tails is the printed 30")
        void tailsDoes30() {
            assertEquals(30, stomp(ScriptedRandom.alwaysTails()));
        }

        @Test
        @DisplayName("heads is 30 more, not a second attack")
        void headsDoes60() {
            assertEquals(60, stomp(ScriptedRandom.alwaysHeads()));
        }

        @Test
        @DisplayName("one coin, whatever the result")
        void flipsExactlyOnce() {
            ScriptedRandom coin = ScriptedRandom.alwaysHeads();
            stomp(coin);
            assertEquals(1, coin.flipsTaken());
        }

        @Test
        @DisplayName("weakness is added once, because the damage lands once")
        void isASingleInstanceOfDamage() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysHeads());
            PokemonInPlay exeggutor = board.active(board.you, Grass.EXEGGUTOR);
            PokemonInPlay weak = board.active(board.them, STRAW_MAN);

            attack(Grass.EXEGGUTOR, "Stomp").execute(board.contextFor(exeggutor));

            // 60 + 20. Split into two DealDamage effects it would be
            // (30 + 20) twice, which is 100 - a different card.
            assertEquals(80, weak.damage());
        }
    }

    @Nested
    @DisplayName("Grass — Skiddo's Surprise Attack")
    class SurpriseAttack {

        /** Weak to Grass, and wearing something that answers being hit. */
        private static final PokemonCard STRAW_MAN =
                TestBoard.card("StrawMan", 500, Type.WATER).withWeakness(Type.GRASS);

        @Test
        @DisplayName("heads is the printed 30, weakness and all")
        void headsAttacks() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysHeads());
            PokemonInPlay skiddo = board.active(board.you, Grass.SKIDDO);
            PokemonInPlay wall = board.active(board.them, STRAW_MAN);

            AttemptResult result =
                    attack(Grass.SKIDDO, "Surprise Attack").execute(board.contextFor(skiddo));

            assertTrue(result.succeeded());
            assertEquals(60, wall.damage(), "40 + 20 weakness");
        }

        @Test
        @DisplayName("tails leaves the attempt failed, not merely empty")
        void tailsDoesNotAttack() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysTails());
            PokemonInPlay skiddo = board.active(board.you, Grass.SKIDDO);
            PokemonInPlay wall = board.active(board.them, STRAW_MAN);

            AttemptResult result =
                    attack(Grass.SKIDDO, "Surprise Attack").execute(board.contextFor(skiddo));

            assertTrue(result.failed());
            assertEquals(0, wall.damage());
        }

        @Test
        @DisplayName("no attack means nothing answers it - the difference from 0 damage")
        void tailsWakesNothingUp() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysTails());
            PokemonInPlay skiddo = board.active(board.you, Grass.SKIDDO);
            PokemonInPlay wall = board.active(board.them, STRAW_MAN);
            wall.attachTool(board.loose(board.them, Trainers.ROCKY_HELMET));

            attack(Grass.SKIDDO, "Surprise Attack").execute(board.contextFor(skiddo));

            // Dealing 0 would still dispatch DamageDealt, and the Helmet would
            // hit back for 20. Nothing was dealt at all, so it stays quiet.
            assertEquals(0, skiddo.damage(), "the Helmet had nothing to answer");
        }

        @Test
        @DisplayName("the Helmet does answer when the attack lands")
        void headsWakesTheHelmet() {
            TestBoard board = new TestBoard(ScriptedRandom.alwaysHeads());
            PokemonInPlay skiddo = board.active(board.you, Grass.SKIDDO);
            PokemonInPlay wall = board.active(board.them, STRAW_MAN);
            wall.attachTool(board.loose(board.them, Trainers.ROCKY_HELMET));

            attack(Grass.SKIDDO, "Surprise Attack").execute(board.contextFor(skiddo));

            assertEquals(20, skiddo.damage());
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

    @Nested
    @DisplayName("A1-037 Vulpix — Tail Whip")
    class TailWhip {

        /** Vine Whip costs one Grass and one Colorless, so two Grass pays it. */
        private static TestBoard boardWith(ScriptedRandom rng) {
            TestBoard board = new TestBoard(rng);
            board.active(board.you, Fire.VULPIX);
            board.active(board.them, Grass.BULBASAUR).attachEnergy(Type.GRASS, 2);
            return board;
        }

        private static boolean defenderCanAttack(TestBoard board) {
            PokemonInPlay defender = board.them.active().orElseThrow();
            return attack(Grass.BULBASAUR, "Vine Whip")
                    .isLegal(board.contextFor(defender).withController(board.them));
        }

        @Test
        @DisplayName("heads locks the defender's attack for its next turn only")
        void headsLocksTheDefendersAttack() {
            TestBoard board = boardWith(ScriptedRandom.alwaysHeads());
            PokemonInPlay vulpix = board.you.active().orElseThrow();

            assertTrue(attack(Fire.VULPIX, "Tail Whip")
                    .execute(board.contextFor(vulpix)).succeeded());

            board.battle.switchSides();
            assertFalse(defenderCanAttack(board));

            board.battle.switchSides();
            board.battle.switchSides();
            assertTrue(defenderCanAttack(board), "one turn, not for the rest of the game");
        }

        @Test
        @DisplayName("tails locks nothing, and is still a successful attack")
        void tailsLocksNothing() {
            TestBoard board = boardWith(ScriptedRandom.alwaysTails());
            PokemonInPlay vulpix = board.you.active().orElseThrow();

            assertTrue(attack(Fire.VULPIX, "Tail Whip")
                    .execute(board.contextFor(vulpix)).succeeded(),
                    "the flip failing is a no-op, not a failed attack");

            board.battle.switchSides();
            assertTrue(defenderCanAttack(board));
        }

        @Test
        @DisplayName("retreating out of the lock is the way around it")
        void retreatingShedsTheLock() {
            TestBoard board = boardWith(ScriptedRandom.alwaysHeads());
            PokemonInPlay vulpix = board.you.active().orElseThrow();
            PokemonInPlay bulbasaur = board.them.active().orElseThrow();
            board.bench(board.them, Grass.ODDISH);

            attack(Fire.VULPIX, "Tail Whip").execute(board.contextFor(vulpix));
            board.battle.switchSides();

            // They are the attacker now, so it is their own turn they switch on.
            new SwitchActive(new AttackerSide()).apply(board.contextWithoutSource());

            assertTrue(board.them.bench().contains(bulbasaur));
            assertTrue(bulbasaur.modifiers().isEmpty(), "leaving the active spot sheds it");
        }
    }

    @Test
    @DisplayName("all grass cards are correctly added to list")
    void grassCardsInList() {
        Set<String> cards = Grass.CARDS.stream()
                .map(PokemonCard::name)
                .map(String::toUpperCase)
                .map(s -> s.replace(" ", "_"))
                .collect(Collectors.toSet());

        Set<String> fields = Stream.of(Grass.class.getDeclaredFields())
                .map(Field::getName)
                .filter(name -> !name.equals("CARDS"))
                .collect(Collectors.toSet());

        assertEquals(cards, fields);
    }
}
