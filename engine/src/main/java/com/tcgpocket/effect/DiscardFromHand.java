package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.number.INumber;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Discards cards from a hand, optionally only those matching a condition.
 *
 * <p>A cost, so it is all-or-nothing: too few matching cards discards nothing
 * and fails.
 *
 * <p>The hand's owner picks which cards go, since it is their hand being
 * paid from. Asked only when there is a real choice.
 *
 * @param matching counts every card in hand when empty
 */
public record DiscardFromHand(
        ISideTarget side,
        INumber cardCount,
        Optional<ICondition<CardInstance>> matching) implements IEffect {

    public DiscardFromHand {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(cardCount, "cardCount");
        Objects.requireNonNull(matching, "matching");
    }

    public DiscardFromHand(ISideTarget side, INumber cardCount) {
        this(side, cardCount, Optional.empty());
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int count = cardCount.evaluate(context);
        if (count <= 0) {
            return EffectOutcome.NO_OP;
        }

        Side target = side.resolve(context);
        List<CardInstance> eligible = target.hand().stream()
                .filter(card -> matching.map(condition -> condition.evaluate(card)).orElse(true))
                .toList();

        if (eligible.size() < count) {
            return EffectOutcome.FAILED;
        }

        List<List<CardInstance>> choices = subsets(eligible, count);
        List<CardInstance> chosen = choices.size() == 1 ? choices.get(0) : target.player().choose(
                new Decision<>(count == 1 ? "Choose a card to discard." : "Choose " + count + " cards to discard.",
                        choices, target, context));
        for (CardInstance card : chosen) {
            target.removeFromHand(card);
            target.addToDiscard(card);
        }
        return EffectOutcome.APPLIED;
    }

    /**
     * Every way to pick {@code count} of {@code cards}, in hand order.
     *
     * <p>ponytail: enumerated rather than asked one card at a time, the same
     * trade as {@link DistributeEnergy}: a plain {@code Decision<T>} needs no
     * special case in any player. C(10, 2) is 45, so it stays small.
     */
    private static List<List<CardInstance>> subsets(List<CardInstance> cards, int count) {
        if (count == 0) {
            return List.of(List.of());
        }
        List<List<CardInstance>> subsets = new ArrayList<>();
        for (int i = 0; i <= cards.size() - count; i++) {
            for (List<CardInstance> rest : subsets(cards.subList(i + 1, cards.size()), count - 1)) {
                List<CardInstance> subset = new ArrayList<>(count);
                subset.add(cards.get(i));
                subset.addAll(rest);
                subsets.add(List.copyOf(subset));
            }
        }
        return subsets;
    }
}
