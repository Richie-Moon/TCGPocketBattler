package com.tcgpocket.card;

import com.tcgpocket.action.IAction;
import com.tcgpocket.trigger.ITrigger;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A Tool, attached to a Pokemon and staying there until that Pokemon leaves
 * play. A Pokemon may hold only one.
 *
 * <p>A Tool does its work through {@link #triggers()} rather than actions, so
 * {@link #actions()} is normally empty. Its triggers fire with the Pokemon
 * holding it as the source, which is what makes {@code Self} mean "the Pokemon
 * I am attached to".
 */
public record ToolCard(
        String id,
        String name,
        String description,
        Set<CardTag> tags,
        List<IAction> actions,
        List<ITrigger> triggers) implements ITrainerCard {

    public ToolCard {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        tags = Set.copyOf(tags);
        actions = List.copyOf(actions);
        triggers = List.copyOf(triggers);
    }

    /** A Tool that does nothing yet — useful for testing attachment itself. */
    public static ToolCard named(String id, String name) {
        return new ToolCard(id, name, "", Set.of(), List.of(), List.of());
    }

    public static ToolCard of(String id, String name, ITrigger... triggers) {
        return new ToolCard(id, name, "", Set.of(), List.of(), List.of(triggers));
    }
}
