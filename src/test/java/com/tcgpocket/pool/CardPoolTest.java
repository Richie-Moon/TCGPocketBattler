package com.tcgpocket.pool;

import com.tcgpocket.card.ICard;
import com.tcgpocket.pool.A1.GeneticApex;
import com.tcgpocket.pool.A1.Lightning;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardPoolTest {

    @Test
    void looksACardUpByItsPrintedId() {
        assertSame(Lightning.PIKACHU_EX, CardPool.get("A1-096"));
    }

    @Test
    @DisplayName("an unknown id is a mistake, not an empty result")
    void unknownIdThrows() {
        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, () -> CardPool.get("A1-999"));
        assertTrue(thrown.getMessage().contains("A1-999"));

        assertTrue(CardPool.find("A1-999").isEmpty(), "find is the asking version");
    }

    @Test
    @DisplayName("every id in the pool is unique")
    void idsDoNotCollide() {
        Set<String> seen = new HashSet<>();
        for (ICard card : CardPool.all()) {
            assertTrue(seen.add(card.id()), "duplicate id: " + card.id());
        }
        assertEquals(CardPool.size(), seen.size());
    }

    @Test
    @DisplayName("ids carry their set code, so the pool can hold more than one set")
    void idsArePrefixedByTheirSet() {
        for (ICard card : GeneticApex.CARDS) {
            assertTrue(card.id().startsWith(GeneticApex.CODE + "-"),
                    card.name() + " has id " + card.id());
        }
    }

    @Test
    void everyCardIsReachableFromThePool() {
        for (ICard card : GeneticApex.CARDS) {
            assertSame(card, CardPool.get(card.id()));
        }
    }
}
