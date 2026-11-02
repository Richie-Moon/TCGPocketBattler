package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Puts the top Evolution card on an evolved Pokemon into its owner's hand —
 * Celebi's Temporal Leaves.
 *
 * <p>A Basic, or a Pokemon the same attack already knocked out, is a
 * {@link EffectOutcome#NO_OP}: a knocked-out Pokemon's whole stack goes to the
 * discard pile. See {@link PokemonInPlay#devolve()} for what carries over.
 */
public record Devolve(ITarget target) implements IEffect {

    public Devolve {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        PokemonInPlay pokemon = resolved.get();
        if (context.shields(pokemon)) {
            return EffectOutcome.PREVENTED;
        }
        if (pokemon.isKnockedOut()) {
            return EffectOutcome.NO_OP;
        }
        return pokemon.devolve().map(card -> {
            pokemon.owner().addToHand(card);
            return EffectOutcome.APPLIED;
        }).orElse(EffectOutcome.NO_OP);
    }
}
