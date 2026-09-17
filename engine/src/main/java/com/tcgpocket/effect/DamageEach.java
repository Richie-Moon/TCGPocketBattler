package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.IMultiTarget;

import java.util.List;
import java.util.Objects;

/**
 * The same damage to every Pokemon in a group — "does 20 damage to each of
 * your opponent's Benched Pokemon".
 *
 * <p>Not attack damage, so weakness and modifiers do not apply, matching the
 * rules for anything other than the Pokemon that was attacked.
 */
public record DamageEach(INumber damageAmount, IMultiTarget targets) implements IEffect {

    public DamageEach {
        Objects.requireNonNull(damageAmount, "damageAmount");
        Objects.requireNonNull(targets, "targets");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        List<PokemonInPlay> resolved = targets.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.NO_OP;
        }

        int amount = damageAmount.evaluate(context);
        for (PokemonInPlay pokemon : resolved) {
            Damage.deal(context, pokemon, amount, false);
        }
        return EffectOutcome.APPLIED;
    }
}
