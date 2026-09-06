package com.tcgpocket.trigger;

import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/**
 * A Pokemon has been knocked out.
 *
 * <p>Not yet dispatched: the knockout <em>procedure</em> — points, discarding
 * the evolution stack, and the replacement the controller has to choose — is
 * turn-engine work, and lands with {@code TurnEngine.checkKnockouts()}. The
 * event exists now so that cards which listen for it can be written today.
 *
 * @param by who dealt the finishing blow; empty for poison, burn or self-damage
 */
public record Knockout(PokemonInPlay knockedOut, Optional<PokemonInPlay> by) implements GameEvent {

    public Knockout {
        Objects.requireNonNull(knockedOut, "knockedOut");
        Objects.requireNonNull(by, "by");
    }

    @Override
    public Optional<PokemonInPlay> subject() {
        return Optional.of(knockedOut);
    }
}
