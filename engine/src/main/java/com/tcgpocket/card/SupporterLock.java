package com.tcgpocket.card;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;

/**
 * The holder's opponent can't play Supporter cards from hand, while the holder
 * itself satisfies {@code whileHolder} — Gengar ex's Shadowy Spellbind, with
 * {@code IsActive}.
 *
 * <p>Read by {@code PlayCardAction.isLegal}. Unlike {@link EvolutionLock}, the
 * condition is on the holder, not on what it restricts: "as long as
 * <em>this</em> Pokemon is in the Active Spot". A turn-start stamp would miss
 * the holder leaving the Active Spot mid-turn.
 */
public record SupporterLock(ICondition<PokemonInPlay> whileHolder) implements IRule {

    public SupporterLock {
        Objects.requireNonNull(whileHolder, "whileHolder");
    }
}
