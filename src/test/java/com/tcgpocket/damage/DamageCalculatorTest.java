package com.tcgpocket.damage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tcgpocket.TestBoard;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.energy.Type;
import com.tcgpocket.state.ActiveModifier;
import com.tcgpocket.state.ModifierKind;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The pipeline order is not commutative, and getting it wrong is silently
 * wrong — it only shows on cards that stack two of the three adjustments. These
 * tests pin the order down.
 */
class DamageCalculatorTest {

    private static final PokemonCard PIKACHU = TestBoard.card("Pikachu", 60, Type.LIGHTNING);

    /** Weak to Lightning, so Pikachu triggers weakness against it. */
    private static final PokemonCard MAROWAK = new PokemonCard(
            "marowak", "Marowak", "", Set.<CardTag>of(), 120, 0, Type.FIGHTING,
            Optional.empty(), 2, Optional.of(Type.LIGHTNING), Optional.empty());

    private final TestBoard board = new TestBoard();
    private final PokemonInPlay attacker = board.active(board.you, PIKACHU);
    private final PokemonInPlay defender = board.active(board.them, MAROWAK);

    private DamageEvent attack(int base) {
        return new DamageEvent(Optional.of(attacker), defender, base, true,
                DamageCalculator.DEFAULT_WEAKNESS_BONUS);
    }

    @Test
    void baseDamagePassesThroughUntouched() {
        PokemonInPlay notWeak = board.bench(board.them, PIKACHU);
        DamageEvent event = new DamageEvent(Optional.of(attacker), notWeak, 30, true, 20);

        assertEquals(30, DamageCalculator.calculate(event, 1));
    }

    @Test
    @DisplayName("weakness adds its bonus when the defender is weak to the attacker's type")
    void weaknessApplies() {
        assertTrue(DamageCalculator.weaknessApplies(attack(30)));
        assertEquals(50, DamageCalculator.calculate(attack(30), 1));
    }

    @Test
    @DisplayName("weakness is skipped for anything but the attacked Pokemon")
    void weaknessOnlyForAttackDamage() {
        DamageEvent spread = new DamageEvent(Optional.of(attacker), defender, 30, false, 20);

        assertFalse(DamageCalculator.weaknessApplies(spread));
        assertEquals(30, DamageCalculator.calculate(spread, 1));
    }

    @Test
    void weaknessNeedsAMatchingType() {
        PokemonInPlay notWeak = board.bench(board.them, PIKACHU);
        DamageEvent event = new DamageEvent(Optional.of(attacker), notWeak, 30, true, 20);

        assertFalse(DamageCalculator.weaknessApplies(event));
    }

    @Test
    @DisplayName("weakness applies to the already-increased amount, not the printed one")
    void increaseIsAppliedBeforeWeakness() {
        attacker.addModifier(new ActiveModifier(ModifierKind.INCREASE_DAMAGE_DEALT, 10, 5));

        // 30 base + 10 increase = 40, then +20 weakness = 60.
        assertEquals(60, DamageCalculator.calculate(attack(30), 1));
    }

    @Test
    @DisplayName("reduction is subtracted after weakness has been added")
    void reductionIsAppliedAfterWeakness() {
        defender.addModifier(new ActiveModifier(ModifierKind.REDUCE_DAMAGE_TAKEN, 20, 5));

        // 30 base + 20 weakness = 50, then -20 reduction = 30.
        assertEquals(30, DamageCalculator.calculate(attack(30), 1));
    }

    @Test
    void allThreeStackInOrder() {
        attacker.addModifier(new ActiveModifier(ModifierKind.INCREASE_DAMAGE_DEALT, 10, 5));
        defender.addModifier(new ActiveModifier(ModifierKind.REDUCE_DAMAGE_TAKEN, 30, 5));

        // 30 + 10 = 40, +20 weakness = 60, -30 = 30.
        assertEquals(30, DamageCalculator.calculate(attack(30), 1));
    }

    @Test
    @DisplayName("prevention overrides the arithmetic entirely")
    void preventionZeroesEvenALargeHit() {
        attacker.addModifier(new ActiveModifier(ModifierKind.INCREASE_DAMAGE_DEALT, 100, 5));
        defender.addModifier(new ActiveModifier(ModifierKind.PREVENT_DAMAGE, 0, 5));

        assertEquals(0, DamageCalculator.calculate(attack(200), 1));
    }

    @Test
    void resultIsFlooredAtZero() {
        defender.addModifier(new ActiveModifier(ModifierKind.REDUCE_DAMAGE_TAKEN, 500, 5));

        assertEquals(0, DamageCalculator.calculate(attack(30), 1));
    }

    @Test
    @DisplayName("a modifier past its expiry turn no longer counts")
    void expiredModifiersAreIgnored() {
        defender.addModifier(new ActiveModifier(ModifierKind.REDUCE_DAMAGE_TAKEN, 20, 2));

        assertEquals(30, DamageCalculator.calculate(attack(30), 2), "still live on turn 2");
        assertEquals(50, DamageCalculator.calculate(attack(30), 3), "expired by turn 3");
    }

    @Test
    void damageWithNoSourceGetsNoWeaknessOrIncrease() {
        DamageEvent poison = DamageEvent.incidental(defender, 10);

        assertFalse(DamageCalculator.weaknessApplies(poison));
        assertEquals(10, DamageCalculator.calculate(poison, 1));
    }
}
