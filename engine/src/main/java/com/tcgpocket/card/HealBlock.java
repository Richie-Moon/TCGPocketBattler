package com.tcgpocket.card;

/**
 * No Pokemon on either side can be healed while the holder is in play —
 * Claydol's Heal Block.
 *
 * <p>Read by {@code ResolutionContext.healingBlocked}, which {@code HealDamage}
 * and {@code HealEach} ask. {@code MoveDamage} does not: moving damage is not
 * healing.
 */
public record HealBlock() implements IRule {
}
