package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.Objects;

/**
 * Discards cards off the top of a deck — Rhyperior's "discard the top 3 cards
 * of your deck".
 *
 * <p>A deck shorter than {@code cardCount} discards what it has; an empty one
 * is a {@link EffectOutcome#NO_OP}. Not a cost, so it never fails.
 */
public record DiscardTopOfDeck(INumber cardCount, ISideTarget side) implements IEffect {

    public DiscardTopOfDeck {
        Objects.requireNonNull(cardCount, "cardCount");
        Objects.requireNonNull(side, "side");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        int count = Math.min(cardCount.evaluate(context), target.deck().size());
        if (count <= 0) {
            return EffectOutcome.NO_OP;
        }

        for (int i = 0; i < count; i++) {
            CardInstance top = target.deck().getFirst();
            target.removeFromDeck(top);
            target.addToDiscard(top);
        }
        return EffectOutcome.APPLIED;
    }
}
