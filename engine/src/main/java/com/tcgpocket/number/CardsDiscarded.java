package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;

/**
 * How many cards a {@code DiscardUpToFromHand} discarded earlier in this
 * resolution — Slowking's Litter.
 */
public record CardsDiscarded() implements INumber {

    @Override
    public int evaluate(ResolutionContext context) {
        return context.scope().cardsDiscarded();
    }
}
