package com.tcgpocket.pool.A2b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Shining Revelry — Fire.
 */
public final class Fire {

    /**
     * A2b-008 - Charmander
     */
    public static final PokemonCard CHARMANDER = PokemonCard.basic(
            "A2b-008", "Charmander", "It has a preference for hot things. When it rains, steam is said to spout from the tip of its tail.",
            60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Combustion", "", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A2b-009 - Charmeleon
     */
    public static final PokemonCard CHARMELEON = PokemonCard.evolution(
            "A2b-009", "Charmeleon", "It has a barbaric nature. In battle, it whips its fiery tail around and slashes away with sharp claws.",
            1, "Charmander", 90, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Combustion", "", EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A2b-010 - Charizard ex
     */
    public static final PokemonCard CHARIZARD_EX = PokemonCard.evolution(
            "A2b-010", "Charizard ex", "",
            2, "Charmeleon", 180, Type.FIRE, EnergyCost.of(Type.COLORLESS, 3), List.of(
                    new Action(
                            "Stoke", "Take 3 Fire Energy from your Energy Zone and attach it to this Pokémon.",
                            EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                            new AttachEnergy(Type.FIRE, new Literal(3), new Self())))),
                    new Action(
                            "Steam Artillery", "", EnergyCost.of(Type.FIRE, 3, Type.COLORLESS, 2), new Attempt(List.of(
                            new DealDamage(new Literal(150), new OpponentActive()))))
            ), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.WATER).withTags(CardTag.EX);

    /**
     * A2b-011 - Magmar
     */
    public static final PokemonCard MAGMAR = PokemonCard.basic(
            "A2b-011", "Magmar", "Magmar dispatches its prey with fire. But it regrets this habit once it realizes that it has burned its intended prey to a charred crisp.",
            70, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flare", "", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A2b-012 - Magmortar
     */
    public static final PokemonCard MAGMORTAR = PokemonCard.evolution(
            "A2b-012", "Magmortar", "When Magmortar inhales deeply, the fire burning in its belly intensifies, rising in temperature to over 3,600 degrees Fahrenheit.",
            1, "Magmar", 120, Type.FIRE, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Smoke Bomb", "During your opponent's next turn, if the Defending Pokémon tries to use an attack, your opponent flips a coin. If tails, that attack doesn't happen.",
                    EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new HinderAttack(new OpponentActive(), new Literal(1))))
            )), CardRarity.RARE
    ).withWeakness(Type.WATER);

    /**
     * A2b-013 - Paldean Tauros
     */
    public static final PokemonCard PALDEAN_TAUROS = PokemonCard.basic(
            "A2b-013", "Paldean Tauros", "When heated by fire energy, its horns can get hotter than 1,800 degrees Fahrenheit. Those gored by them will suffer both wounds and burns.",
            110, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Blaze Tackle", "This Pokémon also does 10 damage to itself.", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new DealDamage(new Literal(10), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /** A2b-080 · Charizard ex (alternate art of A2b-010) */
    public static final PokemonCard CHARIZARD_EX_UR = CHARIZARD_EX.withId("A2b-080", CardRarity.ULTRA_RARE);
    /** A2b-099 · Charmander (alternate art of A2b-008) */
    public static final PokemonCard CHARMANDER_SR = CHARMANDER.withId("A2b-099", CardRarity.SHINY_RARE);
    /** A2b-100 · Charmeleon (alternate art of A2b-009) */
    public static final PokemonCard CHARMELEON_SR = CHARMELEON.withId("A2b-100", CardRarity.SHINY_RARE);
    /** A2b-108 · Charizard ex (alternate art of A2b-010) */
    public static final PokemonCard CHARIZARD_EX_SUR = CHARIZARD_EX.withId("A2b-108", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            CHARMANDER, CHARMELEON, CHARIZARD_EX, MAGMAR, MAGMORTAR, PALDEAN_TAUROS,
            CHARIZARD_EX_UR, CHARMANDER_SR, CHARMELEON_SR, CHARIZARD_EX_SUR);

    private Fire() {
    }
}
