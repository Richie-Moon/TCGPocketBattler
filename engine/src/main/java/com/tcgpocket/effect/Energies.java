package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.RandomSource;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.trigger.EnergyAttached;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Shared plumbing for the energy effects. Package-private implementation
 * detail, not part of the card-text vocabulary.
 */
final class Energies {

    private Energies() {
    }

    /**
     * The only way energy gets attached, so {@link EnergyAttached} fires for every
     * source — the turn's attachment, an ability, a Trainer, a move. One event per
     * unit, because "whenever an Energy is attached" counts Energy, not effects.
     */
    static void attach(ResolutionContext context, PokemonInPlay pokemon, Type type, int count) {
        attach(context, pokemon, type, count, true);
    }

    /** Energy moved off another Pokemon rather than taken from the Energy Zone. */
    static void attachMoved(ResolutionContext context, PokemonInPlay pokemon, Type type, int count) {
        attach(context, pokemon, type, count, false);
    }

    private static void attach(ResolutionContext context, PokemonInPlay pokemon, Type type, int count, boolean fromEnergyZone) {
        pokemon.attachEnergy(type, count);
        for (int i = 0; i < count; i++) {
            TriggerDispatcher.dispatch(context.battle(), new EnergyAttached(pokemon, type, fromEnergyZone));
        }
    }

    /** Attached energy as one entry per unit, so a unit can be picked at random. */
    static List<Type> asUnits(PokemonInPlay pokemon) {
        List<Type> units = new ArrayList<>();
        for (Map.Entry<Type, Integer> entry : pokemon.attachedEnergy().entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                units.add(entry.getKey());
            }
        }
        return units;
    }

    /**
     * Removes {@code count} energy chosen at random, or nothing at all if there
     * is not enough.
     *
     * <p>All-or-nothing on purpose: a partially paid cost that then reports
     * failure would let an attempt abort with the payment already made.
     *
     * @return the types removed, or empty if the cost could not be paid
     */
    static List<Type> takeRandom(PokemonInPlay pokemon, int count, RandomSource rng) {
        List<Type> units = asUnits(pokemon);
        if (count < 0 || units.size() < count) {
            return List.of();
        }

        List<Type> taken = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Type type = units.remove(rng.nextInt(units.size()));
            pokemon.detachEnergy(type, 1);
            taken.add(type);
        }
        return taken;
    }

    /**
     * Removes all attached energy.
     * @return the types removed, or empty if there was no attached energy
     */
    static List<Type> takeAll(PokemonInPlay pokemon) {
        List<Type> units = asUnits(pokemon);
        if (units.isEmpty()) {
            return List.of();
        }
        for (Type type : units) {
            pokemon.discardEnergy(type, 1);
        }
        return units;
    }
}
