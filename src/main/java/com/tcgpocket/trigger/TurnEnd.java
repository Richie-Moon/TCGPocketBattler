package com.tcgpocket.trigger;

import com.tcgpocket.state.Side;

import java.util.Objects;
import java.util.Optional;

/**
 * A side's turn has ended.
 *
 * <p>The busiest event in the game: poison and burn tick here, and Sleep and
 * Paralysis recover here. Every one of those is an ordinary trigger, so the
 * between-turns step has no special cases in it at all.
 */
public record TurnEnd(Side turnOf) implements GameEvent {

    public TurnEnd {
        Objects.requireNonNull(turnOf, "turnOf");
    }

    @Override
    public Optional<Side> side() {
        return Optional.of(turnOf);
    }
}
