package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.List;

/** Active and bench of the side whose turn it is. */
public record AttackerAll() implements IMultiTarget {

    @Override
    public List<PokemonInPlay> resolve(ResolutionContext context) {
        return context.battle().attacker().inPlay();
    }
}
