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

    private static Decks.Api.Draft draft(String name, List<String> cards, Set<Type> energy) {
        return new Decks.Api.Draft(name, cards, energy, null, null,
                "Coin_Pokéball.png", "Sleeve_Ho-Oh_Lugia.png", "Playmat_Default.png");
    }

    @Test
    @DisplayName("a half-built draft with no energy yet is accepted")
    void draft() {
        assertDoesNotThrow(() -> Decks.Api.check(draft(" Sparks ", List.of("A1-094"), Set.of())));
    }

    @Test
    @DisplayName("a blank name, an unknown card or too many cards or energy types is rejected")
    void malformed() {
        rejects(draft(" ", List.of(), Set.of()));
        rejects(draft("x".repeat(101), List.of(), Set.of()));
        rejects(draft("Sparks", List.of("A9-999"), Set.of()));
        rejects(draft("Sparks", Collections.nCopies(21, "A1-094"), Set.of()));
        rejects(draft("Sparks", List.of(), Set.of(Type.FIRE, Type.WATER, Type.GRASS, Type.METAL)));
        rejects(draft("Sparks", null, Set.of()));
    }

    @Test
    @DisplayName("a focus card must be in the deck")
    void focusCards() {
        List<String> cards = List.of("A1-094");
        assertDoesNotThrow(() -> Decks.Api.check(new Decks.Api.Draft("Sparks", cards, Set.of(), "A1-094", null,
                "Coin_Eevee.png", "Sleeve_Default.png", "Playmat_Default.png")));
        rejects(new Decks.Api.Draft("Sparks", cards, Set.of(), "A1-094", "A1-096",
                "Coin_Eevee.png", "Sleeve_Default.png", "Playmat_Default.png"));
    }

    @Test
    @DisplayName("a cosmetic must be a file name of its own kind")
    void cosmetics() {
        rejects(new Decks.Api.Draft("Sparks", List.of(), Set.of(), null, null,
                null, "Sleeve_Default.png", "Playmat_Default.png"));
        rejects(new Decks.Api.Draft("Sparks", List.of(), Set.of(), null, null,
                "Sleeve_Default.png", "Sleeve_Default.png", "Playmat_Default.png"));
        rejects(new Decks.Api.Draft("Sparks", List.of(), Set.of(), null, null,
                "Coin_../x.png", "Sleeve_Default.png", "Playmat_Default.png"));
        rejects(new Decks.Api.Draft("Sparks", List.of(), Set.of(), null, null,
                "Coin_Tails.png", "Sleeve_Default.png", "Playmat_Default.png"));
    }
}
