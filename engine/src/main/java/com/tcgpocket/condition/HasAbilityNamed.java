package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;

/**
 * Whether a Pokemon's Ability has this name — the Unown's "an Ability other
 * than GUARD", which tells two printings of one species apart.
 */
public record HasAbilityNamed(String name) implements ICondition<PokemonInPlay> {

    public HasAbilityNamed {
        Objects.requireNonNull(name, "name");
    }

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.definition().ability().filter(ability -> ability.name().equals(name)).isPresent();
    }
}
