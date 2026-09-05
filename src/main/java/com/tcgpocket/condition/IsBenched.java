package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Zone;

/** Whether a Pokemon is on the bench. */
public record IsBenched() implements ICondition<PokemonInPlay> {

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.zone() == Zone.BENCH;
    }
}
