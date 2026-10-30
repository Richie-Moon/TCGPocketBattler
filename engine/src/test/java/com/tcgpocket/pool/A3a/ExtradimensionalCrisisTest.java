package com.tcgpocket.pool.A3a;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.ScriptedRandom;
import com.tcgpocket.TestBoard;
import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlayCardAction;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.condition.IsTool;
import com.tcgpocket.condition.SupporterPlayedThisTurn;
import com.tcgpocket.effect.EffectOutcome;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.pool.CardPool;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.Knockout;
import com.tcgpocket.trigger.TriggerDispatcher;
import com.tcgpocket.trigger.TurnEnd;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** One assertion per card that needed a new node, or whose tree is not obvious. */
class ExtradimensionalCrisisTest {

    private static Action attack(PokemonInPlay pokemon, int index) {
        return (Action) pokemon.definition().actions().get(index);
    }

    @Test
    @DisplayName("the whole set is in the pool, and the Ultra Beasts are tagged")
    void setIsInThePool() {
        assertEquals(103, ExtradimensionalCrisis.CARDS.size());
        assertEquals("Nihilego", CardPool.get("A3a-103").name());
        assertEquals(19, ExtradimensionalCrisis.CARDS.stream()
                .filter(card -> card.hasTag(CardTag.ULTRA_BEAST)).count());
    }

    @Nested
    @DisplayName("Pokémon")
    class PokemonCards {

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("Big Beat locks itself for the next turn, and leaves Punch usable")
        void bigBeatLocksOnlyItself() {
            PokemonInPlay buzzwole = board.active(board.you, Grass.BUZZWOLE_EX);
            board.active(board.them, Grass.PETILIL);
            buzzwole.attachEnergy(Type.GRASS, 3);
            attack(buzzwole, 1).execute(board.contextFor(buzzwole));

            assertFalse(attack(buzzwole, 1).isLegal(board.contextFor(buzzwole)));
            assertTrue(attack(buzzwole, 0).isLegal(board.contextFor(buzzwole)));
        }

        @Test
        @DisplayName("Heal Block stops healing on both sides while Claydol is in play")
        void healBlockCoversBothSides() {
            PokemonInPlay hurt = board.active(board.you, Grass.PETILIL);
            hurt.takeDamage(30);
            HealDamage heal = new HealDamage(new Literal(20), new Self());
            board.bench(board.them, Fighting.CLAYDOL);

            assertEquals(EffectOutcome.NO_OP, heal.apply(board.contextFor(hurt)));
            assertEquals(30, hurt.damage());
        }

        @Test
        @DisplayName("Guard Dog Visage adds 1 Colorless only while Stoutland is Active")
        void guardDogVisageWhileActive() {
            PokemonInPlay attacker = board.active(board.you, Grass.PETILIL);
            attacker.attachEnergy(Type.GRASS, 1);
            assertTrue(attack(attacker, 0).isLegal(board.contextFor(attacker)));

            board.bench(board.them, Colorless.STOUTLAND);
            assertTrue(attack(attacker, 0).isLegal(board.contextFor(attacker)), "benched");

            board.active(board.them, Colorless.STOUTLAND);
            assertFalse(attack(attacker, 0).isLegal(board.contextFor(attacker)), "Active");
            attacker.attachEnergy(Type.GRASS, 1);
            assertTrue(attack(attacker, 0).isLegal(board.contextFor(attacker)));
        }

        @Test
        @DisplayName("Intimidating Fang softens the attack by 20 only while Luxray is Active")
        void intimidatingFang() {
            PokemonInPlay attacker = board.active(board.you, Colorless.ORANGURU);
            PokemonInPlay luxray = board.active(board.them, Lightning.LUXRAY);
            attack(attacker, 0).execute(board.contextFor(attacker));
            assertEquals(50, luxray.damage(), "Hammer In's 70, less 20");
        }

