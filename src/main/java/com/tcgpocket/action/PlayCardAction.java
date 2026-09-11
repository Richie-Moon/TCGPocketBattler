package com.tcgpocket.action;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.EffectOutcome;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.Zone;
import com.tcgpocket.trigger.CardPlayed;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.List;
import java.util.Objects;

/**
 * Plays a card from hand.
 *
 * <p>A Basic Pokemon goes to the bench; anything else runs its printed
 * actions and is discarded.
 */
public record PlayCardAction(CardInstance card) implements IAction {

    public PlayCardAction {
        Objects.requireNonNull(card, "card");
    }

    @Override
    public boolean isLegal(ResolutionContext context) {
        Side side = context.controller();
        if (!side.hand().contains(card)) {
            return false;
        }

        if (card.definition() instanceof PokemonCard pokemon) {
            return pokemon.isBasic() && !side.benchIsFull();
        }

        if (card.definition() instanceof SupporterCard) {
            return !side.supporterPlayedThisTurn()
                    && !side.supportersLocked(context.battle().turn());
        }
        return true;
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        if (!isLegal(context)) {
            return AttemptResult.failure("cannot play " + card.definition().name(), List.of());
        }

        Side side = context.controller();
        side.removeFromHand(card);
        TriggerDispatcher.dispatch(context.battle(), new CardPlayed(side, card));

        if (card.definition() instanceof PokemonCard pokemon) {
            side.addToBench(new PokemonInPlay(
                    card.instanceId(), pokemon, side, Zone.BENCH, context.battle().turn()));
            return AttemptResult.success(List.of(EffectOutcome.APPLIED));
        }

        if (card.definition() instanceof SupporterCard) {
            side.markSupporterPlayed();
        }

        AttemptResult last = AttemptResult.success(List.of());
        for (IAction action : card.definition().actions()) {
            last = action.execute(context);
        }
        side.addToDiscard(card);
        return last;
    }
}
