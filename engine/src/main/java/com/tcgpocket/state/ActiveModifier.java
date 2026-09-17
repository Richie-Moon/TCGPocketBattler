package com.tcgpocket.state;

import java.util.Objects;

/**
 * A temporary buff or debuff sitting on a Pokemon.
 *
 * <p>This is where an {@code IDurationEffect} lands: the effect describes the
 * change, and applying it installs one of these on the target. Lives in
 * {@code state} rather than alongside the damage calculator because a Pokemon
 * holds it — putting it with the calculator would make the engine and the
 * state mutually dependent.
 *
 * @param expiresOnTurn last turn on which this still applies; the modifier is
 *                      live while {@code currentTurn <= expiresOnTurn}
 */
public record ActiveModifier(ModifierKind kind, int amount, int expiresOnTurn) {

    public ActiveModifier {
        Objects.requireNonNull(kind, "kind");
        if (amount < 0) {
            throw new IllegalArgumentException("modifier amount must not be negative, was " + amount);
        }
    }

    public boolean isActiveOn(int currentTurn) {
        return currentTurn <= expiresOnTurn;
    }
}
