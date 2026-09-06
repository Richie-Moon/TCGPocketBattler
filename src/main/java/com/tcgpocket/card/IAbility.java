package com.tcgpocket.card;

/**
 * An ability printed on a Pokemon, distinct from its attacks.
 *
 * <p>The two kinds differ in how they reach the board, not in what they can do:
 * an {@link ActivatedAbility} is offered to the player as an action, while a
 * {@link PassiveAbility} is a set of triggers that the dispatcher finds on its
 * own. Neither needs a hook in the engine.
 */
public sealed interface IAbility permits ActivatedAbility, PassiveAbility {

    String name();

    String description();
}
