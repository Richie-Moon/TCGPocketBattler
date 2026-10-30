package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;

/**
 * Shows the controller a random card from a deck, leaving it there — the first
 * half of Penny, whose card {@link UsePickedCard} then uses.
 *
 * <p>Shown as a one-option {@link Decision} rather than recorded on {@code Side}
 * as {@link LookAtTopCards} does: the card is not on top of anything, and the
 * player has to see it before its effect resolves. Keeps the pick in the
 * {@code ResolutionScope}, as a flip is kept for the effect after it. Finding
 * nothing is a {@link EffectOutcome#NO_OP}.
 *
 * @param side     whose deck is looked through
 * @param matching which cards may be picked
 */
public record LookAtRandomCard(ISideTarget side, ICondition<CardInstance> matching) implements IEffect {

    public LookAtRandomCard {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(matching, "matching");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        List<CardInstance> cards = side.resolve(context).deck().stream().filter(matching::evaluate).toList();
        if (cards.isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        CardInstance picked = cards.get(context.battle().rng().nextInt(cards.size()));
        context.scope().recordPickedCard(picked);
        context.controller().player().choose(new Decision<>(
                "You looked at this card.", List.of(picked), context.controller(), context));
        return EffectOutcome.APPLIED;
    }
}
