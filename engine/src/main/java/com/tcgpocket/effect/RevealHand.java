package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.Objects;

/**
 * Shows a hand to that side's opponent.
 *
 * <p>The engine has no per-player visibility, so this only records what was
 * shown ({@link Side#revealedHand()}); hiding the rest is the server's job. The
 * reveal lasts until the revealed side's next turn ends.
 */
public record RevealHand(ISideTarget side) implements IEffect {

    public RevealHand {
        Objects.requireNonNull(side, "side");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        if (target.hand().isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        target.revealHand();
        return EffectOutcome.APPLIED;
    }
}
