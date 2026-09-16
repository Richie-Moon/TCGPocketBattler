package com.tcgpocket.card;

import com.tcgpocket.action.IAction;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A Fossil: an Item played as if it were a Basic Pokemon.
 *
 * <p>Both an {@link ITrainerCard} and an {@link IPlayableCard} because both are
 * true — card text that counts Items counts it, and on the board it has HP and
 * takes damage. Its {@code actions} are what it can do in play, which for every
 * Fossil so far is discard itself.
 *
 * <p>TODO: nothing plays it yet. {@code PlayCardAction} benches only a
 * {@link PokemonCard}, and {@code PokemonInPlay} holds one, so a Fossil still
 * needs a route onto the Bench and the "can't retreat" rule.
 */
public record PlayableItemCard(
        String id,
        String name,
        String description,
        Set<CardTag> tags,
        List<IAction> actions,
        int maxHp) implements IPlayableCard, ITrainerCard {

    public PlayableItemCard {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        tags = Set.copyOf(tags);
        actions = List.copyOf(actions);
        if (maxHp <= 0) {
            throw new IllegalArgumentException("maxHp must be positive, was " + maxHp);
        }
    }

    /** A Fossil of the given HP, tagged {@link CardTag#FOSSIL}. */
    public static PlayableItemCard fossil(String id, String name, int maxHp, IAction action) {
        return new PlayableItemCard(id, name, "", Set.of(CardTag.FOSSIL), List.of(action), maxHp);
    }
}
