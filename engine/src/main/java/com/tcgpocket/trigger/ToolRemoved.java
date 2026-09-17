package com.tcgpocket.trigger;

import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/** A Tool has come off a Pokemon. */
public record ToolRemoved(PokemonInPlay holder, CardInstance tool) implements GameEvent {

    public ToolRemoved {
        Objects.requireNonNull(holder, "holder");
        Objects.requireNonNull(tool, "tool");
    }

    @Override
    public Optional<PokemonInPlay> subject() {
        return Optional.of(holder);
    }
}
