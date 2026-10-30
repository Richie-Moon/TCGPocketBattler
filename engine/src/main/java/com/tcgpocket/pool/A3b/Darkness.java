package com.tcgpocket.pool.A3b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsBasic;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardTool;
import com.tcgpocket.effect.PreventAttack;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.OpponentActive;

import java.util.List;

/**
 * Eevee Grove — Darkness.
 */
public final class Darkness {

    /**
     * A3b-043 - Umbreon
     */
    public static final PokemonCard UMBREON = PokemonCard.evolution(
            "A3b-043", "Umbreon", "When exposed to the moon's aura, the rings on its body glow faintly and it gains a mysterious power.",
            1, "Eevee", 100, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Dark Binding", "If the Defending Pokémon is a Basic Pokémon, it can't attack during your opponent's next turn.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new ConditionalEffect(new For(new IsBasic(), new OpponentActive()), new PreventAttack(new OpponentActive(), new Literal(1)))))
            )), CardRarity.RARE
    ).withWeakness(Type.GRASS);

    /**
     * A3b-044 - Sableye
     */
    public static final PokemonCard SABLEYE = PokemonCard.basic(
            "A3b-044", "Sableye", "Materials from gems it has eaten float to the surface of its body and can form an infinite number of patterns among individuals.",
            70, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Scratch", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3b-045 - Purrloin
     */
    public static final PokemonCard PURRLOIN = PokemonCard.basic(
            "A3b-045", "Purrloin", "Opponents that get drawn in by its adorable behavior come away with stinging scratches from its claws and stinging pride from its laughter.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Playful Knockdown", "Discard all Pokémon Tools from your opponent's Active Pokémon.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DiscardTool(new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3b-046 - Liepard
     */
    public static final PokemonCard LIEPARD = PokemonCard.evolution(
            "A3b-046", "Liepard", "Don't be fooled by its gorgeous fur and elegant figure. This is a moody and vicious Pokémon.",
            1, "Purrloin", 90, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Slashing Claw", "", EnergyCost.of(Type.DARKNESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /** A3b-077 · Umbreon (alternate art of A3b-043) */
    public static final PokemonCard UMBREON_IR = UMBREON.withId("A3b-077", CardRarity.ILLUSTRATION_RARE);

    /** A3b-100 · Ekans (shiny of A1-164) */
    public static final PokemonCard EKANS_SR = com.tcgpocket.pool.A1.Darkness.EKANS.withId("A3b-100", CardRarity.SHINY_RARE);

    /** A3b-101 · Arbok (shiny of A1-165) */
    public static final PokemonCard ARBOK_SR = com.tcgpocket.pool.A1.Darkness.ARBOK.withId("A3b-101", CardRarity.SHINY_RARE);

    static final List<PokemonCard> CARDS = List.of(
            UMBREON, SABLEYE, PURRLOIN, LIEPARD, UMBREON_IR,
            EKANS_SR, ARBOK_SR);

    private Darkness() {
    }
}
