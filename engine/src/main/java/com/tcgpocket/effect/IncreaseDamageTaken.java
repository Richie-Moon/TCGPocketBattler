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
 * Worsens damage the target takes, for a number of turns — "during your
 * opponent's next turn, this Pokémon takes +30 damage from attacks". The
 * mirror of {@link ReduceDamageTaken}.
 */
public record IncreaseDamageTaken(INumber increaseAmount, ITarget target, INumber duration)
        implements IDurationEffect {

    public IncreaseDamageTaken {
        Objects.requireNonNull(increaseAmount, "increaseAmount");
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(duration, "duration");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        int amount = increaseAmount.evaluate(context);
        if (amount <= 0) {
            return EffectOutcome.NO_OP;
        }

        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        resolved.get().addModifier(
                new ActiveModifier(ModifierKind.INCREASE_DAMAGE_TAKEN, amount, expiry));
        return EffectOutcome.APPLIED;
    }
}
