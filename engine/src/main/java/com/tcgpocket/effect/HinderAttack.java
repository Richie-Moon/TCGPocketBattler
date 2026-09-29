package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.ActiveModifier;
import com.tcgpocket.state.ModifierKind;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Makes the target flip to attack, for a number of turns — "if the Defending
 * Pokemon tries to use an attack, your opponent flips a coin. If tails, that
 * attack doesn't happen".
 *
 * <p>The coin-flip cousin of {@link PreventAttack}, and a modifier rather than
 * Confusion for the same reasons: it is not a special condition, it expires on
 * a turn count, and retreating sheds it. The attack stays legal to declare;
 * {@code TriggerDispatcher} gives a Pokemon under the modifier Confusion's
 * flip-on-{@code AttackDeclared} trigger, so a tails cancels it the same way.
 */
public record HinderAttack(ITarget target, INumber duration) implements IDurationEffect {

    public HinderAttack {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(duration, "duration");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        if (context.shields(resolved.get())) {
            return EffectOutcome.PREVENTED;
        }

        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        resolved.get().addModifier(new ActiveModifier(ModifierKind.ATTACK_NEEDS_HEADS, 0, expiry));
        return EffectOutcome.APPLIED;
    }
}
