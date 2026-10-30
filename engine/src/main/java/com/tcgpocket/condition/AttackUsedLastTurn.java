package com.tcgpocket.condition;

import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * Whether any of the controller's Pokemon used a named attack on their previous
 * turn — Sweets Relay's "if 1 of your Pokémon used Sweets Relay during your last
 * turn".
 *
 * <p>Two turns back, not one: {@code Battle.turn()} counts both players' turns.
 */
public record AttackUsedLastTurn(String attackName) implements ICondition<ResolutionContext> {

    public AttackUsedLastTurn {
        Objects.requireNonNull(attackName, "attackName");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        return context.controller().usedAttackOn(attackName, context.battle().turn() - 2);
    }
}
