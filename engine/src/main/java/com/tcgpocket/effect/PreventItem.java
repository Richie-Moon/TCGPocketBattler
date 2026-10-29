package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * Locks the opponent out of Item cards for a number of turns — "during your
 * opponent's next turn, they can't play any Item cards from their hand".
 *
 * <p>{@link PreventSupporter}'s twin, and a turn stamp on the {@code Side} for
 * the same reason: the restriction belongs to a player, not to a Pokemon.
 * {@code PlayCardAction.isLegal} reads it, for Fossils too, which are Items.
 * Tools are not Items, so they stay playable.
 */
public record PreventItem(INumber duration) implements IDurationEffect {

    public PreventItem {
        Objects.requireNonNull(duration, "duration");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        context.opponent().lockItemsUntil(expiry);
        return EffectOutcome.APPLIED;
    }
}
