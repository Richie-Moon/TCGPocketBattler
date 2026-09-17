package com.tcgpocket.number;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * How many cards a side holds in a zone — "damage for each of your Benched
 * Pokemon", "draw until your hand has 5 cards".
 *
 * @param matching narrows the count when present; counts everything in the zone
 *                 when empty
 */
public record CountCards(
        ISideTarget side,
        Zone zone,
        Optional<ICondition<CardInstance>> matching) implements INumber {

    public CountCards {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(zone, "zone");
        Objects.requireNonNull(matching, "matching");
    }

    public CountCards(ISideTarget side, Zone zone) {
        this(side, zone, Optional.empty());
    }

    @Override
    public int evaluate(ResolutionContext context) {
        List<CardInstance> cards = side.resolve(context).cardsIn(zone);
        return matching
                .map(condition -> (int) cards.stream().filter(condition::evaluate).count())
                .orElseGet(cards::size);
    }
}
