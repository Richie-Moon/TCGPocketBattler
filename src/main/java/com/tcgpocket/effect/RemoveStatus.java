package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.IStatus;
import com.tcgpocket.target.ITarget;
import com.tcgpocket.trigger.StatusRemoved;
import com.tcgpocket.trigger.TriggerDispatcher;

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

        PokemonInPlay pokemon = resolved.get();
        if (!pokemon.removeStatus(statusToRemove)) {
            return EffectOutcome.NO_OP;
        }

        TriggerDispatcher.dispatch(context.battle(), new StatusRemoved(pokemon, statusToRemove));
        return EffectOutcome.APPLIED;
    }
}
