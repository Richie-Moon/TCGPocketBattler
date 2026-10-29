package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;

/**
 * Puts a hand back into its deck and shuffles.
 *
 * @param redraw then draws as many cards as went back — Iono's "then draws
 *               that many cards", which no later effect could count
 */
public record ShuffleHandIntoDeck(ISideTarget side, boolean redraw) implements IEffect {

    public ShuffleHandIntoDeck {
        Objects.requireNonNull(side, "side");
    }

    public ShuffleHandIntoDeck(ISideTarget side) {
        this(side, false);
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
        if (redraw) {
            hand.forEach(card -> target.drawCard());
        }
        return EffectOutcome.APPLIED;
    }
}
