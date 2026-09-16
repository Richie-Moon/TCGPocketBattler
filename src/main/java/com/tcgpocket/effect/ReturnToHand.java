package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Puts a Pokemon in play, with every card under it and its Tool, into its
 * owner's hand — Koga's "put your Muk or Weezing in the Active Spot into your
 * hand".
 *
 * <p>The same departure as {@link ShuffleIntoDeck} and {@link DiscardFromPlay}:
 * Energy goes, an Active is replaced from the Bench, and with nothing to
 * replace it this {@link EffectOutcome#FAILED fails}.
 */
public record ReturnToHand(ITarget target) implements IEffect {

    public ReturnToHand {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        return ShuffleIntoDeck.leavePlay(context, resolved.get(), resolved.get().owner()::addToHand)
                ? EffectOutcome.APPLIED
                : EffectOutcome.FAILED;
    }
}
