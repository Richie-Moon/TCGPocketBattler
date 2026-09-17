package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.ActiveModifier;
import com.tcgpocket.state.ModifierKind;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Sharpens damage the target deals, for a number of turns.
 *
 * <p>Applied before weakness in the pipeline, so weakness multiplies the
 * boosted figure rather than the printed one.
 */
public record IncreaseDamage(INumber increaseAmount, ITarget target, INumber duration)
        implements IDurationEffect {

    public IncreaseDamage {
        Objects.requireNonNull(increaseAmount, "increaseAmount");
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(duration, "duration");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        int amount = increaseAmount.evaluate(context);
        if (amount <= 0) {
            return EffectOutcome.NO_OP;
        }

        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        resolved.get().addModifier(
                new ActiveModifier(ModifierKind.INCREASE_DAMAGE_DEALT, amount, expiry));
        return EffectOutcome.APPLIED;
    }
}
