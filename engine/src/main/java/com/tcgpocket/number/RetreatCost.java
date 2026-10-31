package com.tcgpocket.number;

import com.tcgpocket.action.RetreatAction;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.target.ITarget;

import java.util.Objects;

/**
 * How much Energy a Pokemon would pay to retreat right now — Grass Knot's "each
 * Energy in your opponent's Active Pokémon's Retreat Cost".
 *
 * <p>The current cost, not the printed one: it goes through {@code RetreatAction}
 * so every reduction and increase the retreat itself honours counts here too.
 * Zero when the target does not resolve.
 */
public record RetreatCost(ITarget target) implements INumber {

    public RetreatCost {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return target.resolve(context)
                .map(pokemon -> RetreatAction.retreatCostOf(context, pokemon).total())
                .orElse(0);
    }
}
