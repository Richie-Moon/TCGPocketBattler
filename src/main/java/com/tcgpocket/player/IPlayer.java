package com.tcgpocket.player;

/**
 * Whoever makes the decisions the rules leave to a player.
 *
 * <p>One generic {@link #choose} rather than a method per kind of decision
 * (chooseBenchSlot, chooseDiscard, chooseAttack, ...): the engine always
 * presents a list of legal options and gets one back, so a new kind of
 * decision never widens this interface.
 *
 * <p>A whole turn goes through the same seam — {@code Battle.legalActions}
 * builds a {@code Decision<IAction>} — which is what makes this a simulator
 * rather than a card renderer.
 */
public interface IPlayer {

    String name();

    /**
     * Picks one of the offered options.
     *
     * <p>Implementations must return one of {@code decision.options()}; the
     * engine only ever offers legal choices, so returning something else is a
     * bug in the player rather than a case the engine handles.
     */
    <T> T choose(Decision<T> decision);
}
