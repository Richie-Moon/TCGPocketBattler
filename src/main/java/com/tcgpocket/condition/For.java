package com.tcgpocket.condition;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;

/**
 * Lifts a card-level condition up to the game level by supplying a target —
 * "if the Defending Pokemon is Burned".
 *
 * <p>This adapter is what lets everything that holds a precondition hold a
 * single {@code ICondition<ResolutionContext>}, instead of needing a parallel
 * set of overloads for each subject type.
 *
 * <p>An unresolvable target evaluates false: nothing satisfies a condition when
 * there is nothing there.
 */
public record For(ICondition<PokemonInPlay> condition, ITarget target)
        implements ICondition<ResolutionContext> {

    public For {
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(target, "target");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        return target.resolve(context)
                .map(condition::evaluate)
                .orElse(false);
    }
}
