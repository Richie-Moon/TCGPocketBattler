package com.tcgpocket.condition;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.state.CardInstance;

/**
 * Whether a card is a Basic Pokemon — "1 of your opponent's Benched Basic
 * Pokemon", "put 1 random Basic Pokemon from your deck into your hand".
 *
 * <p>False for an evolution and for a Trainer.
 *
 * <p>Subjected on {@link CardInstance} rather than on {@code PokemonInPlay},
 * because half the cards that ask this are asking about a card in a deck or a
 * hand. By subtyping it still answers for a Pokemon in play.
 */
public record IsBasic() implements ICondition<CardInstance> {

    @Override
    public boolean evaluate(CardInstance subject) {
        return subject.definition() instanceof PokemonCard pokemon && pokemon.isBasic();
    }
}
