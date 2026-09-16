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
 * Softens damage the target deals, for a number of turns — "during your
 * opponent's next turn, attacks used by the Defending Pokemon do -20 damage".
 *
 * <p>The mirror of {@link IncreaseDamage}: applied before weakness, so
 * weakness adds to the softened figure.
 */
public record ReduceDamageDealt(INumber reductionAmount, ITarget target, INumber duration)
        implements IDurationEffect {

    public ReduceDamageDealt {
        Objects.requireNonNull(reductionAmount, "reductionAmount");
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(duration, "duration");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        int amount = reductionAmount.evaluate(context);
        if (amount <= 0) {
            return EffectOutcome.NO_OP;
        }

        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        resolved.get().addModifier(
                new ActiveModifier(ModifierKind.REDUCE_DAMAGE_DEALT, amount, expiry));
        return EffectOutcome.APPLIED;
    }
}
