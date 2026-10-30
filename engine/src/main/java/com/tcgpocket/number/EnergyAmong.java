package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.IMultiTarget;

import java.util.Objects;

/**
 * Energy attached across several Pokemon — Espeon's "each Energy attached to all
 * of your opponent's Pokémon". {@link EnergyOn} is the single-target form.
 */
public record EnergyAmong(IMultiTarget targets) implements INumber {

    public EnergyAmong {
        Objects.requireNonNull(targets, "targets");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return targets.resolve(context).stream().mapToInt(PokemonInPlay::totalEnergy).sum();
    }
}
