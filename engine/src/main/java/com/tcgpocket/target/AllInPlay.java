package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.ArrayList;
import java.util.List;

/** Every Pokemon on the board, both sides, attacker's first. */
public record AllInPlay() implements IMultiTarget {

    @Override
    public List<PokemonInPlay> resolve(ResolutionContext context) {
        List<PokemonInPlay> all = new ArrayList<>(context.battle().attacker().inPlay());
        all.addAll(context.battle().defender().inPlay());
        return List.copyOf(all);
    }
}
