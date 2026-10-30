package com.tcgpocket.number;

import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.target.ISideTarget;

import java.util.Objects;

/**
 * Energy of a type in a side's discard pile — what makes Flareon ex's Combust
 * unusable with nothing to attach, since attaching from an empty pile is only a
 * {@code NO_OP} and would not stop its "if you do".
 */
public record EnergyInDiscard(ISideTarget side, Type energyType) implements INumber {

    public EnergyInDiscard {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(energyType, "energyType");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return side.resolve(context).discardedEnergy().getOrDefault(energyType, 0);
    }
}
