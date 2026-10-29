package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * Stops the opponent taking Energy from their Energy Zone to attach to their
 * Active Pokemon, for a number of turns — Banette's Night Bind.
 *
 * <p>A turn stamp on the {@code Side}, as {@link PreventSupporter} is: it
 * belongs to the player, so it must hold whichever Pokemon becomes Active.
 * Read by {@code AttachEnergyAction.isLegal} for the once-a-turn attach and by
 * {@link AttachEnergy}, which an attack or ability uses to take Energy from
 * the Zone. Energy moved between Pokemon is not taken from the Zone, so
 * {@code MoveEnergy} ignores it.
 */
public record PreventActiveEnergy(INumber duration) implements IDurationEffect {

    public PreventActiveEnergy {
        Objects.requireNonNull(duration, "duration");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        context.opponent().lockActiveEnergyUntil(expiry);
        return EffectOutcome.APPLIED;
    }
}
