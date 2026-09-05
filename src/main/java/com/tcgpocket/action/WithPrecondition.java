package com.tcgpocket.action;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.List;
import java.util.Objects;

/**
 * Wraps an action in an extra requirement.
 *
 * <p>The condition is checked by {@link #isLegal}, so an action gated this way
 * is simply never offered. Executing one whose condition has since stopped
 * holding fails rather than applying anyway, which matters when the board
 * changed between generating the move list and resolving the choice.
 */
public record WithPrecondition(ICondition<ResolutionContext> condition, IAction action)
        implements IAction {

    public WithPrecondition {
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(action, "action");
    }

    @Override
    public boolean isLegal(ResolutionContext context) {
        return condition.evaluate(context) && action.isLegal(context);
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        if (!condition.evaluate(context)) {
            return AttemptResult.failure("precondition no longer holds", List.of());
        }
        return action.execute(context);
    }
}
