package com.tcgpocket.condition;

import java.util.Objects;

/**
 * Both hold. Short-circuits, so the right side is not evaluated when the left
 * already fails.
 *
 * @param <T> what both conditions examine
 */
public record And<T>(ICondition<T> left, ICondition<T> right) implements ICondition<T> {

    public And {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
    }

    @Override
    public boolean evaluate(T subject) {
        return left.evaluate(subject) && right.evaluate(subject);
    }
}
