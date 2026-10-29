package com.tcgpocket.condition;

import com.tcgpocket.card.ItemCard;
import com.tcgpocket.card.PlayableItemCard;
import com.tcgpocket.state.CardInstance;

/**
 * Whether a card is an Item card — "a random Item card from your opponent's
 * hand". A Fossil counts, since it is printed as an Item.
 */
public record IsItem() implements ICondition<CardInstance> {
    @Override
    public boolean evaluate(CardInstance subject) {
        return subject.definition() instanceof ItemCard || subject.definition() instanceof PlayableItemCard;
    }
}
