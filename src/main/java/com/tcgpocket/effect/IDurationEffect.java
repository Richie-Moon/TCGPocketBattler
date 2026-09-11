package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;

/**
 * An effect whose result lingers for a number of turns.
 *
 * <p>Applying one usually installs an {@code ActiveModifier} on the target,
 * which the damage pipeline reads and the engine expires between turns; a
 * player-wide restriction such as {@link PreventSupporter} stamps the same
 * expiry turn onto the {@code Side} instead. A duration of 1
 * means "until the end of your opponent's next turn" in the usual card
 * phrasing — it lasts through the turn it is applied on and the one after.
 */
public sealed interface IDurationEffect extends IEffect
        permits ReduceDamage, IncreaseDamage, PreventDamage, PreventAttack, PreventSupporter {

    INumber duration();
}
