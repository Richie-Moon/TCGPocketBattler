package com.tcgpocket.effect;

import com.tcgpocket.damage.DamageCalculator;
import com.tcgpocket.damage.DamageEvent;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

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
     * Runs one packet of damage through the pipeline and applies the result.
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

        DamageEvent event = new DamageEvent(source, target, base, isAttackDamage, weaknessBonus);
        int landed = DamageCalculator.calculate(event, context.battle().turn());

        target.takeDamage(landed);

        // TODO: dispatch DamageDealt and run Battle.checkKnockouts() once
        //       ITrigger and the turn engine exist. Both are engine concerns:
        //       an effect applies damage, it does not run the game.
    }
}
