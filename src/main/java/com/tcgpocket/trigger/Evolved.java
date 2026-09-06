package com.tcgpocket.trigger;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/**
 * A Pokemon has evolved.
 *
 * <p>{@code from} is the printed card that was underneath, because the instance
 * is the same object before and after — evolving mutates it in place so damage
 * and energy carry over.
 */
public record Evolved(PokemonCard from, PokemonInPlay to) implements GameEvent {

    public Evolved {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
    }

    @Override
    public Optional<PokemonInPlay> subject() {
        return Optional.of(to);
    }
}
