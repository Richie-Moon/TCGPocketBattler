package com.tcgpocket.card;

/**
 * The holder of this Tool goes to its owner's hand, not the discard pile, when
 * an attack from the opponent's Pokemon Knocks it Out — Rescue Scarf.
 *
 * <p>A rule rather than a trigger, because the cards' destination is decided as
 * they leave play. Energy and the Tool itself are still discarded, and the
 * opponent still scores. Read by {@code TurnEngine.leavePlay}.
 */
public record ReturnToHandWhenKnockedOut() implements IRule {
}
