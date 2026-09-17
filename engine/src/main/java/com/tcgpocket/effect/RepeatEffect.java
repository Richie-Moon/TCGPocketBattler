package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * Applies an effect several times.
 *
 * <p>Stops at the first failure and reports it, matching how an attempt treats
 * a failing step: repeating a cost that can no longer be paid should not keep
 * trying.
 */
public record RepeatEffect(INumber times, IEffect effect) implements IEffect {

    public RepeatEffect {
        Objects.requireNonNull(times, "times");
        Objects.requireNonNull(effect, "effect");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int count = times.evaluate(context);
        if (count <= 0) {
            return EffectOutcome.NO_OP;
        }

        EffectOutcome last = EffectOutcome.NO_OP;
        for (int i = 0; i < count; i++) {
            last = effect.apply(context);
            if (last.isFailure()) {
                return EffectOutcome.FAILED;
            }
        }
        return last;
    }
}
