package com.tcgpocket.target;

import com.tcgpocket.TestBoard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.RandomSource;
import com.tcgpocket.state.PokemonInPlay;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** The target that rolls instead of asking. */
class RandomFromTest {

    private static final PokemonCard PIKACHU = TestBoard.card("Pikachu", 60, Type.LIGHTNING);

    /** Always the last candidate, so a pick of the first can't pass by accident. */
    private static final RandomSource LAST = new RandomSource() {
        @Override public int nextInt(int bound) { return bound - 1; }
        @Override public boolean nextBoolean() { return true; }
        @Override public void shuffle(List<?> list) { }
    };

    @Test
    @DisplayName("the pick is whichever member the battle's RNG lands on")
    void picksWhatTheRngSays() {
        TestBoard board = new TestBoard(LAST);
        board.bench(board.you, PIKACHU);
        PokemonInPlay last = board.bench(board.you, PIKACHU);

        assertSame(last, new RandomFrom(new AttackerBench())
                .resolve(board.contextWithoutSource()).orElseThrow());
    }

    @Test
    @DisplayName("an empty group resolves to nothing")
    void emptyGroupIsEmpty() {
        TestBoard board = new TestBoard(LAST);

        assertTrue(new RandomFrom(new AttackerBench())
                .resolve(board.contextWithoutSource()).isEmpty());
    }
}
