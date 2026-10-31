package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Shuffles one card from a hand into that hand's deck, picked by the
 * controller — "choose a card you find there and shuffle it into your
 * opponent's deck".
 *
 * <p>The controller picks even from the opponent's hand, so card text pairs
 * this with {@link RevealHand}. A hand with nothing to pick is {@link EffectOutcome#NO_OP}.
 *
 * @param matching narrows what can be picked — Silver's "a Supporter card"; empty is any card
 */
public record ShuffleFromHand(ISideTarget side, Optional<ICondition<CardInstance>> matching) implements IEffect {

    public ShuffleFromHand {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(matching, "matching");
    }

    public ShuffleFromHand(ISideTarget side) {
        this(side, Optional.empty());
    }

    public ShuffleFromHand(ISideTarget side, ICondition<CardInstance> matching) {
        this(side, Optional.of(matching));
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        List<CardInstance> offered = target.hand().stream()
                .filter(card -> matching.map(condition -> condition.evaluate(card)).orElse(true))
                .toList();
        if (offered.isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        Side chooser = context.controller();
        CardInstance chosen = chooser.player().choose(
                new Decision<>("Choose a card to shuffle into the deck.", offered, chooser, context));
        target.removeFromHand(chosen);
        target.addToDeck(chosen);
        target.shuffleDeck(context.battle().rng());
        return EffectOutcome.APPLIED;
    }
}
