package com.tcgpocket.engine;

import com.tcgpocket.TestBoard;
import com.tcgpocket.card.ICard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.energy.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DeckValidator — 20 cards, a Basic, two per name, one to three energy types")
class DeckValidatorTest {

    private static final Set<Type> LIGHTNING = Set.of(Type.LIGHTNING);

    /** Ten names, two of each. */
    private static List<ICard> legalDeck() {
        List<ICard> deck = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            deck.add(TestBoard.card("Mon " + i / 2, 60, Type.LIGHTNING));
        }
        return deck;
    }

    private static int problemCount(List<ICard> deck, Set<Type> energy) {
        return DeckValidator.problems(deck, energy).size();
    }

    @Test
    @DisplayName("a legal deck has no problems")
    void legal() {
        assertTrue(DeckValidator.problems(legalDeck(), LIGHTNING).isEmpty());
    }

    @Test
    @DisplayName("19 or 21 cards is illegal")
    void exactlyTwenty() {
        List<ICard> short_ = legalDeck();
        short_.removeLast();
        List<ICard> long_ = legalDeck();
        long_.add(TestBoard.card("Extra", 60, Type.LIGHTNING));

        assertEquals(1, problemCount(short_, LIGHTNING));
        assertEquals(1, problemCount(long_, LIGHTNING));
    }

    @Test
    @DisplayName("a deck without a Basic is illegal")
    void needsABasic() {
        List<ICard> deck = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            deck.add(PokemonCard.evolution("e" + i / 2, "Evo " + i / 2, 1, "Mon", 90, Type.LIGHTNING, 1, List.of()));
        }

        assertEquals(1, problemCount(deck, LIGHTNING));
    }

    @Test
    @DisplayName("copies are counted by name across sets, and an ex is a different name")
    void copiesByName() {
        List<ICard> deck = legalDeck().subList(0, 17);
        deck = new ArrayList<>(deck);
        deck.add(PokemonCard.basic("A1-001", "Bulbasaur", 70, Type.GRASS, 1));
        deck.add(PokemonCard.basic("A2-001", "Bulbasaur", 70, Type.GRASS, 1));
        deck.add(PokemonCard.basic("A1-003", "Bulbasaur ex", 150, Type.GRASS, 1));
        assertEquals(0, problemCount(deck, LIGHTNING), "two Bulbasaur plus a Bulbasaur ex is fine");

        deck.set(deck.size() - 1, PokemonCard.basic("A3-001", "Bulbasaur", 70, Type.GRASS, 1));
        assertEquals(1, problemCount(deck, LIGHTNING), "a third Bulbasaur from another set is not");
    }

    @Test
    @DisplayName("zero or four energy types is illegal, and Colorless is not a choice")
    void energyTypes() {
        assertEquals(1, problemCount(legalDeck(), Set.of()));
        assertEquals(1, problemCount(legalDeck(), Set.of(Type.GRASS, Type.FIRE, Type.WATER, Type.LIGHTNING)));
        assertEquals(0, problemCount(legalDeck(), Set.of(Type.GRASS, Type.FIRE, Type.WATER)));
        assertEquals(1, problemCount(legalDeck(), Set.of(Type.COLORLESS)));
    }

    @Test
    @DisplayName("setup refuses to start with an illegal deck")
    void setupValidates() {
        TestBoard board = new TestBoard();
        legalDeck().forEach(card -> board.inDeck(board.you, card));
        board.you.registerTypes(Type.LIGHTNING);
        Collections.nCopies(20, TestBoard.card("Clone", 60, Type.FIRE)).forEach(card -> board.inDeck(board.them, card));
        board.them.registerTypes(Type.FIRE);

        assertThrows(IllegalStateException.class, () -> new TurnEngine(board.battle).setup());
        assertEquals(20, board.you.deck().size(), "nothing was dealt");
    }
}
