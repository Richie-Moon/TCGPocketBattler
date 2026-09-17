package com.tcgpocket.effect;

import com.tcgpocket.resolve.FlipResult;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Flips until tails comes up, keeping every flip including the final tails.
 *
 * <p>So {@code NumberHeads} reports the run length, which is what "does 20
 * damage for each heads" wants.
 */
public record FlipUntilTails() implements IFlipStrategy {

    /** Guards against a broken RandomSource that only ever returns heads. */
    private static final int SAFETY_LIMIT = 1000;

    @Override
    public FlipResult flip(ResolutionContext context) {
        List<Boolean> results = new ArrayList<>();
        while (results.size() < SAFETY_LIMIT) {
            boolean heads = context.battle().rng().nextBoolean();
            results.add(heads);
            if (!heads) {
                break;
            }
        }
        return new FlipResult(results);
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        context.scope().recordFlip(flip(context));
        return EffectOutcome.APPLIED;
    }
}
