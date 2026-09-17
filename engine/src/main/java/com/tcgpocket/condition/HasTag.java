package com.tcgpocket.condition;

import com.tcgpocket.card.CardTag;
import com.tcgpocket.state.CardInstance;

import java.util.Objects;

/** Whether a card carries a tag, such as EX. */
public record HasTag(CardTag tag) implements ICondition<CardInstance> {

    public HasTag {
        Objects.requireNonNull(tag, "tag");
    }

    @Override
    public boolean evaluate(CardInstance subject) {
        return subject.definition().hasTag(tag);
    }
}
