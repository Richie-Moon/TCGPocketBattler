package com.tcgpocket.card;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;

/**
 * The holder's attacks cost {@code colorless} less Colorless Energy while it
 * satisfies {@code whileHolder} — Abomasnow's Vigor Link, with "you have
 * Arceus or Arceus ex in play".
 *
 * <p>Read by {@code Action.isLegal}. A rule rather than a turn-start stamp
 * because the condition changes mid-turn: benching Arceus and then attacking
 * is the whole point of the card. A cost never drops below its typed part.
 */
public record AttackCostReduction(int colorless, ICondition<PokemonInPlay> whileHolder) implements IRule {

    public AttackCostReduction {
        Objects.requireNonNull(whileHolder, "whileHolder");
        if (colorless < 1) {
            throw new IllegalArgumentException("a reduction must be at least 1, was " + colorless);
        }
    }
}
