package com.tcgpocket.card;

import com.tcgpocket.action.IAction;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * An ability the player spends part of their turn to use.
 *
 * <p>Surfaced to the player as a {@code UseAbilityAction} alongside the
 * Pokemon's attacks, so it needs no separate seam in the turn loop.
 *
 * @param oncePerTurn most abilities are; tracked per Pokemon rather than per
 *                    card, so two copies of the same Pokemon each get a use
 */
public record ActivatedAbility(
        String name,
        String description,
        IAction action,
        boolean oncePerTurn,
        ICondition<ResolutionContext> usableWhen) implements IAbility {

    public ActivatedAbility {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(action, "action");
        Objects.requireNonNull(usableWhen, "usableWhen");
    }

    /** Usable once a turn, with no further restriction. */
    public ActivatedAbility(String name, IAction action) {
        this(name, "", action, true, new Always<>());
    }
}
