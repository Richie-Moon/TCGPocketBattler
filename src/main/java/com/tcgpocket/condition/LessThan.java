package com.tcgpocket.condition;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/** Whether one number evaluates strictly less than another. */
public record LessThan(INumber left, INumber right) implements ICondition<ResolutionContext> {

    public LessThan {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        return left.evaluate(context) < right.evaluate(context);
    }
}
