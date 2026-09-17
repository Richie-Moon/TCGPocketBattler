package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Damage on one Pokemon that nobody dealt — poison, burn, self-inflicted
 * recoil, "place 2 damage counters on".
 *
 * <p>The single-target counterpart to {@link DamageEach}: not attack damage, so
 * weakness and modifiers are skipped, and the resulting event carries no
 * source. That last part matters — a Tool that retaliates against whoever hurt
 * its wearer must not fire back at poison.
 */
public record PlaceDamage(INumber damageAmount, ITarget target) implements IEffect {

    public PlaceDamage {
        Objects.requireNonNull(damageAmount, "damageAmount");
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        int amount = damageAmount.evaluate(context);
        if (amount <= 0) {
            return EffectOutcome.NO_OP;
        }

        Damage.place(context, resolved.get(), amount);
        return EffectOutcome.APPLIED;
    }
}
