package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The side's player looks at the top cards of their own deck and puts them back
 * in any order — Hiker.
 *
 * <p>One question per position, top first, and none for the last card. They stay
 * known afterwards, as with {@link LookAtTopCards}. A deck shorter than
 * {@code cardCount} arranges what it has.
 */
public record ArrangeTopCards(INumber cardCount, ISideTarget side) implements IEffect {

    public ArrangeTopCards {
        Objects.requireNonNull(cardCount, "cardCount");
        Objects.requireNonNull(side, "side");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int count = cardCount.evaluate(context);
        Side target = side.resolve(context);
        if (count <= 0 || target.deck().isEmpty()) {
            return EffectOutcome.NO_OP;
        }

        target.lookAtTopCards(count);
        List<CardInstance> remaining = new ArrayList<>(target.seenTopCards());
        List<CardInstance> arranged = new ArrayList<>(remaining.size());
        while (remaining.size() > 1) {
            CardInstance next = target.player().choose(new Decision<>(
                    "Choose card " + (arranged.size() + 1) + " from the top of your deck.", List.copyOf(remaining), target, context));
            remaining.remove(next);
            arranged.add(next);
        }
        arranged.addAll(remaining);
        target.replaceTopOfDeck(arranged);
        target.lookAtTopCards(arranged.size());
        return EffectOutcome.APPLIED;
    }
}
