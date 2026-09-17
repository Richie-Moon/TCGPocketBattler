package com.tcgpocket.damage;

import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/**
 * One packet of damage on its way through the pipeline.
 *
 * @param isAttackDamage true only for damage the attacking Pokemon deals to the
 *                       Pokemon it attacked. Spread damage, bench damage and
 *                       between-turns poison and burn are all false, and skip
 *                       weakness and modifiers entirely. Carried on the event
 *                       rather than inferred from whether a source exists,
 *                       because an attack that hits the bench has a source but
 *                       is still not attack damage.
 * @param weaknessBonus  pre-evaluated extra damage if weakness applies. The
 *                       calculator decides <em>whether</em> it applies; the
 *                       caller supplies how much, because the printed value is
 *                       an INumber and only the caller holds a context to
 *                       evaluate it in.
 */
public record DamageEvent(
        Optional<PokemonInPlay> source,
        PokemonInPlay target,
        int baseAmount,
        boolean isAttackDamage,
        int weaknessBonus) {

    public DamageEvent {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(target, "target");
    }

    /** Damage with no attacker and no weakness — poison, burn, self-damage. */
    public static DamageEvent incidental(PokemonInPlay target, int amount) {
        return new DamageEvent(Optional.empty(), target, amount, false, 0);
    }
}
