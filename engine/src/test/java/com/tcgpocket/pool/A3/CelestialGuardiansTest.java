package com.tcgpocket.pool.A3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.TestBoard;
import com.tcgpocket.action.PlayCardAction;
import com.tcgpocket.energy.Type;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.PoisonStatus;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** One assertion per card that the tree does what the printed text says. */
class CelestialGuardiansTest {

    @Nested
    @DisplayName("Trainers")
    class TrainerCards {

        private final TestBoard board = new TestBoard();

        @Test
        @DisplayName("Rare Candy evolves a Basic straight to its Stage 2, but not on your first turn")
        void rareCandySkipsStage1() {
            PokemonInPlay rowlet = board.active(board.you, Grass.ROWLET);
            board.bench(board.you, Grass.EXEGGCUTE);
            board.inHand(board.you, Grass.DECIDUEYE_EX);
            CardInstance candy = board.inHand(board.you, Trainers.RARE_CANDY);

            assertTrue(PlayCardAction.all(candy, board.contextWithoutSource()).isEmpty(), "first turn");
            board.you.markTurnTaken();

            List<PlayCardAction> moves = PlayCardAction.all(candy, board.contextWithoutSource());
            assertEquals(List.of(Optional.of(rowlet)), moves.stream().map(PlayCardAction::onto).toList(),
                    "Exeggcute has no Stage 2 in hand");
            assertTrue(moves.getFirst().execute(board.contextWithoutSource()).succeeded());
            assertEquals(Grass.DECIDUEYE_EX, rowlet.definition());
            assertTrue(board.you.hand().isEmpty());
            assertEquals(Trainers.RARE_CANDY, board.you.discardPile().getFirst().definition());
        }

        @Test
        @DisplayName("Kiawe attaches 2 Fire Energy and ends the turn")
        void kiaweEndsTurn() {
            PokemonInPlay marowak = board.active(board.you, Fire.ALOLAN_MAROWAK);
            CardInstance kiawe = board.inHand(board.you, Trainers.KIAWE);

            assertTrue(new PlayCardAction(kiawe, marowak).execute(board.contextWithoutSource()).succeeded());
            assertEquals(2, marowak.energyOf(Type.FIRE));
            assertTrue(board.you.turnEnded());
            board.you.resetTurnFlags();
            assertFalse(board.you.turnEnded());
        }

        @Test
        @DisplayName("Leaf Cape gives +30 HP to a Grass Pokémon only")
        void leafCapeOnlyForGrass() {
            PokemonInPlay rowlet = board.active(board.you, Grass.ROWLET);
            PokemonInPlay popplio = board.bench(board.you, Water.POPPLIO);
            rowlet.attachTool(board.loose(board.you, Trainers.LEAF_CAPE));
            popplio.attachTool(board.loose(board.you, Trainers.LEAF_CAPE));

            assertEquals(Grass.ROWLET.maxHp() + 30, rowlet.maxHp());
            assertEquals(Water.POPPLIO.maxHp(), popplio.maxHp());
        }

        @Test
        @DisplayName("Acerola moves 40 of Mimikyu's damage to the opponent's Active")
        void acerolaMovesForty() {
            PokemonInPlay mimikyu = board.active(board.you, Psychic.MIMIKYU);
            PokemonInPlay target = board.active(board.them, TestBoard.card("Wall", 200, Type.COLORLESS));
            mimikyu.takeDamage(60);

            assertTrue(new PlayCardAction(board.inHand(board.you, Trainers.ACEROLA), mimikyu)
                    .execute(board.contextWithoutSource()).succeeded());
            assertEquals(20, mimikyu.damage());
            assertEquals(40, target.damage());
        }

        @Test
        @DisplayName("Big Malasada heals 10 and cures a status, and needs something to fix")
        void bigMalasadaHealsAndCures() {
            PokemonInPlay rowlet = board.active(board.you, Grass.ROWLET);
            CardInstance malasada = board.inHand(board.you, Trainers.BIG_MALASADA);
            assertFalse(new PlayCardAction(malasada).isLegal(board.contextWithoutSource()));

            rowlet.takeDamage(20);
            rowlet.addStatus(new PoisonStatus());
            assertTrue(new PlayCardAction(malasada).execute(board.contextWithoutSource()).succeeded());
            assertEquals(10, rowlet.damage());
            assertTrue(rowlet.statuses().isEmpty());
        }

        @Test
        @DisplayName("Fishing Net takes a Basic Water Pokémon from the discard pile")
        void fishingNetRecovers() {
            board.active(board.you, Grass.ROWLET);
            CardInstance net = board.inHand(board.you, Trainers.FISHING_NET);
            board.inDiscard(board.you, Grass.ROWLET);
            assertFalse(new PlayCardAction(net).isLegal(board.contextWithoutSource()));

            CardInstance popplio = board.inDiscard(board.you, Water.POPPLIO);
            assertTrue(new PlayCardAction(net).execute(board.contextWithoutSource()).succeeded());
            assertEquals(List.of(popplio), board.you.hand());
        }

        @Test
        @DisplayName("Guzma discards a Benched Pokémon's Tool though the Active holds none")
        void guzmaDiscardsEveryTool() {
            board.active(board.you, Grass.ROWLET);
            board.active(board.them, Grass.EXEGGCUTE);
            PokemonInPlay benched = board.bench(board.them, Water.POPPLIO);
            benched.attachTool(board.loose(board.them, Trainers.POISON_BARB));

            assertTrue(new PlayCardAction(board.inHand(board.you, Trainers.GUZMA))
                    .execute(board.contextWithoutSource()).succeeded());
            assertFalse(benched.hasTool());
            assertEquals(Trainers.POISON_BARB, board.them.discardPile().getFirst().definition());
        }
    }
}
