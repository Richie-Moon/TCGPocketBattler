package com.tcgpocket.card;

/**
 * An ability printed on a Pokemon, distinct from its attacks.
 *
 * <p>Only the activated kind exists so far. A passive ability is implemented
 * entirely as triggers — exactly like a Tool or a Stadium — so it arrives with
 * the {@code ITrigger} hierarchy and needs nothing new to dispatch it.
 */
public sealed interface IAbility permits ActivatedAbility {

    // TODO: PassiveAbility(List<ITrigger> triggers).

    String name();

    String description();
}
