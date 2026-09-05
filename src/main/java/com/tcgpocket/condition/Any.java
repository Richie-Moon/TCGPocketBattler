package com.tcgpocket.condition;

import java.util.List;

/**
 * At least one listed condition holds. False when the list is empty, mirroring
 * {@link All}'s vacuous truth.
 *
 * @param <T> what the conditions examine
 */
public record Any<T>(List<ICondition<T>> conditions) implements ICondition<T> {

    public Any {
        conditions = List.copyOf(conditions);
    }

    @Override
    public boolean evaluate(T subject) {
        return conditions.stream().anyMatch(condition -> condition.evaluate(subject));
    }
}
