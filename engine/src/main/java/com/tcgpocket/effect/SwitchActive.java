package com.tcgpocket.effect;

import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;
import com.tcgpocket.target.ITarget;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Swaps a side's active Pokemon with one from its bench.
 *
 * <p>The Pokemon leaving the active spot sheds its statuses and temporary
 * modifiers, which is the rule that makes switching a way out of Paralysis.
 *
 * @param replacement empty means the side's controller picks
 */
public record SwitchActive(ISideTarget side, Optional<ITarget> replacement) implements IEffect {

    public SwitchActive {
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(replacement, "replacement");
    }

    /** The controller of that side chooses who comes up. */
    public SwitchActive(ISideTarget side) {
        this(side, Optional.empty());
    }

    /** A named replacement, which may itself be a {@code ChosenFrom}. */
    public SwitchActive(ISideTarget side, ITarget replacement) {
        this(side, Optional.of(replacement));
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        if (target.bench().isEmpty()) {
            return EffectOutcome.FAILED;
        }

        Optional<PokemonInPlay> incoming = chooseReplacement(context, target);
        if (incoming.isEmpty() || !target.bench().contains(incoming.get())) {
            return EffectOutcome.FAILED;
        }

        PokemonInPlay promoted = incoming.get();
        Optional<PokemonInPlay> outgoing = target.active();

        target.removeFromBench(promoted);
        outgoing.ifPresent(pokemon -> {
            pokemon.clearTemporaryState();
            target.addToBench(pokemon);
        });
        target.setActive(promoted);

        return EffectOutcome.APPLIED;
    }

    /**
     * Who comes up.
     *
     * <p>With no replacement named, it is the switched side's own player who
     * decides — the Sabrina reading, and the default because it is the one the
     * rules fall back to. A card that takes the choice away from them says so
     * by naming a {@code ChosenFrom} whose chooser is somebody else.
     */
    private Optional<PokemonInPlay> chooseReplacement(ResolutionContext context, Side target) {
        if (replacement.isPresent()) {
            return replacement.get().resolve(context);
        }

        List<PokemonInPlay> bench = target.bench();
        if (bench.size() == 1) {
            return Optional.of(bench.get(0));
        }
        return Optional.of(target.player().choose(
                new Decision<>("Choose a Pokemon to bring up", bench, target, context)));
    }
}
