package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Resolves a target once and runs an effect that names it as
 * {@code PlayTarget} — "20 damage to 1 of your opponent's Pokemon for each
 * Energy attached to <em>that</em> Pokemon".
 *
 * <p>{@code ChosenFrom} asks again every time it is resolved, so card text that
 * names the chosen Pokemon twice — once to hit it, once to count on it — would
 * ask twice and could get two answers. This settles the choice first and hands
 * it on the way a dragged-onto Trainer hands on its Pokemon, so the inner
 * effect reads it through {@code PlayTarget} with nobody asked again.
 *
 * <p>FAILED if the choice resolves to nobody, as any effect with an unresolved
 * target is.
 */
public record ChooseThen(ITarget choice, IEffect then) implements IEffect {

    public ChooseThen {
        Objects.requireNonNull(choice, "choice");
        Objects.requireNonNull(then, "then");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> chosen = choice.resolve(context);
        if (chosen.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        return then.apply(context.withPlayTarget(chosen));
    }
}
