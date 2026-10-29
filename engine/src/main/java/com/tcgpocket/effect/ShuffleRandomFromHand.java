package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;

/**
 * Shuffles one card chosen at random from a hand into that hand's deck —
 * "your opponent reveals a random card from their hand and shuffles it into
 * their deck".
 *
 * <p>The sibling of {@link ShuffleFromHand}, as {@link DiscardRandomFromHand}
 * is of {@link DiscardFromHand}: nobody chooses, so no reveal is needed. An
 * empty hand is a {@link EffectOutcome#NO_OP}.
 */
public record ShuffleRandomFromHand(ISideTarget side) implements IEffect {

    public ShuffleRandomFromHand {
        Objects.requireNonNull(side, "side");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        List<CardInstance> hand = target.hand();
        if (hand.isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        CardInstance card = hand.get(context.battle().rng().nextInt(hand.size()));
        target.removeFromHand(card);
        target.addToDeck(card);
        target.shuffleDeck(context.battle().rng());
        return EffectOutcome.APPLIED;
    }
}
