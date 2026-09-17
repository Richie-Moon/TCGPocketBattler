package com.tcgpocket.card;

/** A card that can occupy the active spot or the bench and take damage. */
public sealed interface IPlayableCard extends ICard permits PokemonCard, PlayableItemCard {

    /**
     * Printed HP. Current HP is this minus the damage on a particular copy, so
     * it is tracked on the instance rather than here.
     */
    int maxHp();

    /**
     * This card as the Pokemon it is in play: a Pokemon is itself, a Fossil is
     * the Basic its text says to play it as. What {@code PokemonInPlay} reads
     * HP, type, attacks and ability from, so the board needs no Fossil case.
     */
    PokemonCard asPokemon();
}
