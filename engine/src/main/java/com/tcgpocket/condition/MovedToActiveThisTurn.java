package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;

/**
 * Whether a Pokemon moved from the Bench to the Active Spot this turn —
 * Golisopod's First Impression.
 *
 * <p>Stamped by {@code Side.setActive}, so retreating, a switching effect and
 * promotion after a Knock Out all count, and cleared with the other turn flags.
 * Starting the game in the Active Spot is not moving there.
 */
public record MovedToActiveThisTurn() implements ICondition<PokemonInPlay> {

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.movedToActiveThisTurn();
    }
}
