package com.tcgpocket.pool.A3;

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
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Celestial Guardians — Grass.
 */
public final class Grass {

    /**
     * A3-001 - Exeggcute
     */
    public static final PokemonCard EXEGGCUTE = PokemonCard.basic(
            "A3-001", "Exeggcute", "Though it may look like it's just a bunch of eggs, it's a proper Pokémon. Exeggcute communicates with others of its kind via telepathy, apparently.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Rolling Tackle", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-002 - Alolan Exeggutor
     */
    public static final PokemonCard ALOLAN_EXEGGUTOR = PokemonCard.evolution(
            "A3-002", "Alolan Exeggutor", "Blazing sunlight has brought out the true form and powers of this Pokémon.",
            1, "Exeggcute", 150, Type.GRASS, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Tropical Hammer", "Flip a coin. If tails, this attack does nothing.", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new Fail()),
                    new DealDamage(new Literal(150), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A3-003 - Surskit
     */
    public static final PokemonCard SURSKIT = PokemonCard.basic(
            "A3-003", "Surskit", "They usually live on ponds, but after an evening shower, they may appear on puddles in towns.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Quick Attack", "Flip a coin. If heads, this attack does 20 more damage.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(
                            new Literal(10),
                            new Product(new NumberHeads(), new Literal(20))), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-004 - Masquerain
     */
    public static final PokemonCard MASQUERAIN = PokemonCard.evolution(
            "A3-004", "Masquerain", "It flaps its four wings to hover and fly freely in any direction—to and fro and sideways.",
            1, "Surskit", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                    "Bug Buzz", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-005 - Maractus
     */
    public static final PokemonCard MARACTUS = PokemonCard.basic(
            "A3-005", "Maractus", "With noises that could be mistaken for the rattles of maracas, it creates an upbeat rhythm, startling bird Pokémon and making them fly off in a hurry.",
            90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sting", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-006 - Karrablast
     */
    public static final PokemonCard KARRABLAST = PokemonCard.basic(
            "A3-006", "Karrablast", "Its strange physiology reacts to electrical energy in interesting ways. The presence of a Shelmet will cause this Pokémon to evolve.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Headbutt", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-007 - Phantump
     */
    public static final PokemonCard PHANTUMP = PokemonCard.basic(
            "A3-007", "Phantump", "After a lost child perished in the forest, their spirit possessed a tree stump, causing the spirit's rebirth as this Pokémon.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Hook", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-008 - Trevenant
     */
    public static final PokemonCard TREVENANT = PokemonCard.evolution(
            "A3-008", "Trevenant", "People fear it due to a belief that it devours any who try to cut down trees in its forest, but to the Pokémon it shares its woods with, it's kind.",
            1, "Phantump", 130, Type.GRASS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Claw Slash", "", EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-009 - Rowlet
     */
    public static final PokemonCard ROWLET = PokemonCard.basic(
            "A3-009", "Rowlet", "It sends its feathers, which are as sharp as blades, flying in attack. Its legs are strong, so its kicks are also formidable.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leafage", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-010 - Rowlet
     */
    public static final PokemonCard ROWLET_SKILL_DIVE = PokemonCard.basic(
            "A3-010", "Rowlet", "It sends its feathers, which are as sharp as blades, flying in attack. Its legs are strong, so its kicks are also formidable.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Skill Dive", "This attack does 10 damage to 1 of your opponent's Pokémon.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-011 - Dartrix
     */
    public static final PokemonCard DARTRIX = PokemonCard.evolution(
            "A3-011", "Dartrix", "This narcissistic Pokémon is a clean freak. If you don't groom it diligently, it may stop listening to you.",
            1, "Rowlet", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Razor Wing", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-012 - Decidueye ex
     */
    public static final PokemonCard DECIDUEYE_EX = PokemonCard.evolution(
            "A3-012", "Decidueye ex", "",
            2, "Dartrix", 170, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(
                    new Action(
                            "Pierce the Pain", "This attack does 100 damage to 1 of your opponent's Pokémon that have damage on them.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                            new DealDamage(new Literal(100), new ChosenFrom(
                                    new OpponentAll(), new AttackerSide(), new IsDamaged(), "Select a damaged Pokémon:"))))),
                    new Action(
                            "Razor Leaf", "", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                            new DealDamage(new Literal(80), new OpponentActive()))))
            ), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX);

    /**
     * A3-013 - Grubbin
     */
    public static final PokemonCard GRUBBIN = PokemonCard.basic(
            "A3-013", "Grubbin", "Its natural enemies, like Rookidee, may flee rather than risk getting caught in its large mandibles that can snap thick tree branches.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Gnaw", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-014 - Fomantis
     */
    public static final PokemonCard FOMANTIS = PokemonCard.basic(
            "A3-014", "Fomantis", "Fomantis hates having its naps interrupted. It fires off beams using energy it gathers by bathing in the sun.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leafage", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-015 - Lurantis
     */
    public static final PokemonCard LURANTIS = PokemonCard.evolution(
            "A3-015", "Lurantis", "By masquerading as a bug Pokémon, it lowers the guard of actual bug Pokémon lured in by a scent of sweet flowers. Its sickles bring them down.",
            1, "Fomantis", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Petal Blizzard", "This attack does 20 damage to each of your opponent's Pokémon.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DamageEach(new Literal(20), new OpponentAll())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-016 - Morelull
     */
    public static final PokemonCard MORELULL = PokemonCard.basic(
            "A3-016", "Morelull", "Pokémon living in the forest eat the delicious caps on Morelull's head. The caps regrow overnight.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Stampede", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-017 - Shiinotic
     */
    public static final PokemonCard SHIINOTIC = PokemonCard.evolution(
            "A3-017", "Shiinotic", "Its flickering spores lure in prey and put them to sleep. Once this Pokémon has its prey snoozing, it drains their vitality with its fingertips.",
            1, "Morelull", 100, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Flickering Light", "Flip a coin. If heads, the Defending Pokémon can't attack during your opponent's next turn.", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(
                            new LastCoinTossHeads(),
                            new PreventAttack(new OpponentActive(), new Literal(1)))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-018 - Bounsweet
     */
    public static final PokemonCard BOUNSWEET = PokemonCard.basic(
            "A3-018", "Bounsweet", "Its sweat is sweet, like syrup made from boiled-down fruit. Because of this, Bounsweet was highly valued in the past, when sweeteners were scarce.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Stampede", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-019 - Steenee
     */
    public static final PokemonCard STEENEE = PokemonCard.evolution(
            "A3-019", "Steenee", "Steenee spreads a sweet scent that makes others feel invigorated. This same scent is popular for antiperspirants.",
            1, "Bounsweet", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Double Spin", "Flip 2 coins. This attack does 30 damage for each heads.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Product(new Literal(30), new NumberHeads()), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-020 - Tsareena
     */
    public static final PokemonCard TSAREENA = PokemonCard.evolution(
            "A3-020", "Tsareena", "This Pokémon is proud and aggressive. However, it is said that a Tsareena will instantly become calm if someone touches the crown on its calyx.",
            2, "Steenee", 130, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Three Kick Combo", "Flip 3 coins. This attack does 50 damage for each heads.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new FlipN(new Literal(3)),
                    new DealDamage(new Product(new Literal(50), new NumberHeads()), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A3-021 - Wimpod
     */
    public static final PokemonCard WIMPOD = PokemonCard.basic(
            "A3-021", "Wimpod", "It's nature's cleaner—it eats anything and everything, including garbage and rotten things. The ground near its nest is always clean.",
            70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Gnaw", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE).withAbility(new PassiveAbility("Wimp Out",
            "During your first turn, this Pokémon has no Retreat Cost.",
            new NoRetreatCost(new OwnersFirstTurn())));

    /**
     * A3-022 - Golisopod
     */
    public static final PokemonCard GOLISOPOD = PokemonCard.evolution(
            "A3-022", "Golisopod", "It will do anything to win, taking advantage of every opening and finishing opponents off with the small claws on its front legs.",
            1, "Wimpod", 120, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "First Impression", "If this Pokémon moved from your Bench to the Active Spot this turn, this attack does 60 more damage.", EnergyCost.of(Type.GRASS, 3), new Attempt(List.of(
                    new DealDamage(new Sum(
                            new Literal(60),
                            new Branch(List.of(new Branch.Case(
                                    new For(new MovedToActiveThisTurn(), new Self()), new Literal(60))),
                                    new Literal(0))), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A3-023 - Dhelmise ex
     */
    public static final PokemonCard DHELMISE_EX = PokemonCard.basic(
            "A3-023", "Dhelmise ex", "",
            140, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Anchor Shot", "During your opponent's next turn, the Defending Pokémon can't retreat.", EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new PreventRetreat(new OpponentActive(), new Literal(1))))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX);

    /**
     * A3-024 - Tapu Bulu
     */
    public static final PokemonCard TAPU_BULU = PokemonCard.basic(
            "A3-024", "Tapu Bulu", "Although it's called a guardian deity, it's violent enough to crush anyone it sees as an enemy.",
            120, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Stuck-In Tackle", "Flip a coin. If tails, this Pokémon also does 20 damage to itself.", EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Literal(100), new OpponentActive()),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new DealDamage(new Literal(20), new Self()))))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /** A3-156 · Alolan Exeggutor (alternate art of A3-002) */
    public static final PokemonCard ALOLAN_EXEGGUTOR_IR = ALOLAN_EXEGGUTOR.withId("A3-156", CardRarity.ILLUSTRATION_RARE);
    /** A3-157 · Morelull (alternate art of A3-016) */
    public static final PokemonCard MORELULL_IR = MORELULL.withId("A3-157", CardRarity.ILLUSTRATION_RARE);
    /** A3-158 · Tsareena (alternate art of A3-020) */
    public static final PokemonCard TSAREENA_IR = TSAREENA.withId("A3-158", CardRarity.ILLUSTRATION_RARE);
    /** A3-159 · Tapu Bulu (alternate art of A3-024) */
    public static final PokemonCard TAPU_BULU_IR = TAPU_BULU.withId("A3-159", CardRarity.ILLUSTRATION_RARE);
    /** A3-180 · Decidueye ex (alternate art of A3-012) */
    public static final PokemonCard DECIDUEYE_EX_UR = DECIDUEYE_EX.withId("A3-180", CardRarity.ULTRA_RARE);
    /** A3-181 · Dhelmise ex (alternate art of A3-023) */
    public static final PokemonCard DHELMISE_EX_UR = DHELMISE_EX.withId("A3-181", CardRarity.ULTRA_RARE);
    /** A3-198 · Decidueye ex (alternate art of A3-012) */
    public static final PokemonCard DECIDUEYE_EX_SIR = DECIDUEYE_EX.withId("A3-198", CardRarity.SPECIAL_ILLUSTRATION_RARE);
    /** A3-199 · Dhelmise ex (alternate art of A3-023) */
    public static final PokemonCard DHELMISE_EX_SIR = DHELMISE_EX.withId("A3-199", CardRarity.SPECIAL_ILLUSTRATION_RARE);
    /** A3-210 · Bulbasaur (shiny of A1-001) */
    public static final PokemonCard BULBASAUR_SR = com.tcgpocket.pool.A1.Grass.BULBASAUR.withId("A3-210", CardRarity.SHINY_RARE);
    /** A3-211 · Ivysaur (shiny of A1-002) */
    public static final PokemonCard IVYSAUR_SR = com.tcgpocket.pool.A1.Grass.IVYSAUR.withId("A3-211", CardRarity.SHINY_RARE);
    /** A3-212 · Venusaur (shiny of A1-003) */
    public static final PokemonCard VENUSAUR_SR = com.tcgpocket.pool.A1.Grass.VENUSAUR.withId("A3-212", CardRarity.SHINY_RARE);
    /** A3-213 · Exeggcute (shiny of A3-001) */
    public static final PokemonCard EXEGGCUTE_SR = EXEGGCUTE.withId("A3-213", CardRarity.SHINY_RARE);
    /** A3-214 · Exeggutor (shiny of A1a-002) */
    public static final PokemonCard EXEGGUTOR_SR = com.tcgpocket.pool.A1a.Grass.EXEGGUTOR.withId("A3-214", CardRarity.SHINY_RARE);
    /** A3-230 · Venusaur ex (shiny of A1-004) */
    public static final PokemonCard VENUSAUR_EX_SUR = com.tcgpocket.pool.A1.Grass.VENUSAUR_EX.withId("A3-230", CardRarity.SHINY_ULTRA_RARE);
    /** A3-231 · Exeggutor ex (shiny of A1-023) */
    public static final PokemonCard EXEGGUTOR_EX_SUR = com.tcgpocket.pool.A1.Grass.EXEGGUTOR_EX.withId("A3-231", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            EXEGGCUTE, ALOLAN_EXEGGUTOR, SURSKIT, MASQUERAIN, MARACTUS, KARRABLAST, PHANTUMP, TREVENANT,
            ROWLET, ROWLET_SKILL_DIVE, DARTRIX, DECIDUEYE_EX, GRUBBIN, FOMANTIS, LURANTIS, MORELULL,
            SHIINOTIC, BOUNSWEET, STEENEE, TSAREENA, WIMPOD, GOLISOPOD, DHELMISE_EX, TAPU_BULU,
            ALOLAN_EXEGGUTOR_IR, MORELULL_IR, TSAREENA_IR, TAPU_BULU_IR, DECIDUEYE_EX_UR, DHELMISE_EX_UR,
            DECIDUEYE_EX_SIR, DHELMISE_EX_SIR,
            BULBASAUR_SR, IVYSAUR_SR, VENUSAUR_SR, EXEGGCUTE_SR, EXEGGUTOR_SR, VENUSAUR_EX_SUR, EXEGGUTOR_EX_SUR);

    private Grass() {
    }
}
