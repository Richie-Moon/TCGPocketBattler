package com.tcgpocket.condition;

import com.tcgpocket.energy.Type;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;

/** Whether a Pokemon is of a given type — "each of your Lightning Pokemon". */
public record HasType(Type type) implements ICondition<PokemonInPlay> {

    public HasType {
        Objects.requireNonNull(type, "type");
    }

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.definition().type() == type;
    }
}
