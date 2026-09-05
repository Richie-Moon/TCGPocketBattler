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
        List<IAction> actions) implements ITrainerCard {

    public ItemCard {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        tags = Set.copyOf(tags);
        actions = List.copyOf(actions);
    }

    public static ItemCard of(String id, String name, IAction action) {
        return new ItemCard(id, name, "", Set.of(), List.of(action));
    }
}
