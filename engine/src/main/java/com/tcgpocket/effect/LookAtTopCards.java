package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.Objects;

/**
 * Shows the top cards of a deck to {@code viewer}: Porygon's Data Scan, or
 * Unown's CHECK, which can look at either player's deck.
 *
 * <p>Like {@link RevealHand} this only records what was seen
 * ({@link Side#seenTopCards()}, {@link Side#topCardsSeenByOpponent()});
 * showing it is the server's job. A deck shorter than {@code cardCount}
 * shows what it has.
 */
public record LookAtTopCards(INumber cardCount, ISideTarget side, ISideTarget viewer) implements IEffect {

    public LookAtTopCards {
        Objects.requireNonNull(cardCount, "cardCount");
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(viewer, "viewer");
    }

    /** The deck's owner looks at their own deck. */
    public LookAtTopCards(INumber cardCount, ISideTarget side) {
        this(cardCount, side, side);
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int count = cardCount.evaluate(context);
        Side target = side.resolve(context);
        if (count <= 0 || target.deck().isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        if (viewer.resolve(context) == target) {
            target.lookAtTopCards(count);
        } else {
            target.showTopCardsToOpponent(count);
        }
        return EffectOutcome.APPLIED;
    }
}
