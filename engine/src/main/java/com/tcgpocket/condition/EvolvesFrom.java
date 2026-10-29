package com.tcgpocket.condition;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.state.CardInstance;

import java.util.Objects;
import java.util.Optional;

/**
 * Whether a card evolves from a particular species — Rockruff's "a random card
 * that evolves from Rockruff".
 *
 * <p>Not {@link IsSpecies} on a list of names: the card names the species it
 * evolves <em>from</em>, so a new Lycanroc needs no edit here. False for
 * anything that is not a Pokemon, and for a Basic.
 */
public record EvolvesFrom(String species) implements ICondition<CardInstance> {

    public EvolvesFrom {
        Objects.requireNonNull(species, "species");
    }

    @Override
    public boolean evaluate(CardInstance subject) {
        return subject.definition() instanceof PokemonCard pokemon
                && pokemon.evolvesFrom().equals(Optional.of(species));
    }
}
