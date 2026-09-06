package com.tcgpocket.trigger;

import com.tcgpocket.state.Side;

import java.util.Objects;
import java.util.Optional;

/** A side's turn has begun. */
public record TurnStart(Side turnOf) implements GameEvent {

    public TurnStart {
        Objects.requireNonNull(turnOf, "turnOf");
    }

    @Override
    public Optional<Side> side() {
        return Optional.of(turnOf);
    }
}
