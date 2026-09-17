package com.tcgpocket.trigger;

import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.IStatus;

import java.util.Objects;
import java.util.Optional;

/** A special condition has been put on a Pokemon. */
public record StatusApplied(PokemonInPlay target, IStatus status) implements GameEvent {

    public StatusApplied {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(status, "status");
    }

    @Override
    public Optional<PokemonInPlay> subject() {
        return Optional.of(target);
    }
}
