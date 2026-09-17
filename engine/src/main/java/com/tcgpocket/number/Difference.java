package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * Subtraction.
 *
 * <p>May evaluate negative, deliberately. Flooring at zero is the damage
 * pipeline's job, because only it knows whether a negative intermediate should
 * become no damage or cancel out against a later bonus.
 */
public record Difference(INumber left, INumber right) implements INumber {

    public Difference {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return left.evaluate(context) - right.evaluate(context);
    }
}
