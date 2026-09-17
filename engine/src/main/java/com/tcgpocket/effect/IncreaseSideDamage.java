package com.tcgpocket.effect;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/**
 * Sharpens every attack a player's Pokemon make against the opponent's Active
 * — "during this turn, attacks used by your Pokemon do +10 damage to your
 * opponent's Active Pokemon".
 *
 * <h2>Why this is not {@link IncreaseDamage}</h2>
 *
 * <p>That installs an {@code ActiveModifier} on one Pokemon, and this card text
 * names no Pokemon: it covers whichever of yours attacks, including one that
 * retreats into the Active Spot after the card was played. So, like
 * {@link PreventSupporter}, it stamps the controller's {@code Side}, and
 * {@code DamageCalculator} reads it for the attacker.
 *
 * @param matching narrows which attackers benefit — Blaine's "your Ninetales,
 *                 Rapidash, or Magmar"; empty covers all of them
 */
public record IncreaseSideDamage(
        INumber increaseAmount,
        Optional<ICondition<? super PokemonInPlay>> matching,
        INumber duration) implements IDurationEffect {

    public IncreaseSideDamage {
        Objects.requireNonNull(increaseAmount, "increaseAmount");
        Objects.requireNonNull(matching, "matching");
        Objects.requireNonNull(duration, "duration");
    }

    /** Every one of the controller's Pokemon. */
    public IncreaseSideDamage(INumber increaseAmount, INumber duration) {
        this(increaseAmount, Optional.empty(), duration);
    }

    /** Only the controller's Pokemon that match. */
    public IncreaseSideDamage(INumber increaseAmount, ICondition<? super PokemonInPlay> matching, INumber duration) {
        this(increaseAmount, Optional.of(matching), duration);
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int amount = increaseAmount.evaluate(context);
        if (amount <= 0) {
            return EffectOutcome.NO_OP;
        }
        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        context.controller().addAttackBonus(
                amount,
                pokemon -> matching.map(condition -> condition.evaluate(pokemon)).orElse(true),
                expiry);
        return EffectOutcome.APPLIED;
    }
}
