package com.tcgpocket.trigger;

import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;

import java.util.Optional;

/**
 * Something that happened on the board, which triggers can listen for.
 *
 * <p>Events carry their payload, so a trigger's attempt can read what actually
 * occurred — "when this Pokemon is damaged, deal that much back" needs the
 * amount, which an empty marker type could not supply.
 *
 * <p>{@link #subject()} and {@link #side()} are what let a trigger ask the only
 * question it almost always wants to ask — "was that about <em>me</em>?" —
 * without a condition per event type. See {@code EventConcerns} and
 * {@code EventSideIs}.
 */
public sealed interface GameEvent permits
        TurnStart, TurnEnd,
        AttackDeclared, DamageDealt, Knockout, Healed,
        StatusApplied, StatusRemoved,
        CardPlayed, Evolved, ToolAttached, ToolRemoved, Retreated, EnergyAttached {

    // TODO: PhaseEntered awaits the Phase enum, which arrives with TurnEngine.
    //       CoinFlipped is modelled but deliberately not built yet: nothing can
    //       consume it (a trigger fires with a fresh scope, so it could not read
    //       the flip back) and dispatching on every flip would walk the board
    //       constantly for a card pattern that does not exist in Pocket.

    /**
     * The Pokemon this event is chiefly about, if any.
     *
     * <p>"Chiefly" is doing real work: {@link DamageDealt} is about the Pokemon
     * that <em>took</em> the damage, not the one that dealt it, because that is
     * the one whose Tool and ability need to react.
     */
    default Optional<PokemonInPlay> subject() {
        return Optional.empty();
    }

    /**
     * The side this event is chiefly about.
     *
     * <p>Derived from the subject's owner by default, which is right for
     * everything that happens to a Pokemon. Turn and card-play events override
     * it, since they concern a side with no single Pokemon at their centre.
     */
    default Optional<Side> side() {
        return subject().map(PokemonInPlay::owner);
    }
}
