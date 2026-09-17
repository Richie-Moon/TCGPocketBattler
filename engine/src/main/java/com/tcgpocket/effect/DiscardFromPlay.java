package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Puts a Pokemon in play, with every card under it and its Tool, into its
 * owner's discard pile — a Fossil's "discard this card from play".
 *
 * <p>Not a knockout: no points are awarded. An Active that leaves is replaced
 * from the Bench as for {@link ShuffleIntoDeck}, and with nothing to replace it
 * this {@link EffectOutcome#FAILED fails}.
 */
public record DiscardFromPlay(ITarget target) implements IEffect {

    public DiscardFromPlay {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        return ShuffleIntoDeck.leavePlay(context, resolved.get(), resolved.get().owner()::addToDiscard)
                ? EffectOutcome.APPLIED
                : EffectOutcome.FAILED;
    }
}
