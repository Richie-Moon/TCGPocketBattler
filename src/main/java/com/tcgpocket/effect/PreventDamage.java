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
 * Blocks damage to the target entirely, for a number of turns.
 *
 * <p>Overrides the arithmetic rather than subtracting from it, so it holds
 * however large the incoming hit is.
 */
public record PreventDamage(ITarget target, INumber duration) implements IDurationEffect {

    public PreventDamage {
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
        resolved.get().addModifier(new ActiveModifier(ModifierKind.PREVENT_DAMAGE, 0, expiry));
        return EffectOutcome.APPLIED;
    }
}
