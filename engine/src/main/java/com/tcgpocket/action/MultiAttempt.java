package com.tcgpocket.action;

import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.EffectOutcome;
import com.tcgpocket.effect.IAttempt;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Several attempts made independently.
 *
 * <p>The contrast with {@code Attempt} is the point: inside one attempt a
 * failure aborts what follows, because the later effects depended on the
 * earlier ones. Here each attempt is its own proposition, so one failing does
 * not stop the next — "does 20 damage to each of 2 different Pokemon" should
 * still hit the second when the first is not there.
 *
 * <p>Reports success if any attempt succeeded.
 */
public record MultiAttempt(List<IAttempt> attempts) implements IAction {

    public MultiAttempt {
        attempts = List.copyOf(attempts);
    }

    public MultiAttempt(IAttempt... attempts) {
        this(List.of(attempts));
    }

    @Override
    public boolean isLegal(ResolutionContext context) {
        return true;
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        List<EffectOutcome> combined = new ArrayList<>();
        boolean anySucceeded = false;

        for (IAttempt attempt : attempts) {
            AttemptResult result = attempt.execute(context);
            combined.addAll(result.outcomes());
            anySucceeded |= result.succeeded();
        }

        return anySucceeded
                ? AttemptResult.success(combined)
                : AttemptResult.failure("every attempt failed", combined);
    }
}
