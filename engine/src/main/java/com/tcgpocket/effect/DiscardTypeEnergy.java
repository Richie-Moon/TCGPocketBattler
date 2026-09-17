package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Discards energy of a specific type — "discard a Fire Energy from this
 * Pokemon".
 *
 * <p>A cost, so it is all-or-nothing: too little of that type discards nothing
 * and fails.
 */
public record DiscardTypeEnergy(Type energyType, INumber energyCount, ITarget target) implements IEffect {

    public DiscardTypeEnergy {
        Objects.requireNonNull(energyType, "energyType");
        Objects.requireNonNull(energyCount, "energyCount");
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        int count = energyCount.evaluate(context);
        if (count <= 0) {
            return EffectOutcome.NO_OP;
        }

        PokemonInPlay pokemon = resolved.get();
        if (pokemon.energyOf(energyType) < count) {
            return EffectOutcome.FAILED;
        }

        pokemon.discardEnergy(energyType, count);
        return EffectOutcome.APPLIED;
    }
}
