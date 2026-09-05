package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.RandomSource;
import com.tcgpocket.state.PokemonInPlay;

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
            pokemon.discardEnergy(type, 1);
            taken.add(type);
        }
        return taken;
    }
}
