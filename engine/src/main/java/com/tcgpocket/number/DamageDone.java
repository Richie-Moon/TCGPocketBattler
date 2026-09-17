package com.tcgpocket.number;

import com.tcgpocket.resolve.ResolutionContext;

/**
 * How much HP the damage dealt so far in this resolution actually removed; 0
 * before anything has been hit.
 *
 * <p>After weakness and modifiers, and capped at what the target had left, so
 * "heal the same amount of damage you did" heals 20 off a 20 HP Pokemon.
 * Incidental damage (poison, recoil) is not counted.
 */
public record DamageDone() implements INumber {

    @Override
    public int evaluate(ResolutionContext context) {
        return context.scope().damageDealtThisResolution();
    }
}
