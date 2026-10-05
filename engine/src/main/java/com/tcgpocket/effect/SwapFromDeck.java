package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;

/**
 * Swaps a chosen card in hand with a random matching one from the deck, then
 * shuffles — Pokemon Communication.
 *
 * <p>Fails, moving nothing, unless both the hand and the deck hold a match:
 * half a swap is not what the card says.
 */
public record SwapFromDeck(ISideTarget side, ICondition<CardInstance> matching) implements IEffect {

    public SwapFromDeck {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(matching, "matching");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        List<CardInstance> inHand = target.hand().stream().filter(matching::evaluate).toList();
        List<CardInstance> inDeck = target.deck().stream().filter(matching::evaluate).toList();
        if (inHand.isEmpty() || inDeck.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        CardInstance given = inHand.size() == 1 ? inHand.get(0) : target.player().choose(
                new Decision<>("Choose a card to swap into your deck.", inHand, target, context));
        CardInstance taken = inDeck.get(context.battle().rng().nextInt(inDeck.size()));
        target.removeFromHand(given);
        target.removeFromDeck(taken);
        target.addToHand(taken);
        target.addToDeck(given);
        target.shuffleDeck(context.battle().rng());
        return EffectOutcome.APPLIED;
    }
}
