package com.tcgpocket.condition;

import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;

/**
 * Whether a Pokemon has the energy to pay a cost.
 *
 * <p>This is what gates an attack. Colorless requirements are paid by any
 * leftover energy, so the check is delegated to
 * {@link EnergyCost#isSatisfiedBy}.
 */
public record HasEnergy(EnergyCost cost) implements ICondition<PokemonInPlay> {

    public HasEnergy {
        Objects.requireNonNull(cost, "cost");
    }

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return cost.isSatisfiedBy(subject.attachedEnergy());
    }
}
