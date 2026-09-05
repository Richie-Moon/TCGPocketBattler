package com.tcgpocket.action;

import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.List;

/**
 * Passes.
 *
 * <p>Always offered, so a player is never stuck without a legal move — which
 * matters for a random agent that has run out of anything useful to do.
 */
public record EndTurnAction() implements IAction {

    @Override
    public boolean isLegal(ResolutionContext context) {
        return true;
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        return AttemptResult.success(List.of());
    }
}
