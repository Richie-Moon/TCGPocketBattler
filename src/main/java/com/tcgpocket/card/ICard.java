package com.tcgpocket.card;

import java.util.Set;

/**
 * A card as printed: immutable, and shared by every copy of it in a game.
 *
 * <p>The mutable battle state of one physical copy lives on
 * {@code CardInstance} instead. Two copies of Pikachu share one
 * {@code PokemonCard} but take damage independently.
 */
public sealed interface ICard permits IPlayableCard {

    /** Stable identifier for the printed card, unique across the card pool. */
    String id();

    String name();

    String description();

    Set<CardTag> tags();

    default boolean hasTag(CardTag tag) {
        return tags().contains(tag);
    }

    // TODO: List<IAction> actions() — awaits the IAction hierarchy.
    // TODO: ITrainerCard branch (Item, Supporter, Tool, Stadium, Fossil) —
    //       those need IAction and ITrigger, so the permits clause grows later.
}
