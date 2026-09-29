package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * Softens every attack the opponent's Pokemon make against the controller's
 * Pokemon — Blue's "during your opponent's next turn, all of your Pokemon take
 * −10 damage from attacks from your opponent's Pokemon".
 *
 * <p>Not {@link ReduceDamageTaken}, for the reason {@link IncreaseSideDamage}
 * is not {@link IncreaseDamage}: the text names no Pokemon, so it stamps the
 * controller's {@code Side} and {@code DamageCalculator} reads it for whichever
 * Pokemon is hit.
 */
public record ReduceSideDamageTaken(INumber reductionAmount, INumber duration) implements IDurationEffect {

    public ReduceSideDamageTaken {
        Objects.requireNonNull(reductionAmount, "reductionAmount");
        Objects.requireNonNull(duration, "duration");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int amount = reductionAmount.evaluate(context);
        if (amount <= 0) {
            return EffectOutcome.NO_OP;
        }
        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        context.controller().addDamageReduction(amount, expiry);
        return EffectOutcome.APPLIED;
    }
}
