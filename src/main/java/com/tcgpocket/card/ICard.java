package com.tcgpocket.card;

import com.tcgpocket.action.IAction;

import java.util.List;
import java.util.Set;

/**
 * A card as printed: immutable, and shared by every copy of it in a game.
 *
 * <p>The mutable battle state of one physical copy lives on
 * {@code CardInstance} instead. Two copies of Pikachu share one
 * {@code PokemonCard} but take damage independently.
 */
public sealed interface ICard permits IPlayableCard, ITrainerCard {

    /** Stable identifier for the printed card, unique across the card pool. */
    String id();

    String name();

    String description();

    Set<CardTag> tags();

    /**
     * What this card can do: a Pokemon's attacks, a Trainer's single effect.
     *
     * <p>This is the whole point of the design — a card's behaviour is a list
     * of values, not a branch in a switch somewhere.
     */
    List<IAction> actions();

    default boolean hasTag(CardTag tag) {
        return tags().contains(tag);
    }
}
