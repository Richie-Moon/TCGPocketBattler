package com.tcgpocket.condition;

import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * Whether a particular Stadium is the one currently in play.
 *
 * <p>Matches on the printed card's id rather than its name, so two Stadiums
 * sharing a name across sets stay distinguishable.
 */
public record StadiumInPlay(String cardId) implements ICondition<ResolutionContext> {

    public StadiumInPlay {
        Objects.requireNonNull(cardId, "cardId");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        return context.battle().stadium()
                .map(stadium -> stadium.definition().id().equals(cardId))
                .orElse(false);
    }
}
