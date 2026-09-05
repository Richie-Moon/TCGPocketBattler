package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;

/**
 * An ordered group of effects that can fail as a unit.
 *
 * <h2>Why this is not just a {@code List<IEffect>}</h2>
 *
 * <p>An attempt applies its effects in order and <b>stops at the first one
 * that reports {@link EffectOutcome#FAILED}</b>. That short-circuit is exactly
 * the "... <b>If you do</b>, ..." clause that runs through real card text:
 *
 * <pre>{@code
 * "Discard an Energy from this Pokemon. If you do,
 *  this attack does 40 more damage."
 * }</pre>
 *
 * <p>becomes
 *
 * <pre>{@code
 * new Attempt(
 *     new DiscardTypeEnergy(Type.FIRE, new Literal(1), new Self()),
 *     new IncreaseDamage(new Literal(40), new Self(), new Literal(0)))
 * }</pre>
 *
 * <p>and the bonus cannot happen when the cost could not be paid. A bare list
 * would have no way to express that dependency without every effect knowing
 * about the one before it.
 *
 * <p>{@link EffectOutcome#NO_OP} deliberately does <b>not</b> abort. Healing a
 * Pokemon already at full HP is a legal nothing-happened, not a failed cost,
 * and must not swallow the rest of the card.
 */
public sealed interface IAttempt permits Attempt {

    AttemptResult execute(ResolutionContext context);
}
