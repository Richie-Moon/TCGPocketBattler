package com.tcgpocket.condition;

import com.tcgpocket.resolve.FlipResult;
import com.tcgpocket.resolve.ResolutionContext;

/**
 * Whether every flip in the most recent flip of this resolution landed heads —
 * "flip 2 coins. If both are heads, ...".
 *
 * <p>False when nothing has been flipped: a card that asks for all-heads has
 * not met its condition if no flip happened, even though "all of nothing" is
 * vacuously true.
 */
public record AllFlipsHeads() implements ICondition<ResolutionContext> {

    @Override
    public boolean evaluate(ResolutionContext context) {
        return context.scope().lastFlip()
                .map(flip -> flip.flips() > 0 && flip.allHeads())
                .orElse(false);
    }
}
