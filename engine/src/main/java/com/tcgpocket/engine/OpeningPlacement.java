package com.tcgpocket.engine;

import com.tcgpocket.state.CardInstance;

import java.util.List;
import java.util.Objects;

/**
 * One side's opening board: its Active and its Benched Pokemon, all Basics from its hand.
 *
 * <p>Chosen whole, as one answer, rather than one Pokemon at a time: in Pocket both players lay
 * out their board together and confirm it, and one answer per side is what lets a caller ask both
 * at once (see {@link TurnEngine#openingDecision}).
 */
public record OpeningPlacement(CardInstance active, List<CardInstance> bench) {

    public OpeningPlacement {
        Objects.requireNonNull(active, "active");
        bench = List.copyOf(bench);
    }
}
