package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Moves every Energy on one Pokemon to another, keeping its types — Feathery
 * Cyclone's "move all Energy from this Pokémon".
 *
 * <p>Not {@link MoveEnergy}: that asks which type each unit is, a question with
 * only one answer when all of them go. Fails if either end does not resolve; with
 * nothing attached it is a {@link EffectOutcome#NO_OP}.
 */
public record MoveAllEnergy(ITarget from, ITarget to) implements IEffect {

    public MoveAllEnergy {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> source = from.resolve(context);
        Optional<PokemonInPlay> destination = to.resolve(context);
        if (source.isEmpty() || destination.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        if (context.shields(source.get())) {
            return EffectOutcome.PREVENTED;
        }
        if (source.get().totalEnergy() == 0) {
            return EffectOutcome.NO_OP;
        }
        for (Type type : Energies.asUnits(source.get())) {
            source.get().detachEnergy(type, 1);
            Energies.attachMoved(context, destination.get(), type, 1);
        }
        return EffectOutcome.APPLIED;
    }
}
