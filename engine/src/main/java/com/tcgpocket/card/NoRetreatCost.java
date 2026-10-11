package com.tcgpocket.card;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;

/**
 * The holder has no Retreat Cost while it satisfies {@code whileHolder} —
 * Giratina's Levitate, with "has any Energy attached".
 *
 * <p>Read by {@code RetreatAction}. A rule rather than a turn-start stamp
 * because the condition changes mid-turn: attaching an Energy and then
 * retreating is the whole point of the card.
 */
public record NoRetreatCost(ICondition<PokemonInPlay> whileHolder) implements IRule {

    public NoRetreatCost {
        Objects.requireNonNull(whileHolder, "whileHolder");
    }
}
