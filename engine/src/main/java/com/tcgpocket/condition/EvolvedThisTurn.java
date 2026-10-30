package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;

/**
 * Whether this Pokemon evolved during this turn — Jolteon's Beginning Bolt.
 *
 * <p>Not {@code turnPlayed}, which is also this turn for a Basic just benched.
 */
public record EvolvedThisTurn() implements ICondition<PokemonInPlay> {

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.evolvedThisTurn();
    }
}
