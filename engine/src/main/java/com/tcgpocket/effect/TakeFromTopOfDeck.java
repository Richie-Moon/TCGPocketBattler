package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;

/**
 * Puts every matching card among the top of a deck into its owner's hand and
 * shuffles the rest back — Traveling Merchant.
 *
 * <p>Every card looked at ends up in the hand or shuffled, so nothing is recorded
 * as seen. An empty deck is a {@link EffectOutcome#NO_OP}.
 */
public record TakeFromTopOfDeck(INumber cardCount, ISideTarget side, ICondition<CardInstance> matching)
        implements IEffect {

    public TakeFromTopOfDeck {
        Objects.requireNonNull(cardCount, "cardCount");
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(matching, "matching");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        if (target.deck().isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        List<CardInstance> top = List.copyOf(
                target.deck().subList(0, Math.min(cardCount.evaluate(context), target.deck().size())));
        for (CardInstance card : top) {
            if (matching.evaluate(card)) {
                target.removeFromDeck(card);
                target.addToHand(card);
            }
        }
        target.shuffleDeck(context.battle().rng());
        return EffectOutcome.APPLIED;
    }
}
