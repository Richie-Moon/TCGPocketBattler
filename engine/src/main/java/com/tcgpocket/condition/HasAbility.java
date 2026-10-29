package com.tcgpocket.condition;

import com.tcgpocket.card.CardTag;
import com.tcgpocket.state.PokemonInPlay;

/**
 * Whether a Pokemon has an Ability — "if your opponent's Active Pokémon has an
 * Ability".
 *
 * <p>A Fossil in play offers its discard as an ability, but the printed card
 * has none, so it does not count.
 */
public record HasAbility() implements ICondition<PokemonInPlay> {

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.definition().ability().isPresent() && !subject.definition().tags().contains(CardTag.FOSSIL);
    }
}
