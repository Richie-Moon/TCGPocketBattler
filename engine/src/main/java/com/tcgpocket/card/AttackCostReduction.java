package com.tcgpocket.card;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.energy.Type;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;

/**
 * The holder's attacks cost {@code amount} less {@code type} Energy while it
 * satisfies {@code whileHolder} — Abomasnow's Vigor Link, with "you have
 * Arceus or Arceus ex in play", or Cherubi's En-fruits-iastic in Grass, with
 * "has a Pokémon Tool attached".
 *
 * <p>Read by {@code Action.isLegal}. A rule rather than a turn-start stamp
 * because the condition changes mid-turn: benching Arceus and then attacking
 * is the whole point of the card. A cost never drops below its typed part.
 */
public record AttackCostReduction(Type type, int amount, ICondition<PokemonInPlay> whileHolder) implements IRule {
    public AttackCostReduction {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(whileHolder, "whileHolder");
        if (amount < 1) {
            throw new IllegalArgumentException("a reduction must be at least 1, was " + amount);
        }
    }

    /** Colorless, which is what most of them take off. */
    public AttackCostReduction(int colorless, ICondition<PokemonInPlay> whileHolder) {
        this(Type.COLORLESS, colorless, whileHolder);
    }
}
