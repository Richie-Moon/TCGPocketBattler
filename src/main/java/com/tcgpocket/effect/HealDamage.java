package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Removes damage counters.
 *
 * <p>Healing a Pokemon already at full HP is a {@link EffectOutcome#NO_OP},
 * not a failure — it must not abort the rest of the attempt.
 */
public record HealDamage(INumber healAmount, ITarget target) implements IEffect {

    public HealDamage {
        Objects.requireNonNull(healAmount, "healAmount");
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        PokemonInPlay pokemon = resolved.get();
        int amount = healAmount.evaluate(context);
        if (amount <= 0 || pokemon.damage() == 0) {
            return EffectOutcome.NO_OP;
        }

        pokemon.heal(amount);
        return EffectOutcome.APPLIED;
    }
}
