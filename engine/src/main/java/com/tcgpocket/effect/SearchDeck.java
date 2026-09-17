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
 * Puts cards from a deck into its owner's hand — "put 1 random Grass Pokemon
 * from your deck into your hand".
 *
 * <p>Random rather than chosen, which is how Pocket does it: no deck browsing,
 * so this needs no {@code IPlayer} and stays reproducible from a seed. A
 * hypothetical "choose" variant would be a different effect, not a flag here.
 *
 * <p>Finding nothing is a {@link EffectOutcome#NO_OP}, not a failure. An empty
 * deck or a deck with no Grass Pokemon in it is a legal nothing-happened, and
 * must not abort the rest of the card. Finding fewer than asked for is likewise
 * fine — it takes what is there.
 *
 * @param matching what counts; empty means any card at all
 */
public record SearchDeck(
        ISideTarget side,
        INumber count,
        Optional<ICondition<CardInstance>> matching) implements IEffect {

    public SearchDeck {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(count, "count");
        Objects.requireNonNull(matching, "matching");
    }

    /** One card, narrowed by a condition — by far the commonest printing. */
    public SearchDeck(ISideTarget side, INumber count, ICondition<CardInstance> matching) {
        this(side, count, Optional.of(matching));
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int wanted = count.evaluate(context);
        if (wanted <= 0) {
            return EffectOutcome.NO_OP;
        }

        Side searching = side.resolve(context);
        List<CardInstance> candidates = new ArrayList<>(searching.deck().stream()
                .filter(card -> matching.map(condition -> condition.evaluate(card)).orElse(true))
                .toList());

        if (candidates.isEmpty()) {
            return EffectOutcome.NO_OP;
        }

        int taken = Math.min(wanted, candidates.size());
        for (int i = 0; i < taken; i++) {
            // Removed from the candidate list as well as the deck, so asking for
            // two never picks the same card twice.
            CardInstance found = candidates.remove(context.battle().rng().nextInt(candidates.size()));
            searching.removeFromDeck(found);
            searching.addToHand(found);
        }
        return EffectOutcome.APPLIED;
    }
}
