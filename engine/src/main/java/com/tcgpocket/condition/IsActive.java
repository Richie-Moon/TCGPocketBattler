package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Zone;

/**
 * Whether a Pokemon is in the active spot.
 *
 * <p>Answerable from the instance alone, because an instance tracks its own
 * zone. Kept as its own node rather than folded into {@link InZone} because it
 * reads far more naturally in card text.
 */
public record IsActive() implements ICondition<PokemonInPlay> {

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.zone() == Zone.ACTIVE;
    }
}
