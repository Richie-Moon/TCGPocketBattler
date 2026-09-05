package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.ParalysisStatus;

/** Whether a Pokemon is Paralyzed. */
public record IsParalyzed() implements ICondition<PokemonInPlay> {

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.hasStatus(new ParalysisStatus());
    }
}
