package com.tcgpocket.status;

/**
 * Governs how statuses stack.
 *
 * <p>The rules let Poisoned and Burned sit alongside exactly one of Asleep,
 * Paralyzed or Confused — which is why a Pokemon holds a set of statuses rather
 * than a single optional one.
 */
public enum StatusCategory {

    /** At most one at a time; applying a second replaces the first. */
    SPECIAL,

    /** Any number may coexist; resolved between turns. */
    DAMAGE_OVER_TIME
}
