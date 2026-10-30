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
 * Stops the target using one named attack, for a number of turns — Buzzwole
 * ex's "during your next turn, this Pokemon can't use Big Beat".
 *
 * <p>Not {@link PreventAttack}, which forbids every attack: that is the same
 * restriction only on a Pokemon with one attack, and Buzzwole ex can still use
 * Punch. Retreating sheds it, like every modifier.
 */
public record PreventNamedAttack(String attackName, ITarget target, INumber duration) implements IDurationEffect {

    public PreventNamedAttack {
        Objects.requireNonNull(attackName, "attackName");
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
        resolved.get().addModifier(new ActiveModifier(
                ModifierKind.CANNOT_USE_ATTACK, 0, expiry, Optional.of(attackName)));
        return EffectOutcome.APPLIED;
    }
}
