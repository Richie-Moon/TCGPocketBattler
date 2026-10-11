package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.number.INumber;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Moves energy from one Pokemon to another, preserving its types.
 *
 * <p>Fails if either end does not resolve or the source has too little, and in
 * that case moves nothing.
 *
 * @param ofType moves only that type — Vaporeon's "move a Water Energy"; empty
 *               lets the source's owner choose the type, as in Dawn's "move an Energy"
 */
public record MoveEnergy(ITarget from, ITarget to, INumber energyCount, Optional<Type> ofType) implements IEffect {

    public MoveEnergy {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        Objects.requireNonNull(energyCount, "energyCount");
        Objects.requireNonNull(ofType, "ofType");
    }

    public MoveEnergy(ITarget from, ITarget to, INumber energyCount) {
        this(from, to, energyCount, Optional.empty());
    }

    public MoveEnergy(ITarget from, ITarget to, INumber energyCount, Type ofType) {
        this(from, to, energyCount, Optional.of(ofType));
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> source = from.resolve(context);
        Optional<PokemonInPlay> destination = to.resolve(context);
        if (source.isEmpty() || destination.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        if (context.shields(source.get())) {
            return EffectOutcome.PREVENTED;
        }

        int count = energyCount.evaluate(context);
        if (count <= 0) {
            return EffectOutcome.NO_OP;
        }

        List<Type> moved = ofType
                .map(type -> source.get().energyOf(type) < count
                        ? List.<Type>of()
                        : Collections.nCopies(source.get().detachEnergy(type, count), type))
                .orElseGet(() -> takeChosen(source.get(), count, context));
        if (moved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        moved.forEach(type -> Energies.attachMoved(context, destination.get(), type, 1));
        return EffectOutcome.APPLIED;
    }

    /**
     * The source's owner picks the type of each unit, asked only when more
     * than one type is attached. All-or-nothing, like {@link Energies#takeRandom}.
     */
    private static List<Type> takeChosen(PokemonInPlay source, int count, ResolutionContext context) {
        if (source.totalEnergy() < count) {
            return List.of();
        }
        List<Type> taken = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            List<Type> types = List.copyOf(source.attachedEnergy().keySet());
            Type type = types.size() == 1 ? types.getFirst() : source.owner().player().choose(
                    new Decision<>("Choose an Energy to move.", types, source.owner(), context));
            source.detachEnergy(type, 1);
            taken.add(type);
        }
        return taken;
    }
}
