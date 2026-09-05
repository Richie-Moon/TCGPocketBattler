package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;

/** A target's remaining HP — max HP less damage taken; 0 if it does not resolve. */
public record CurrentHP(ITarget target) implements INumber {

    public CurrentHP {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return target.resolve(context).map(PokemonInPlay::currentHp).orElse(0);
    }
}
