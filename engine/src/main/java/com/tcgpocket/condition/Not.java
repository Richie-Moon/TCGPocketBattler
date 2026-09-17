package com.tcgpocket.condition;

import java.util.Objects;

/**
 * Negation, generic over any subject.
 *
 * @param <T> what the wrapped condition examines
 */
public record Not<T>(ICondition<T> condition) implements ICondition<T> {

    public Not {
        Objects.requireNonNull(condition, "condition");
    }

    @Override
    public boolean evaluate(T subject) {
        return !condition.evaluate(subject);
    }
}
