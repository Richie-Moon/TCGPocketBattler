package com.tcgpocket.action;

import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.IAttempt;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * A printed action with no cost of its own: an attempt, and nothing else.
 *
 * <p>The counterpart to {@link Action}, which is an attack and so carries an
 * {@link com.tcgpocket.energy.EnergyCost} and the rules about being Active and
 * awake. This one carries neither, which is why it serves both a Trainer card
 * and an {@code ActivatedAbility} — two things that look unrelated but are
 * the same shape once the gating is taken out of them.
 *
 * <p>And the gating is always somewhere else. Whether a Trainer may be played
 * at all (in hand, one Supporter a turn) is {@link PlayCardAction}'s business;
 * whether an ability may be used (once a turn, {@code usableWhen}) is
 * {@link UseAbilityAction}'s. Hence {@link #isLegal} being unconditionally
 * true: this is the payload, not the permission.
 */
public record PlainAction(String description, IAttempt attempt) implements IAction {

    public PlainAction {
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
