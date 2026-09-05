package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;

/**
 * Whether a Pokemon already holds a Tool.
 *
 * <p>Gates attaching another, since a Pokemon may hold only one.
 */
public record HasTool() implements ICondition<PokemonInPlay> {

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.hasTool();
    }
}
