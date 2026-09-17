package com.tcgpocket.trigger;

import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;

import java.util.Objects;
import java.util.Optional;

/** A card has been played from hand. */
public record CardPlayed(Side playedBy, CardInstance card) implements GameEvent {

    public CardPlayed {
        Objects.requireNonNull(playedBy, "playedBy");
        Objects.requireNonNull(card, "card");
    }

    @Override
    public Optional<Side> side() {
        return Optional.of(playedBy);
    }
}
