package com.tcgpocket.pool.A1;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardTypeEnergy;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.PreventSupporter;
import com.tcgpocket.effect.ReduceDamage;
import com.tcgpocket.effect.SwitchActive;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;

import java.util.List;

/**
 * Genetic Apex — Psychic.
 */
public final class Psychic {

    /**
     * A1-113 - Clefairy
     */
    public static final PokemonCard CLEFAIRY = PokemonCard.basic(
                    "A1-113", "Clefairy", "It is said that happiness will come to those who see a gathering of Clefairy dancing under a full moon.",
                    60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Slap", "", EnergyCost.of(Type.PSYCHIC, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.METAL);
    /**
     * A1-114 - Clefable
     */
    public static final PokemonCard CLEFABLE = PokemonCard.evolution(
                    "A1-114", "Clefable", "A timid fairy Pokémon that is rarely seen, it will run and hide the moment it senses people.",
                    1, "Clefairy", 100, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Magical Shot", "", EnergyCost.of(Type.PSYCHIC, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.METAL);

    /**
     * A1-115 - Abra
     */
    public static final PokemonCard ABRA = PokemonCard.basic(
                    "A1-115", "Abra", "This Pokémon uses its psychic powers while it sleeps. The contents of Abra's dreams affect the powers that the Pokémon wields.",
                    60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Teleport", "Switch this Pokémon with 1 of your Benched Pokémon.", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new SwitchActive(new SelfSide())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.DARKNESS);
    
    /**
     * A1-116 - Kadabra
     */
    public static final PokemonCard KADABRA = PokemonCard.evolution(
                    "A1-116", "Kadabra", "Using its psychic power, Kadabra levitates as it sleeps. It uses its springy tail as a pillow.",
                    1, "Abra", 80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Super Psy Bolt", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(60), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-117 - Alakazam
     */
    public static final PokemonCard ALAKAZAM = PokemonCard.evolution(
                    "A1-117", "Alakazam", "It has an incredibly high level of intelligence. Some say that Alakazam remembers everything that ever happens to it, from birth till death.",
                    2, "Kadabra", 130, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Psychic", "This attack does 30 more damage for each Energy attached to your opponent's Active Pokémon.", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Sum(new Literal(60), new Product(new Literal(30), new EnergyOn(new OpponentActive()))), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-118 - Slowpoke
     */
    public static final PokemonCard SLOWPOKE = PokemonCard.basic(
                    "A1-118", "Slowpoke", "It is incredibly slow and dopey. It takes five seconds for it to feel pain when under attack.",
                    70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Tail Whap", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-119 - Slowbro
     */
    public static final PokemonCard SLOWBRO = PokemonCard.evolution(
                    "A1-119", "Slowbro", "When a Slowpoke went hunting in the sea, its tail was bitten by a Shellder. That made it evolve into Slowbro.",
                    1, "Slowpoke", 130, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Super Psy Bolt", "", EnergyCost.of(Type.PSYCHIC, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(80), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-120 - Gastly
     */
    public static final PokemonCard GASTLY = PokemonCard.basic(
                    "A1-120", "Gastly", "It wraps its opponent in its gas-like body, slowly weakening its prey by poisoning it through the skin.",
                    60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Suffocating Gas", "", EnergyCost.of(Type.PSYCHIC, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-121 - Haunter
     */
    public static final PokemonCard HAUNTER = PokemonCard.evolution(
                    "A1-121", "Haunter", "It likes to lurk in the dark and tap shoulders with a gaseous hand. Its touch causes endless shuddering.",
                    1, "Gastly", 80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Will-O-Wisp", "", EnergyCost.of(Type.PSYCHIC, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-122 - Gengar
     */
    public static final PokemonCard GENGAR = PokemonCard.evolution(
                    "A1-122", "Gengar", "To steal the life of its target, it slips into the prey's shadow and silently waits for an opportunity.",
                    2, "Haunter", 130, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Bother", "Your opponent can't use any Supporter cards from their hand during their next turn.", EnergyCost.of(Type.PSYCHIC, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(50), new OpponentActive()),
                                    new PreventSupporter(new Literal(1))
                            )))), CardRarity.RARE)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-123 - Gengar ex
     */
    public static final PokemonCard GENGAR_EX = PokemonCard.basic(
                    "A1-123", "Gengar ex", "",
                    170, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Spooky Shot", "", EnergyCost.of(Type.PSYCHIC, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(100), new OpponentActive())
                            )))), CardRarity.DOUBLE_RARE)
            .withWeakness(Type.DARKNESS)
            .withTags(CardTag.EX);

    /**
     * A1-124 - Drowzee
     */
    public static final PokemonCard DROWZEE = PokemonCard.basic(
                    "A1-124", "Drowzee", "It remembers every dream it eats. It rarely eats the dreams of adults because children's are much tastier.",
                    70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Mumble", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-125 - Hypno
     */
    public static final PokemonCard HYPNO = PokemonCard.evolution(
                    "A1-125", "Hypno", "When it locks eyes with an enemy, it will use a mix of psi moves, such as Hypnosis and Confusion.",
                    1, "Drowzee", 100, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Psypunch", "", EnergyCost.of(Type.PSYCHIC, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(50), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-126 - Mr. Mime
     */
    public static final PokemonCard MR_MIME = PokemonCard.basic(
                    "A1-126", "Mr. Mime", "The broadness of its hands may be no coincidence—many scientists believe its palms became enlarged specifically for pantomiming.",
                    80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Barrier Attack", "During your opponent's next turn, this Pokémon takes -20 damage from attacks.", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive()),
                                    new ReduceDamage(new Literal(20), new Self(), new Literal(1))
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-127 - Jynx
     */
    public static final PokemonCard JYNX = PokemonCard.basic(
                    "A1-127", "Jynx", "Its strange cries sound like human language. There are some musicians who compose songs for Jynx to sing.",
                    80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Psychic", "This attack does 20 more damage for each Energy attached to your opponent's Active Pokémon.", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Sum(new Literal(30), new Product(new Literal(20), new EnergyOn(new OpponentActive()))), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-128 - Mewtwo
     */
    public static final PokemonCard MEWTWO = PokemonCard.basic(
                    "A1-128", "Mewtwo", "It was created by a scientist after years of horrific gene-splicing and DNA-engineering experiments.",
                    120, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Power Blast", "Discard 2 {P} Energy from this Pokémon.", EnergyCost.of(Type.PSYCHIC, 2, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(120), new OpponentActive()),
                                    new DiscardTypeEnergy(Type.PSYCHIC, new Literal(2), new Self())
                            )))), CardRarity.RARE)
            .withWeakness(Type.DARKNESS);


    /**
     * A1-129 · Mewtwo ex — Psychic Sphere for 50, or Psydrive for 150 at the
     * cost of two Energy.
     *
     * <p>Note the order in Psydrive: the damage lands and <em>then</em> the
     * Energy goes. Reversing it would be a different card — the discard would
     * be a cost that could fail, which under an attempt's short-circuit would
     * cancel the damage.
     */
    public static final PokemonCard MEWTWO_EX = PokemonCard.basic(
                    "A1-129", "Mewtwo ex", "", 150, Type.PSYCHIC,
                    EnergyCost.of(Type.COLORLESS, 2),
                    List.of(
                            new Action(
                                    "Psychic Sphere",
                                    "This attack does 50 damage.",
                                    EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1),
                                    new Attempt(List.of(
                                            new DealDamage(new Literal(50), new OpponentActive())))),
                            new Action(
                                    "Psydrive",
                                    "This attack does 150 damage. Discard 2 Psychic Energy from this Pokemon.",
                                    EnergyCost.of(Type.PSYCHIC, 2, Type.COLORLESS, 2),
                                    new Attempt(List.of(
                                            new DealDamage(new Literal(150), new OpponentActive()),
                                            new DiscardTypeEnergy(
                                                    Type.PSYCHIC, new Literal(2), new Self()))))),
                    CardRarity.DOUBLE_RARE)
            .withWeakness(Type.DARKNESS)
            .withTags(CardTag.EX);

