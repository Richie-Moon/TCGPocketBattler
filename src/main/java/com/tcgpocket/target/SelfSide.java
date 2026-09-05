package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.Side;

/**
 * The side controlling the resolving card.
 *
 * <p>Differs from {@link AttackerSide} whenever a defender's trigger fires
 * during the attacker's turn.
 */
public record SelfSide() implements ISideTarget {

    @Override
    public Side resolve(ResolutionContext context) {
        return context.controller();
    }
}
