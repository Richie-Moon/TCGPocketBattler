package com.tcgpocket.effect;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;

/**
 * Puts one random matching Basic Pokemon from a deck straight onto its owner's
 * Bench — "put 1 random Nidoran♂ from your deck onto your Bench".
 *
 * <p>The sibling of {@link SearchDeck}, not a destination flag on it: landing
 * on the Bench makes the card a {@link PokemonInPlay} and brings the Bench rules
 * with it. Only a Basic may be benched, whatever the condition says, and a full
 * Bench stops it.
 *
 * <p>A full Bench or nothing to find is a {@link EffectOutcome#NO_OP}, for the
 * same reason as in {@code SearchDeck}: a legal nothing-happened must not abort
 * the rest of the card.
 */
public record BenchFromDeck(ISideTarget side, ICondition<CardInstance> matching) implements IEffect {

    public BenchFromDeck {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(matching, "matching");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side searching = side.resolve(context);
        if (searching.benchIsFull()) {
            return EffectOutcome.NO_OP;
        }

        List<CardInstance> candidates = searching.deck().stream()
                .filter(card -> card.definition() instanceof PokemonCard pokemon && pokemon.isBasic())
                .filter(matching::evaluate)
                .toList();
        if (candidates.isEmpty()) {
            return EffectOutcome.NO_OP;
        }

        CardInstance found = candidates.get(context.battle().rng().nextInt(candidates.size()));
        searching.removeFromDeck(found);
        searching.addToBench(new PokemonInPlay(found.instanceId(), (PokemonCard) found.definition(),
                searching, Zone.BENCH, context.battle().turn()));
        return EffectOutcome.APPLIED;
    }
}
