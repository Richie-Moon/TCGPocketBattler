package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.trigger.DamageDealt;
import com.tcgpocket.trigger.DamageIncoming;

/**
 * How much damage the event currently being handled dealt (or is about to
 * deal, for {@code DamageIncoming}); 0 outside a trigger,
 * or when the event was something other than damage.
 *
 * <p>This is what lets a card retaliate for what it just took — the reason
 * {@code ResolutionContext} carries the event rather than only the board.
 */
public record EventDamage() implements INumber {

    @Override
    public int evaluate(ResolutionContext context) {
        return switch (context.event().orElse(null)) {
            case DamageDealt damage -> damage.amount();
            case DamageIncoming incoming -> incoming.amount();
            case null, default -> 0;
        };
    }
}
