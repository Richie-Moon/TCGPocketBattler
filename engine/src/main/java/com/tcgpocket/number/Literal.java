package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;

/** A constant, the leaf of most expressions: the 30 in "this attack does 30 damage". */
public record Literal(int value) implements INumber {

    @Override
    public int evaluate(ResolutionContext context) {
        return value;
    }
}
