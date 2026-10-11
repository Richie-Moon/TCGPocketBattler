package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A group without one Pokemon in it — "your <em>other</em> Pokemon", usually
 * {@code new Except(new AttackerAll(), new Self())}.
 *
 * <p>Not a {@link Matching}: a condition sees only the Pokemon it is asked
 * about, never the resolving context, so it cannot ask "is this me?".
 * An excluded target that does not resolve leaves the group whole.
 */
public record Except(IMultiTarget from, ITarget excluded) implements IMultiTarget {

    public Except {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(excluded, "excluded");
    }

    @Override
    public List<PokemonInPlay> resolve(ResolutionContext context) {
        Optional<PokemonInPlay> left = excluded.resolve(context);
        return from.resolve(context).stream()
                .filter(pokemon -> left.isEmpty() || pokemon != left.get())
                .toList();
    }
}
