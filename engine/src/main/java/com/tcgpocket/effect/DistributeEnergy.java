package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.number.INumber;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.IMultiTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Hands a player several Energy at once and lets them place them across a
 * group "in any way you like" — Inferno Dance.
 *
 * <p>Not {@code RepeatEffect} over a {@code ChosenFrom}: that asks one question
 * per Energy, which is the wrong shape both for the player (three prompts, no
 * way to change your mind about the first once you have seen the third) and for
 * anything reading the decision — an interface showing all three Energy at once
 * with a confirm button, or a search that wants to score a whole placement.
 * <b>One placement is one {@link Decision}.</b>
 *
 * <p>The options are every allocation of {@code amount} Energy over the group,
 * as a list holding one entry per Energy: {@code [A, A, B]} means two on A and
 * one on B. Order within an option carries no meaning, so the same multiset
 * never appears twice and a player cannot pick between two spellings of the
 * same placement.
 *
 * <p>Fails on an empty group, since there is nowhere to put them; an amount of
 * zero is a {@link EffectOutcome#NO_OP} and asks nothing.
 */
public record DistributeEnergy(
        Type energyType, INumber amount, IMultiTarget among, String prompt) implements IEffect {

    public DistributeEnergy {
        Objects.requireNonNull(energyType, "energyType");
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(among, "among");
        Objects.requireNonNull(prompt, "prompt");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        List<PokemonInPlay> candidates = among.resolve(context);
        if (candidates.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        int count = amount.evaluate(context);
        if (count <= 0) {
            return EffectOutcome.NO_OP;
        }

        List<List<PokemonInPlay>> placements = placements(candidates, count);
        List<PokemonInPlay> chosen = placements.size() == 1
                ? placements.get(0)
                : ask(context, placements);

        chosen.forEach(pokemon -> pokemon.attachEnergy(energyType, 1));
        return EffectOutcome.APPLIED;
    }

    private List<PokemonInPlay> ask(ResolutionContext context, List<List<PokemonInPlay>> placements) {
        Side chooser = context.controller();
        return chooser.player().choose(new Decision<>(prompt, placements, chooser, context));
    }

    /**
     * Every way to place {@code count} Energy on {@code candidates}, as
     * non-decreasing index sequences so that each multiset appears once.
     *
     * <p>ponytail: enumerates them all rather than modelling a placement
     * lazily. There are C(n+count-1, count) of them — ten, for three Energy
     * over a full bench — and enumerating keeps this a plain
     * {@code Decision<T>}, so {@code RandomPlayer} and {@code ScriptedPlayer}
     * need no special case. Revisit only if some format lifts the bench cap.
     */
    private static List<List<PokemonInPlay>> placements(List<PokemonInPlay> candidates, int count) {
        if (count == 0) {
            return List.of(List.of());
        }

        List<List<PokemonInPlay>> placements = new ArrayList<>();
        for (int i = 0; i < candidates.size(); i++) {
            PokemonInPlay first = candidates.get(i);
            for (List<PokemonInPlay> rest : placements(candidates.subList(i, candidates.size()), count - 1)) {
                List<PokemonInPlay> placement = new ArrayList<>(rest.size() + 1);
                placement.add(first);
                placement.addAll(rest);
                placements.add(List.copyOf(placement));
            }
        }
        return placements;
    }
}
