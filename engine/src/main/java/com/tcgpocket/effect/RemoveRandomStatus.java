package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.IStatus;
import com.tcgpocket.target.ITarget;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Clears one of the target's statuses, chosen at random — Big Malasada's
 * "remove a random Special Condition".
 *
 * <p>The counterpart to {@link AddRandomStatus}. Every status counts, as Pocket
 * calls all five Special Conditions. The removal goes through
 * {@link RemoveStatus}, so {@code StatusRemoved} is announced as usual; a
 * Pokemon with no status is a {@link EffectOutcome#NO_OP}.
 */
public record RemoveRandomStatus(ITarget target) implements IEffect {

    public RemoveRandomStatus {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        List<IStatus> statuses = List.copyOf(resolved.get().statuses());
        if (statuses.isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        IStatus chosen = statuses.get(context.battle().rng().nextInt(statuses.size()));
        return new RemoveStatus(target, chosen).apply(context);
    }
}
