package com.tcgpocket.condition;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * True with the given percentage chance.
 *
 * <p>An escape hatch for odds that are not expressible as coin flips. Card text
 * overwhelmingly uses flips, which record their result in the resolution scope
 * so later nodes can read it — this does not, so prefer a flip whenever the
 * card actually says "flip a coin".
 *
 * <p>Context-subjected rather than generic, because it needs the battle's
 * {@code RandomSource}: a bare Pokemon could not supply one.
 *
 * @param percentChance 0 or less is never, 100 or more is always
 */
public record Probability(INumber percentChance) implements ICondition<ResolutionContext> {

    public Probability {
        Objects.requireNonNull(percentChance, "percentChance");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        int percent = percentChance.evaluate(context);
        if (percent <= 0) {
            return false;
        }
        if (percent >= 100) {
            return true;
        }
        return context.battle().rng().nextInt(100) < percent;
    }
}
