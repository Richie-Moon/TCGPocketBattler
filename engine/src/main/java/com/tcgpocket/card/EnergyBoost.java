package com.tcgpocket.card;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.energy.Type;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;

/**
 * Each {@code type} Energy attached to the holder's matching Pokemon provides
 * {@code provides} Energy — Serperior's Jungle Totem.
 *
 * <p>Read by {@link PokemonInPlay#providedEnergy()}, which attack and retreat
 * costs are checked against. A rule rather than a trigger because it has to
 * hold at every moment of its owner's turn, through benching, evolving and
 * attacking.
 *
 * <p>It changes what Energy <em>provides</em> to a cost, never what is
 * <em>attached</em>: discarding or moving still handles one card per unit.
 * Boosts of the same type do not stack; the largest wins.
 *
 * @param appliesTo which of the holder's own Pokemon benefit — "your Grass
 *                  Pokemon". Never the opponent's.
 */
public record EnergyBoost(Type type, ICondition<PokemonInPlay> appliesTo, int provides) implements IRule {

    public EnergyBoost {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(appliesTo, "appliesTo");
        if (provides < 1) {
            throw new IllegalArgumentException("each Energy must provide at least 1, was " + provides);
        }
    }
}
