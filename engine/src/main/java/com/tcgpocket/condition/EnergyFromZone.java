package com.tcgpocket.condition;

import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.trigger.EnergyAttached;

import java.util.Objects;

/**
 * Whether the event being handled is an Energy of a given type attached from
 * the Energy Zone — Darkrai ex's "whenever you attach a Darkness Energy from
 * your Energy Zone". Pair it with {@link EventConcerns} for "to this Pokemon".
 *
 * <p>False outside a trigger, for any other event, and for Energy moved off
 * another Pokemon.
 */
public record EnergyFromZone(Type type) implements ICondition<ResolutionContext> {

    public EnergyFromZone {
        Objects.requireNonNull(type, "type");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        return context.event()
                .filter(event -> event instanceof EnergyAttached attached
                        && attached.fromEnergyZone() && attached.energyType() == type)
                .isPresent();
    }
}
