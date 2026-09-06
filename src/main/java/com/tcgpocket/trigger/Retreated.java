package com.tcgpocket.trigger;

import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/**
 * A side has retreated, swapping its active Pokemon for a benched one.
 *
 * <p>The subject is the Pokemon coming <em>up</em>: a trigger reacting to a
 * retreat almost always cares about who is now in front of it.
 */
public record Retreated(PokemonInPlay from, PokemonInPlay to) implements GameEvent {

    public Retreated {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
    }

    @Override
    public Optional<PokemonInPlay> subject() {
        return Optional.of(to);
    }
}
