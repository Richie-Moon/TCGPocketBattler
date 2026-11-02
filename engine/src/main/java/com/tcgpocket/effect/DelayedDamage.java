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
 * Places damage on the target at the end of a later turn — Mismagius' "at the
 * end of your opponent's next turn, do 90 damage to the Defending Pokémon".
 *
 * <p>Installs a {@link ModifierKind#DELAYED_DAMAGE} modifier, and
 * {@code TriggerDispatcher} gives its holder a {@code TurnEnd} trigger on the
 * modifier's last turn. Like any other temporary modifier it ends if the holder
 * leaves the Active Spot. It lands as {@link PlaceDamage}, so nothing modifies it.
 */
public record DelayedDamage(INumber damageAmount, ITarget target, INumber duration) implements IDurationEffect {

    public DelayedDamage {
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
        if (context.shields(resolved.get())) {
            return EffectOutcome.PREVENTED;
        }
        int amount = damageAmount.evaluate(context);
        if (amount <= 0) {
            return EffectOutcome.NO_OP;
        }
        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        resolved.get().addModifier(new ActiveModifier(ModifierKind.DELAYED_DAMAGE, amount, expiry));
        return EffectOutcome.APPLIED;
    }
}
