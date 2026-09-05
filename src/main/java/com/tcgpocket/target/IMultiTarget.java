package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.List;

/**
 * Resolves a group of Pokemon — "each of your opponent's Benched Pokemon".
 *
 * <p>Returns a list rather than an {@link java.util.Optional} because an empty
 * group is a normal answer, not a failure: an empty bench simply means nothing
 * is hit.
 */
public sealed interface IMultiTarget
        permits AttackerBench, OpponentBench, AttackerAll, OpponentAll, AllInPlay, Matching {

    List<PokemonInPlay> resolve(ResolutionContext context);
}
