package com.tcgpocket.card;

import java.util.Objects;

/**
 * The holder can also be evolved by whatever evolves from {@code species} — Eevee
 * ex's Veevee 'volve, whose name alone would not match Leafeon's "evolves from
 * Eevee".
 *
 * <p>Read by {@code EvolveAction.isLegal}.
 */
public record EvolvesAs(String species) implements IRule {

    public EvolvesAs {
        Objects.requireNonNull(species, "species");
    }
}
