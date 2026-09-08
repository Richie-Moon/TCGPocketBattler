package com.tcgpocket.state;

/** What an {@link ActiveModifier} does while it lasts. */
public enum ModifierKind {

    /** Adds to damage this Pokemon deals. */
    INCREASE_DAMAGE_DEALT,

    /** Subtracts from damage this Pokemon takes. */
    REDUCE_DAMAGE_TAKEN,

    /** Zeroes damage this Pokemon takes, whatever the arithmetic said. */
    PREVENT_DAMAGE,

    /**
     * Forbids this Pokemon declaring an attack. Read by {@code Action.isLegal},
     * not by the damage pipeline — retreating, attaching, playing Trainers and
     * using abilities all stay legal.
     */
    CANNOT_ATTACK
}
