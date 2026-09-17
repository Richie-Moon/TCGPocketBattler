package com.tcgpocket.effect;

import com.tcgpocket.damage.DamageCalculator;
import com.tcgpocket.damage.DamageEvent;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.trigger.DamageDealt;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.Optional;

/**
 * Shared plumbing for the damage-dealing effects.
 *
 * <p>Package-private, because it is an implementation detail of this package
 * rather than part of the card-text vocabulary.
 */
final class Damage {

    private Damage() {
    }

    /**
     * Damage from whoever is currently resolving, run through the pipeline.
     *
     * <p>The weakness bonus is evaluated here rather than inside the
     * calculator, because it is printed as an {@code INumber} and only this
     * side of the boundary holds a context to evaluate it in.
     */
    static void deal(ResolutionContext context, PokemonInPlay target, int base, boolean isAttackDamage) {
        Optional<PokemonInPlay> source = context.source();

        int weaknessBonus = target.definition().weaknessDamage()
                .map(printed -> printed.evaluate(context))
                .orElse(DamageCalculator.DEFAULT_WEAKNESS_BONUS);

        int removed = apply(context, new DamageEvent(source, target, base, isAttackDamage, weaknessBonus));
        context.scope().recordDamageDealt(removed);
    }

    /**
     * Damage nobody dealt — poison, burn, recoil.
     *
     * <p>Distinct from {@link #deal} with {@code isAttackDamage} false: that
     * still names the resolving Pokemon as the source, which is right for an
     * attack that splashes onto the bench and wrong for a status ticking.
     */
    static void place(ResolutionContext context, PokemonInPlay target, int amount) {
        apply(context, DamageEvent.incidental(target, amount));
    }

    /** Returns the HP actually removed, which is less than landed when it overkills. */
    private static int apply(ResolutionContext context, DamageEvent event) {
        int landed = DamageCalculator.calculate(event, context.battle().turn());
        int before = event.target().damage();
        event.target().takeDamage(landed);

        TriggerDispatcher.dispatch(
                context.battle(),
                new DamageDealt(event.source(), event.target(), landed));

        // No knockout here: an effect applies damage; it does not run the game.
        // TurnEngine.checkKnockouts does, once the whole action has resolved.
        return event.target().damage() - before;
    }
}
