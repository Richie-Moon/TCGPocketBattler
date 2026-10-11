package com.tcgpocket.trigger;

import com.tcgpocket.energy.Type;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/**
 * Energy has been attached to a Pokemon.
 *
 * @param fromEnergyZone true when it came out of the Energy Zone — the turn's
 *                       attachment or card text that "takes" one from there —
 *                       and false when it was moved off another Pokemon.
 *                       Darkrai ex's Nightmare Aura counts only the first.
 */
public record EnergyAttached(PokemonInPlay target, Type energyType, boolean fromEnergyZone) implements GameEvent {

    public EnergyAttached {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(energyType, "energyType");
    }

    @Override
    public Optional<PokemonInPlay> subject() {
        return Optional.of(target);
    }
}
