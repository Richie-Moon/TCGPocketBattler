package com.tcgpocket.effect;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;

/**
 * Puts a chosen matching Basic Pokemon from a discard pile onto its owner's
 * Bench — Pokémon Flute's "put a Basic Pokemon from your opponent's discard
 * pile onto their Bench".
 *
 * <p>The sibling of {@link BenchFromDeck}, but chosen rather than random: a
 * discard pile is face up, so the controller picks, whichever side's pile it
 * is. The same Bench rules apply — only a Basic, and a full Bench stops it.
 *
 * <p>A full Bench or nothing to find is a {@link EffectOutcome#NO_OP}.
 */
public record BenchFromDiscard(ISideTarget side, ICondition<CardInstance> matching) implements IEffect {

    public BenchFromDiscard {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(matching, "matching");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side owner = side.resolve(context);
        if (owner.benchIsFull()) {
            return EffectOutcome.NO_OP;
        }

        List<CardInstance> candidates = owner.discardPile().stream()
                .filter(card -> card.definition() instanceof PokemonCard pokemon && pokemon.isBasic())
                .filter(matching::evaluate)
                .toList();
        if (candidates.isEmpty()) {
            return EffectOutcome.NO_OP;
        }

        CardInstance chosen = context.controller().player().choose(
                new Decision<>("Choose a Pokemon to put on the Bench", candidates, context.controller(), context));
        owner.removeFromDiscard(chosen);
        owner.addToBench(new PokemonInPlay(chosen.instanceId(), (PokemonCard) chosen.definition(),
                owner, Zone.BENCH, context.battle().turn()));
        return EffectOutcome.APPLIED;
    }
}
