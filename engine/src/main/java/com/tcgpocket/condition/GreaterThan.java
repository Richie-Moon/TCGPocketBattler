package com.tcgpocket.condition;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * Whether one number evaluates strictly greater than another.
 *
 * <p>Subjected on {@link ResolutionContext} rather than on {@code INumber}:
 * this holds <em>both</em> operands and asks a question about the game, rather
 * than testing some number handed to it from outside.
 */
public record GreaterThan(INumber left, INumber right) implements ICondition<ResolutionContext> {

    public GreaterThan {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        return left.evaluate(context) > right.evaluate(context);
    }
}
