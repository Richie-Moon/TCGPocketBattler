package com.tcgpocket.resolve;

import java.util.List;

/**
 * Every source of chance in the game, behind one seam.
 *
 * <p>Coin flips, deck shuffles, random target selection and spread-damage
 * distribution all go through here so that a whole game can be replayed
 * exactly from a seed. A simulator that cannot reproduce a game cannot be
 * debugged, and self-play results that cannot be reproduced cannot be trusted.
 */
public interface RandomSource {

    /** Uniform in {@code [0, bound)}. */
    int nextInt(int bound);

    boolean nextBoolean();

    void shuffle(List<?> list);
}
