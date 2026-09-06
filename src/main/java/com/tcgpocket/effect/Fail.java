package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;

/**
 * Does nothing, and reports failure.
 *
 * <p>The counterpart to {@link NoEffect}, and the way to abort an attempt
 * deliberately rather than as a side effect of a cost that could not be paid.
 * Paired with {@link ConditionalEffect} it is how a trigger vetoes an event:
 *
 * <pre>{@code
 * // Confusion: "flip a coin, and on tails the attack does nothing"
 * new Attempt(List.of(
 *     new FlipN(new Literal(1)),
 *     new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new Fail())))
 * }</pre>
 */
public record Fail() implements IEffect {

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        return EffectOutcome.FAILED;
    }
}
