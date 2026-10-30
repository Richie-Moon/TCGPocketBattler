package com.tcgpocket.state;

import java.util.Objects;
import java.util.Optional;

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
 * @param attackName    the attack a {@code CANNOT_USE_ATTACK} forbids; empty otherwise
 */
public record ActiveModifier(ModifierKind kind, int amount, int expiresOnTurn, Optional<String> attackName) {

    public ActiveModifier {
        Objects.requireNonNull(kind, "kind");
        Objects.requireNonNull(attackName, "attackName");
        if (amount < 0) {
            throw new IllegalArgumentException("modifier amount must not be negative, was " + amount);
        }
    }

    public ActiveModifier(ModifierKind kind, int amount, int expiresOnTurn) {
        this(kind, amount, expiresOnTurn, Optional.empty());
    }

    public boolean isActiveOn(int currentTurn) {
        return currentTurn <= expiresOnTurn;
    }
}
