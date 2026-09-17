package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Optional;

/**
 * A bench slot of the side that is not taking its turn, addressed by index.
 *
 * <p>An index past the end of the bench resolves to empty rather than throwing.
 */
public record OpponentBenchSpecific(int benchIndex) implements ITarget {

    @Override
    public Optional<PokemonInPlay> resolve(ResolutionContext context) {
        return AttackerBenchSpecific.at(context.battle().defender().bench(), benchIndex);
    }
}
