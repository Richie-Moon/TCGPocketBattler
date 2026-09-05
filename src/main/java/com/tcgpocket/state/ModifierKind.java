package com.tcgpocket.state;

/** What an {@link ActiveModifier} does to the damage pipeline. */
public enum ModifierKind {

    /** Adds to damage this Pokemon deals. */
    INCREASE_DAMAGE_DEALT,

    /** Subtracts from damage this Pokemon takes. */
    REDUCE_DAMAGE_TAKEN,

    /** Zeroes damage this Pokemon takes, whatever the arithmetic said. */
    PREVENT_DAMAGE
}
