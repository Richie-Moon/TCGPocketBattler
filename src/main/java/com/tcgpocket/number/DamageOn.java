package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;

/**
 * Damage counters already on a target; 0 if it does not resolve.
 *
 * <p>Backs the "this attack does more damage the more hurt it is" family, and
 * is the complement of {@link CurrentHP} rather than a duplicate of it.
 */
public record DamageOn(ITarget target) implements INumber {

    public DamageOn {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return target.resolve(context).map(PokemonInPlay::damage).orElse(0);
    }
}
