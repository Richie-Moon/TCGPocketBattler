package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.number.INumber;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Lets a hand's owner discard up to some number of matching cards — Slowking's
 * "discard up to 2 Pokémon Tool cards from your hand".
 *
 * <p>Not a cost, unlike {@link DiscardFromHand}: the empty choice declines, and
 * having nothing to discard asks nothing and is a {@link EffectOutcome#NO_OP}.
 * The count lands on the scope for {@code CardsDiscarded} to read.
 */
public record DiscardUpToFromHand(ISideTarget side, INumber maxCards, ICondition<CardInstance> matching)
        implements IEffect {

    public DiscardUpToFromHand {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(maxCards, "maxCards");
        Objects.requireNonNull(matching, "matching");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        List<CardInstance> eligible = target.hand().stream().filter(matching::evaluate).toList();
        int most = Math.min(maxCards.evaluate(context), eligible.size());
        if (most <= 0) {
            return EffectOutcome.NO_OP;
        }
        List<List<CardInstance>> choices = new ArrayList<>();
        for (int count = 0; count <= most; count++) {
            choices.addAll(DiscardFromHand.subsets(eligible, count));
        }
        List<CardInstance> chosen = target.player().choose(
                new Decision<>("Choose up to " + most + " cards to discard.", choices, target, context));
        for (CardInstance card : chosen) {
            target.removeFromHand(card);
            target.addToDiscard(card);
        }
        context.scope().recordCardsDiscarded(chosen.size());
        return chosen.isEmpty() ? EffectOutcome.NO_OP : EffectOutcome.APPLIED;
    }
}
