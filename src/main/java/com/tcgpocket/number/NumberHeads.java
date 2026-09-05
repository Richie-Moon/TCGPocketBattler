package com.tcgpocket.number;

import com.tcgpocket.resolve.FlipResult;
import com.tcgpocket.resolve.ResolutionContext;

/**
 * How many heads the most recent coin flip produced in this resolution; 0 if
 * nothing has been flipped.
 *
 * <p>Reads the {@code ResolutionScope}, which is why a flip performed by one
 * effect is visible to the next effect in the same attempt but invisible to the
 * rest of the game.
 */
public record NumberHeads() implements INumber {

    @Override
    public int evaluate(ResolutionContext context) {
        return context.scope().lastFlip().map(FlipResult::heads).orElse(0);
    }
}
