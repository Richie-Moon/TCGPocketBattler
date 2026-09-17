package com.tcgpocket.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.TestBoard;
import com.tcgpocket.energy.Type;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/** The shape of a printed card, as opposed to what its text does. */
class ICardTest {

    @Nested
    @DisplayName("a Pokemon's printed types")
    class Types {

        private static final PokemonCard ODDISH = TestBoard.card("Oddish", 60, Type.GRASS);

        @Test
        @DisplayName("a single-typed card is the one-element case, not a special case")
        void singleTypeIsASetOfOne() {
            assertEquals(Set.of(Type.GRASS), ODDISH.types());
        }

        @Test
        void withTypesMakesItDualTyped() {
            PokemonCard dual = ODDISH.withTypes(Type.GRASS, Type.WATER);

            assertEquals(Set.of(Type.GRASS, Type.WATER), dual.types());
        }

        @Test
        @DisplayName("the types iterate in printed order, whatever order they were given in")
        void typesAreCanonicallyOrdered() {
            // Type declares them in the order the sets print them: Grass, Fire, Water, ...
            assertEquals(List.of(Type.FIRE, Type.WATER),
                    List.copyOf(ODDISH.withTypes(Type.WATER, Type.FIRE).types()));
        }

        @Test
        void aRepeatedTypeCollapses() {
            assertEquals(Set.of(Type.GRASS), ODDISH.withTypes(Type.GRASS, Type.GRASS).types());
        }

        @Test
        @DisplayName("a Pokemon with no type at all is a transcription error, not a card")
        void typesMustNotBeEmpty() {
            IllegalArgumentException thrown =
                    assertThrows(IllegalArgumentException.class, ODDISH::withTypes);

            assertTrue(thrown.getMessage().contains("at least one type"), thrown.getMessage());
        }

        @Test
        @DisplayName("a dual type does not disturb the single printed weakness")
        void weaknessStaysSingular() {
            PokemonCard dual = ODDISH.withWeakness(Type.FIRE).withTypes(Type.GRASS, Type.WATER);

            assertEquals(Optional.of(Type.FIRE), dual.weakness());
            assertEquals(Set.of(Type.GRASS, Type.WATER), dual.types());
        }
    }
}
