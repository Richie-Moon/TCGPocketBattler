package com.tcgpocket.trigger;

import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.resolve.ResolutionContext;

/**
 * A standing reaction to something happening on the board.
 *
 * <p>This is the hierarchy that lets a Tool, a Stadium, a passive ability and a
 * status condition all be pure data. None of them needs a hook in the engine:
 * they simply supply triggers, and {@link TriggerDispatcher} finds them.
 *
 * <p>Note that the <em>events</em> are the sealed hierarchy here, not the
 * triggers. A trigger is one shape — "when this happens, if that holds, do
 * this" — so a single record covers every card in the game. It is the events
 * that vary, because they carry different payloads.
 */
public sealed interface ITrigger permits Trigger {

    /**
     * Whether this trigger listens for that kind of event.
     *
     * <p>A type test only. The trigger's own condition — which may look at the
     * whole board — is checked by {@link #fire}, because it needs a context.
     */
    boolean appliesTo(GameEvent event);

    /**
     * Runs the trigger's attempt, if its condition holds.
     *
     * <p>The context arrives with {@code source} set to the trigger's holder
     * and {@code event} set to what was dispatched, so {@code Self},
     * {@code EventConcerns} and {@code EventDamage} all resolve against the
     * right things.
     *
     * <p>Returns an empty success when the condition does not hold: a trigger
     * that declines to fire has neither changed anything nor failed.
     */
    AttemptResult fire(ResolutionContext context);
}
