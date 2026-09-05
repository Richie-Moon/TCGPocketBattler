package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.target.ISideTarget;

import java.util.Objects;

/** Points a side has scored; three wins the game. */
public record Points(ISideTarget side) implements INumber {

    public Points {
        Objects.requireNonNull(side, "side");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return side.resolve(context).points();
    }
}
