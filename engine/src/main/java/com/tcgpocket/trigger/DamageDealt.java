package com.tcgpocket.trigger;

import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/**
 * Damage landed on a Pokemon.
 *
 * <p>The subject is the Pokemon that <em>took</em> it, not the one that dealt
 * it, so that "when this Pokemon is damaged, ..." is the natural reading of
 * {@code EventConcerns(new Self())}.
 *
 * @param source who dealt it; empty for damage with no attacker, such as
 *               poison or burn between turns
 * @param amount damage actually applied, after weakness and modifiers
 */
public record DamageDealt(
        Optional<PokemonInPlay> source,
        PokemonInPlay target,
        int amount) implements GameEvent {

    public DamageDealt {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(target, "target");
    }

    @Override
    public Optional<PokemonInPlay> subject() {
        return Optional.of(target);
    }
}
