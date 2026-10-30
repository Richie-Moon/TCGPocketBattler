package com.tcgpocket.card;

import com.tcgpocket.action.IAction;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/** An Item, playable any number of times a turn. */
public record ItemCard(
        String id,
        String name,
        String description,
        Set<CardTag> tags,
        List<IAction> actions,
        CardRarity rarity) implements ITrainerCard {

    public ItemCard {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(rarity, "rarity");
        tags = Set.copyOf(tags);
        actions = List.copyOf(actions);
    }

    /** An Item of the usual Trainer rarity, {@link CardRarity#UNCOMMON}. */
    public ItemCard(String id, String name, String description, Set<CardTag> tags, List<IAction> actions) {
        this(id, name, description, tags, actions, CardRarity.UNCOMMON);
    }

    /** The same card under another id: an alternate art or a reprint. */
    public ItemCard withId(String newId, CardRarity newRarity) {
        return new ItemCard(newId, name, description, tags, actions, newRarity);
    }

    public static ItemCard of(String id, String name, IAction action) {
        return new ItemCard(id, name, "", Set.of(), List.of(action));
    }
}
