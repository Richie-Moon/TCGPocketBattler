package com.tcgpocket.resolve;

import java.util.Optional;

/**
 * The scratchpad for a single resolution, and the only mutable thing reachable
 * from an otherwise immutable {@link ResolutionContext}.
 *
 * <p>It exists because a coin flip performed by one effect has to be readable
 * by the effect that follows it in the same attempt — "flip 3 coins, this
 * attack does 30 damage for each heads". Scoping that here rather than putting
 * it on {@code Battle} keeps a flip inside one attack invisible to everything
 * else, and makes nested resolution (a trigger firing mid-attack) correct
 * without any push/pop bookkeeping.
 */
public final class ResolutionScope {

    private FlipResult lastFlip;

    public Optional<FlipResult> lastFlip() {
        return Optional.ofNullable(lastFlip);
    }

    public void recordFlip(FlipResult result) {
        this.lastFlip = result;
    }
}
