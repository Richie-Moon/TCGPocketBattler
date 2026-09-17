package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.IMultiTarget;
import com.tcgpocket.trigger.Healed;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.List;
import java.util.Objects;

/**
 * The same healing to every Pokemon in a group — "heal 20 damage from each of
 * your Pokemon".
 *
 * <p>The healing counterpart to {@link DamageEach}, and it inherits
 * {@link HealDamage}'s rule about doing nothing: a group where everything is
 * already at full HP is a {@link EffectOutcome#NO_OP}, not a failure, so it
 * cannot abort the rest of an attempt. It reports {@code APPLIED} when at least
 * one Pokemon actually lost a damage counter.
 */
public record HealEach(INumber healAmount, IMultiTarget targets) implements IEffect {

    public HealEach {
        Objects.requireNonNull(healAmount, "healAmount");
        Objects.requireNonNull(targets, "targets");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        List<PokemonInPlay> resolved = targets.resolve(context);
        int amount = healAmount.evaluate(context);
        if (resolved.isEmpty() || amount <= 0) {
            return EffectOutcome.NO_OP;
        }

        boolean anyHealed = false;
        for (PokemonInPlay pokemon : resolved) {
            int before = pokemon.damage();
            if (before == 0) {
                continue;
            }

            pokemon.heal(amount);
            anyHealed = true;

            // One event per Pokemon, carrying what actually came off that one.
            // A group heal is several healings, not a single bulk one.
            TriggerDispatcher.dispatch(
                    context.battle(), new Healed(pokemon, before - pokemon.damage()));
        }

        return anyHealed ? EffectOutcome.APPLIED : EffectOutcome.NO_OP;
    }
}
