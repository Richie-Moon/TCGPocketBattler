package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;

/**
 * Rerolls the Energy a side's zone has previewed for its next turn, to one of
 * {@code types} at random — Porygon-Z's Buggy Beam.
 *
 * <p>The types are the card's printed list rather than "every type", so a card
 * naming a narrower list needs no new node. They need not be types that side's
 * deck registered: that is the point of the attack.
 */
public record ChangeNextEnergy(ISideTarget side, List<Type> types) implements IEffect {

    public ChangeNextEnergy {
        Objects.requireNonNull(side, "side");
        types = List.copyOf(types);
        if (types.isEmpty() || !types.stream().allMatch(Type::isGeneratable)) {
            throw new IllegalArgumentException("ChangeNextEnergy needs generatable types, was " + types);
        }
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        if (target.nextEnergy().isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        target.setNextEnergy(types.get(context.battle().rng().nextInt(types.size())));
        return EffectOutcome.APPLIED;
    }
}
