package com.tcgpocket.pool.A2a;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.ScriptedRandom;
import com.tcgpocket.TestBoard;
import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlayCardAction;
import com.tcgpocket.action.RetreatAction;
import com.tcgpocket.action.UseAbilityAction;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.IsPoisoned;
import com.tcgpocket.effect.RemoveStatus;
import com.tcgpocket.energy.Type;
import com.tcgpocket.player.ScriptedPlayer;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.AttackerBenchSpecific;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.trigger.TriggerDispatcher;
import com.tcgpocket.trigger.TurnEnd;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** One assertion per card that the tree does what the printed text says. */
class TriumphantLightTest {

    private static Action attack(PokemonCard card) {
        return (Action) card.actions().getFirst();
    }

    private static boolean canRetreat(TestBoard board) {
        return new RetreatAction(new AttackerBenchSpecific(0)).isLegal(board.contextWithoutSource());
    }

    @Nested
    @DisplayName("Link abilities, on with Arceus or Arceus ex in play")
    class LinkAbilities {

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("Power Link: Carnivine does +30 to the Active")
        void powerLink() {
            PokemonInPlay carnivine = board.active(board.you, Grass.CARNIVINE);
            PokemonInPlay wall = board.active(board.them, TestBoard.card("Wall", 200, Type.COLORLESS));
            board.bench(board.you, Colorless.ARCEUS);

            assertTrue(attack(Grass.CARNIVINE).execute(board.contextFor(carnivine)).succeeded());
            assertEquals(50, wall.damage());
        }

        @Test
        @DisplayName("Speed Link: Heatran retreats for free only once Arceus ex is benched")
        void speedLink() {
            board.active(board.you, Fire.HEATRAN);
            board.bench(board.you, Colorless.EEVEE);
            assertFalse(canRetreat(board), "Retreat Cost 3, no Energy");

            board.bench(board.you, Colorless.ARCEUS_EX);
            assertTrue(canRetreat(board));
        }

        @Test
        @DisplayName("Vigor Link: Abomasnow's Mega Punch costs 1 less Colorless")
        void vigorLink() {
            PokemonInPlay abomasnow = board.active(board.you, Water.ABOMASNOW);
            abomasnow.attachEnergy(Type.WATER, 2);
            assertFalse(attack(Water.ABOMASNOW).isLegal(board.contextFor(abomasnow)), "needs Water, Water, Colorless");

            board.bench(board.you, Colorless.ARCEUS);
            assertTrue(attack(Water.ABOMASNOW).isLegal(board.contextFor(abomasnow)));
        }

        @Test
        @DisplayName("Resilience Link: Raichu takes −30 from the opponent's attack")
        void resilienceLink() {
            PokemonInPlay raichu = board.active(board.you, Lightning.RAICHU);
            board.bench(board.you, Colorless.ARCEUS);
            PokemonInPlay snorlax = board.active(board.them, Colorless.SNORLAX);
            board.battle.switchSides();

            assertTrue(attack(Colorless.SNORLAX).execute(ResolutionContext.of(board.battle, board.them, snorlax)).succeeded());
            assertEquals(70, raichu.damage());
        }

        @Test
        @DisplayName("Cunning Link: Crobat's 30 damage is only usable with Arceus in play")
        void cunningLink() {
            PokemonInPlay crobat = board.active(board.you, Darkness.CROBAT);
            PokemonInPlay wall = board.active(board.them, TestBoard.card("Wall", 200, Type.COLORLESS));
            UseAbilityAction use = new UseAbilityAction(crobat, Darkness.CROBAT.ability().orElseThrow());
            assertFalse(use.isLegal(board.contextWithoutSource()));

            board.bench(board.you, Colorless.ARCEUS);
            assertTrue(use.execute(board.contextWithoutSource()).succeeded());
            assertEquals(30, wall.damage());
        }
    }

    @Nested
    @DisplayName("Pokémon")
    class PokemonCards {

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("Heracross does 70 more only when both coins are heads")
        void heracrossNeedsBothHeads() {
            TestBoard coins = new TestBoard(ScriptedRandom.flipping(true, false, true, true));
            PokemonInPlay heracross = coins.active(coins.you, Grass.HERACROSS);
            PokemonInPlay wall = coins.active(coins.them, TestBoard.card("Wall", 300, Type.COLORLESS));

            attack(Grass.HERACROSS).execute(coins.contextFor(heracross));
            assertEquals(50, wall.damage());
            attack(Grass.HERACROSS).execute(coins.contextFor(heracross));
            assertEquals(50 + 120, wall.damage());
        }

        @Test
        @DisplayName("Toxic poisons for 20, replacing a 10-damage Poison, and is still just Poisoned")
        void toxicPoisonsForTwenty() {
            PokemonInPlay toxicroak = board.active(board.you, Darkness.TOXICROAK);
            PokemonInPlay wall = board.active(board.them, TestBoard.card("Wall", 200, Type.COLORLESS));
            wall.addStatus(new PoisonStatus());

            assertTrue(attack(Darkness.TOXICROAK).execute(board.contextFor(toxicroak)).succeeded());
            assertEquals(Set.of(new PoisonStatus(20)), wall.statuses());
            assertTrue(new IsPoisoned().evaluate(wall));

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));
            assertEquals(20, wall.damage());

