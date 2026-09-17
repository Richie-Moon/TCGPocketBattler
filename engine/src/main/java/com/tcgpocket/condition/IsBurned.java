package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.BurnStatus;

/** Whether a Pokemon is Burned. */
public record IsBurned() implements ICondition<PokemonInPlay> {

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.hasStatus(new BurnStatus());
    }
}
