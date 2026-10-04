package com.tcgpocket.engine;

import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * One side's opening board: its Active and its Benched Pokemon, all Basics from its hand.
 *
 * <p>Chosen whole, as one answer, rather than one Pokemon at a time: in Pocket both players lay
 * out their board together and confirm it, and one answer per side is what lets a caller ask both
 * at once (see {@link TurnEngine#openingDecision}).
 *
 * @param bench by Bench slot, {@link Side#BENCH_LIMIT} of them; empty for an empty slot
 */
public record OpeningPlacement(CardInstance active, List<Optional<CardInstance>> bench) {

    public OpeningPlacement {
        Objects.requireNonNull(active, "active");
        bench = List.copyOf(bench);
        if (bench.size() != Side.BENCH_LIMIT) {
            throw new IllegalArgumentException("a bench has " + Side.BENCH_LIMIT + " slots, not " + bench.size());
        }
    }

    /** The Benched cards, in slot order. */
    public List<CardInstance> benched() {
        return bench.stream().flatMap(Optional::stream).toList();
    }
}
