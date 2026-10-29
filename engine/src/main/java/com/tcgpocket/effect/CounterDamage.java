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
 * Hits back at whatever damages the target with an attack, for a number of
 * turns — "during your opponent's next turn, if this Pokemon is damaged by an
 * attack, do 40 damage to the Attacking Pokemon".
 *
 * <p>Rough Skin for a while, granted by an attack instead of printed as an
 * ability. Installs a {@link ModifierKind#COUNTER_DAMAGE} modifier, and
 * {@code TriggerDispatcher} gives a Pokemon under it a retaliating
 * {@code DamageDealt} trigger while it lasts, as it does Confusion's for
 * {@link HinderAttack}. Damage nobody dealt (poison, burn) resolves no
 * attacker, so it is never answered.
 */
public record CounterDamage(INumber damageAmount, ITarget target, INumber duration) implements IDurationEffect {

    public CounterDamage {
        Objects.requireNonNull(damageAmount, "damageAmount");
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(duration, "duration");
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

        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        resolved.get().addModifier(new ActiveModifier(ModifierKind.COUNTER_DAMAGE, amount, expiry));
        return EffectOutcome.APPLIED;
    }
}
