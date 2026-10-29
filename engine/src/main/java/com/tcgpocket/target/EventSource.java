package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.trigger.DamageDealt;
import com.tcgpocket.trigger.DamageIncoming;
import com.tcgpocket.trigger.Knockout;

import java.util.Optional;

/**
 * The Pokemon that dealt (or is about to deal) the damage the resolving
 * trigger is answering, or the knockout — the Attacking Pokemon.
 *
 * <p>What lets a card retaliate against whatever just hit it (Rough Skin) or
 * knocked it out (Innards Out), or asking who is about to hit it (Safeguard). Empty outside a trigger, for other events, and
 * for damage or knockouts nobody dealt (poison, burn), so a retaliating effect
 * fails there instead of hitting some bystander.
 */
public record EventSource() implements ITarget {

    @Override
    public Optional<PokemonInPlay> resolve(ResolutionContext context) {
        return context.event().flatMap(event ->
                switch (event) {
            case DamageDealt dealt -> dealt.source();
            case DamageIncoming incoming -> incoming.source();
            case Knockout knockout -> knockout.by();
            default -> Optional.empty();
        });
    }
}
