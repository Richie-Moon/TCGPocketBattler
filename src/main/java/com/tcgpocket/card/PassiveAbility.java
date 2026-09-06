package com.tcgpocket.card;

import com.tcgpocket.trigger.ITrigger;

import java.util.List;
import java.util.Objects;

/**
 * An ability that works on its own, without the player spending anything to
 * use it.
 *
 * <p>Structurally identical to a Tool or a Stadium: a bag of triggers. That is
 * the payoff of the trigger hierarchy — three quite different-looking cards
 * turn out to be the same mechanism, and the dispatcher needs no idea which is
 * which beyond knowing where to look.
 */
public record PassiveAbility(
        String name,
        String description,
        List<ITrigger> triggers) implements IAbility {

    public PassiveAbility {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(description, "description");
        triggers = List.copyOf(triggers);
    }

    public PassiveAbility(String name, ITrigger... triggers) {
        this(name, "", List.of(triggers));
    }
}
