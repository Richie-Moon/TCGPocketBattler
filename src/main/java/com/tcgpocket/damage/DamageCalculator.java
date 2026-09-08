package com.tcgpocket.damage;

import com.tcgpocket.state.ActiveModifier;
import com.tcgpocket.state.ModifierKind;
import com.tcgpocket.state.PokemonInPlay;

/**
 * Turns a base damage amount into the number that actually lands.
 *
 * <p>The order is not commutative and getting it wrong is silently wrong — it
 * only shows on cards that stack two of the three:
 *
 * <ol>
 *   <li>base
 *   <li>plus the source's INCREASE_DAMAGE_DEALT modifiers
 *   <li>plus weakness, applied to the already-increased amount
 *   <li>minus the target's REDUCE_DAMAGE_TAKEN modifiers
 *   <li>zero if the target has PREVENT_DAMAGE
 *   <li>floored at zero
 * </ol>
 *
 * <p>Steps 2 to 5 run only for attack damage. Bench, spread and between-turns
 * damage take the base amount and nothing else.
 *
 * <p>Takes no {@code ResolutionContext}: everything it needs is on the event
 * and the two Pokemon. That keeps this package out of the model's dependency
 * cycle, so it stays independently testable.
 */
public final class DamageCalculator {

    /** Weakness is a flat +20 in Pocket when a card does not print its own. */
    public static final int DEFAULT_WEAKNESS_BONUS = 20;

    private DamageCalculator() {
    }

    public static int calculate(DamageEvent event, int currentTurn) {
        int amount = event.baseAmount();

        if (!event.isAttackDamage()) {
            return Math.max(0, amount);
        }

        amount += event.source()
                .map(source -> sumModifiers(source, ModifierKind.INCREASE_DAMAGE_DEALT, currentTurn))
                .orElse(0);

        if (weaknessApplies(event)) {
            amount += event.weaknessBonus();
        }

        amount -= sumModifiers(event.target(), ModifierKind.REDUCE_DAMAGE_TAKEN, currentTurn);

        if (event.target().hasModifier(ModifierKind.PREVENT_DAMAGE, currentTurn)) {
            return 0;
        }

        return Math.max(0, amount);
    }

    /** Whether the defender is weak to the attacker's type. */
    public static boolean weaknessApplies(DamageEvent event) {
        if (!event.isAttackDamage() || event.source().isEmpty()) {
            return false;
        }
        return event.target().definition().weakness()
                .map(weakness -> weakness == event.source().get().definition().type())
                .orElse(false);
    }

    private static int sumModifiers(PokemonInPlay pokemon, ModifierKind kind, int currentTurn) {
        return pokemon.modifiers().stream()
                .filter(modifier -> modifier.kind() == kind)
                .filter(modifier -> modifier.isActiveOn(currentTurn))
                .mapToInt(ActiveModifier::amount)
                .sum();
    }
}
