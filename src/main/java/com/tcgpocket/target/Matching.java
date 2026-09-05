package com.tcgpocket.target;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.List;
import java.util.Objects;

/**
 * Narrows a group — "each of your Lightning Pokemon".
 *
 * <p>Reuses the card-level conditions rather than defining a parallel
 * vocabulary of filters, which is why {@code ICondition} is generic over its
 * subject.
 */
public record Matching(IMultiTarget from, ICondition<PokemonInPlay> condition) implements IMultiTarget {

    public Matching {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(condition, "condition");
    }

    @Override
    public List<PokemonInPlay> resolve(ResolutionContext context) {
        return from.resolve(context).stream()
                .filter(condition::evaluate)
                .toList();
    }
}
