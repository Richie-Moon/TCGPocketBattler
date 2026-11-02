package com.tcgpocket.condition;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.EventSource;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Whether a particular Pokemon dealt the damage or knockout being handled —
 * Zoroark's "Knocked Out by damage from this Pokémon's attacks".
 *
 * <p>The other end of the event from {@link EventConcerns}. False outside a
 * trigger, and for damage or knockouts nobody dealt.
 */
public record EventSourceIs(ITarget target) implements ICondition<ResolutionContext> {

    public EventSourceIs {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        Optional<PokemonInPlay> source = new EventSource().resolve(context);
        Optional<PokemonInPlay> wanted = target.resolve(context);
        return source.isPresent() && wanted.isPresent() && source.get() == wanted.get();
    }
}
