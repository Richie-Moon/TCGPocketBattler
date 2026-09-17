package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.Objects;

/**
 * Draws cards.
 *
 * <p>Running out mid-draw is a {@link EffectOutcome#NO_OP} rather than a
 * failure — decking out is not a loss in Pocket, so a short draw must not
 * abort the rest of the card.
 */
public record DrawCard(INumber cardCount, ISideTarget side) implements IEffect {

    public DrawCard {
        Objects.requireNonNull(cardCount, "cardCount");
        Objects.requireNonNull(side, "side");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int count = cardCount.evaluate(context);
        if (count <= 0) {
            return EffectOutcome.NO_OP;
        }

        Side drawing = side.resolve(context);
        int drawn = 0;
        for (int i = 0; i < count && drawing.drawCard().isPresent(); i++) {
            drawn++;
        }
        return drawn == 0 ? EffectOutcome.NO_OP : EffectOutcome.APPLIED;
    }
}
