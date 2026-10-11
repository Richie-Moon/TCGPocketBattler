package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Discards a Pokemon's Tool to its owner's discard pile — "discard all Pokemon
 * Tools from your opponent's Active Pokemon".
 *
 * <p>A Pokemon holds at most one Tool, so "all" is that one. No Tool is
 * {@link EffectOutcome#NO_OP}: the attack it rides on still goes ahead.
 */
public record DiscardTool(ITarget target) implements IEffect {

    public DiscardTool {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        if (resolved.get().tool().isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        if (context.shields(resolved.get())) {
            return EffectOutcome.PREVENTED;
        }
        resolved.get().removeTool().ifPresent(resolved.get().owner()::addToDiscard);
        return EffectOutcome.APPLIED;
    }
}
