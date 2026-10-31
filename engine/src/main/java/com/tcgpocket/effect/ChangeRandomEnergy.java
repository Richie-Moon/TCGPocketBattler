package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Changes one random attached Energy to one of {@code types} at random —
 * Smeargle's Splatter Coating.
 *
 * <p>Nothing is attached, so {@code EnergyAttached} does not fire. The new type
 * may be the old one, as the card's list includes every type. With no Energy
 * attached it is a {@link EffectOutcome#NO_OP}.
 */
public record ChangeRandomEnergy(ITarget target, List<Type> types) implements IEffect {

    public ChangeRandomEnergy {
        Objects.requireNonNull(target, "target");
        types = List.copyOf(types);
        if (types.isEmpty()) {
            throw new IllegalArgumentException("ChangeRandomEnergy needs at least one type");
        }
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        if (context.shields(resolved.get())) {
            return EffectOutcome.PREVENTED;
        }

        PokemonInPlay pokemon = resolved.get();
        List<Type> units = Energies.asUnits(pokemon);
        if (units.isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        pokemon.detachEnergy(units.get(context.battle().rng().nextInt(units.size())), 1);
        pokemon.attachEnergy(types.get(context.battle().rng().nextInt(types.size())), 1);
        return EffectOutcome.APPLIED;
    }
}
