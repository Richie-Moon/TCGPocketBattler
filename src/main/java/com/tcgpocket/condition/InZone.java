package com.tcgpocket.condition;

import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Zone;

import java.util.Objects;

/**
 * Whether a card is in a given zone — the general form of "in your hand".
 *
 * <p>Subjected on {@link CardInstance} rather than {@code PokemonInPlay}, so it
 * applies to Trainer cards too; by subtyping it still accepts a Pokemon.
 */
public record InZone(Zone zone) implements ICondition<CardInstance> {

    public InZone {
        Objects.requireNonNull(zone, "zone");
    }

    @Override
    public boolean evaluate(CardInstance subject) {
        return subject.zone() == zone;
    }
}
