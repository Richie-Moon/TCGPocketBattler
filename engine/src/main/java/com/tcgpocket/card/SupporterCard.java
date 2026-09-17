package com.tcgpocket.card;

import com.tcgpocket.action.IAction;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A Supporter, of which only one may be played per turn.
 *
 * <p>That limit is why Supporters are their own type rather than Items with a
 * flag: {@code PlayCardAction} has to be able to tell them apart.
 */
public record SupporterCard(
        String id,
        String name,
        String description,
        Set<CardTag> tags,
        List<IAction> actions) implements ITrainerCard {

    public SupporterCard {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        tags = Set.copyOf(tags);
        actions = List.copyOf(actions);
    }

    public static SupporterCard of(String id, String name, IAction action) {
        return new SupporterCard(id, name, "", Set.of(), List.of(action));
    }
}
