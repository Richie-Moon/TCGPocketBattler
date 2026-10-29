package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;

/**
 * Whether the Pokemon's owner is still on their first turn — Wimpod's Wimp Out.
 *
 * <p>Asks the owner rather than the turn number, because the first turn is
 * turn 1 for one player and turn 2 for the other. It is also true during the
 * opponent's first turn if they went first, which no card can tell apart:
 * "during your first turn" text only matters on your own turn.
 */
public record OwnersFirstTurn() implements ICondition<PokemonInPlay> {

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return !subject.owner().hasTakenATurn();
    }
}
