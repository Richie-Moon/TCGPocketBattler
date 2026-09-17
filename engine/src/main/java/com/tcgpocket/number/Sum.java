package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/** Addition. */
public record Sum(INumber left, INumber right) implements INumber {

    public Sum {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return left.evaluate(context) + right.evaluate(context);
    }
}