        @Test
        @DisplayName("Brave Buddies does 50 more once a Supporter has been played")
        void braveBuddies() {
            PokemonInPlay silvally = board.active(board.you, Colorless.SILVALLY);
            PokemonInPlay target = board.active(board.them, Fighting.CLAYDOL);
            assertFalse(new SupporterPlayedThisTurn().evaluate(board.contextFor(silvally)));

            board.you.markSupporterPlayed();
            attack(silvally, 0).execute(board.contextFor(silvally));
            assertEquals(100, target.damage());
        }

        @Test
        @DisplayName("Superpowered Hug knocks out only on two heads")
        void superpoweredHug() {
            TestBoard both = new TestBoard(ScriptedRandom.flipping(true, true));
            PokemonInPlay bewear = both.active(both.you, Colorless.BEWEAR);
            PokemonInPlay target = both.active(both.them, Fighting.CLAYDOL);
            attack(bewear, 0).execute(both.contextFor(bewear));
            assertEquals(0, target.currentHp());

            TestBoard one = new TestBoard(ScriptedRandom.flipping(true, false));
            PokemonInPlay bewear2 = one.active(one.you, Colorless.BEWEAR);
            PokemonInPlay target2 = one.active(one.them, Fighting.CLAYDOL);
            attack(bewear2, 0).execute(one.contextFor(bewear2));
            assertEquals(0, target2.damage());
        }

        @Test
        @DisplayName("Meddle discards a Tool from the opponent's hand, and nothing else")
        void meddleOnlyTakesTools() {
            CardInstance tool = board.inHand(board.them, Trainers.BEASTITE);
            CardInstance item = board.inHand(board.them, Trainers.REPEL);
            assertTrue(new IsTool().evaluate(tool));
            assertFalse(new IsTool().evaluate(item));

            PokemonInPlay meowth = board.active(board.you, Darkness.ALOLAN_MEOWTH);
            board.active(board.them, Grass.PETILIL);
            attack(meowth, 0).execute(board.contextFor(meowth));
            assertEquals(1, board.them.hand().size());
            assertEquals(Trainers.REPEL, board.them.hand().getFirst().definition());
        }
    }

    @Nested
    @DisplayName("Trainers")
    class TrainerCards {

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("Lusamine needs an opposing point and an Ultra Beast, and attaches 2 from the discard")
        void lusamine() {
            PokemonInPlay kartana = board.active(board.you, Grass.KARTANA);
            board.bench(board.you, Grass.PETILIL);
            board.you.addDiscardedEnergy(Type.GRASS, 1);
            board.you.addDiscardedEnergy(Type.FIRE, 2);
            CardInstance lusamine = board.inHand(board.you, Trainers.LUSAMINE);
            assertTrue(PlayCardAction.all(lusamine, board.contextWithoutSource()).isEmpty(), "no points yet");

            board.them.awardPoints(1);
            var moves = PlayCardAction.all(lusamine, board.contextWithoutSource());
            assertEquals(1, moves.size(), "only the Ultra Beast");
            assertTrue(moves.getFirst().execute(board.contextWithoutSource()).succeeded());
            assertEquals(2, kartana.totalEnergy());
            assertEquals(1, board.you.discardedEnergy().values().stream().mapToInt(Integer::intValue).sum());
        }

        @Test
        @DisplayName("Beast Wall is playable only while the opponent has no points")
        void beastWall() {
            board.active(board.you, Grass.KARTANA);
            CardInstance wall = board.inHand(board.you, Trainers.BEAST_WALL);
            assertEquals(1, PlayCardAction.all(wall, board.contextWithoutSource()).size());
            board.them.awardPoints(1);
            assertTrue(PlayCardAction.all(wall, board.contextWithoutSource()).isEmpty());
        }

        @Test
        @DisplayName("Electrical Cord moves 2 Lightning Energy to 2 different Benched Pokémon on an attack knockout")
        void electricalCord() {
            PokemonInPlay attacker = board.active(board.you, Grass.KARTANA);
            PokemonInPlay koko = board.active(board.them, Lightning.TAPU_KOKO_EX);
            PokemonInPlay first = board.bench(board.them, Lightning.SHINX);
            PokemonInPlay second = board.bench(board.them, Lightning.BLITZLE);
            koko.attachEnergy(Type.LIGHTNING, 3);
            koko.attachTool(board.loose(board.them, Trainers.ELECTRICAL_CORD));

            TriggerDispatcher.dispatch(board.battle, new Knockout(koko, Optional.of(attacker)));

            assertEquals(1, first.energyOf(Type.LIGHTNING));
            assertEquals(1, second.energyOf(Type.LIGHTNING));
            assertEquals(1, koko.energyOf(Type.LIGHTNING));
        }

