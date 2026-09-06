package com.tcgpocket.trigger;

import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/** A Tool has been attached to a Pokemon. */
public record ToolAttached(PokemonInPlay holder, CardInstance tool) implements GameEvent {

    public ToolAttached {
        Objects.requireNonNull(holder, "holder");
        Objects.requireNonNull(tool, "tool");
    }

    @Override
    public Optional<PokemonInPlay> subject() {
        return Optional.of(holder);
    }
}
