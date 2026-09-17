package com.tcgpocket.player;

import com.tcgpocket.resolve.RandomSource;

import java.util.Objects;

/**
 * Picks uniformly at random.
 *
 * <p>Useful out of proportion to its intelligence: it drives self-play, it
 * fuzzes the rules engine looking for states that throw, and it is the
 * baseline any real AI has to beat. Seeded, so a game that breaks can be
 * replayed exactly.
 */
public final class RandomPlayer implements IPlayer {

    private final String name;
    private final RandomSource rng;

    public RandomPlayer(String name, RandomSource rng) {
        this.name = Objects.requireNonNull(name, "name");
        this.rng = Objects.requireNonNull(rng, "rng");
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public <T> T choose(Decision<T> decision) {
        return decision.options().get(rng.nextInt(decision.options().size()));
    }
}
