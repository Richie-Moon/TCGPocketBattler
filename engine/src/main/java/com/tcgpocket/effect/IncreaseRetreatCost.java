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
 * Raises the target's retreat cost by Colorless, for a number of turns —
 * "during your opponent's next turn, its Retreat Cost is 1 Colorless more".
 * The mirror of {@link ReduceRetreatCost}.
 */
public record IncreaseRetreatCost(INumber increaseAmount, ITarget target, INumber duration)
        implements IDurationEffect {

    public IncreaseRetreatCost {
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
                new ActiveModifier(ModifierKind.INCREASE_RETREAT_COST, amount, expiry));
        return EffectOutcome.APPLIED;
    }
}
