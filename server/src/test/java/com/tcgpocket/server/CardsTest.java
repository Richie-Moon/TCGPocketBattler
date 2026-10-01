package com.tcgpocket.server;

import com.tcgpocket.energy.Type;
import com.tcgpocket.pool.CardPool;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Cards")
class CardsTest {

    @Test
    @DisplayName("a Pokémon is listed with its types and can be found by its attack")
    void pokemon() {
        assertEquals(new Cards.Entry("A1-096", "Pikachu ex", "Pokémon", Set.of(Type.LIGHTNING), "Circle Circuit"),
                Cards.entry(CardPool.get("A1-096")));
    }

    @Test
    @DisplayName("the catalogue is the whole pool")
    void everyCard() {
        assertEquals(CardPool.size(), new Cards().all().size());
    }
}
