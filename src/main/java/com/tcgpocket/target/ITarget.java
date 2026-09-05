package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Optional;

/**
 * Resolves "who" against the board.
 *
 * <p>Returns an {@link Optional} because a target can legitimately fail to
 * resolve — an empty bench, a knocked-out active, a Trainer card with no source
 * Pokemon. Callers decide what that means: an effect treats it as a failure
 * that aborts its attempt, whereas an {@code INumber} reading a missing target
 * contributes zero.
 */
public sealed interface ITarget permits
        AttackerActive, OpponentActive, Self,
        AttackerBenchSpecific, OpponentBenchSpecific {

    // TODO: AttackerBenchRandom, OpponentBenchRandom, AttackerRandom,
    //       OpponentRandom, ChosenFrom, EventSource — ChosenFrom needs IPlayer.

    Optional<PokemonInPlay> resolve(ResolutionContext context);
}
