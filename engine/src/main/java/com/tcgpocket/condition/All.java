package com.tcgpocket.condition;

import java.util.List;

/**
 * Every listed condition holds. Vacuously true when the list is empty, which
 * makes it a safe default for a card with no restrictions.
 *
 * @param <T> what the conditions examine
 */
public record All<T>(List<ICondition<T>> conditions) implements ICondition<T> {

    public All {
        conditions = List.copyOf(conditions);
    }

    @Override
    public boolean evaluate(T subject) {
        return conditions.stream().allMatch(condition -> condition.evaluate(subject));
    }
}
