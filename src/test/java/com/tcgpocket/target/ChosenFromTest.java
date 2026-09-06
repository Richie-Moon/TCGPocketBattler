package com.tcgpocket.target;

import com.tcgpocket.TestBoard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.IsBasic;
import com.tcgpocket.energy.Type;
import com.tcgpocket.player.ScriptedPlayer;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** The one target that asks a question. */
class ChosenFromTest {

    private static final PokemonCard PIKACHU = TestBoard.card("Pikachu", 60, Type.LIGHTNING);
    private static final PokemonCard RAICHU = PokemonCard.evolution(
            "raichu", "Raichu", 1, "Pikachu", 100, Type.LIGHTNING, 1, List.of());

    @Test
    @DisplayName("the named chooser is asked, not the side being chosen from")
    void asksTheChooser() {
        ScriptedPlayer you = new ScriptedPlayer("you", 1);
        ScriptedPlayer them = new ScriptedPlayer("them");
        TestBoard board = new TestBoard(you, them);

        board.bench(board.them, PIKACHU);
        PokemonInPlay second = board.bench(board.them, PIKACHU);

        Optional<PokemonInPlay> picked =
                new ChosenFrom(new OpponentBench(), new AttackerSide(), "Pick one")
                        .resolve(board.contextWithoutSource());

        assertSame(second, picked.orElseThrow(), "you picked option 1 off their bench");
        assertEquals(0, you.remaining());
        assertEquals(0, them.remaining(), "their player was never asked");
    }

    @Test
    @DisplayName("the condition narrows the group before it is offered")
    void onlyMatchingCandidatesAreOffered() {
        // Scripted to pick option 1: if the evolution were offered there would
        // be two options and this would choose it. There is only one Basic, so
        // the script is never consulted at all.
        ScriptedPlayer you = new ScriptedPlayer("you", 1);
        TestBoard board = new TestBoard(you, new ScriptedPlayer("them"));

        PokemonInPlay basic = board.bench(board.them, PIKACHU);
        board.bench(board.them, RAICHU);

        Optional<PokemonInPlay> picked =
                new ChosenFrom(new OpponentBench(), new AttackerSide(), new IsBasic(), "Pick a Basic")
                        .resolve(board.contextWithoutSource());

        assertSame(basic, picked.orElseThrow());
        assertEquals(1, you.remaining(), "a group of one is not worth asking about");
    }

    @Test
    void anEmptyGroupResolvesToEmpty() {
        TestBoard board = new TestBoard(new ScriptedPlayer("you"), new ScriptedPlayer("them"));
        board.bench(board.them, RAICHU);

        assertTrue(new ChosenFrom(new OpponentBench(), new AttackerSide(), new IsBasic(), "Pick")
                .resolve(board.contextWithoutSource())
                .isEmpty(), "no Basic on that bench");
    }

    @Test
    @DisplayName("resolving a target changes nothing on the board")
    void resolvingIsAReadNotAMove() {
        ScriptedPlayer you = new ScriptedPlayer("you", 0);
        TestBoard board = new TestBoard(you, new ScriptedPlayer("them"));
        PokemonInPlay first = board.bench(board.them, PIKACHU);
        board.bench(board.them, PIKACHU);

        Optional<PokemonInPlay> picked =
                new ChosenFrom(new OpponentBench(), new AttackerSide(), "Pick one")
                        .resolve(board.contextWithoutSource());

        assertSame(first, picked.orElseThrow());
        assertEquals(2, board.them.bench().size(), "still on the bench; choosing is not moving");
        assertEquals(0, you.remaining());
    }
}
