package com.tcgpocket.condition;

import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.state.CardInstance;

/** Whether a card is a Supporter — Penny's "a random Supporter card". */
public record IsSupporter() implements ICondition<CardInstance> {

    @Override
    public boolean evaluate(CardInstance subject) {
        return subject.definition() instanceof SupporterCard;
    }
}
