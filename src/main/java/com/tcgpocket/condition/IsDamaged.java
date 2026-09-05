package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;

/** Whether a Pokemon has any damage on it — "heal a damaged Pokemon". */
public record IsDamaged() implements ICondition<PokemonInPlay> {

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.damage() > 0;
    }
}
