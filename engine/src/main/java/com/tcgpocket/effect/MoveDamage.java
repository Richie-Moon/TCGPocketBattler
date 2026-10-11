package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Moves all the damage on one Pokemon onto another — Dusknoir's Shadow Void.
 *
 * <p>Neither healing nor damage dealt, so it announces neither {@code Healed}
 * nor {@code DamageDealt}, and weakness and modifiers do not touch it. Damage
 * past the receiver's HP is lost, as {@code takeDamage} caps at a knockout,
 * which the engine then checks after the action.
 *
 * <p>Moving from a Pokemon with no damage, or onto itself, is a
 * {@link EffectOutcome#NO_OP}.
 */
public record MoveDamage(ITarget from, ITarget to) implements IEffect {

    public MoveDamage {
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

        int amount = source.get().damage();
        if (amount == 0 || source.get() == destination.get()) {
            return EffectOutcome.NO_OP;
        }

        source.get().heal(amount);
        destination.get().takeDamage(amount);
        return EffectOutcome.APPLIED;
    }
}
