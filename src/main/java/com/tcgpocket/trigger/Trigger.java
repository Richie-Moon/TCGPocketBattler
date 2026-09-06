package com.tcgpocket.trigger;

import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.IAttempt;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.List;
import java.util.Objects;

/**
 * The one and only trigger shape: listen for an event type, check a condition,
 * run an attempt.
 *
 * @param on         the event class to listen for. A {@code Class} rather than
 *                   an enum tag because it is the payload type the attempt will
 *                   read back through {@code ResolutionContext.event()}, so
 *                   keeping the two in step matters more than avoiding
 *                   reflection on a single {@code isInstance} call.
 * @param condition  a further gate, evaluated with the holder as source. This
 *                   is where "while this is your Active Pokemon" lives.
 * @param onTrigger  what to do. An {@link IAttempt} rather than an
 *                   {@code IEffect} so a trigger can fail as a unit — which is
 *                   how Confusion cancels an attack.
 */
public record Trigger(
        Class<? extends GameEvent> on,
        ICondition<ResolutionContext> condition,
        IAttempt onTrigger) implements ITrigger {

    public Trigger {
        Objects.requireNonNull(on, "on");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(onTrigger, "onTrigger");
    }

    /** Fires on every event of that type, with no further condition. */
    public Trigger(Class<? extends GameEvent> on, IAttempt onTrigger) {
        this(on, new Always<>(), onTrigger);
    }

    @Override
    public boolean appliesTo(GameEvent event) {
        return on.isInstance(event);
    }

    @Override
    public AttemptResult fire(ResolutionContext context) {
        if (!condition.evaluate(context)) {
            return AttemptResult.success(List.of());
        }
        return onTrigger.execute(context);
    }
}
