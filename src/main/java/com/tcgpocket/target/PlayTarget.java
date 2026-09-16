package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Optional;

/**
 * The Pokemon a Trainer was dragged onto — "that Pokemon" in Misty's text.
 *
 * <p>Unlike {@link ChosenFrom}, resolving this asks nobody anything: the choice
 * was made when the card was played, as part of the move, so it is settled
 * before the first coin is flipped. It resolves through
 * {@link ResolutionContext#playTarget()}, and is empty outside a
 * {@code PlayedOnto}.
 */
public record PlayTarget() implements ITarget {

    @Override
    public Optional<PokemonInPlay> resolve(ResolutionContext context) {
        return context.playTarget();
    }
}
