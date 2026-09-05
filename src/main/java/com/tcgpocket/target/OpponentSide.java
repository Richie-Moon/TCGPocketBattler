package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.Side;

/** The side that is not taking its turn. */
public record OpponentSide() implements ISideTarget {

    @Override
    public Side resolve(ResolutionContext context) {
        return context.battle().defender();
    }
}
