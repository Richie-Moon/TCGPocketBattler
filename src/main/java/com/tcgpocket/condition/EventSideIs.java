package com.tcgpocket.condition;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;
import com.tcgpocket.trigger.GameEvent;

import java.util.Objects;
import java.util.Optional;

/**
 * Whether the event being handled belongs to a particular side.
 *
 * <p>Written for {@code TurnEnd}, where "whose turn just ended" is the entire
 * question: Paralysis wears off at the end of its victim's own turn, so it
 * asks {@code new EventSideIs(new SelfSide())}, while Poison ticks at every
 * turn end and asks nothing.
 *
 * <p>False outside a trigger.
 */
public record EventSideIs(ISideTarget side) implements ICondition<ResolutionContext> {

    public EventSideIs {
        Objects.requireNonNull(side, "side");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        Optional<Side> eventSide = context.event().flatMap(GameEvent::side);
        return eventSide.isPresent() && eventSide.get() == side.resolve(context);
    }
}
