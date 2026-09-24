package com.tcgpocket.pool.PA;

import com.tcgpocket.TestBoard;
import com.tcgpocket.action.PlayCardAction;
import com.tcgpocket.pool.A1.Grass;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Promo-A Trainers")
class TrainersTest {

    private final TestBoard board = new TestBoard();

    @Test
    @DisplayName("Potion is dragged onto a damaged Pokémon; a full-HP one is no drop target")
    void potionOnlyDropsOnDamaged() {
        PokemonInPlay hurt = board.active(board.you, Grass.BULBASAUR);
        board.bench(board.you, Grass.BULBASAUR);
        hurt.takeDamage(30);
        CardInstance potion = board.inHand(board.you, Trainers.POTION);
        ResolutionContext context = board.contextWithoutSource();

        assertEquals(List.of(new PlayCardAction(potion, hurt)), PlayCardAction.all(potion, context));
    }

    @Test
    @DisplayName("with nothing hurt, Potion has no moves at all")
    void potionIsUnplayableAtFullHp() {
        board.active(board.you, Grass.BULBASAUR);
        CardInstance potion = board.inHand(board.you, Trainers.POTION);

        assertEquals(List.of(), PlayCardAction.all(potion, board.contextWithoutSource()));
    }
}
