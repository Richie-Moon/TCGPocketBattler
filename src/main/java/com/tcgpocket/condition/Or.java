package com.tcgpocket.condition;

import java.util.Objects;

/**
 * Either holds. Short-circuits once the left side succeeds.
 *
 * @param <T> what both conditions examine
 */
public record Or<T>(ICondition<T> left, ICondition<T> right) implements ICondition<T> {

    public Or {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
    }

    @Override
    public boolean evaluate(T subject) {
        return left.evaluate(subject) || right.evaluate(subject);
    }
}
