package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.Side;

/** The side whose turn it is. */
public record AttackerSide() implements ISideTarget {

    @Override
    public Side resolve(ResolutionContext context) {
        return context.battle().attacker();
    }
}
