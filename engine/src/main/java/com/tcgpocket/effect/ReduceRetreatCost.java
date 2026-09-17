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
 * Lowers the target's retreat cost, for a number of turns — "during this turn,
 * the Retreat Cost of your Active Pokemon is 1 less".
 *
 * <p>A modifier on the Pokemon, like {@link PreventRetreat}, so it leaves the
 * Active Spot with its holder: whatever switches in pays its printed cost.
 * {@code RetreatAction} subtracts it, never below zero.
 */
public record ReduceRetreatCost(INumber reductionAmount, ITarget target, INumber duration)
        implements IDurationEffect {

    public ReduceRetreatCost {
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
                new ActiveModifier(ModifierKind.REDUCE_RETREAT_COST, amount, expiry));
        return EffectOutcome.APPLIED;
    }
}
