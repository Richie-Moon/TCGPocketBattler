package com.tcgpocket.pool.A2b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.*;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Shining Revelry — Metal.
 */
public final class Metal {

    /**
     * A2b-052 - Tinkatink
     */
    public static final PokemonCard TINKATINK = PokemonCard.basic(
            "A2b-052", "Tinkatink", "It swings its handmade hammer around to protect itself, but the hammer is often stolen by Pokémon that eat metal.",
            60, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Corkscrew Punch", "", EnergyCost.of(Type.METAL, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2b-053 - Tinkatuff
     */
    public static final PokemonCard TINKATUFF = PokemonCard.evolution(
            "A2b-053", "Tinkatuff", "This Pokémon will attack groups of Pawniard and Bisharp, gathering metal from them in order to create a large and sturdy hammer.",
            1, "Tinkatink", 90, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Tenacious Hammer", "Flip a coin. If heads, this attack does 30 more damage.", EnergyCost.of(Type.METAL, 2), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(new Literal(30), new Product(new NumberHeads(), new Literal(30))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2b-054 - Tinkaton ex
     */
    public static final PokemonCard TINKATON_EX = PokemonCard.evolution(
            "A2b-054", "Tinkaton ex", "",
            2, "Tinkatuff", 170, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Terrific Thumping", "Flip a coin. If heads, this attack does 80 more damage.", EnergyCost.of(Type.METAL, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(new Literal(80), new Product(new NumberHeads(), new Literal(80))), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX);

    /**
     * A2b-055 - Varoom
     */
    public static final PokemonCard VAROOM = PokemonCard.basic(
            "A2b-055", "Varoom", "It is said that this Pokémon was born when an unknown poison Pokémon entered and inspirited an engine left at a scrap-processing factory.",
            70, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Headbutt", "", EnergyCost.of(Type.METAL, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2b-056 - Revavroom
     */
    public static final PokemonCard REVAVROOM = PokemonCard.evolution(
            "A2b-056", "Revavroom", "It creates a gas out of poison and minerals from rocks. It then detonates the gas in its cylinders—now numbering eight—to generate energy.",
            1, "Varoom", 110, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Spinning Drift", "Flip a coin. If heads, during your opponent's next turn, prevent all damage from—and effects of—attacks done to this Pokémon.",
                    EnergyCost.of(Type.METAL, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventDamage(new Self(), new Literal(1))),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventEffects(new Self(), new Literal(1)))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2b-057 - Gholdengo
     */
    public static final PokemonCard GHOLDENGO = PokemonCard.evolution(
            "A2b-057", "Gholdengo", "Its body seems to be made up of 1,000 coins. This Pokémon gets along well with others and is quick to make friends with anybody.",
            1, "Gimmighoul", 120, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Scintillating Surfing", "Flip a coin for each Metal Energy attached to this Pokémon. This attack does 50 damage for each heads.",
                    EnergyCost.of(Type.METAL, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new FlipN(new EnergyOn(new Self(), Type.METAL)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(50)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /** A2b-077 · Gholdengo (alternate art of A2b-057) */
    public static final PokemonCard GHOLDENGO_IR = GHOLDENGO.withId("A2b-077", CardRarity.ILLUSTRATION_RARE);
    /** A2b-086 · Tinkaton ex (alternate art of A2b-054) */
    public static final PokemonCard TINKATON_EX_UR = TINKATON_EX.withId("A2b-086", CardRarity.ULTRA_RARE);
    /** A2b-094 · Tinkaton ex (alternate art of A2b-054) */
    public static final PokemonCard TINKATON_EX_SIR = TINKATON_EX.withId("A2b-094", CardRarity.SPECIAL_ILLUSTRATION_RARE);
    /** A2b-105 · Varoom (alternate art of A2b-055) */
    public static final PokemonCard VAROOM_SR = VAROOM.withId("A2b-105", CardRarity.SHINY_RARE);
    /** A2b-106 · Revavroom (alternate art of A2b-056) */
    public static final PokemonCard REVAVROOM_SR = REVAVROOM.withId("A2b-106", CardRarity.SHINY_RARE);

    static final List<PokemonCard> CARDS = List.of(
            TINKATINK, TINKATUFF, TINKATON_EX, VAROOM, REVAVROOM, GHOLDENGO,
            GHOLDENGO_IR, TINKATON_EX_UR, TINKATON_EX_SIR, VAROOM_SR, REVAVROOM_SR);

    private Metal() {
    }
}
