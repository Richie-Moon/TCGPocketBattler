package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.Objects;

/**
 * Takes the top card of a deck into its owner's hand if it matches, and
 * otherwise puts it on the bottom — Mythical Slab's "if that card is a Psychic
 * Pokemon, put it into your hand. If it is not, put it on the bottom of your
 * deck".
 *
 * <p>Both outcomes are {@link EffectOutcome#APPLIED}: the card moved either
 * way. An empty deck is a {@link EffectOutcome#NO_OP}.
 */
public record TopCardToHand(ISideTarget side, ICondition<CardInstance> matching) implements IEffect {

    public TopCardToHand {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(matching, "matching");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        if (target.deck().isEmpty()) {
            return EffectOutcome.NO_OP;
        }

        CardInstance top = target.deck().getFirst();
        target.removeFromDeck(top);
        if (matching.evaluate(top)) {
            target.addToHand(top);
        } else {
            // addToDeck appends, and the top of the deck is index 0, so this is the bottom.
            target.addToDeck(top);
        }
        return EffectOutcome.APPLIED;
    }
}
