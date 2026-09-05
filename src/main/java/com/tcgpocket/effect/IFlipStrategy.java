package com.tcgpocket.effect;

import com.tcgpocket.resolve.FlipResult;
import com.tcgpocket.resolve.ResolutionContext;

/**
 * Flips coins and records the result in the resolution scope, where
 * {@code NumberHeads}, {@code LastCoinTossHeads} and {@code AllFlipsHeads}
 * then read it.
 *
 * <p>Splitting the flip from its consumers is what lets one flip feed several
 * later nodes — "flip 2 coins. This attack does 30 damage for each heads. If
 * both are heads, the Defending Pokemon is now Paralyzed."
 */
public sealed interface IFlipStrategy extends IEffect permits FlipN, FlipUntilTails {

    FlipResult flip(ResolutionContext context);
}
