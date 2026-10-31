package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Moves one Energy of any of {@code types} between two Pokemon — Elemental
 * Switch's "move a Fire, Water, or Lightning Energy".
 *
 * <p>Not {@link MoveEnergy}, which names one type or offers all of them. The
 * source's owner picks, asked only when more than one listed type is attached.
 * Fails with none of them attached.
 */
public record MoveEnergyOfTypes(List<Type> types, ITarget from, ITarget to) implements IEffect {

    public MoveEnergyOfTypes {
        types = List.copyOf(types);
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> source = from.resolve(context);
        Optional<PokemonInPlay> destination = to.resolve(context);
        if (source.isEmpty() || destination.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        List<Type> attached = types.stream().filter(type -> source.get().energyOf(type) > 0).toList();
        if (attached.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        Type type = attached.size() == 1 ? attached.getFirst() : source.get().owner().player().choose(
                new Decision<>("Choose an Energy to move.", attached, source.get().owner(), context));
        source.get().detachEnergy(type, 1);
        Energies.attachMoved(context, destination.get(), type, 1);
        return EffectOutcome.APPLIED;
    }
}
