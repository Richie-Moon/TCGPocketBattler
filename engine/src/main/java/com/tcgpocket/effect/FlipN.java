package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.number.Literal;
import com.tcgpocket.resolve.FlipResult;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Flips a fixed number of coins.
 *
 * @param forCardEffect false for a Special Condition's own flip, which
 *                      {@link GuaranteeNextHeads} does not reach
 */
public record FlipN(INumber n, boolean forCardEffect) implements IFlipStrategy {

    public FlipN {
        Objects.requireNonNull(n, "n");
    }

    public FlipN(INumber n) {
        this(n, true);
    }

    /** The one coin a Special Condition flips. */
    public static FlipN forSpecialCondition() {
        return new FlipN(new Literal(1), false);
    }

    @Override
    public FlipResult flip(ResolutionContext context) {
        int count = Math.max(0, n.evaluate(context));
        List<Boolean> results = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            results.add(forCardEffect ? coin(context) : context.battle().rng().nextBoolean());
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
