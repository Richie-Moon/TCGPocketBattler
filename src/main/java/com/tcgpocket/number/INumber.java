package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;

/**
 * An arithmetic expression appearing in card text, evaluated against the board.
 *
 * <p>Damage amounts, heal amounts, card counts and durations are all values of
 * this type rather than plain {@code int}s, because card text is full of
 * numbers that are only knowable at resolution time: "30 damage for each heads"
 * is {@code new Product(new NumberHeads(), new Literal(30))}.
 *
 * <h2>Contract</h2>
 * <ul>
 *   <li><b>Pure.</b> Evaluating never mutates the board. Only {@code IEffect}
 *       may write, so the same expression can safely be evaluated twice — which
 *       legal-move generation relies on when previewing an attack.
 *   <li><b>Missing targets contribute zero.</b> A node reading an unresolvable
 *       target ({@code MaxHP} of an empty bench slot) evaluates to 0 rather
 *       than throwing. Failing to resolve is an effect-level concern.
 *   <li><b>No clamping.</b> {@link Difference} may go negative. Flooring damage
 *       at zero belongs to the damage pipeline, which knows whether a negative
 *       intermediate is meaningful.
 * </ul>
 */
public sealed interface INumber permits
        Literal, Sum, Difference, Product, Quotient, Branch,
        MaxHP, CurrentHP, DamageOn, Stage, EnergyOn, CountCards,
        NumberHeads, EventDamage, Points {

    int evaluate(ResolutionContext context);
}
