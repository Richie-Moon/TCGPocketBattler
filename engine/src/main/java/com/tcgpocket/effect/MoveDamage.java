package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Moves the damage on one Pokemon onto another — all of it for Dusknoir's
 * Shadow Void, up to {@code upTo} for Acerola's "move 40 of its damage".
 *
 * <p>Neither healing nor damage dealt, so it announces neither {@code Healed}
 * nor {@code DamageDealt}, and weakness and modifiers do not touch it. Damage
 * past the receiver's HP is lost, as {@code takeDamage} caps at a knockout,
 * which the engine then checks after the action. A source with less than
 * {@code upTo} moves what it has.
 *
 * <p>Moving from a Pokemon with no damage, or onto itself, is a
 * {@link EffectOutcome#NO_OP}.
 *
 * @param upTo the most to move; empty means all of it
 */
public record MoveDamage(ITarget from, ITarget to, Optional<INumber> upTo) implements IEffect {

    public MoveDamage {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        Objects.requireNonNull(upTo, "upTo");
    }

    /** All of it. */
    public MoveDamage(ITarget from, ITarget to) {
        this(from, to, Optional.empty());
    }

    /** At most {@code upTo}. */
    public MoveDamage(ITarget from, ITarget to, INumber upTo) {
        this(from, to, Optional.of(upTo));
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> source = from.resolve(context);
        Optional<PokemonInPlay> destination = to.resolve(context);
        if (source.isEmpty() || destination.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        int all = source.get().damage();
        int amount = upTo.map(limit -> Math.min(all, limit.evaluate(context))).orElse(all);
        if (amount <= 0 || source.get() == destination.get()) {
            return EffectOutcome.NO_OP;
        }

        source.get().heal(amount);
        destination.get().takeDamage(amount);
        return EffectOutcome.APPLIED;
    }
}
