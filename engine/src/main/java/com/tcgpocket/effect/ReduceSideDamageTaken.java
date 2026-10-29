package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/**
 * Softens every attack the opponent's Pokemon make against the controller's
 * Pokemon — Blue's "during your opponent's next turn, all of your Pokemon take
 * −10 damage from attacks from your opponent's Pokemon".
 *
 * <p>Not {@link ReduceDamageTaken}, for the reason {@link IncreaseSideDamage}
 * is not {@link IncreaseDamage}: the text names no Pokemon, so it stamps the
 * controller's {@code Side} and {@code DamageCalculator} reads it for whichever
 * Pokemon is hit.
 *
 * @param matching narrows which of the controller's Pokemon are covered —
 *                 Adaman's "all of your Metal Pokémon"; empty covers all of them
 */
public record ReduceSideDamageTaken(
        INumber reductionAmount,
        Optional<ICondition<? super PokemonInPlay>> matching,
        INumber duration) implements IDurationEffect {

    public ReduceSideDamageTaken {
        Objects.requireNonNull(reductionAmount, "reductionAmount");
        Objects.requireNonNull(matching, "matching");
        Objects.requireNonNull(duration, "duration");
    }

    /** Every one of the controller's Pokemon. */
    public ReduceSideDamageTaken(INumber reductionAmount, INumber duration) {
        this(reductionAmount, Optional.empty(), duration);
    }

    /** Only the controller's Pokemon that match. */
    public ReduceSideDamageTaken(INumber reductionAmount, ICondition<? super PokemonInPlay> matching, INumber duration) {
        this(reductionAmount, Optional.of(matching), duration);
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int amount = reductionAmount.evaluate(context);
        if (amount <= 0) {
            return EffectOutcome.NO_OP;
        }
        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        context.controller().addDamageReduction(
                amount,
                pokemon -> matching.map(condition -> condition.evaluate(pokemon)).orElse(true),
                expiry);
        return EffectOutcome.APPLIED;
    }
}
