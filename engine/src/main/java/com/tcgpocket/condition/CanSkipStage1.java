package com.tcgpocket.condition;

import com.tcgpocket.card.EvolutionLock;
import com.tcgpocket.effect.EvolveSkippingStage1;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;

/**
 * Whether Rare Candy could evolve the target right now: not on its owner's first
 * turn, a Basic that was not put into play this turn, not under an
 * {@link EvolutionLock}, whose owner holds a Stage 2 that evolves from it.
 *
 * <p>Context-level rather than on the Pokemon because "this turn" and the
 * opponent's locks are only known to the context. The same rules
 * {@code EvolveAction} applies, less its "a Stage 1 in hand".
 */
public record CanSkipStage1(ITarget target) implements ICondition<ResolutionContext> {

    public CanSkipStage1 {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public boolean evaluate(ResolutionContext context) {
        return target.resolve(context).filter(basic -> canEvolve(basic, context)).isPresent();
    }

    private static boolean canEvolve(PokemonInPlay basic, ResolutionContext context) {
        boolean locked = context.opponent().standingRules().stream()
                .anyMatch(held -> held.rule() instanceof EvolutionLock lock && lock.appliesTo().evaluate(basic));
        return !locked
                && basic.owner().hasTakenATurn()
                && basic.turnPlayed() < context.battle().turn()
                && !EvolveSkippingStage1.stage2sInHand(basic).isEmpty();
    }
}
