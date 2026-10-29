package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;

/**
 * Puts one random matching card from a discard pile into its owner's hand —
 * Fishing Net's "put a random Basic Water Pokémon from your discard pile into
 * your hand".
 *
 * <p>The discard-pile sibling of {@link SearchDeck}: random, as the card says,
 * so it needs no {@code IPlayer}. Finding nothing is a
 * {@link EffectOutcome#NO_OP}.
 */
public record RecoverFromDiscard(ISideTarget side, ICondition<CardInstance> matching) implements IEffect {

    public RecoverFromDiscard {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(matching, "matching");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side owner = side.resolve(context);
        List<CardInstance> candidates = owner.discardPile().stream().filter(matching::evaluate).toList();
        if (candidates.isEmpty()) {
            return EffectOutcome.NO_OP;
        }

        CardInstance found = candidates.get(context.battle().rng().nextInt(candidates.size()));
        owner.removeFromDiscard(found);
        owner.addToHand(found);
        return EffectOutcome.APPLIED;
    }
}
