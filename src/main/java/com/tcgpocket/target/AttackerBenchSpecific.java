package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.List;
import java.util.Optional;

/**
 * A bench slot of the side whose turn it is, addressed by index.
 *
 * <p>For when the caller already knows which one — a scripted test, or an
 * agent that has made its choice. Card text that says "choose 1 of your
 * Benched Pokemon" wants {@code ChosenFrom} instead, once {@code IPlayer}
 * exists.
 *
 * <p>An index past the end of the bench resolves to empty rather than throwing.
 */
public record AttackerBenchSpecific(int benchIndex) implements ITarget {

    @Override
    public Optional<PokemonInPlay> resolve(ResolutionContext context) {
        return at(context.battle().attacker().bench(), benchIndex);
    }

    static Optional<PokemonInPlay> at(List<PokemonInPlay> bench, int index) {
        if (index < 0 || index >= bench.size()) {
            return Optional.empty();
        }
        return Optional.of(bench.get(index));
    }
}
