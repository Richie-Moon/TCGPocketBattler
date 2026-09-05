package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.PoisonStatus;

/** Whether a Pokemon is Poisoned. */
public record IsPoisoned() implements ICondition<PokemonInPlay> {

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.hasStatus(new PoisonStatus());
    }
}
