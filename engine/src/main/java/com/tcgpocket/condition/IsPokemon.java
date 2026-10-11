package com.tcgpocket.condition;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.state.CardInstance;

/**
 * Whether a card is a Pokemon card of any stage — "choose a Pokemon in your
 * hand". False for a Trainer, including a Fossil.
 */
public record IsPokemon() implements ICondition<CardInstance> {
    @Override
    public boolean evaluate(CardInstance subject) {
        return subject.definition() instanceof PokemonCard;
    }
}
