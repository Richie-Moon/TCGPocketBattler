package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.target.ITarget;

import java.util.Objects;

/** A target's evolution stage: 0 for a Basic, 1 or 2 for its evolutions. */
public record Stage(ITarget target) implements INumber {

    public Stage {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return target.resolve(context)
                .map(pokemon -> pokemon.definition().stage())
                .orElse(0);
    }
}
