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
 * Moves energy from one Pokemon to another, preserving its types.
 *
 * <p>Fails if either end does not resolve or the source has too little, and in
 * that case moves nothing.
 */
public record MoveEnergy(ITarget from, ITarget to, INumber energyCount) implements IEffect {

    public MoveEnergy {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        Objects.requireNonNull(energyCount, "energyCount");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> source = from.resolve(context);
        Optional<PokemonInPlay> destination = to.resolve(context);
        if (source.isEmpty() || destination.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        int count = energyCount.evaluate(context);
        if (count <= 0) {
            return EffectOutcome.NO_OP;
        }

        List<Type> moved = Energies.takeRandom(source.get(), count, context.battle().rng());
        if (moved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        moved.forEach(type -> destination.get().attachEnergy(type, 1));
        return EffectOutcome.APPLIED;
    }
}
