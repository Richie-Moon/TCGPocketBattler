package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * Integer division, truncating toward zero — "damage equal to half its
 * remaining HP" on an odd number rounds down.
 *
 * <p>A zero divisor throws rather than silently yielding zero. Every real card
 * divides by a printed constant, so a zero here means the card was built wrong,
 * and a loud failure beats an attack that quietly does no damage.
 */
public record Quotient(INumber left, INumber right) implements INumber {

    public Quotient {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
    }

    @Override
    public int evaluate(ResolutionContext context) {
        int divisor = right.evaluate(context);
        if (divisor == 0) {
            throw new ArithmeticException("Quotient divided by zero; divisor expression was " + right);
        }
        return left.evaluate(context) / divisor;
    }
}
