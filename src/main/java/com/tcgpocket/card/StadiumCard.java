package com.tcgpocket.card;

import com.tcgpocket.action.IAction;
import com.tcgpocket.trigger.ITrigger;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A Stadium: played into a slot of its own, and read by <em>both</em> players'
 * cards until it is replaced.
 *
 * <p>The only card that is in play but belongs to neither side's board. It has
 * no holder, so its triggers fire with no {@code source} — which means Stadium
 * text cannot say {@code Self}, and does not need to.
 */
public record StadiumCard(
        String id,
        String name,
        String description,
        Set<CardTag> tags,
        List<IAction> actions,
        List<ITrigger> triggers) implements ITrainerCard {

    public StadiumCard {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        tags = Set.copyOf(tags);
        actions = List.copyOf(actions);
        triggers = List.copyOf(triggers);
    }

    public static StadiumCard of(String id, String name, ITrigger... triggers) {
        return new StadiumCard(id, name, "", Set.of(), List.of(), List.of(triggers));
    }
}
