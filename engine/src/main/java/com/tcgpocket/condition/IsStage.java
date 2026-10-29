package com.tcgpocket.condition;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.state.CardInstance;

/**
 * Whether a card is a Pokemon of the given evolution stage — Lillie's "1 of
 * your Stage 2 Pokémon".
 *
 * <p>Subjected on {@link CardInstance}, like {@link IsBasic}, so it answers for
 * a card in hand as well as a Pokemon in play. False for a Trainer.
 */
public record IsStage(int stage) implements ICondition<CardInstance> {

    @Override
    public boolean evaluate(CardInstance subject) {
        return subject.definition() instanceof PokemonCard pokemon && pokemon.stage() == stage;
    }
}
