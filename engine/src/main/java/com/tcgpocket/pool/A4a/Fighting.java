package com.tcgpocket.pool.A4a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.EventSideIs;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.condition.IsType;
import com.tcgpocket.condition.Not;
import com.tcgpocket.condition.PickedCard;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardTopOfDeck;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.ReduceDamageTaken;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.DamageOn;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.AttackDeclared;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * Secluded Springs (A4a): the Fighting cards, in printed set order.
 */
public final class Fighting {

    /**
     * A4a-040 - Diglett
     */
    public static final PokemonCard DIGLETT = PokemonCard.basic(
            "A4a-040", "Diglett", "It lives about one yard underground, where it feeds on plant roots. It sometimes appears aboveground.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Headbutt", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4a-041 - Dugtrio — an empty deck discards nothing, so no bonus.
     */
    public static final PokemonCard DUGTRIO = PokemonCard.evolution(
            "A4a-041", "Dugtrio", "Its three heads bob separately up and down to loosen the soil nearby, making it easier for it to burrow.",
            1, "Diglett", 90, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Cliff Crumbler", "Discard the top card of your deck. If that card is a Fighting Pokémon, this attack does 60 more damage.", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DiscardTopOfDeck(new Literal(1), new AttackerSide()),
                    new DealDamage(new Branch(List.of(new Branch.Case(new PickedCard(new IsType(Type.FIGHTING)), new Literal(100))), new Literal(40)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4a-042 - Poliwrath ex
     */
    public static final PokemonCard POLIWRATH_EX = PokemonCard.evolution(
            "A4a-042", "Poliwrath ex", "",
            2, "Poliwhirl", 180, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Hydro Knuckle", "If this Pokémon has any Water Energy attached, this attack does 40 more damage.", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(new GreaterThan(new EnergyOn(new Self(), Type.WATER), new Literal(0)), new Literal(140))), new Literal(100)), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.PSYCHIC).withTags(CardTag.EX);

    /**
     * A4a-043 - Phanpy
     */
    public static final PokemonCard PHANPY = PokemonCard.basic(
            "A4a-043", "Phanpy", "This Pokémon lives and nests on a riverbank. After playing in the mud, it won't be able to settle down unless it washes its body.",
            70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Flail", "This attack does damage to your opponent's Active Pokémon equal to the damage this Pokémon has on it.", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new DamageOn(new Self()), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4a-044 - Donphan
     */
    public static final PokemonCard DONPHAN = PokemonCard.evolution(
            "A4a-044", "Donphan", "Donphan is covered in tough hide, so even being hit by a car won't faze this Pokémon. However, it is extremely susceptible to rain.",
            1, "Phanpy", 120, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Land Crush", "", EnergyCost.of(Type.FIGHTING, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS).withAbility(new PassiveAbility("Exoskeleton",
            "This Pokémon takes −20 damage from attacks.",
            List.of(new Trigger(AttackDeclared.class,
                    new Not<>(new EventSideIs(new SelfSide())),
                    new Attempt(List.of(new ReduceDamageTaken(new Literal(20), new Self(), new Literal(0))))))));

    /**
     * A4a-045 - Relicanth
     */
    public static final PokemonCard RELICANTH = PokemonCard.basic(
            "A4a-045", "Relicanth", "Rock-hard scales and oil-filled swim bladders allow this Pokémon to survive the intense water pressure of the deep sea.",
            90, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Take Down", "This Pokémon also does 10 damage to itself.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new DealDamage(new Literal(10), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4a-046 - Dwebble
     */
    public static final PokemonCard DWEBBLE = PokemonCard.basic(
            "A4a-046", "Dwebble", "It makes a hole in a suitable rock. If that rock breaks, the Pokémon remains agitated until it locates a replacement.",
            70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Sand Spray", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4a-047 - Crustle
     */
    public static final PokemonCard CRUSTLE = PokemonCard.evolution(
            "A4a-047", "Crustle", "This highly territorial Pokémon prefers dry climates. It won't come out of its boulder on rainy days.",
            1, "Dwebble", 140, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Stone Edge", "Flip a coin. If heads, this attack does 50 more damage.", EnergyCost.of(Type.FIGHTING, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(new Literal(100), new Product(new NumberHeads(), new Literal(50))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /** A4a-076 · Phanpy (alternate art of A4a-043) */
    public static final PokemonCard PHANPY_IR = PHANPY.withId("A4a-076", CardRarity.ILLUSTRATION_RARE);

    /** A4a-082 · Poliwrath ex (alternate art of A4a-042) */
    public static final PokemonCard POLIWRATH_EX_UR = POLIWRATH_EX.withId("A4a-082", CardRarity.ULTRA_RARE);

    /** A4a-089 · Poliwrath ex (alternate art of A4a-042) */
    public static final PokemonCard POLIWRATH_EX_SIR = POLIWRATH_EX.withId("A4a-089", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    /** A4a-098 · Gible (shiny of A2a-045) */
    public static final PokemonCard GIBLE_SR = com.tcgpocket.pool.A2a.Fighting.GIBLE.withId("A4a-098", CardRarity.SHINY_RARE);

    /** A4a-099 · Gabite (shiny of A2a-046) */
    public static final PokemonCard GABITE_SR = com.tcgpocket.pool.A2a.Fighting.GABITE.withId("A4a-099", CardRarity.SHINY_RARE);

    /** A4a-103 · Garchomp ex (shiny of A2a-047) */
    public static final PokemonCard GARCHOMP_EX_SUR = com.tcgpocket.pool.A2a.Fighting.GARCHOMP_EX.withId("A4a-103", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            DIGLETT, DUGTRIO, POLIWRATH_EX, PHANPY, DONPHAN, RELICANTH,
            DWEBBLE, CRUSTLE, PHANPY_IR, POLIWRATH_EX_UR, POLIWRATH_EX_SIR, GIBLE_SR,
            GABITE_SR, GARCHOMP_EX_SUR);

    private Fighting() {
    }
}
