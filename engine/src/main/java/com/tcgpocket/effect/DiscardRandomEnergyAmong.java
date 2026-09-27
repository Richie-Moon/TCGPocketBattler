package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.IMultiTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Discards energy chosen at random from among everything attached to a group —
 * Gyarados ex's "a random Energy from among the Energy attached to all
 * Pokemon".
 *
 * <p>Not {@code DiscardRandomEnergy} over a {@code RandomFrom}: that picks a
 * Pokemon first, so one carrying a single Energy would be as likely to lose it
 * as one carrying five. Here every attached unit is pooled before the draw.
 *
 * <p>All-or-nothing like its single-target sibling: fails, discarding nothing,
 * if the group holds fewer than {@code energyCount}. Energy on a Pokemon
 * shielded from this attack's effects is left out of the pool.
 */
public record DiscardRandomEnergyAmong(INumber energyCount, IMultiTarget from) implements IEffect {

    public DiscardRandomEnergyAmong {
        Objects.requireNonNull(energyCount, "energyCount");
        Objects.requireNonNull(from, "from");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int count = energyCount.evaluate(context);
        if (count <= 0) {
            return EffectOutcome.NO_OP;
        }
        List<Map.Entry<PokemonInPlay, Type>> units = new ArrayList<>();
        for (PokemonInPlay pokemon : from.resolve(context)) {
            if (!context.shields(pokemon)) {
                Energies.asUnits(pokemon).forEach(type -> units.add(Map.entry(pokemon, type)));
            }
        }
        if (units.size() < count) {
            return EffectOutcome.FAILED;
        }
        for (int i = 0; i < count; i++) {
            Map.Entry<PokemonInPlay, Type> unit = units.remove(context.battle().rng().nextInt(units.size()));
            unit.getKey().discardEnergy(unit.getValue(), 1);
        }
        return EffectOutcome.APPLIED;
    }
}
