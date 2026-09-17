package com.tcgpocket.target;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A Pokemon picked at random out of a group — "1 of your Benched Pokemon at
 * random", "1 of your opponent's Pokemon at random".
 *
 * <p>One node over any {@link IMultiTarget} rather than a
 * {@code AttackerBenchRandom}, {@code OpponentRandom}, ... per group: the group
 * is already a node, and {@link Matching} narrows it.
 *
 * <h2>Only inside an effect</h2>
 *
 * <p>Like {@link ChosenFrom}, resolving this is not free: it draws from the
 * battle's {@code RandomSource}. Resolved from {@code IAction.isLegal} it would
 * advance the RNG once per candidate move and break seeded replay.
 *
 * <p>An empty group resolves to empty, which the surrounding effect treats as
 * the failure it is.
 */
public record RandomFrom(IMultiTarget from) implements ITarget {

    public RandomFrom {
        Objects.requireNonNull(from, "from");
    }

    @Override
    public Optional<PokemonInPlay> resolve(ResolutionContext context) {
        List<PokemonInPlay> candidates = from.resolve(context);
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(candidates.get(context.battle().rng().nextInt(candidates.size())));
    }
}
