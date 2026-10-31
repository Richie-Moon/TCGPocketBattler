package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;

/**
 * How many Pokemon a {@code DiscardAnyFromPlay} discarded earlier in this
 * resolution — Wild Swing's "for each Benched Pokémon you discarded in this way".
 */
public record PokemonDiscarded() implements INumber {

    @Override
    public int evaluate(ResolutionContext context) {
        return context.scope().pokemonDiscarded();
    }
}
