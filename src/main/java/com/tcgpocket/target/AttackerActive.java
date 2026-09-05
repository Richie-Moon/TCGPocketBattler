package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Optional;

/**
 * The active Pokemon of the side whose turn it is.
 *
 * <p>Turn-relative, not source-relative: during the opponent's turn this is
 * still their active, even when the resolving card belongs to you. Use
 * {@link Self} when a card means "this Pokemon".
 */
public record AttackerActive() implements ITarget {

    @Override
    public Optional<PokemonInPlay> resolve(ResolutionContext context) {
        return context.battle().attacker().active();
    }
}
