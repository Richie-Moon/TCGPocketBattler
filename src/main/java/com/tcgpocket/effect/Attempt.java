package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * The ordinary attempt: run these effects in order, stopping at the first
 * failure.
 *
 * <p>An empty attempt succeeds vacuously, so a card with no effects needs no
 * special case at the call site.
 */
public record Attempt(List<IEffect> effects) implements IAttempt {

    public Attempt {
        effects = List.copyOf(effects);
    }

    /** Reads naturally when building card text: {@code new Attempt(flip, damage)}. */
    public Attempt(IEffect... effects) {
        this(List.of(effects));
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        List<EffectOutcome> outcomes = new ArrayList<>(effects.size());

        for (int step = 0; step < effects.size(); step++) {
            IEffect effect = effects.get(step);
            EffectOutcome outcome = effect.apply(context);
            outcomes.add(outcome);

            if (outcome.isFailure()) {
                return AttemptResult.failure(describeFailure(effect, step), outcomes);
            }
        }

        return AttemptResult.success(outcomes);
    }

    private static String describeFailure(IEffect effect, int step) {
        return "step " + step + " (" + effect.getClass().getSimpleName() + ") could not be applied";
    }
}
