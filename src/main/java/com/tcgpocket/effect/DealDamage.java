package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * The ordinary attack: damage to one target, through the full pipeline.
 *
 * <p>{@code damageAmount} is the <em>base</em>. Weakness and active modifiers
 * are applied by the pipeline, not here, so a card never has to restate them.
 */
public record DealDamage(INumber damageAmount, ITarget target) implements IEffect {

    public DealDamage {
        Objects.requireNonNull(damageAmount, "damageAmount");
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        Damage.deal(context, resolved.get(), damageAmount.evaluate(context), true);
        return EffectOutcome.APPLIED;
    }
}
