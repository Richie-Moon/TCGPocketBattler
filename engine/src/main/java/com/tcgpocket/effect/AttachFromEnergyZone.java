package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Takes the energy currently in the controller's Energy Zone and attaches it.
 *
 * <p>Deliberately does <em>not</em> enforce the once-per-turn limit: that is a
 * rule about the turn action, and card text that grants an extra attachment is
 * supposed to bypass it. {@code AttachEnergyAction} is where the limit belongs.
 *
 * <p>Fails when the zone is empty, since there is nothing to attach.
 */
public record AttachFromEnergyZone(ITarget target) implements IEffect {

    public AttachFromEnergyZone {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        Side side = context.controller();
        Optional<Type> available = side.currentEnergy();
        if (available.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        side.consumeCurrentEnergy();
        resolved.get().attachEnergy(available.get(), 1);
        return EffectOutcome.APPLIED;
    }
}
