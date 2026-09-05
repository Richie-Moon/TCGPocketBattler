package com.tcgpocket.card;

import java.util.Objects;
import java.util.Set;

/**
 * A Tool, attached to a Pokemon and staying there until that Pokemon leaves
 * play. A Pokemon may hold only one.
 */
public record ToolCard(
        String id,
        String name,
        String description,
        Set<CardTag> tags) implements ITrainerCard {

    // TODO: List<ITrigger> triggers — a Tool does its work entirely through
    //       triggers, which is what makes `Self` targeting necessary.

    public ToolCard {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        tags = Set.copyOf(tags);
    }

    public static ToolCard named(String id, String name) {
        return new ToolCard(id, name, "", Set.of());
    }
}
