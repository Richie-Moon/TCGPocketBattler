package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.IMultiTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Takes Energy of different types from the Energy Zone and lets the controller
 * place them across a group "in any way you like" — Phoenix Turbo's Fire, Water
 * and Lightning.
 *
 * <p>{@link DistributeEnergy} with the types told apart. One placement is still
 * one {@link Decision}, but an option now says which Energy goes where, so it is
 * a list of {@link EnergyPlacement} rather than of Pokemon, and {@code [A, B]}
 * and {@code [B, A]} are different placements.
 *
 * <p>Fails on an empty group. A group of one is not worth asking about.
 *
 * <p>ponytail: enumerates all n^k placements, 27 for three Energy over a full
 * bench. Revisit only if some card hands out many more Energy at once.
 */
public record DistributeMixedEnergy(List<Type> energies, IMultiTarget among, String prompt) implements IEffect {

    /** One Energy going onto one Pokemon: the part of an option a player reads. */
    public record EnergyPlacement(Type energy, PokemonInPlay pokemon) {
    }

    public DistributeMixedEnergy {
        energies = List.copyOf(energies);
        Objects.requireNonNull(among, "among");
        Objects.requireNonNull(prompt, "prompt");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        List<PokemonInPlay> candidates = among.resolve(context);
        if (candidates.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        List<List<EnergyPlacement>> placements = placements(candidates, 0);
        List<EnergyPlacement> chosen = placements.size() == 1
                ? placements.get(0)
                : context.controller().player().choose(
                        new Decision<>(prompt, placements, context.controller(), context));
        chosen.forEach(placement -> Energies.attach(context, placement.pokemon(), placement.energy(), 1));
        return EffectOutcome.APPLIED;
    }

    private List<List<EnergyPlacement>> placements(List<PokemonInPlay> candidates, int from) {
        if (from == energies.size()) {
            return List.of(List.of());
        }
        List<List<EnergyPlacement>> placements = new ArrayList<>();
        for (PokemonInPlay pokemon : candidates) {
            for (List<EnergyPlacement> rest : placements(candidates, from + 1)) {
                List<EnergyPlacement> placement = new ArrayList<>(rest.size() + 1);
                placement.add(new EnergyPlacement(energies.get(from), pokemon));
                placement.addAll(rest);
                placements.add(List.copyOf(placement));
            }
        }
        return placements;
    }
}