            new RemoveStatus(new OpponentActive(), new PoisonStatus()).apply(board.contextWithoutSource());
            assertTrue(wall.statuses().isEmpty());
        }

        @Test
        @DisplayName("Snowy Terrain hits Glaceon's opponent's Active at every Pokémon Checkup")
        void snowyTerrainEveryCheckup() {
            PokemonInPlay glaceon = board.active(board.you, Water.GLACEON_EX);
            PokemonInPlay wall = board.active(board.them, TestBoard.card("Wall", 200, Type.COLORLESS));

            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.you));
            assertEquals(10, wall.damage());

            board.battle.switchSides();
            TriggerDispatcher.dispatch(board.battle, new TurnEnd(board.them));
            assertEquals(20, wall.damage());
            assertEquals(0, glaceon.damage());
        }

        @Test
        @DisplayName("Sky Support takes 1 off an Active Basic's Retreat Cost per benched Shaymin")
        void skySupportStacks() {
            PokemonInPlay snorlax = board.active(board.you, Colorless.SNORLAX);
            snorlax.attachEnergy(Type.COLORLESS, 2);
            board.bench(board.you, Colorless.SHAYMIN);
            assertFalse(canRetreat(board), "Retreat Cost 4 less 1");

            board.bench(board.you, Colorless.SHAYMIN);
            assertTrue(canRetreat(board), "4 less 2");
        }

        @Test
        @DisplayName("Sky Support does not help an evolved Active")
        void skySupportOnlyForBasics() {
            board.active(board.you, Colorless.NOCTOWL);
            board.bench(board.you, Colorless.SHAYMIN);
            assertFalse(canRetreat(board));
        }

        @Test
        @DisplayName("Fabled Luster: Arceus ex can't be Poisoned")
        void fabledLuster() {
            board.active(board.you, Darkness.TOXICROAK);
            PokemonInPlay arceus = board.active(board.them, Colorless.ARCEUS_EX);

            attack(Darkness.TOXICROAK).execute(board.contextFor(board.you.active().orElseThrow()));
            assertTrue(arceus.statuses().isEmpty());
        }

        @Test
        @DisplayName("CHECK shows you the top of the deck you choose, even your opponent's")
        void unownChecksEitherDeck() {
            TestBoard scripted = new TestBoard(new ScriptedPlayer("you", 1), new ScriptedPlayer("them"));
            PokemonInPlay unown = scripted.active(scripted.you, Psychic.UNOWN);
            scripted.inDeck(scripted.you, Colorless.SNORLAX);
            CardInstance theirTop = scripted.inDeck(scripted.them, Colorless.EEVEE);
            UseAbilityAction check = new UseAbilityAction(unown, Psychic.UNOWN.ability().orElseThrow());

            assertTrue(check.execute(scripted.contextWithoutSource()).succeeded());
            assertEquals(List.of(theirTop), scripted.them.topCardsSeenByOpponent());
            assertEquals(List.of(), scripted.them.seenTopCards(), "not shown to its owner");
        }

        @Test
        @DisplayName("Forest Breath only works from the Active Spot")
        void forestBreathFromActive() {
            PokemonInPlay benched = board.bench(board.you, Grass.LEAFEON_EX);
            board.active(board.you, Grass.CARNIVINE);
            assertFalse(new UseAbilityAction(benched, Grass.LEAFEON_EX.ability().orElseThrow())
                    .isLegal(board.contextWithoutSource()));
        }
    }

    @Nested
    @DisplayName("Trainers")
    class TrainerCards {

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("Barry makes Snorlax's Collapse cost 2 less, but not Eevee's attack")
        void barryCheapensNamedAttackers() {
            PokemonInPlay snorlax = board.active(board.you, Colorless.SNORLAX);
            snorlax.attachEnergy(Type.COLORLESS, 2);
            assertFalse(attack(Colorless.SNORLAX).isLegal(board.contextFor(snorlax)));

            assertTrue(new PlayCardAction(board.inHand(board.you, Trainers.BARRY))
                    .execute(board.contextWithoutSource()).succeeded());
            assertTrue(attack(Colorless.SNORLAX).isLegal(board.contextFor(snorlax)));

            PokemonInPlay eevee = board.active(board.you, Colorless.EEVEE);
            eevee.attachEnergy(Type.COLORLESS, 1);
            assertFalse(attack(Colorless.EEVEE).isLegal(board.contextFor(eevee)));
        }

        @Test
        @DisplayName("Adaman softens attacks on Metal Pokémon only")
        void adamanForMetalOnly() {
            PokemonInPlay probopass = board.active(board.you, Metal.PROBOPASS_EX);
            assertTrue(new PlayCardAction(board.inHand(board.you, Trainers.ADAMAN))
                    .execute(board.contextWithoutSource()).succeeded());
            PokemonInPlay snorlax = board.active(board.them, Colorless.SNORLAX);
            board.battle.switchSides();

            attack(Colorless.SNORLAX).execute(ResolutionContext.of(board.battle, board.them, snorlax));
            assertEquals(80, probopass.damage());

            PokemonInPlay wall = board.active(board.you, TestBoard.card("Wall", 200, Type.COLORLESS));
            attack(Colorless.SNORLAX).execute(ResolutionContext.of(board.battle, board.them, snorlax));
            assertEquals(100, wall.damage());
        }

        @Test
        @DisplayName("Irida heals only Pokémon with Water Energy, and needs one hurt")
        void iridaHealsWaterPokemon() {
            PokemonInPlay marill = board.active(board.you, Water.MARILL);
            PokemonInPlay eevee = board.bench(board.you, Colorless.EEVEE);
            marill.attachEnergy(Type.WATER, 1);
            eevee.takeDamage(30);
            assertFalse(new PlayCardAction(board.inHand(board.you, Trainers.IRIDA)).isLegal(board.contextWithoutSource()));

            marill.takeDamage(50);
            assertTrue(new PlayCardAction(board.inHand(board.you, Trainers.IRIDA))
                    .execute(board.contextWithoutSource()).succeeded());
            assertEquals(10, marill.damage());
            assertEquals(30, eevee.damage());
        }
    }
}
