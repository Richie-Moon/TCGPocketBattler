package com.tcgpocket.condition;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;

/**
 * A boolean test, generic over what it examines.
 *
 * <p>There is deliberately no {@link ResolutionContext} parameter on
 * {@link #evaluate}, because each kind of subject is self-sufficient:
 *
 * <ul>
 *   <li>{@code ICondition<ResolutionContext>} — the context <em>is</em> the
 *       subject, so these can ask about anything on the board
 *       ({@link GreaterThan}, {@link Probability}, {@link StadiumInPlay})
 *   <li>{@code ICondition<PokemonInPlay>} — an instance already knows its own
 *       zone, statuses, damage and attached energy ({@link IsActive},
 *       {@link IsBurned}, {@link HasEnergy})
 *   <li>{@code ICondition<CardInstance>} — the same, for cards that are not in
 *       play ({@link InZone}, {@link IsSpecies}, {@link HasTag}). By subtyping
 *       these accept a {@link PokemonInPlay} too.
 * </ul>
 *
 * <p>{@link For} and {@link ForAny} bridge the levels: they resolve a target
 * and apply a Pokemon-level condition to it, yielding a context-level
 * condition. That is what lets every precondition in the model be typed simply
 * as {@code ICondition<ResolutionContext>}.
 *
 * <p>The combinators ({@link And}, {@link Or}, {@link Not}, {@link All},
 * {@link Any}, {@link Always}) are generic and so compose at any level.
 *
 * @param <T> what this condition examines
 */
public sealed interface ICondition<T> permits
        // combinators, generic over any subject
        And, Or, Not, All, Any, Always,
        // subject: ResolutionContext
        GreaterThan, LessThan, EqualTo, Probability, For, ForAny,
        LastCoinTossHeads, AllFlipsHeads, StadiumInPlay,
        // subject: PokemonInPlay
        HasType, IsPoisoned, IsBurned, IsAsleep, IsParalyzed, IsConfused,
        IsActive, IsBenched, IsDamaged, HasTool, HasEnergy,
        // subject: CardInstance
        InZone, IsSpecies, HasTag {

    boolean evaluate(T subject);
}
