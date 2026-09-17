package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * The same target struck several times — "does 20 damage times the number of
 * heads", when the card hits repeatedly rather than adding up.
 *
 * <p>Each hit runs the pipeline separately rather than multiplying once, which
 * matters: weakness and modifiers apply per hit, and the target may be knocked
 * out partway through.
 */
public record MultiHit(ITarget target, INumber damage, INumber timesToHit) implements IEffect {

    public MultiHit {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(damage, "damage");
        Objects.requireNonNull(timesToHit, "timesToHit");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        int hits = timesToHit.evaluate(context);
        if (hits <= 0) {
            return EffectOutcome.NO_OP;
        }

        int perHit = damage.evaluate(context);
        for (int i = 0; i < hits; i++) {
            Damage.deal(context, resolved.get(), perHit, true);
        }
        return EffectOutcome.APPLIED;
    }
}
