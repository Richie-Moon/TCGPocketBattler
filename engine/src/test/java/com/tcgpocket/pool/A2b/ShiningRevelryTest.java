package com.tcgpocket.pool.A2b;

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
import com.tcgpocket.energy.Type;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.AttackerBenchSpecific;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** One assertion per card that the tree does what the printed text says. */
class ShiningRevelryTest {

    private static Action attack(PokemonCard card) {
        return (Action) card.actions().getFirst();
    }

    private static boolean canRetreat(TestBoard board) {
        return new RetreatAction(new AttackerBenchSpecific(0)).isLegal(board.contextWithoutSource());
    }

    private static boolean play(TestBoard board, CardInstance card) {
        return new PlayCardAction(card).execute(board.contextWithoutSource()).succeeded();
    }

    @Nested
    @DisplayName("Abilities")
    class Abilities {

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("Retreat Directive: a benched Tatsugiri frees the Active Dondozo")
        void retreatDirectiveFreesDondozo() {
            board.active(board.you, Water.DONDOZO);
            board.bench(board.you, Water.TATSUGIRI);
            assertTrue(canRetreat(board), "Retreat Cost 3, no Energy");
        }

        @Test
        @DisplayName("Retreat Directive: nothing without Tatsugiri, or for anyone but Dondozo")
        void retreatDirectiveOnlyForDondozo() {
            board.active(board.you, Water.DONDOZO);
            board.bench(board.you, Colorless.LICKITUNG);
            assertFalse(canRetreat(board));

            TestBoard other = new TestBoard();
            other.active(other.you, Colorless.LICKITUNG);
            other.bench(other.you, Water.TATSUGIRI);
            assertFalse(canRetreat(other));
        }

        @Test
        @DisplayName("Broken-Space Bellow attaches a Psychic Energy and ends the turn")
        void brokenSpaceBellowEndsTurn() {
            PokemonInPlay giratina = board.active(board.you, Psychic.GIRATINA_EX);
            assertTrue(new UseAbilityAction(giratina, Psychic.GIRATINA_EX.ability().orElseThrow())
                    .execute(board.contextWithoutSource()).succeeded());
            assertEquals(1, giratina.energyOf(Type.PSYCHIC));
            assertTrue(board.you.turnEnded());
        }
    }

    @Nested
    @DisplayName("Trainers")
    class TrainerCards {

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("Red adds 20 against an Active Pokémon ex only")
        void redOnlyAgainstEx() {
            PokemonInPlay rattata = board.active(board.you, Colorless.RATTATA);
            PokemonInPlay pikachu = board.active(board.them, Lightning.PIKACHU_EX);
            assertTrue(play(board, board.inHand(board.you, Trainers.RED)));

            attack(Colorless.RATTATA).execute(board.contextFor(rattata));
            assertEquals(30, pikachu.damage());

            PokemonInPlay wall = board.active(board.them, TestBoard.card("Wall", 200, Type.COLORLESS));
            attack(Colorless.RATTATA).execute(board.contextFor(rattata));
            assertEquals(10, wall.damage());
        }

        @Test
        @DisplayName("Iono: each player draws as many cards as they shuffled back")
        void ionoRedrawsThatMany() {
            board.active(board.you, Colorless.RATTATA);
            board.inHand(board.you, Colorless.RATTATA);
            board.inHand(board.them, Colorless.RATTATA);
            board.inHand(board.them, Colorless.RATTATA);
            board.inHand(board.them, Colorless.RATTATA);
            for (int i = 0; i < 5; i++) {
                board.inDeck(board.you, Colorless.BUNEARY);
                board.inDeck(board.them, Colorless.BUNEARY);
            }

            assertTrue(play(board, board.inHand(board.you, Trainers.IONO)));
            assertEquals(1, board.you.hand().size(), "Iono itself is not shuffled back");
            assertEquals(3, board.them.hand().size());
            assertEquals(5, board.you.deck().size());
            assertEquals(5, board.them.deck().size());
        }

        @Test
        @DisplayName("Pokémon Center Lady heals 30 and cures the one Pokémon she picks")
        void pokemonCenterLadyHealsAndCures() {
            PokemonInPlay lickitung = board.active(board.you, Colorless.LICKITUNG);
            lickitung.takeDamage(50);
            lickitung.addStatus(new PoisonStatus());

            assertTrue(play(board, board.inHand(board.you, Trainers.POKEMON_CENTER_LADY)));
            assertEquals(20, lickitung.damage());
            assertTrue(lickitung.statuses().isEmpty());
        }

        @Test
        @DisplayName("Pokémon Center Lady can't be played with nothing to fix")
        void pokemonCenterLadyNeedsATarget() {
            board.active(board.you, Colorless.LICKITUNG);
            assertFalse(new PlayCardAction(board.inHand(board.you, Trainers.POKEMON_CENTER_LADY))
                    .isLegal(board.contextWithoutSource()));
        }

        @Test
        @DisplayName("Team Rocket Grunt discards one Energy per heads, stopping when there is none left")
        void teamRocketGruntPerHeads() {
            TestBoard flipping = new TestBoard(ScriptedRandom.flipping(true, true, true, false));
            flipping.active(flipping.you, Colorless.RATTATA);
            PokemonInPlay defender = flipping.active(flipping.them, Colorless.LICKITUNG);
            defender.attachEnergy(Type.COLORLESS, 2);

            assertTrue(play(flipping, flipping.inHand(flipping.you, Trainers.TEAM_ROCKET_GRUNT)));
            assertEquals(0, defender.totalEnergy());
        }
    }
}
