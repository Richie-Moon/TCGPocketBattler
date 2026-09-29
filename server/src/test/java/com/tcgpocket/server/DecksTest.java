package com.tcgpocket.server;

import com.tcgpocket.energy.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Decks")
class DecksTest {

    private static void rejects(Decks.Api.Draft draft) {
        assertThrows(ResponseStatusException.class, () -> Decks.Api.check(draft));
    }

    @Test
    @DisplayName("a half-built draft with no energy yet is accepted")
    void draft() {
        assertDoesNotThrow(() -> Decks.Api.check(new Decks.Api.Draft(" Sparks ", List.of("A1-094"), Set.of())));
    }

    @Test
    @DisplayName("a blank name, an unknown card or too many cards or energy types is rejected")
    void malformed() {
        rejects(new Decks.Api.Draft(" ", List.of(), Set.of()));
        rejects(new Decks.Api.Draft("x".repeat(101), List.of(), Set.of()));
        rejects(new Decks.Api.Draft("Sparks", List.of("A9-999"), Set.of()));
        rejects(new Decks.Api.Draft("Sparks", Collections.nCopies(21, "A1-094"), Set.of()));
        rejects(new Decks.Api.Draft("Sparks", List.of(), Set.of(Type.FIRE, Type.WATER, Type.GRASS, Type.METAL)));
        rejects(new Decks.Api.Draft("Sparks", null, Set.of()));
    }
}
