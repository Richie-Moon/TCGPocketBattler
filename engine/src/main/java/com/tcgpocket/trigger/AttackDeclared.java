package com.tcgpocket.trigger;

import com.tcgpocket.action.Action;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/**
 * An attack has been declared but has not yet resolved.
 *
 * <p>Dispatched before the attack's own attempt runs, so a trigger that fails
 * here cancels it. That is the whole of Confusion.
 */
public record AttackDeclared(PokemonInPlay attacker, Action action) implements GameEvent {

    public AttackDeclared {
        Objects.requireNonNull(attacker, "attacker");
        Objects.requireNonNull(action, "action");
    }

    @Override
    public Optional<PokemonInPlay> subject() {
        return Optional.of(attacker);
    }
}
