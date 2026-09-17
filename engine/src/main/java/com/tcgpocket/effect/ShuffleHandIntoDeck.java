package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;

/** Puts a hand back into its deck and shuffles. */
public record ShuffleHandIntoDeck(ISideTarget side) implements IEffect {

    public ShuffleHandIntoDeck {
        Objects.requireNonNull(side, "side");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        List<CardInstance> hand = List.copyOf(target.hand());
        if (hand.isEmpty()) {
            return EffectOutcome.NO_OP;
        }

        for (CardInstance card : hand) {
            target.removeFromHand(card);
            target.addToDeck(card);
        }
        target.shuffleDeck(context.battle().rng());
        return EffectOutcome.APPLIED;
    }
}
