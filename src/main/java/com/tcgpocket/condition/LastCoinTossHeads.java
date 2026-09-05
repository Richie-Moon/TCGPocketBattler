package com.tcgpocket.condition;

import com.tcgpocket.resolve.FlipResult;
import com.tcgpocket.resolve.ResolutionContext;

/**
 * Whether the most recent flip in this resolution landed heads — "flip a coin.
 * If heads, ...".
 *
 * <p>False when nothing has been flipped.
 */
public record LastCoinTossHeads() implements ICondition<ResolutionContext> {

    @Override
    public boolean evaluate(ResolutionContext context) {
        return context.scope().lastFlip().map(FlipResult::lastWasHeads).orElse(false);
    }
}
