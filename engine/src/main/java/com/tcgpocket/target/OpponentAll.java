package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.List;

/** Active and bench of the side that is not taking its turn. */
public record OpponentAll() implements IMultiTarget {

    @Override
    public List<PokemonInPlay> resolve(ResolutionContext context) {
        return context.battle().defender().inPlay();
    }
}
