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
 * Stops the target retreating, for a number of turns — "during your opponent's
 * next turn, the Defending Pokemon can't retreat".
 *
 * <p>The mirror of {@link PreventAttack}, and a modifier rather than a status
 * for the same reasons: Paralysis and Sleep would also forbid attacking, and
 * would compete for the special-condition slot. It still leaves the active spot
 * with its holder, so a switch effect gets around it.
 */
public record PreventRetreat(ITarget target, INumber duration) implements IDurationEffect {

    public PreventRetreat {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(duration, "duration");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        resolved.get().addModifier(new ActiveModifier(ModifierKind.CANNOT_RETREAT, 0, expiry));
        return EffectOutcome.APPLIED;
    }
}
