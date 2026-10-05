package com.tcgpocket.card;

/**
 * Extra max HP for the Pokemon holding the Tool that carries this — Giant
 * Cape's "+20 HP".
 *
 * <p>Read by {@code PokemonInPlay.maxHp()}, so the bonus goes the moment the
 * Tool does, and damage already over the new max then knocks the holder out.
 */
public record HpBonus(int amount) implements IRule {
}
