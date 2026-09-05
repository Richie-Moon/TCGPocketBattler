package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.SleepStatus;

/** Whether a Pokemon is Asleep. */
public record IsAsleep() implements ICondition<PokemonInPlay> {

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.hasStatus(new SleepStatus());
    }
}
