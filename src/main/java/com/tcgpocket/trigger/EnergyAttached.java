package com.tcgpocket.trigger;

import com.tcgpocket.energy.Type;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/** Energy has been attached to a Pokemon. */
public record EnergyAttached(PokemonInPlay target, Type energyType) implements GameEvent {

    public EnergyAttached {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(energyType, "energyType");
    }

    @Override
    public Optional<PokemonInPlay> subject() {
        return Optional.of(target);
    }
}
