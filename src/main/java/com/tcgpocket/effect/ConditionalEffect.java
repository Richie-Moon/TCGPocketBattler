package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * Applies its inner effect only when the condition holds.
 *
 * <p>Distinct from an attempt's short-circuit: a branch that is <em>meant</em>
 * to be skipped reports {@link EffectOutcome#NO_OP}, so what follows still
 * runs. Use this for "if heads, ..."; let a failing cost handle
 * "... if you do, ...".
 */
public record ConditionalEffect(ICondition<ResolutionContext> condition, IEffect effect)
        implements IEffect {

    public ConditionalEffect {
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(effect, "effect");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        return condition.evaluate(context) ? effect.apply(context) : EffectOutcome.NO_OP;
    }
}
