package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;

/** A target's printed HP; 0 if the target does not resolve. */
public record MaxHP(ITarget target) implements INumber {

    public MaxHP {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return target.resolve(context).map(PokemonInPlay::maxHp).orElse(0);
    }
}
