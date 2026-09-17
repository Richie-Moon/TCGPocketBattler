package com.tcgpocket.condition;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;
import com.tcgpocket.trigger.GameEvent;

import java.util.Objects;
import java.util.Optional;

/**
 * Whether the event being handled is about a particular Pokemon.
 *
 * <p>The gate almost every trigger needs. A board-wide dispatch reaches every
 * Tool in play, and a Cape that heals its wearer must fire only when its wearer
 * was the one that took the hit — {@code new EventConcerns(new Self())}.
 *
 * <p>False outside a trigger, and false for events that are about a side rather
 * than a Pokemon.
 */
public record EventConcerns(ITarget target) implements ICondition<ResolutionContext> {

    public EventConcerns {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        Optional<PokemonInPlay> subject = context.event().flatMap(GameEvent::subject);
        Optional<PokemonInPlay> wanted = target.resolve(context);
        return subject.isPresent() && wanted.isPresent() && subject.get() == wanted.get();
    }
}
