package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;

/**
 * Does nothing, successfully — the null object.
 *
 * <p>Reports {@link EffectOutcome#NO_OP} rather than APPLIED, so a placeholder
 * never claims to have changed the board, and never aborts an attempt either.
 */
public record NoEffect() implements IEffect {

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        return EffectOutcome.NO_OP;
    }
}
