package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/**
 * Makes a player's attacks cheaper by Colorless, for a number of turns —
 * Barry's "during this turn, attacks used by your Snorlax, Heracross, and
 * Staraptor cost 2 less Colorless Energy".
 *
 * <p>The side-level opposite of {@link IncreaseAttackCost}, stamped on the
 * controller's {@code Side} for the reason {@link IncreaseSideDamage} is: the
 * text covers whichever matching Pokemon attacks, including one that switches
 * in after the card was played. {@code Action.isLegal} reads it, and a cost
 * never drops below its typed part.
 *
 * @param matching narrows which attackers benefit; empty covers all of them
 */
public record ReduceSideAttackCost(
        INumber reductionAmount,
        Optional<ICondition<? super PokemonInPlay>> matching,
        INumber duration) implements IDurationEffect {

    public ReduceSideAttackCost {
        Objects.requireNonNull(reductionAmount, "reductionAmount");
        Objects.requireNonNull(matching, "matching");
        Objects.requireNonNull(duration, "duration");
    }

    /** Only the controller's Pokemon that match. */
    public ReduceSideAttackCost(INumber reductionAmount, ICondition<? super PokemonInPlay> matching, INumber duration) {
        this(reductionAmount, Optional.of(matching), duration);
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int amount = reductionAmount.evaluate(context);
        if (amount <= 0) {
            return EffectOutcome.NO_OP;
        }
        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        context.controller().addAttackCostReduction(
                amount,
                pokemon -> matching.map(condition -> condition.evaluate(pokemon)).orElse(true),
                expiry);
        return EffectOutcome.APPLIED;
    }
}
