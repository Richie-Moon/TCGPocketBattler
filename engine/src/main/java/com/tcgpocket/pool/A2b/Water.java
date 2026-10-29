package com.tcgpocket.pool.A2b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.NoRetreatCost;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.*;

import java.util.List;
import java.util.Optional;

/**
 * Shining Revelry — Water.
 */
public final class Water {

    /**
     * A2b-014 - Tentacool
     */
    public static final PokemonCard TENTACOOL = PokemonCard.basic(
            "A2b-014", "Tentacool", "Tentacool is not a particularly strong swimmer. It drifts across the surface of shallow seas as it searches for prey.",
            70, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Rain Splash", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2b-015 - Tentacruel
     */
    public static final PokemonCard TENTACRUEL = PokemonCard.evolution(
            "A2b-015", "Tentacruel", "When the red orbs on Tentacruel's head glow brightly, watch out. The Pokémon is about to fire off a burst of ultrasonic waves.",
            1, "Tentacool", 100, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Wave Splash", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2b-016 - Buizel
     */
    public static final PokemonCard BUIZEL = PokemonCard.basic(
            "A2b-016", "Buizel", "It inflates its flotation sac, keeping its face above water in order to watch for prey movement.",
            70, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Water Gun", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2b-017 - Floatzel
     */
    public static final PokemonCard FLOATZEL = PokemonCard.evolution(
            "A2b-017", "Floatzel", "With its flotation sac inflated, it can carry people on its back. It deflates the sac before it dives.",
            1, "Buizel", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Water Arrow", "This attack does 30 damage to 1 of your opponent's Pokémon.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2b-018 - Wiglett
     */
    public static final PokemonCard WIGLETT = PokemonCard.basic(
            "A2b-018", "Wiglett", "This Pokémon can pick up the scent of a Veluza just over 65 feet away and will hide itself in the sand.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Spring Out", "1 of your opponent's Pokémon is chosen at random. Do 30 damage to it.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new RandomFrom(new OpponentAll()))))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2b-019 - Wugtrio ex
     */
    public static final PokemonCard WUGTRIO_EX = PokemonCard.evolution(
            "A2b-019", "Wugtrio ex", "",
            1, "Wiglett", 140, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Pop Out Throughout", "1 of your opponent's Pokémon is chosen at random 3 times. For each time a Pokémon was chosen, do 50 damage to it.",
                    EnergyCost.of(Type.WATER, 3), new Attempt(List.of(
                    new RepeatEffect(new Literal(3), new DealDamage(new Literal(50), new RandomFrom(new OpponentAll())))))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A2b-020 - Dondozo
     */
    public static final PokemonCard DONDOZO = PokemonCard.basic(
            "A2b-020", "Dondozo", "This Pokémon is a glutton, but it's bad at getting food. It teams up with a Tatsugiri to catch prey.",
            120, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Ocean Cyclone", "This attack also does 10 damage to each of your opponent's Benched Pokémon.",
                    EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new DamageEach(new Literal(10), new OpponentBench())))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2b-021 - Tatsugiri. Only the Active ever retreats, so "Active" needs no
     * condition of its own.
     */
    public static final PokemonCard TATSUGIRI = PokemonCard.basic(
            "A2b-021", "Tatsugiri", "This is a small dragon Pokémon. It lives inside the mouth of Dondozo to protect itself from enemies on the outside.",
            70, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Rain Splash", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING).withAbility(new PassiveAbility("Retreat Directive",
            "Your Active Dondozo has no Retreat Cost.",
            new NoRetreatCost(Optional.of(new IsSpecies("Dondozo")), new Always<>())));

    /** A2b-074 · Buizel (alternate art of A2b-016) */
    public static final PokemonCard BUIZEL_IR = BUIZEL.withId("A2b-074", CardRarity.ILLUSTRATION_RARE);
    /** A2b-075 · Tatsugiri (alternate art of A2b-021) */
    public static final PokemonCard TATSUGIRI_IR = TATSUGIRI.withId("A2b-075", CardRarity.ILLUSTRATION_RARE);
    /** A2b-081 · Wugtrio ex (alternate art of A2b-019) */
    public static final PokemonCard WUGTRIO_EX_UR = WUGTRIO_EX.withId("A2b-081", CardRarity.ULTRA_RARE);
    /** A2b-101 · Wiglett (alternate art of A2b-018) */
    public static final PokemonCard WIGLETT_SR = WIGLETT.withId("A2b-101", CardRarity.SHINY_RARE);
    /** A2b-102 · Dondozo (alternate art of A2b-020) */
    public static final PokemonCard DONDOZO_SR = DONDOZO.withId("A2b-102", CardRarity.SHINY_RARE);
    /** A2b-109 · Wugtrio ex (alternate art of A2b-019) */
    public static final PokemonCard WUGTRIO_EX_SUR = WUGTRIO_EX.withId("A2b-109", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            TENTACOOL, TENTACRUEL, BUIZEL, FLOATZEL, WIGLETT, WUGTRIO_EX, DONDOZO, TATSUGIRI,
            BUIZEL_IR, TATSUGIRI_IR, WUGTRIO_EX_UR, WIGLETT_SR, DONDOZO_SR, WUGTRIO_EX_SUR);

    private Water() {
    }
}
