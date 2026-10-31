package com.tcgpocket.condition;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.target.ITarget;

import java.util.Objects;

/**
 * Whether the target was damaged by an attack during the previous turn while
 * in the Active Spot — Wobbuffet's Reply Strongly.
 *
 * <p>Asks the context because only it knows the turn. Having moved since does
 * not matter: the stamp records where it was when hit. Poison, burn and damage
 * taken on the Bench do not stamp, and an attacker's own recoil stamps its own
 * turn, which is never the previous one by the time it attacks again.
 */
public record DamagedWhileActiveLastTurn(ITarget target) implements ICondition<ResolutionContext> {

    public DamagedWhileActiveLastTurn {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        return target.resolve(context)
                .filter(pokemon -> pokemon.damagedByAttackWhileActiveOn(context.battle().turn() - 1))
                .isPresent();
    }
}
