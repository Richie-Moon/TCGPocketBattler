package com.tcgpocket.condition;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.energy.Type;
import com.tcgpocket.state.CardInstance;

import java.util.Objects;

/**
 * Whether a card is a Pokemon of a given type — "1 random Grass Pokemon from
 * your deck".
 *
 * <p>False for a Trainer card, which has no type at all.
 *
 * <p>The card-level twin of {@link HasType}. The split is not ideal: they ask
 * the same question, but {@code HasType} is subjected on {@code PokemonInPlay}
 * so it composes with {@code IsBurned} and the rest of the in-play conditions,
 * while this one has to work on a card sitting in a deck, where there is no
 * such thing as being burned. They would collapse into one if {@link And} and
 * friends took {@code ICondition<? super T>}.
 */
public record IsType(Type type) implements ICondition<CardInstance> {

    public IsType {
        Objects.requireNonNull(type, "type");
    }

    @Override
    public boolean evaluate(CardInstance subject) {
        return subject.definition() instanceof PokemonCard pokemon && pokemon.type() == type;
    }
}
