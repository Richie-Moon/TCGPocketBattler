package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Optional;

/** The active Pokemon of the side that is not taking its turn. */
public record OpponentActive() implements ITarget {

    @Override
    public Optional<PokemonInPlay> resolve(ResolutionContext context) {
        return context.battle().defender().active();
    }
}
