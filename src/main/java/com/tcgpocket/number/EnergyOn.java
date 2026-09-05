package com.tcgpocket.number;

import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Energy attached to a target; 0 if it does not resolve.
 *
 * @param ofType counts only this type when present, every attached energy when
 *               empty
 */
public record EnergyOn(ITarget target, Optional<Type> ofType) implements INumber {

    public EnergyOn {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(ofType, "ofType");
    }

    /** Counts every attached energy regardless of type. */
    public EnergyOn(ITarget target) {
        this(target, Optional.empty());
    }

    public EnergyOn(ITarget target, Type type) {
        this(target, Optional.of(type));
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return target.resolve(context)
                .map(pokemon -> ofType.map(pokemon::energyOf).orElseGet(pokemon::totalEnergy))
                .orElse(0);
    }
}
