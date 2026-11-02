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
 * A player looks at the top cards of a deck and puts them back in any order —
 * Hiker with their own, Morty with their opponent's.
 *
 * <p>One question per position, top first, and none for the last card. They stay
 * known afterwards, as with {@link LookAtTopCards}. A deck shorter than
 * {@code cardCount} arranges what it has.
 */
public record ArrangeTopCards(INumber cardCount, ISideTarget side, ISideTarget viewer) implements IEffect {

    public ArrangeTopCards {
        Objects.requireNonNull(cardCount, "cardCount");
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(viewer, "viewer");
    }

    /** The deck's owner arranges their own deck. */
    public ArrangeTopCards(INumber cardCount, ISideTarget side) {
        this(cardCount, side, side);
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int count = cardCount.evaluate(context);
        Side target = side.resolve(context);
        if (count <= 0 || target.deck().isEmpty()) {
            return EffectOutcome.NO_OP;
        }

        Side arranger = viewer.resolve(context);
        boolean own = arranger == target;
        show(target, own, count);
        List<CardInstance> remaining = new ArrayList<>(own ? target.seenTopCards() : target.topCardsSeenByOpponent());
        List<CardInstance> arranged = new ArrayList<>(remaining.size());
        while (remaining.size() > 1) {
            CardInstance next = arranger.player().choose(new Decision<>(
                    "Choose card " + (arranged.size() + 1) + " from the top of " + (own ? "your" : "your opponent's") + " deck.",
                    List.copyOf(remaining), arranger, context));
            remaining.remove(next);
            arranged.add(next);
        }
        arranged.addAll(remaining);
        target.replaceTopOfDeck(arranged);
        show(target, own, arranged.size());
        return EffectOutcome.APPLIED;
    }

    private static void show(Side deck, boolean own, int count) {
        if (own) {
            deck.lookAtTopCards(count);
        } else {
            deck.showTopCardsToOpponent(count);
        }
    }
}
