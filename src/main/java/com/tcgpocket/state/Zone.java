package com.tcgpocket.state;

/** Where a card instance currently sits. */
public enum Zone {
    DECK,
    HAND,
    ACTIVE,
    BENCH,
    DISCARD,
    /** In play, but attached to a Pokemon rather than occupying a slot of its own. */
    ATTACHED
}
