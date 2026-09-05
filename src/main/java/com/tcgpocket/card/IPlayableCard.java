package com.tcgpocket.card;

/** A card that can occupy the active spot or the bench and take damage. */
public sealed interface IPlayableCard extends ICard permits PokemonCard {

    /**
     * Printed HP. Current HP is this minus the damage on a particular copy, so
     * it is tracked on the instance rather than here.
     */
    int maxHp();
}
