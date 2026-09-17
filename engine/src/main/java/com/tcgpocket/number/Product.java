package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * Multiplication.
 *
 * <p>Carries most of the "X damage for each Y" card text:
 * {@code new Product(new NumberHeads(), new Literal(30))}.
 */
public record Product(INumber left, INumber right) implements INumber {

    public Product {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        return left.evaluate(context) * right.evaluate(context);
    }
}
