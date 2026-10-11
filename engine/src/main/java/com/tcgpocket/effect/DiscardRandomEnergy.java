package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Discards energy chosen at random, whatever its type.
 *
 * <p>A cost: if the target does not have enough, nothing is discarded and this
 * reports {@link EffectOutcome#FAILED}, so an attempt aborts before the payoff
 * that depended on the payment.
 */
public record DiscardRandomEnergy(INumber energyCount, ITarget target) implements IEffect {

    public DiscardRandomEnergy {
        Objects.requireNonNull(energyCount, "energyCount");
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        if (context.shields(resolved.get())) {
            return EffectOutcome.PREVENTED;
        }

        int count = energyCount.evaluate(context);
        if (count <= 0) {
            return EffectOutcome.NO_OP;
        }

        List<Type> taken = Energies.takeRandom(resolved.get(), count, context.battle().rng());
        taken.forEach(type -> resolved.get().owner().addDiscardedEnergy(type, 1));
        return !taken.isEmpty() ? EffectOutcome.APPLIED : EffectOutcome.FAILED;
    }
}
