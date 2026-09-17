package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Discards all energy attached to the target.
 */
public record DiscardAllEnergy(ITarget target) implements IEffect {
    
    public DiscardAllEnergy {
        Objects.requireNonNull(target, "target");
    }
    
    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        
        boolean paid = !Energies.takeAll(resolved.get()).isEmpty();
        return paid ? EffectOutcome.APPLIED : EffectOutcome.FAILED;
    }
}
