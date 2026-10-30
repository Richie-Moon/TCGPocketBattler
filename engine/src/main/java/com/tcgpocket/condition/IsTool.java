package com.tcgpocket.condition;

import com.tcgpocket.card.ToolCard;
import com.tcgpocket.state.CardInstance;

/** Whether a card is a Pokemon Tool — "a random Pokemon Tool card from your opponent's hand". */
public record IsTool() implements ICondition<CardInstance> {
    @Override
    public boolean evaluate(CardInstance subject) {
        return subject.definition() instanceof ToolCard;
    }
}
