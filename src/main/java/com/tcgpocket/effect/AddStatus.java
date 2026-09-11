package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.IStatus;
import com.tcgpocket.target.ITarget;
import com.tcgpocket.trigger.StatusApplied;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.Objects;
import java.util.Optional;

/**
 * Inflicts a status.
 *
 * <p>Stacking is the Pokemon's business: applying a second special condition
 * replaces the first, while poison and burn accumulate alongside it.
 */
public record AddStatus(IStatus statusToAdd, ITarget target) implements IEffect {

    public AddStatus {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(statusToAdd, "statusToAdd");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        PokemonInPlay pokemon = resolved.get();
        if (pokemon.hasStatus(statusToAdd)) {
            return EffectOutcome.NO_OP;
        }

        pokemon.addStatus(statusToAdd);
        TriggerDispatcher.dispatch(context.battle(), new StatusApplied(pokemon, statusToAdd));
        return EffectOutcome.APPLIED;
    }
}
