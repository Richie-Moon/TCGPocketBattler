package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.IMultiTarget;

import java.util.List;
import java.util.Objects;

/**
 * Damage scattered at random across a group, ten at a time.
 *
 * <p>Counters are placed one by one, each on a randomly chosen member, so the
 * same Pokemon can be hit more than once. Not attack damage, so weakness and
 * modifiers do not apply.
 */
public record SpreadDamage(INumber damageAmount, IMultiTarget targets) implements IEffect {

    /** Damage counters come in tens. */
    private static final int COUNTER = 10;

    public SpreadDamage {
        Objects.requireNonNull(damageAmount, "damageAmount");
        Objects.requireNonNull(targets, "targets");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        List<PokemonInPlay> candidates = targets.resolve(context);
        int total = damageAmount.evaluate(context);

        if (candidates.isEmpty() || total < COUNTER) {
            return EffectOutcome.NO_OP;
        }

        int counters = total / COUNTER;
        for (int i = 0; i < counters; i++) {
            PokemonInPlay unlucky = candidates.get(context.battle().rng().nextInt(candidates.size()));
            Damage.deal(context, unlucky, COUNTER, false);
        }
        return EffectOutcome.APPLIED;
    }
}
