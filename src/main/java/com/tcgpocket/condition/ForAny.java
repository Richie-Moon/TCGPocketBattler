package com.tcgpocket.condition;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.IMultiTarget;

import java.util.Objects;

/**
 * The same lift as {@link For}, over a group: true when at least one of the
 * targeted Pokemon satisfies the condition.
 *
 * <p>False for an empty group.
 */
public record ForAny(ICondition<? super PokemonInPlay> condition, IMultiTarget targets)
        implements ICondition<ResolutionContext> {

    public ForAny {
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(targets, "targets");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        return targets.resolve(context).stream().anyMatch(condition::evaluate);
    }
}
