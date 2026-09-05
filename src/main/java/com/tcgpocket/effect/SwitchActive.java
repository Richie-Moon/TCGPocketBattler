package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;
import com.tcgpocket.target.ITarget;

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

    private Optional<PokemonInPlay> chooseReplacement(ResolutionContext context, Side target) {
        if (replacement.isPresent()) {
            return replacement.get().resolve(context);
        }
        // TODO: this is a decision for that side's IPlayer. Until the agent
        //       seam exists, take the first benched Pokemon so the effect is
        //       usable and testable rather than dead.
        return Optional.of(target.bench().get(0));
    }
}
