package com.tcgpocket.card;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;

/**
 * The holder's opponent's attacks cost more Colorless, while the holder itself
 * satisfies {@code whileHolder} — Stoutland's Guard Dog Visage, with
 * {@code IsActive}.
 *
 * <p>Read by {@code Action.isLegal}. A rule rather than a trigger because cost
 * is asked before any attack is declared, and a turn-start stamp would miss the
 * holder leaving the Active Spot mid-turn.
 */
public record AttackCostIncrease(int colorless, ICondition<PokemonInPlay> whileHolder) implements IRule {

    public AttackCostIncrease {
        Objects.requireNonNull(whileHolder, "whileHolder");
    }
}
