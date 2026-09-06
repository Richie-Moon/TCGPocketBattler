package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;
import com.tcgpocket.trigger.Healed;
import com.tcgpocket.trigger.TriggerDispatcher;

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

        int before = pokemon.damage();
        pokemon.heal(amount);

        // What actually came off, not what was asked for: healing 50 off a
        // Pokemon with 20 damage on it heals 20, and a listener wants the 20.
        TriggerDispatcher.dispatch(
                context.battle(), new Healed(pokemon, before - pokemon.damage()));
        return EffectOutcome.APPLIED;
    }
}
