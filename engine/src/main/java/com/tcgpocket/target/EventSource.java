package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.trigger.DamageDealt;

import java.util.Optional;

/**
 * The Pokemon that dealt the damage the resolving trigger is answering.
 *
 * <p>What lets a card retaliate against whatever just hit it. Empty outside a
 * trigger, for events other than {@link DamageDealt}, and for damage nobody
 * dealt (poison, burn), so a retaliating effect fails there instead of hitting
 * some bystander.
 */
public record EventSource() implements ITarget {

    @Override
    public Optional<PokemonInPlay> resolve(ResolutionContext context) {
        return context.event().flatMap(event ->
                event instanceof DamageDealt dealt ? dealt.source() : Optional.empty());
    }
}
