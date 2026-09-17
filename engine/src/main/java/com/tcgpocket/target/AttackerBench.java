package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.List;

/** The bench of the side whose turn it is. */
public record AttackerBench() implements IMultiTarget {

    @Override
    public List<PokemonInPlay> resolve(ResolutionContext context) {
        return context.battle().attacker().bench();
    }
}
