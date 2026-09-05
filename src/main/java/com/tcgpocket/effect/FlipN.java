package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.FlipResult;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Flips a fixed number of coins. */
public record FlipN(INumber n) implements IFlipStrategy {

    public FlipN {
        Objects.requireNonNull(n, "n");
    }

    @Override
    public FlipResult flip(ResolutionContext context) {
        int count = Math.max(0, n.evaluate(context));
        List<Boolean> results = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            results.add(context.battle().rng().nextBoolean());
        }
        return new FlipResult(results);
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        FlipResult result = flip(context);
        context.scope().recordFlip(result);
        return result.flips() == 0 ? EffectOutcome.NO_OP : EffectOutcome.APPLIED;
    }
}
