package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.ConfusionStatus;

/** Whether a Pokemon is Confused. */
public record IsConfused() implements ICondition<PokemonInPlay> {

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.hasStatus(new ConfusionStatus());
    }
}
