package com.tcgpocket.pool.A3a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardRandomEnergy;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.OpponentActive;

import java.util.List;

import static com.tcgpocket.pool.A1.Water.FROAKIE;
import static com.tcgpocket.pool.A1.Water.FROGADIER;
import static com.tcgpocket.pool.A1.Water.GRENINJA;

/**
 * Extradimensional Crisis — Water.
 */
public final class Water {

    /**
     * A3a-010 - Mantine
     */
    public static final PokemonCard MANTINE = PokemonCard.basic(
            "A3a-010", "Mantine", "If it builds up enough speed swimming, it can jump out above the waves and glide for over 300 feet.",
            90, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Surf", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3a-011 - Carvanha
     */
    public static final PokemonCard CARVANHA = PokemonCard.basic(
            "A3a-011", "Carvanha", "It won't attack while it's alone—not even if it spots prey. Instead, it waits for other Carvanha to join it, and then the Pokémon attack as a group.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3a-012 - Sharpedo
     */
    public static final PokemonCard SHARPEDO = PokemonCard.evolution(
            "A3a-012", "Sharpedo", "As soon as it catches the scent of prey, Sharpedo will jet seawater from its backside, hurtling toward the target to attack at 75 mph.",
            1, "Carvanha", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Crunch", "Flip a coin. If heads, discard a random Energy from your opponent's Active Pokémon.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new DiscardRandomEnergy(new Literal(1), new OpponentActive()))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /** A3a-091 · Froakie (alternate art of A1-087) */
    public static final PokemonCard FROAKIE_SR = FROAKIE.withId("A3a-091", CardRarity.SHINY_RARE);
    /** A3a-092 · Frogadier (alternate art of A1-088) */
    public static final PokemonCard FROGADIER_SR = FROGADIER.withId("A3a-092", CardRarity.SHINY_RARE);
    /** A3a-093 · Greninja (alternate art of A1-089) */
    public static final PokemonCard GRENINJA_SR = GRENINJA.withId("A3a-093", CardRarity.SHINY_RARE);

    static final List<PokemonCard> CARDS = List.of(
            MANTINE, CARVANHA, SHARPEDO,
            FROAKIE_SR, FROGADIER_SR, GRENINJA_SR);

    private Water() {
    }
}
