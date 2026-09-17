package com.tcgpocket.effect;

/**
 * What happened when an effect was applied.
 *
 * <p>The distinction between {@link #NO_OP} and {@link #FAILED} is the whole
 * point of this enum, and it is what makes {@code IAttempt} able to model
 * "... <b>If you do</b>, ..." card text: an attempt short-circuits on FAILED
 * but carries on past NO_OP.
 */
public enum EffectOutcome {

    /** Did what it said. */
    APPLIED,

    /**
     * There was nothing to do, and that is fine — healing a Pokemon already at
     * full HP, removing a status it does not have. Does <b>not</b> abort the
     * rest of an attempt.
     */
    NO_OP,

    /**
     * Could not be done: a target that did not resolve, a cost that could not
     * be paid. <b>Aborts</b> the rest of the attempt, which is how a failed
     * cost cancels the payoff that depended on it.
     */
    FAILED;

    public boolean isFailure() {
        return this == FAILED;
    }
}
