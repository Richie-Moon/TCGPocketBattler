package com.tcgpocket.card;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;

/**
 * The holder's opponent can't play a Pokemon from hand to evolve their
 * Pokemon matching {@code appliesTo} — Aerodactyl ex's Primeval Law, with
 * {@code IsActive}.
 *
 * <p>Read by {@code EvolveAction.isLegal}. A rule rather than a turn-start
 * stamp because the opponent can change their Active mid-turn: the Pokemon
 * that retreats may then evolve on the Bench, and the one switched in may not.
 */
public record EvolutionLock(ICondition<PokemonInPlay> appliesTo) implements IRule {

    public EvolutionLock {
        Objects.requireNonNull(appliesTo, "appliesTo");
    }
}
