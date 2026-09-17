package com.tcgpocket.card;

/**
 * Classifications printed on a card that the rules care about.
 *
 * <p>{@link #EX} is load-bearing: knocking out an EX awards two points rather
 * than one.
 */
public enum CardTag {
    EX,
    MEGA_EX,
    FOSSIL,
    ULTRA_BEAST
}