        @Test
        @DisplayName("Electrical Cord does nothing on a knockout nobody dealt")
        void electricalCordIgnoresPoison() {
            PokemonInPlay koko = board.active(board.them, Lightning.TAPU_KOKO_EX);
            PokemonInPlay benched = board.bench(board.them, Lightning.SHINX);
            koko.attachEnergy(Type.LIGHTNING, 2);
            koko.attachTool(board.loose(board.them, Trainers.ELECTRICAL_CORD));

            TriggerDispatcher.dispatch(board.battle, new Knockout(koko, Optional.empty()));

            assertEquals(0, benched.totalEnergy());
        }

        @Test
        @DisplayName("Beastite adds 10 per point for an Ultra Beast only")
        void beastite() {
            board.you.awardPoints(2);
            PokemonInPlay kartana = board.active(board.you, Grass.KARTANA);
            PokemonInPlay target = board.active(board.them, Colorless.ORANGURU);
            kartana.attachTool(board.loose(board.you, Trainers.BEASTITE));
            attack(kartana, 0).execute(board.contextFor(kartana));
            assertEquals(60, target.damage(), "Thrash Metal's 40, plus 20");

            TestBoard other = new TestBoard();
            other.you.awardPoints(2);
            PokemonInPlay petilil = other.active(other.you, Grass.PETILIL);
            PokemonInPlay target2 = other.active(other.them, Colorless.ORANGURU);
            petilil.attachTool(other.loose(other.you, Trainers.BEASTITE));
            attack(petilil, 0).execute(other.contextFor(petilil));
            assertEquals(20, target2.damage());
        }

        @Test
        @DisplayName("More Poison adds 10 to the opposing Active on either player's turn end, and never to its own side")
        void morePoison() {
            board.bench(board.you, Darkness.NIHILEGO);
            PokemonInPlay own = board.active(board.you, Grass.KARTANA);
            PokemonInPlay foe = board.active(board.them, Fighting.CLAYDOL);
            own.addStatus(new PoisonStatus());
            foe.addStatus(new PoisonStatus());

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));
            assertEquals(20, foe.damage(), "Poison's 10, plus 10");
            assertEquals(10, own.damage(), "your own Poison is not worsened");

            board.battle.switchSides();
            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.them));
            assertEquals(40, foe.damage());
            assertEquals(20, own.damage());
        }

        @Test
        @DisplayName("Thunderclap Flash attaches at the end of your first turn only")
        void thunderclapFlash() {
            PokemonInPlay zeraora = board.active(board.you, Lightning.ZERAORA);

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.them));
            assertEquals(0, zeraora.totalEnergy(), "not on the opponent's turn");

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));
            assertEquals(1, zeraora.energyOf(Type.LIGHTNING));

            board.you.markTurnTaken();
            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));
            assertEquals(1, zeraora.energyOf(Type.LIGHTNING));
        }

        @Test
        @DisplayName("Repel needs a Basic Active and something on the Bench")
        void repel() {
            board.active(board.you, Grass.KARTANA);
            CardInstance repel = board.inHand(board.you, Trainers.REPEL);
            board.active(board.them, Grass.PETILIL);
            assertTrue(PlayCardAction.all(repel, board.contextWithoutSource()).isEmpty(), "no Bench");

            board.bench(board.them, Grass.ROWLET);
            assertEquals(1, PlayCardAction.all(repel, board.contextWithoutSource()).size());

            board.active(board.them, Grass.LILLIGANT);
            assertTrue(PlayCardAction.all(repel, board.contextWithoutSource()).isEmpty(), "Stage 1 Active");
        }
    }
}
