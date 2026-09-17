package com.tcgpocket.resolve;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/** The ordinary {@link RandomSource}: a seeded PRNG, reproducible across runs. */
public final class SeededRandom implements RandomSource {

    private final Random random;
    private final long seed;

    public SeededRandom(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
    }

    /** The seed this was built with, so a failing game can be replayed. */
    public long seed() {
        return seed;
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    @Override
    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    @Override
    public void shuffle(List<?> list) {
        Collections.shuffle(list, random);
    }
}
