package com.tcgpocket.pool.A3b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.AttackUsedLastTurn;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.ShuffleRandomFromHand;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Eevee Grove — Grass.
 */
public final class Grass {

    /**
     * A3b-001 - Tropius
     */
    public static final PokemonCard TROPIUS = PokemonCard.basic(
            "A3b-001", "Tropius", "It lives in tropical jungles. The bunch of fruit around its neck is delicious. The fruit grows twice a year.",
            100, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Cutting Wind", "", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3b-002 - Leafeon
     */
    public static final PokemonCard LEAFEON = PokemonCard.evolution(
            "A3b-002", "Leafeon", "When you see Leafeon asleep in a patch of sunshine, you'll know it is using photosynthesis to produce clean air.",
            1, "Eevee", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leaf Blast", "This attack does 20 more damage for each Grass Energy attached to this Pokémon.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Sum(new Literal(10), new Product(new EnergyOn(new Self(), Type.GRASS), new Literal(20))), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A3b-003 - Bounsweet
     */
    public static final PokemonCard BOUNSWEET = PokemonCard.basic(
            "A3b-003", "Bounsweet", "Its sweat is sweet, like syrup made from boiled-down fruit. Because of this, Bounsweet was highly valued in the past, when sweeteners were scarce.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flop", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3b-004 - Steenee
     */
    public static final PokemonCard STEENEE = PokemonCard.evolution(
            "A3b-004", "Steenee", "Steenee spreads a sweet scent that makes others feel invigorated. This same scent is popular for antiperspirants.",
            1, "Bounsweet", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Leaf Step", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3b-005 - Tsareena
     */
    public static final PokemonCard TSAREENA = PokemonCard.evolution(
            "A3b-005", "Tsareena", "This Pokémon is proud and aggressive. However, it is said that a Tsareena will instantly become calm if someone touches the crown on its calyx.",
            2, "Steenee", 140, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Kick Down", "Your opponent reveals a random card from their hand and shuffles it into their deck.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new ShuffleRandomFromHand(new OpponentSide())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3b-006 - Applin
     */
    public static final PokemonCard APPLIN = PokemonCard.basic(
            "A3b-006", "Applin", "It grows by eating the flesh of an apple. Applin uses its own body fluid to strengthen the apple's skin and prevent it from rotting.",
            40, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3b-007 - Appletun
     */
    public static final PokemonCard APPLETUN = PokemonCard.evolution(
            "A3b-007", "Appletun", "It feeds mainly on sweet apples. It will also eat small bug Pokémon that are attracted by its sweet nectar.",
            1, "Applin", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Sweets Relay", "If 1 of your Pokémon used Sweets Relay during your last turn, this attack does 30 more damage.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(
                            new AttackUsedLastTurn("Sweets Relay"),
                            new Literal(70))
                    ), new Literal(40)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /** A3b-070 · Leafeon (alternate art of A3b-002) */
    public static final PokemonCard LEAFEON_IR = LEAFEON.withId("A3b-070", CardRarity.ILLUSTRATION_RARE);

    /** A3b-093 · Pinsir (shiny of A2b-004) */
    public static final PokemonCard PINSIR_SR = com.tcgpocket.pool.A2b.Grass.PINSIR.withId("A3b-093", CardRarity.SHINY_RARE);

    static final List<PokemonCard> CARDS = List.of(
            TROPIUS, LEAFEON, BOUNSWEET, STEENEE, TSAREENA, APPLIN,
            APPLETUN, LEAFEON_IR,
            PINSIR_SR);

    private Grass() {
    }
}
