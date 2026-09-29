package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.Objects;

/**
 * Shows the top cards of a deck to that deck's owner — Porygon's Data Scan.
 *
 * <p>Like {@link RevealHand} this only records what was seen
 * ({@link Side#seenTopCards()}); showing it is the server's job. A deck
 * shorter than {@code cardCount} shows what it has.
 */
public record LookAtTopCards(INumber cardCount, ISideTarget side) implements IEffect {

    public LookAtTopCards {
        Objects.requireNonNull(cardCount, "cardCount");
        Objects.requireNonNull(side, "side");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int count = cardCount.evaluate(context);
        Side target = side.resolve(context);
        if (count <= 0 || target.deck().isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        target.lookAtTopCards(count);
        return EffectOutcome.APPLIED;
    }
}
