package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Discards cards chosen at random from a hand — "discard a random card from
 * your opponent's hand".
 *
 * <p>The sibling of {@link DiscardFromHand}, as {@link DiscardRandomEnergy} is
 * of {@link DiscardTypeEnergy}: nobody chooses, so it is never a cost. A short
 * hand discards what there is, and an empty one is a {@link EffectOutcome#NO_OP}.
 *
 * @param matching draws from every card in hand when empty
 */
public record DiscardRandomFromHand(
        ISideTarget side,
        INumber cardCount,
        Optional<ICondition<CardInstance>> matching) implements IEffect {

    public DiscardRandomFromHand {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(cardCount, "cardCount");
        Objects.requireNonNull(matching, "matching");
    }

    public DiscardRandomFromHand(ISideTarget side, INumber cardCount) {
        this(side, cardCount, Optional.empty());
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        List<CardInstance> hand = new ArrayList<>(target.hand().stream()
                .filter(card -> matching.map(condition -> condition.evaluate(card)).orElse(true))
                .toList());
        int count = Math.min(cardCount.evaluate(context), hand.size());
        if (count <= 0) {
            return EffectOutcome.NO_OP;
        }

        for (int i = 0; i < count; i++) {
            CardInstance card = hand.remove(context.battle().rng().nextInt(hand.size()));
            target.removeFromHand(card);
            target.addToDiscard(card);
        }
        return EffectOutcome.APPLIED;
    }
}
