package com.tcgpocket.condition;

import com.tcgpocket.state.CardInstance;

import java.util.Objects;

/**
 * Whether a card is a particular species — "discard a Pikachu from your hand".
 *
 * <p>Subjected on {@link CardInstance} rather than on the resolution context,
 * because it asks about a specific copy handed to it. An instance can answer
 * this from its own definition, with no board lookup.
 */
public record IsSpecies(String species) implements ICondition<CardInstance> {

    public IsSpecies {
        Objects.requireNonNull(species, "species");
    }

    @Override
    public boolean evaluate(CardInstance subject) {
        return subject.definition().name().equals(species);
    }
}
