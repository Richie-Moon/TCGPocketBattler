package com.tcgpocket.trigger;

import com.tcgpocket.effect.AttemptResult;

import java.util.List;
import java.util.Objects;

/**
 * What came of dispatching one event.
 *
 * @param results one entry per trigger that listened for this event type, in
 *                dispatch order. A trigger whose condition did not hold still
 *                appears, as an empty success.
 */
public record DispatchResult(GameEvent event, List<AttemptResult> results) {

    public DispatchResult {
        Objects.requireNonNull(event, "event");
        results = List.copyOf(results);
    }

    public static DispatchResult nothing(GameEvent event) {
        return new DispatchResult(event, List.of());
    }

    /**
     * Whether a trigger cancelled whatever was about to happen.
     *
     * <p>A failed trigger attempt is a veto. Only the caller that dispatched a
     * <em>cancellable</em> event asks — today that is {@code AttackDeclared}
     * alone, which is how Confusion works. Everywhere else the flag is ignored,
     * so an incidental failure costs nothing.
     *
     * <p>The consequence, worth knowing when writing a card: a trigger on
     * {@code AttackDeclared} must not fail by accident. Reach for
     * {@code ConditionalEffect} over an effect whose target might not resolve,
     * or the attack goes away for the wrong reason.
     */
    public boolean vetoed() {
        return results.stream().anyMatch(AttemptResult::failed);
    }

    /** How many triggers listened for this event. */
    public int fired() {
        return results.size();
    }

    public boolean changedTheBoard() {
        return results.stream().anyMatch(AttemptResult::changedTheBoard);
    }
}
