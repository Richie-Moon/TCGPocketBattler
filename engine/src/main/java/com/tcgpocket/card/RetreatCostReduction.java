package com.tcgpocket.card;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;

/**
 * The holder's side's retreating Pokemon that meets {@code retreating} pays
 * {@code amount} less, while the holder satisfies {@code whileHolder} —
 * Shaymin's Sky Support: "as long as this Pokémon is on your Bench, your
 * Active Basic Pokémon's Retreat Cost is 1 less", with {@code IsBasic} and
 * {@code IsBenched}. Only the Active ever retreats, so "Active" needs no
 * condition of its own.
 *
 * <p>Read by {@code RetreatAction}. Unlike {@link NoRetreatCost} it helps
 * another Pokemon, and two holders stack.
 */
public record RetreatCostReduction(
        int amount,
        ICondition<? super PokemonInPlay> retreating,
        ICondition<PokemonInPlay> whileHolder) implements IRule {

    public RetreatCostReduction {
        Objects.requireNonNull(retreating, "retreating");
        Objects.requireNonNull(whileHolder, "whileHolder");
        if (amount < 1) {
            throw new IllegalArgumentException("a reduction must be at least 1, was " + amount);
        }
    }
}
