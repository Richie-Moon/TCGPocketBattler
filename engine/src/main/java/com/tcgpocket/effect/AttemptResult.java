package com.tcgpocket.effect;

import java.util.List;
import java.util.Objects;

/**
 * What came of running an {@link IAttempt}.
 *
 * @param reason   why it failed, or empty when it succeeded
 * @param outcomes one entry per effect that actually ran. On failure the list
 *                 stops at the effect that failed, so its length says how far
 *                 the attempt got — which is what a card-text log or a
 *                 debugging session wants to know.
 */
public record AttemptResult(boolean succeeded, String reason, List<EffectOutcome> outcomes) {

    public AttemptResult {
        Objects.requireNonNull(reason, "reason");
        outcomes = List.copyOf(outcomes);
    }

    public static AttemptResult success(List<EffectOutcome> outcomes) {
        return new AttemptResult(true, "", outcomes);
    }

    public static AttemptResult failure(String reason, List<EffectOutcome> outcomes) {
        return new AttemptResult(false, reason, outcomes);
    }

    public boolean failed() {
        return !succeeded;
    }

    /**
     * Whether anything actually changed.
     *
     * <p>Distinct from {@link #succeeded()}: an attempt whose every effect was
     * a no-op succeeded without touching the board.
     */
    public boolean changedTheBoard() {
        return outcomes.stream().anyMatch(outcome -> outcome == EffectOutcome.APPLIED);
    }

    /** How many effects ran before the attempt stopped. */
    public int stepsRun() {
        return outcomes.size();
    }
}
