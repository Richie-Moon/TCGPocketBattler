package com.tcgpocket.condition;

import com.tcgpocket.resolve.ResolutionContext;

/**
 * Whether any of the controller's Pokemon were Knocked Out by damage from an
 * attack during the previous turn — which, being the turn before the
 * controller's own, was the opponent's.
 *
 * <p>Reads the controller rather than {@code battle.attacker()}, so it stays
 * correct if a defender's trigger ever asks. Poison, burn and other
 * between-turn knockouts do not count: {@code TurnEngine} only stamps the
 * side when an attack caused the knockout.
 */
public record KnockedOutLastTurn() implements ICondition<ResolutionContext> {

    @Override
    public boolean evaluate(ResolutionContext context) {
        return context.controller().knockedOutByAttackOn(context.battle().turn() - 1);
    }
}
