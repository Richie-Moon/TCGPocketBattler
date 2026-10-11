package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Attaches energy from the controller's discard pile — Volkner's "attach 2
 * Lightning Energy from your discard pile to that Pokemon".
 *
 * <p>Takes what is there, up to the amount; an empty pile is
 * {@link EffectOutcome#NO_OP}. Not from the Energy Zone, so it announces
 * {@code EnergyAttached} as a move does.
 */
public record AttachFromDiscard(Type energyType, INumber amount, ITarget target) implements IEffect {

    public AttachFromDiscard {
        Objects.requireNonNull(energyType, "energyType");
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        int taken = context.controller().takeDiscardedEnergy(energyType, amount.evaluate(context));
        if (taken == 0) {
            return EffectOutcome.NO_OP;
        }
        Energies.attachMoved(context, resolved.get(), energyType, taken);
        return EffectOutcome.APPLIED;
    }
}
