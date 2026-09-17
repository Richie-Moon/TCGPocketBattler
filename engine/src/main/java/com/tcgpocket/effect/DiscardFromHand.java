package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Discards cards from a hand, optionally only those matching a condition.
 *
 * <p>A cost, so it is all-or-nothing: too few matching cards discards nothing
 * and fails.
 *
 * @param matching counts every card in hand when empty
 */
public record DiscardFromHand(
        ISideTarget side,
        INumber cardCount,
        Optional<ICondition<CardInstance>> matching) implements IEffect {

    // TODO: which cards go is the player's choice; this takes them in hand
    //       order. Becomes an IPlayer.choose once the agent seam exists.

    public DiscardFromHand {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(cardCount, "cardCount");
        Objects.requireNonNull(matching, "matching");
    }

    public DiscardFromHand(ISideTarget side, INumber cardCount) {
        this(side, cardCount, Optional.empty());
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int count = cardCount.evaluate(context);
        if (count <= 0) {
            return EffectOutcome.NO_OP;
        }

        Side target = side.resolve(context);
        List<CardInstance> eligible = target.hand().stream()
                .filter(card -> matching.map(condition -> condition.evaluate(card)).orElse(true))
                .toList();

        if (eligible.size() < count) {
            return EffectOutcome.FAILED;
        }

        for (CardInstance card : eligible.subList(0, count)) {
            target.removeFromHand(card);
            target.addToDiscard(card);
        }
        return EffectOutcome.APPLIED;
    }
}
