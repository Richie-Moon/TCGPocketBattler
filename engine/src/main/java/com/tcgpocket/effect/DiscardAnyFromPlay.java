package com.tcgpocket.effect;

import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.IMultiTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Lets the controller discard any number of a group from play — Wild Swing's
 * "you may discard any number of your Benched Water Pokémon".
 *
 * <p>One {@link Decision} over every subset, the shape of {@link DistributeEnergy},
 * with the empty subset as the way to decline. The count lands on the scope for
 * {@code PokemonDiscarded} to read. Discarding is {@link DiscardFromPlay}'s, so no
 * points are awarded. An empty group asks nothing and is a {@link EffectOutcome#NO_OP}.
 */
public record DiscardAnyFromPlay(IMultiTarget among, String prompt) implements IEffect {

    public DiscardAnyFromPlay {
        Objects.requireNonNull(among, "among");
        Objects.requireNonNull(prompt, "prompt");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        List<PokemonInPlay> candidates = among.resolve(context);
        if (candidates.isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        List<PokemonInPlay> chosen = context.controller().player().choose(
                new Decision<>(prompt, subsets(candidates), context.controller(), context));
        int discarded = 0;
        for (PokemonInPlay pokemon : chosen) {
            if (ShuffleIntoDeck.leavePlay(context, pokemon, pokemon.owner()::addToDiscard)) {
                discarded++;
            }
        }
        context.scope().recordPokemonDiscarded(discarded);
        return discarded == 0 ? EffectOutcome.NO_OP : EffectOutcome.APPLIED;
    }

    /** Every subset, the empty one first. Eight at most, for a full bench. */
    private static List<List<PokemonInPlay>> subsets(List<PokemonInPlay> candidates) {
        List<List<PokemonInPlay>> subsets = new ArrayList<>();
        for (int mask = 0; mask < 1 << candidates.size(); mask++) {
            List<PokemonInPlay> subset = new ArrayList<>();
            for (int i = 0; i < candidates.size(); i++) {
                if ((mask & 1 << i) != 0) {
                    subset.add(candidates.get(i));
                }
            }
            subsets.add(List.copyOf(subset));
        }
        return subsets;
    }
}
