package com.tcgpocket.action;

import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.IAttempt;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * What a Trainer card does when played.
 *
 * <p>Always legal in itself — whether the card may be played at all (a
 * Supporter once per turn, a card in hand) is {@link PlayCardAction}'s
 * business, not this one's.
 */
public record TrainerAction(String description, IAttempt attempt) implements IAction {

    public TrainerAction {
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(attempt, "attempt");
    }

    @Override
    public boolean isLegal(ResolutionContext context) {
        return true;
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        return attempt.execute(context);
    }
}
