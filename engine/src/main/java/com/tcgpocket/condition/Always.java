package com.tcgpocket.condition;

/**
 * Always true, whatever the subject.
 *
 * <p>The identity element for conditions: it lets a {@code Branch} express a
 * final catch-all case, and lets an unconditional card be built without a
 * special case in the builder.
 *
 * @param <T> what this condition nominally examines, and ignores
 */
public record Always<T>() implements ICondition<T> {

    @Override
    public boolean evaluate(T subject) {
        return true;
    }
}
