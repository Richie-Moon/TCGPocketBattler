package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Optional;

/**
 * A bench slot of the side whose turn it is, addressed by index.
 *
 * <p>For when the caller already knows which one — a scripted test, or an
 * agent that has made its choice. Card text that says "choose 1 of your
 * Benched Pokemon" wants {@code ChosenFrom} instead, once {@code IPlayer}
 * exists.
 *
 * <p>The index is the slot, not the position among Benched Pokemon: slots
 * keep their place when a neighbour leaves. An empty slot, or one past the
 * end, resolves to empty rather than throwing.
 */
public record AttackerBenchSpecific(int benchIndex) implements ITarget {

    @Override
    public Optional<PokemonInPlay> resolve(ResolutionContext context) {
        return context.battle().attacker().benchAt(benchIndex);
    }
}
