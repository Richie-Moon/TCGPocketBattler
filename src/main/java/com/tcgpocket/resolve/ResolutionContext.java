package com.tcgpocket.resolve;

import com.tcgpocket.state.Battle;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.trigger.GameEvent;

import java.util.Objects;
import java.util.Optional;

/**
 * Everything a node of card text is evaluated against.
 *
 * <p>This replaces passing a bare {@link Battle} around, because a Battle alone
 * cannot answer three things card text asks constantly:
 *
 * <ul>
 *   <li><b>who is "this"?</b> — a Tool's or Ability's trigger means the Pokemon
 *       it is attached to, which is not the same as whoever is currently
 *       attacking. That is {@link #source()}, and {@code Self} resolves to it.
 *   <li><b>what just happened?</b> — a trigger's attempt needs the payload of
 *       the event that fired it. That is {@link #event()}.
 *   <li><b>what did we just flip?</b> — held in {@link #scope()}, scoped to one
 *       resolution rather than leaking through a field on Battle.
 * </ul>
 *
 * @param controller the side that owns the card being resolved. Differs from
 *                   {@code battle.attacker()} whenever a defender's trigger
 *                   fires during the attacker's turn.
 * @param source     the Pokemon whose text is resolving; empty for a Trainer
 *                   card, which has none
 */
public record ResolutionContext(
        Battle battle,
        Side controller,
        Optional<PokemonInPlay> source,
        Optional<GameEvent> event,
        ResolutionScope scope) {

    public ResolutionContext {
        Objects.requireNonNull(battle, "battle");
        Objects.requireNonNull(controller, "controller");
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(event, "event");
        Objects.requireNonNull(scope, "scope");
    }

    /** A context for a Pokemon's own text, with a fresh scope and no event. */
    public static ResolutionContext of(Battle battle, Side controller, PokemonInPlay source) {
        return new ResolutionContext(
                battle, controller, Optional.of(source), Optional.empty(), new ResolutionScope());
    }

    /** A context with no source Pokemon, for Trainer cards and engine-driven effects. */
    public static ResolutionContext of(Battle battle, Side controller) {
        return new ResolutionContext(
                battle, controller, Optional.empty(), Optional.empty(), new ResolutionScope());
    }

    /** The side opposing whoever controls the resolving card. */
    public Side opponent() {
        return battle.opponentOf(controller);
    }

    /**
     * Re-points the context at a different source, keeping the same scope so
     * coin flips stay visible across the switch.
     */
    public ResolutionContext withSource(PokemonInPlay newSource) {
        return new ResolutionContext(battle, controller, Optional.ofNullable(newSource), event, scope);
    }

    public ResolutionContext withController(Side newController) {
        return new ResolutionContext(battle, newController, source, event, scope);
    }

    public ResolutionContext withEvent(GameEvent newEvent) {
        return new ResolutionContext(battle, controller, source, Optional.ofNullable(newEvent), scope);
    }
}
