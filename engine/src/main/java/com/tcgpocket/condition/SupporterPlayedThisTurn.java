package com.tcgpocket.condition;

import com.tcgpocket.resolve.ResolutionContext;

/**
 * Whether the controller has played a Supporter this turn — Silvally's "if you
 * played a Supporter card from your hand during this turn".
 */
public record SupporterPlayedThisTurn() implements ICondition<ResolutionContext> {

    @Override
    public boolean evaluate(ResolutionContext context) {
        return context.controller().supporterPlayedThisTurn();
    }
}
