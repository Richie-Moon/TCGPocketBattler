package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.IStatus;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Clears a status.
 *
 * <p>Removing one the Pokemon does not have is a {@link EffectOutcome#NO_OP},
 * not a failure.
 */
public record RemoveStatus(ITarget target, IStatus statusToRemove) implements IEffect {

    public RemoveStatus {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(statusToRemove, "statusToRemove");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        return resolved.get().removeStatus(statusToRemove)
                ? EffectOutcome.APPLIED
                : EffectOutcome.NO_OP;
    }
}
