package com.tcgpocket.state;

import com.tcgpocket.resolve.RandomSource;
import com.tcgpocket.resolve.SeededRandom;

import java.util.Objects;
import java.util.Optional;

/**
 * The two sides and whose turn it is.
 *
 * <p>Deliberately a state holder rather than a rules engine: turn orchestration,
 * trigger dispatch, knockout handling and legal-move generation belong to
 * {@code TurnEngine}. Keeping them out of here is what stops the engine package
 * from becoming part of the model's dependency cycle.
 */
public final class Battle {

    // TODO: Phase phase.

    private final RandomSource rng;
    private Side attacker;
    private Side defender;
    private CardInstance stadium;
    private int turn;

    public Battle(Side first, Side second, RandomSource rng) {
        this.attacker = Objects.requireNonNull(first, "first");
        this.defender = Objects.requireNonNull(second, "second");
        this.rng = Objects.requireNonNull(rng, "rng");
        this.turn = 1;
    }

    /**
     * Convenience for exploratory use. Seeded from the clock, so the game is
     * <em>not</em> reproducible — tests and self-play runs should pass an
     * explicit {@link SeededRandom} instead.
     */
    public Battle(Side first, Side second) {
        this(first, second, new SeededRandom(System.nanoTime()));
    }

    /** Every source of chance in the game runs through here. */
    public RandomSource rng() {
        return rng;
    }

    /** The Stadium currently in play, which either player's cards may read. */
    public Optional<CardInstance> stadium() {
        return Optional.ofNullable(stadium);
    }

    public void setStadium(CardInstance card) {
        this.stadium = card;
    }

    /** The side whose turn it is. */
    public Side attacker() {
        return attacker;
    }

    /** The side waiting; still able to act through triggers. */
    public Side defender() {
        return defender;
    }

    public int turn() {
        return turn;
    }

    public Side opponentOf(Side side) {
        if (side == attacker) {
            return defender;
        }
        if (side == defender) {
            return attacker;
        }
        throw new IllegalArgumentException("side is not part of this battle: " + side);
    }

    public void switchSides() {
        Side previousAttacker = attacker;
        attacker = defender;
        defender = previousAttacker;
        turn++;
    }
}
