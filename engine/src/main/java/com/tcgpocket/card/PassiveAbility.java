package com.tcgpocket.card;

import com.tcgpocket.trigger.ITrigger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An ability that works on its own, without the player spending anything to
 * use it.
 *
 * <p>Structurally identical to a Tool or a Stadium: a bag of triggers. That is
 * the payoff of the trigger hierarchy — three quite different-looking cards
 * turn out to be the same mechanism, and the dispatcher needs no idea which is
 * which beyond knowing where to look.
 *
 * <p>The exception is a standing rule that must hold continuously rather than
 * happen at a moment; see {@link IRule}.
 */
public record PassiveAbility(
        String name,
        String description,
        List<ITrigger> triggers,
        Optional<IRule> rule) implements IAbility {

    public PassiveAbility {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(rule, "rule");
        triggers = List.copyOf(triggers);
    }

    public PassiveAbility(String name, String description, List<ITrigger> triggers) {
        this(name, description, triggers, Optional.empty());
    }

    public PassiveAbility(String name, String description, IRule rule) {
        this(name, description, List.of(), Optional.of(rule));
    }

    public PassiveAbility(String name, ITrigger... triggers) {
        this(name, "", List.of(triggers));
    }
}
