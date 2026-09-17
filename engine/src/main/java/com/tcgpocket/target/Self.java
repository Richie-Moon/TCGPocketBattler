package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Optional;

/**
 * The Pokemon whose text is currently resolving.
 *
 * <p>This is the target that a bare {@code Battle} could not express: a Tool's
 * trigger means "the Pokemon I am attached to", which is unrelated to whose
 * turn it is. It resolves through {@link ResolutionContext#source()}, and is
 * empty for a Trainer card.
 */
public record Self() implements ITarget {

    @Override
    public Optional<PokemonInPlay> resolve(ResolutionContext context) {
        return context.source();
    }
}
