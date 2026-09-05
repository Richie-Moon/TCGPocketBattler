package com.tcgpocket.condition;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/** Whether two numbers evaluate to the same value. */
public record EqualTo(INumber left, INumber right) implements ICondition<ResolutionContext> {

    public EqualTo {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        return left.evaluate(context) == right.evaluate(context);
    }
}
