package com.tcgpocket.card;

import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;

/**
 * Extra max HP for the Pokemon holding the Tool that carries this — Giant
 * Cape's "+20 HP", or Leaf Cape's "the Grass Pokémon this card is attached to
 * gets +30 HP" with {@code holder} narrowing who benefits.
 *
 * <p>Read by {@code PokemonInPlay.maxHp()}, so the bonus goes the moment the
 * Tool does, and damage already over the new max then knocks the holder out.
 */
public record HpBonus(int amount, ICondition<PokemonInPlay> holder) implements IRule {

    public HpBonus {
        Objects.requireNonNull(holder, "holder");
    }

    /** For any holder. */
    public HpBonus(int amount) {
        this(amount, new Always<>());
    }
}
