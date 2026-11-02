package com.tcgpocket.trigger;

import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/**
 * A Pokemon has been played from hand onto the Bench — Misdreavus' "when you put
 * this Pokémon from your hand onto your Bench".
 *
 * <p>Separate from {@link CardPlayed}, which announces before the card lands, so
 * that a Pokemon's own ability can hear it. Not dispatched for a Pokemon benched
 * by an effect or during setup.
 */
public record PokemonBenched(PokemonInPlay pokemon) implements GameEvent {

    public PokemonBenched {
        Objects.requireNonNull(pokemon, "pokemon");
    }

    @Override
    public Optional<PokemonInPlay> subject() {
        return Optional.of(pokemon);
    }
}
