package com.tcgpocket.pool.A1;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.IsType;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.*;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.target.*;

import java.util.List;
import java.util.Optional;

/**
 * Genetic Apex — Lightning.
 */
public final class Lightning {

    /**
     * A1-094 - Pikachu
     */
    public static final PokemonCard PIKACHU = PokemonCard.basic(
                    "A1-094", "Pikachu", "When it is angered, it immediately discharges the energy stored in the pouches in its cheeks.",
                    60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Gnaw", "", EnergyCost.of(Type.LIGHTNING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);
    /**
     * A1-095 - Raichu
     */
    public static final PokemonCard RAICHU = PokemonCard.evolution(
                    "A1-095", "Raichu", "Its tail discharges electricity into the ground, protecting it from getting shocked.",
                    1, "Pikachu", 100, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Thunderbolt", "Discard all Energy from this Pokémon.", EnergyCost.of(Type.LIGHTNING, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(140), new OpponentActive()),
                                    new DiscardAllEnergy(new Self())
                            )))), CardRarity.RARE)
            .withWeakness(Type.FIGHTING);
    /**
     * A1-096 - Pikachu ex
     */
    public static final PokemonCard PIKACHU_EX = PokemonCard.basic(
                    "A1-096", "Pikachu ex", "",
                    120, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Circle Circuit", "This attack does 30 damage for each of your Benched Lightning Pokémon.", EnergyCost.of(Type.LIGHTNING, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Product(
                                            new Literal(30), new CountCards(new AttackerSide(), Zone.BENCH, Optional.of(new IsType(Type.LIGHTNING)))
                                    ), new OpponentActive())
                            )))), CardRarity.DOUBLE_RARE)
            .withWeakness(Type.FIGHTING).withTags(CardTag.EX);
    /**
     * A1-097 - Magnemite
     */
    public static final PokemonCard MAGNEMITE = PokemonCard.basic(
                    "A1-097", "Magnemite", "The electromagnetic waves emitted by the units at the sides of its head expel antigravity, which allows it to float.",
                    60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Lightning Ball", "", EnergyCost.of(Type.LIGHTNING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-098 - Magneton
     */
    public static final PokemonCard MAGNETON = PokemonCard.evolution(
                    "A1-098", "Magneton", "Three Magnemite are linked by a strong magnetic force. Earaches will occur if you get too close.",
                    1, "Magnemite", 80, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Spinning Attack", "", EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(60), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.FIGHTING)
            .withAbility(new ActivatedAbility(
                    "Volt Charge", "Once during your turn, you may take a Lightning Energy from your Energy Zone and attack it to this Pokémon.",
                    new PlainAction("", new Attempt(List.of(
                            new AttachEnergy(Type.LIGHTNING, new Self())
                    ))), true, new Always<>()
            ));

    /**
     * A1-099 - Voltorb
     */
    public static final PokemonCard VOLTORB = PokemonCard.basic(
                    "A1-099", "Voltorb", "It rolls to move. If the ground is uneven, a sudden jolt from hitting a bump can cause it to explode.",
                    60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Tackle", "", EnergyCost.of(Type.LIGHTNING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-100 - Electrode
     */
    public static final PokemonCard ELECTRODE = PokemonCard.evolution(
                    "A1-100", "Electrode", "The more energy it charges up, the faster it gets. But this also makes it more likely to explode.",
                    1, "Voltorb", 80, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                            "Electro Ball", "", EnergyCost.of(Type.LIGHTNING, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-101 - Electabuzz
     */
    public static final PokemonCard ELECTABUZZ = PokemonCard.basic(
                    "A1-101", "Electabuzz", "Many power plants keep Ground-type Pokémon around as a defense against Electabuzz that come seeking electricity.",
                    70, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Thunder Punch", "Flip a coin. If heads, this attack does 40 more damage. If tails, this Pokémon also does 20 damage to itself.", EnergyCost.of(Type.LIGHTNING, 2),
                            new Attempt(List.of(
                                    new FlipN(new Literal(1)),
                                    new DealDamage(new Sum(
                                            new Literal(40),
                                            new Product(new Literal(20), new NumberHeads())
                                    ), new OpponentActive()),
                                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new DealDamage(new Literal(20), new Self()))
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-102 - Jolteon
     */
    public static final PokemonCard JOLTEON = PokemonCard.evolution(
                    "A1-102", "Jolteon", "It concentrates the weak electric charges emitted by its cells and launches wicked lightning bolts.",
                    1, "Eevee", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Pin Missile", "Flip 4 coins. This attack does 40 damage for each heads.", EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new FlipN(new Literal(4)),
                                    new DealDamage(new Product(new Literal(40), new NumberHeads()), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-103 - Zapdos
     */
    public static final PokemonCard ZAPDOS = PokemonCard.basic(
                    "A1-103", "Zapdos", "This Pokémon has complete control over electricity. There are tales of Zapdos nesting in the dark depths of pitch-black thunderclouds.",
                    100, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Raging Thunder", "This attack also does 30 damage to 1 of your Benched Pokémon.", EnergyCost.of(Type.LIGHTNING, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(100), new OpponentActive()),
                                    new DealDamage(new Literal(30), new RandomFrom(new AttackerBench()))
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-104 - Zapdos ex
     */
    public static final PokemonCard ZAPDOS_EX = PokemonCard.basic(
                    "A1-104", "Zapdos ex", "",
                    130, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Peck", "", EnergyCost.of(Type.LIGHTNING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            ))), new Action(
                            "Thundering Hurricane", "Flip 4 coins. This attack does 50 damage for each heads.", EnergyCost.of(Type.LIGHTNING, 3),
                            new Attempt(List.of(
                                    new FlipN(new Literal(4)),
                                    new DealDamage(new Product(new Literal(50), new NumberHeads()), new OpponentActive())
                            )))), CardRarity.DOUBLE_RARE)
            .withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A1-105 - Blitzle
     */
    public static final PokemonCard BLITZLE = PokemonCard.basic(
                    "A1-105", "Blitzle", "When thunderclouds cover the sky, it will appear. It can catch lightning with its mane and store the electricity.",
                    60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Zap Kick", "", EnergyCost.of(Type.LIGHTNING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-106 - Zebstrika
     */
    public static final PokemonCard ZEBSTRIKA = PokemonCard.evolution(
                    "A1-106", "Zebstrika", "When this ill-tempered Pokémon runs wild, it shoots lightning from its mane in all directions.",
                    1, "Blitzle", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Thunder Spear", "This attack does 30 damage to 1 of your opponent's Pokémon.", EnergyCost.of(Type.LIGHTNING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"))
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-107 - Tynamo
     */
    public static final PokemonCard TYNAMO = PokemonCard.basic(
                    "A1-107", "Tynamo", "While one alone doesn't have much power, a chain of many Tynamo can be as powerful as lightning.",
                    30, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Tiny Charge", "", EnergyCost.of(Type.LIGHTNING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-108 - Eelektrik
     */
    public static final PokemonCard EELEKTRIK = PokemonCard.evolution(
                    "A1-108", "Eelektrik", "They coil around foes and shock them with electricity-generating organs that seem simply to be circular patterns.",
                    1, "Tynamo", 80, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Head Bolt", "", EnergyCost.of(Type.LIGHTNING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-109 - Eelektross
     */
    public static final PokemonCard EELEKTROSS = PokemonCard.evolution(
                    "A1-109", "Eelektross", "They crawl out of the ocean using their arms. They will attack prey on shore and immediately drag it into the ocean.",
                    2, "Eelektrik", 140, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Thunder Fang", "Flip a coin. If heads, your opponent's Active Pokémon is now Paralyzed.", EnergyCost.of(Type.LIGHTNING, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new FlipN(new Literal(1)),
                                    new DealDamage(new Literal(80), new OpponentActive()),
                                    new ConditionalEffect(
                                            new LastCoinTossHeads(),
                                            new AddStatus(new ParalysisStatus(),
                                                    new OpponentActive()))
                            )))), CardRarity.RARE)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-110 - Helioptile
     */
    public static final PokemonCard HELIOPTILE = PokemonCard.basic(
                    "A1-110", "Helioptile", "When spread, the frills on its head act like solar panels, generating the power behind this Pokémon's electric moves.",
                    60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Tail Whap", "", EnergyCost.of(Type.LIGHTNING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-111 - Heliolisk
     */
    public static final PokemonCard HELIOLISK = PokemonCard.evolution(
                    "A1-111", "Heliolisk", "One Heliolisk basking in the sun with its frill outspread is all it would take to produce enough electricity to power a city.",
                    1, "Helioptile", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Quick Attack", "Flip a coin. If heads, this attack does 40 more damage.", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new FlipN(new Literal(1)),
                                    new DealDamage(new Sum(
                                            new Literal(40),
                                            new Product(new Literal(40), new NumberHeads())
                                    ), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-112 - Pincurchin
     */
    public static final PokemonCard PINCURCHIN = PokemonCard.basic(
                    "A1-112", "Pincurchin", "This Pokémon generates electricity when it digests food. It uses its five hard teeth to scrape seaweed off surfaces and eat it.",
                    70, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Thunder Shock", "Flip a coin. If heads, your opponent's Active Pokémon is now Paralyzed.", EnergyCost.of(Type.LIGHTNING, 2),
                            new Attempt(List.of(
                                    new FlipN(new Literal(1)),
                                    new DealDamage(new Literal(30), new OpponentActive()),
                                    new ConditionalEffect(
                                            new LastCoinTossHeads(),
                                            new AddStatus(new ParalysisStatus(),
                                                    new OpponentActive()))
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    static final List<PokemonCard> CARDS = List.of(PIKACHU, RAICHU, PIKACHU_EX, MAGNEMITE, MAGNETON,
            VOLTORB, ELECTRODE, ELECTABUZZ, JOLTEON, ZAPDOS, BLITZLE, ZEBSTRIKA, TYNAMO, EELEKTRIK, EELEKTROSS,
            HELIOPTILE, HELIOLISK, PINCURCHIN);

    private Lightning() {
    }
}
