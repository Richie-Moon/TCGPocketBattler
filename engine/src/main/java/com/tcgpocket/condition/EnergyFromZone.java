package com.tcgpocket.condition;

import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.trigger.EnergyAttached;

import java.util.Objects;
import java.util.Optional;

/**
 * Whether the event being handled is an Energy of a given type attached from
 * the Energy Zone — Darkrai ex's "whenever you attach a Darkness Energy from
 * your Energy Zone". Pair it with {@link EventConcerns} for "to this Pokemon".
 *
 * <p>False outside a trigger, for any other event, and for Energy moved off
 * another Pokemon.
 *
 * @param type empty matches Energy of any type — Komala's "whenever you
 *             attach an Energy from your Energy Zone"
 */
public record EnergyFromZone(Optional<Type> type) implements ICondition<ResolutionContext> {

    public EnergyFromZone {
        Objects.requireNonNull(type, "type");
    }

    public EnergyFromZone(Type type) {
        this(Optional.of(type));
    }

    /** Energy of any type. */
    public EnergyFromZone() {
        this(Optional.empty());
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        return context.event()
                .filter(event -> event instanceof EnergyAttached attached
                        && attached.fromEnergyZone() && type.map(attached.energyType()::equals).orElse(true))
                .isPresent();
    }
}
