package com.tcgpocket.condition;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;

import java.util.Objects;

/**
 * Whether the card this resolution last picked out meets a condition — Dugtrio's
 * "if that card is a Fighting Pokémon". False when no card was picked.
 */
public record PickedCard(ICondition<CardInstance> condition) implements ICondition<ResolutionContext> {

    public PickedCard {
        Objects.requireNonNull(condition, "condition");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        return context.scope().pickedCard().filter(condition::evaluate).isPresent();
    }
}
