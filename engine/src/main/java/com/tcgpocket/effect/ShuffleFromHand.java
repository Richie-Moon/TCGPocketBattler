package com.tcgpocket.effect;

import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.Objects;

/**
 * Shuffles one card from a hand into that hand's deck, picked by the
 * controller — "choose a card you find there and shuffle it into your
 * opponent's deck".
 *
 * <p>The controller picks even from the opponent's hand, so card text pairs
 * this with {@link RevealHand}. An empty hand is {@link EffectOutcome#NO_OP}.
 */
public record ShuffleFromHand(ISideTarget side) implements IEffect {

    public ShuffleFromHand {
        Objects.requireNonNull(side, "side");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        if (target.hand().isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        Side chooser = context.controller();
        CardInstance chosen = chooser.player().choose(
                new Decision<>("Choose a card to shuffle into the deck.", target.hand(), chooser, context));
        target.removeFromHand(chosen);
        target.addToDeck(chosen);
        target.shuffleDeck(context.battle().rng());
        return EffectOutcome.APPLIED;
    }
}
