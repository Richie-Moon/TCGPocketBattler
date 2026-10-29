package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;

/**
 * "Your turn ends." — Kiawe.
 *
 * <p>Only marks the controller's side; {@code TurnEngine} ends the turn once
 * the action that played it is done, the same way an attack does, so knockouts
 * are still checked first.
 */
public record EndTurn() implements IEffect {

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        context.controller().endTurn();
        return EffectOutcome.APPLIED;
    }
}
