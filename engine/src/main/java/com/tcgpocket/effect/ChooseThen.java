package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.List;
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
 * <p>{@code then} runs in order, like an {@code Attempt}'s effects — Pokémon
 * Center Lady heals the chosen Pokemon and then cures it. A FAILED one stops
 * the rest; otherwise APPLIED if any of them was.
 *
 * <p>FAILED if the choice resolves to nobody, as any effect with an unresolved
 * target is.
 */
public record ChooseThen(ITarget choice, List<IEffect> then) implements IEffect {

    public ChooseThen {
        Objects.requireNonNull(choice, "choice");
        then = List.copyOf(then);
    }

    public ChooseThen(ITarget choice, IEffect then) {
        this(choice, List.of(then));
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> chosen = choice.resolve(context);
        if (chosen.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        ResolutionContext onChosen = context.withPlayTarget(chosen);
        EffectOutcome result = EffectOutcome.NO_OP;
        for (IEffect effect : then) {
            EffectOutcome outcome = effect.apply(onChosen);
            if (outcome.isFailure()) {
                return outcome;
            }
            if (outcome == EffectOutcome.APPLIED || result == EffectOutcome.NO_OP) {
                result = outcome;
            }
        }
        return result;
    }
}
