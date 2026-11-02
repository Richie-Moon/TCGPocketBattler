package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.target.ITarget;

import java.util.Objects;

/** How many different types of Energy a target has attached; 0 if it does not resolve — Altaria's Dragon Arcana. */
public record EnergyTypesOn(ITarget target) implements INumber {

    public EnergyTypesOn {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return target.resolve(context).map(pokemon -> pokemon.attachedEnergy().size()).orElse(0);
    }
}
