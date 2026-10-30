package com.tcgpocket.pool.A3b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.AttackUsedLastTurn;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardRandomEnergy;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.FlipUntilTails;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentAll;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;

import java.util.List;

/**
 * Eevee Grove — Water.
 */
public final class Water {

    /**
     * A3b-016 - Vaporeon
     */
    public static final PokemonCard VAPOREON = PokemonCard.evolution(
            "A3b-016", "Vaporeon", "It lives close to water. Its long tail is ridged with a fin, which is often mistaken for a mermaid's.",
            1, "Eevee", 120, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Hyper Whirlpool", "Flip a coin until you get tails. For each heads, discard a random Energy from your opponent's Active Pokémon.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new FlipUntilTails(),
                    new DiscardRandomEnergy(new NumberHeads(), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3b-017 - Glaceon
     */
    public static final PokemonCard GLACEON = PokemonCard.evolution(
            "A3b-017", "Glaceon", "It can control its body temperature at will. This enables it to freeze the moisture in the atmosphere, creating flurries of diamond dust.",
            1, "Eevee", 100, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Ice Blade", "This attack does 50 damage to 1 of your opponent's Pokémon.", EnergyCost.of(Type.WATER, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new ChosenFrom(new OpponentAll(), new SelfSide(), "Choose 1 of your opponent's Pokémon to damage"))))
            )), CardRarity.RARE
    ).withWeakness(Type.METAL);

    /**
     * A3b-018 - Vanillite
     */
    public static final PokemonCard VANILLITE = PokemonCard.basic(
            "A3b-018", "Vanillite", "Unable to survive in hot areas, it makes itself comfortable by breathing out air cold enough to cause snow. It burrows into the snow to sleep.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sweets Relay", "If 1 of your Pokémon used Sweets Relay during your last turn, this attack does 20 more damage.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(
                            new AttackUsedLastTurn("Sweets Relay"),
                            new Literal(30))
                    ), new Literal(10)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A3b-019 - Vanillish
     */
    public static final PokemonCard VANILLISH = PokemonCard.evolution(
            "A3b-019", "Vanillish", "By drinking pure water, it grows its icy body. This Pokémon can be hard to find on days with warm, sunny weather.",
            1, "Vanillite", 80, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Sweets Relay", "If 1 of your Pokémon used Sweets Relay during your last turn, this attack does 20 more damage.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(
                            new AttackUsedLastTurn("Sweets Relay"),
                            new Literal(50))
                    ), new Literal(30)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A3b-020 - Vanilluxe
     */
    public static final PokemonCard VANILLUXE = PokemonCard.evolution(
            "A3b-020", "Vanilluxe", "When its anger reaches a breaking point, this Pokémon unleashes a fierce blizzard that freezes every creature around it, be they friend or foe.",
            2, "Vanillish", 140, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Double Spin", "Flip 2 coins. This attack does 80 damage for each heads.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(80)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /**
     * A3b-021 - Alomomola
     */
    public static final PokemonCard ALOMOMOLA = PokemonCard.basic(
            "A3b-021", "Alomomola", "It gently holds injured and weak Pokémon in its fins. Its special membrane heals their wounds.",
            110, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Water Pulse", "Your opponent's Active Pokémon is now Asleep.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new AddStatus(new SleepStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3b-022 - Popplio
     */
    public static final PokemonCard POPPLIO = PokemonCard.basic(
            "A3b-022", "Popplio", "The balloons it inflates with its nose grow larger and larger as it practices day by day.",
            70, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Watering", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3b-023 - Brionne
     */
    public static final PokemonCard BRIONNE = PokemonCard.evolution(
            "A3b-023", "Brionne", "It gets excited when it sees a dance it doesn't know. This hard worker practices diligently until it can learn that dance.",
            1, "Popplio", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Water Gun", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3b-024 - Primarina ex — Hydro Pump's "1 extra" is beyond the 1 the attack costs, as A1-055 Blastoise's reads it.
     */
    public static final PokemonCard PRIMARINA_EX = PokemonCard.evolution(
            "A3b-024", "Primarina ex", "",
            2, "Brionne", 180, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Hydro Pump", "If this Pokémon has at least 1 extra Water Energy attached, this attack does 40 more damage.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(
                            new GreaterThan(new EnergyOn(new Self(), Type.WATER), new Literal(1)),
                            new Literal(80))
                    ), new Literal(40)), new OpponentActive())))
            ), new Action(
                    "Sparkling Aria", "Heal 20 damage from this Pokémon.", EnergyCost.of(Type.WATER, 3), new Attempt(List.of(
                    new DealDamage(new Literal(100), new OpponentActive()),
                    new HealDamage(new Literal(20), new Self())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /** A3b-072 · Vaporeon (alternate art of A3b-016) */
    public static final PokemonCard VAPOREON_IR = VAPOREON.withId("A3b-072", CardRarity.ILLUSTRATION_RARE);

    /** A3b-073 · Glaceon (alternate art of A3b-017) */
    public static final PokemonCard GLACEON_IR = GLACEON.withId("A3b-073", CardRarity.ILLUSTRATION_RARE);

    /** A3b-080 · Primarina ex (alternate art of A3b-024) */
    public static final PokemonCard PRIMARINA_EX_UR = PRIMARINA_EX.withId("A3b-080", CardRarity.ULTRA_RARE);

    /** A3b-088 · Primarina ex (alternate art of A3b-024) */
    public static final PokemonCard PRIMARINA_EX_SIR = PRIMARINA_EX.withId("A3b-088", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    /** A3b-094 · Lapras (shiny of A1-079) */
    public static final PokemonCard LAPRAS_SR = com.tcgpocket.pool.A1.Water.LAPRAS.withId("A3b-094", CardRarity.SHINY_RARE);

    /** A3b-104 · Articuno ex (shiny of A1-084) */
    public static final PokemonCard ARTICUNO_EX_SUR = com.tcgpocket.pool.A1.Water.ARTICUNO_EX.withId("A3b-104", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            VAPOREON, GLACEON, VANILLITE, VANILLISH, VANILLUXE, ALOMOMOLA,
            POPPLIO, BRIONNE, PRIMARINA_EX, VAPOREON_IR, GLACEON_IR, PRIMARINA_EX_UR,
            PRIMARINA_EX_SIR, LAPRAS_SR, ARTICUNO_EX_SUR);

    private Water() {
    }
}
