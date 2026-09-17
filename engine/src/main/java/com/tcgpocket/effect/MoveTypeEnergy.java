package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.IMultiTarget;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Moves every Energy of one type from a group onto one Pokemon — Lt. Surge's
 * "move all Lightning Energy from your Benched Pokemon to your Raichu".
 *
 * <p>Not {@link MoveEnergy}, which moves a count of random Energy from a single
 * source: here the type is named, the amount is "all", and there are several
 * sources. Finding none to move is a {@link EffectOutcome#NO_OP}; only an
 * unresolved destination fails.
 */
public record MoveTypeEnergy(Type energyType, IMultiTarget from, ITarget to) implements IEffect {

    public MoveTypeEnergy {
        Objects.requireNonNull(energyType, "energyType");
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> destination = to.resolve(context);
        if (destination.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        int moved = 0;
        for (PokemonInPlay source : from.resolve(context)) {
            if (source != destination.get()) {
                moved += source.discardEnergy(energyType, source.energyOf(energyType));
            }
        }
        destination.get().attachEnergy(energyType, moved);
        return moved > 0 ? EffectOutcome.APPLIED : EffectOutcome.NO_OP;
    }
}