    /**
     * A1-130 - Ralts
     */
    public static final PokemonCard RALTS = PokemonCard.basic(
                    "A1-130", "Ralts", "The horns on its head provide a strong power that enables it to sense people's emotions.",
                    60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Ram", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-131 - Kirlia
     */
    public static final PokemonCard KIRLIA = PokemonCard.evolution(
                    "A1-131", "Kirlia", "It has a psychic power that enables it to distort the space around it and see into the future.",
                    1, "Ralts", 80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Smack", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-132 - Gardevoir
     */
    public static final PokemonCard GARDEVOIR = PokemonCard.evolution(
                    "A1-132", "Gardevoir", "To protect its Trainer, it will expend all its psychic power to create a small black hole.",
                    2, "Kirlia", 110, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Psyshot", "", EnergyCost.of(Type.PSYCHIC, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(60), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-133 - Woobat
     */
    public static final PokemonCard WOOBAT = PokemonCard.basic(
                    "A1-133", "Woobat", "While inside a cave, if you look up and see lots of heart-shaped marks lining the walls, it's evidence that Woobat live there.",
                    60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Gnaw", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-134 - Swoobat
     */
    public static final PokemonCard SWOOBAT = PokemonCard.evolution(
                    "A1-134", "Swoobat", "Emitting powerful sound waves tires it out. Afterward, it won't be able to fly for a little while.",
                    1, "Woobat", 90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Heart Stamp", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(60), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-135 - Golett
     */
    public static final PokemonCard GOLETT = PokemonCard.basic(
                    "A1-135", "Golett", "They were sculpted from clay in ancient times. No one knows why, but some of them are driven to continually line up boulders.",
                    90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Mega Punch", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(50), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.DARKNESS);

    /**
     * A1-136 - Golurk
     */
    public static final PokemonCard GOLURK = PokemonCard.evolution(
                    "A1-136", "Golurk", "Artillery platforms built into the walls of ancient castles served as perches from which Golurk could fire energy beams.",
                    1, "Golett", 140, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                            "Double Lariat", "Flip 2 coins. This attack does 100 damage for each heads.", EnergyCost.of(Type.PSYCHIC, 2, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new FlipN(new Literal(2)),
                                    new DealDamage(new Product(new Literal(100), new NumberHeads()), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.DARKNESS);

    static final List<PokemonCard> CARDS = List.of(
            CLEFAIRY, CLEFABLE, ABRA, KADABRA, ALAKAZAM, SLOWPOKE, SLOWBRO, GASTLY, HAUNTER, GENGAR, GENGAR_EX,
            DROWZEE, HYPNO, MR_MIME, JYNX, MEWTWO, MEWTWO_EX, RALTS, KIRLIA, GARDEVOIR, WOOBAT, SWOOBAT, GOLETT, GOLURK);

    private Psychic() {
    }
}
