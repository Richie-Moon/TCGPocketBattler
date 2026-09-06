package com.tcgpocket.trigger;

import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/** Damage counters have come off a Pokemon. */
public record Healed(PokemonInPlay target, int amount) implements GameEvent {

    public Healed {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public Optional<PokemonInPlay> subject() {
        return Optional.of(target);
    }
}
