package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.IStatus;
import com.tcgpocket.target.ITarget;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Inflicts one status chosen at random from {@code among}, skipping any the
 * target already has — "1 Special Condition … is chosen at random … Any
 * Special Conditions already affecting that Pokémon will not be chosen."
 *
 * <p>The chosen status goes through {@link AddStatus}, so shields and status
 * immunity apply as usual. A target that already has every candidate is a
 * {@link EffectOutcome#NO_OP}.
 */
public record AddRandomStatus(List<IStatus> among, ITarget target) implements IEffect {

    public AddRandomStatus {
        among = List.copyOf(among);
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        List<IStatus> candidates = among.stream().filter(status -> !resolved.get().hasStatus(status)).toList();
        if (candidates.isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        IStatus chosen = candidates.get(context.battle().rng().nextInt(candidates.size()));
        return new AddStatus(chosen, target).apply(context);
    }
}
