package com.tcgpocket.trigger;

import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/**
 * Attack damage is about to land on a Pokemon — Bastiodon's "if any damage is
 * done to this Pokemon by attacks, flip a coin".
 *
 * <p>Dispatched after the pipeline has worked out that more than 0 would land
 * and before it lands, then the damage is worked out again, so a modifier a
 * trigger installs here ({@code ReduceDamageTaken} with duration 0) counts.
 * Only for attack damage: bench, spread and between-turns damage skip the
 * modifiers this exists to set, so there is nothing to react with.
 *
 * <p>The subject is the Pokemon about to take it, as for {@link DamageDealt}.
 */
public record DamageIncoming(Optional<PokemonInPlay> source, PokemonInPlay target) implements GameEvent {

    public DamageIncoming {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(target, "target");
    }

    @Override
    public Optional<PokemonInPlay> subject() {
        return Optional.of(target);
    }
}
