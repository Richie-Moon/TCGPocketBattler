package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.number.INumber;
import com.tcgpocket.number.Literal;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Attaches energy of a given type that card text conjures — "take a Fire
 * Energy from your Energy Zone and attach it", where the zone supplies any
 * type on demand.
 *
 * <p>Does not spend the zone's current Energy or the once-per-turn attachment
 * limit; see {@link AttachFromEnergyZone} for the ordinary turn action. Counts
 * as from the Energy Zone for {@code EnergyAttached}; a future "from your
 * discard pile" card would need its own flag there.
 */
public record AttachEnergy(Type energyType, INumber energyCount, ITarget target) implements IEffect {

    public AttachEnergy {
        Objects.requireNonNull(energyType, "energyType");
        Objects.requireNonNull(energyCount, "energyCount");
        Objects.requireNonNull(target, "target");
    }

    public AttachEnergy(Type energyType, ITarget target) {
        this(energyType, new Literal(1), target);
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        int count = energyCount.evaluate(context);
        if (count <= 0) {
            return EffectOutcome.NO_OP;
        }

        Energies.attach(context, resolved.get(), energyType, count);
        return EffectOutcome.APPLIED;
    }
}
