package com.tcgpocket.pool.A1;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.*;

import java.lang.classfile.attribute.StackMapFrameInfo;
import java.util.List;

public final class Water {
    /**
     * A1-053 - Squirtle
     */
    public static final PokemonCard SQUIRTLE = PokemonCard.basic(
                    "A1-053", "Squirtle", "When it retracts its long neck into its shell, it squirts out water with vigorous force.",
                    60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Water Gun", "", EnergyCost.of(Type.WATER, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-054 - Wartortle
     */
    public static final PokemonCard WARTORTLE = PokemonCard.evolution(
                    "A1-054", "Wartortle", "It is recognized as a symbol of longevity. If its shell has algae on it, that Wartortle is very old.",
                    1, "Squirtle", 80, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Wave Splash", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-055 - Blastoise
     */
    public static final PokemonCard BLASTOISE = PokemonCard.evolution(
                    "A1-055", "Blastoise", "It crushes its foe under its heavy body to cause fainting. In a pinch, it will withdraw inside its shell.",
                    2, "Wartortle", 150, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Hydro Pump", "If this Pokémon has at least 2 extra Water Energy attached, this attack does 60 more damage.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Branch(
                                            List.of(new Branch.Case(
                                                    new GreaterThan(new EnergyOn(new Self(), Type.WATER), new Literal(3)),
                                                    new Literal(140))),
                                            new Literal(80)), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-056 - Blastoise ex
     */
    public static final PokemonCard BLASTOISE_EX = PokemonCard.evolution(
                    "A1-056", "Blastoise ex", "",
                    2, "Wartortle", 180, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Surf", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            ))), new Action(
                            "Hydro Bazooka", "If this Pokémon has at least 2 extra Water Energy attached, this attack does 60 more damage.", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Branch(
                                            List.of(new Branch.Case(
                                                    new GreaterThan(new EnergyOn(new Self(), Type.WATER), new Literal(4)),
                                                    new Literal(160)
                                            )), new Literal(100)
                                    ), new OpponentActive())
                            )))), CardRarity.DOUBLE_RARE)
            .withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A1-057 - Psyduck
     */
    public static final PokemonCard PSYDUCK = PokemonCard.basic(
                    "A1-057", "Psyduck", "It is constantly wracked by a headache. When the headache turns intense, it begins using mysterious powers.",
                    60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Headache", "Your opponent can't use any Supporter cards from their hand during their next turn.", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive()),
                                    new PreventSupporter(new Literal(1))
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-058 - Golduck
     */
    public static final PokemonCard GOLDUCK = PokemonCard.evolution(
                    "A1-058", "Golduck", "When it swims at full speed using its long, webbed limbs, its forehead somehow begins to glow.",
                    1, "Psyduck", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Aqua Edge", "", EnergyCost.of(Type.WATER, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-059 - Poliwag
     */
    public static final PokemonCard POLIWAG = PokemonCard.basic(
                    "A1-059", "Poliwag", "For Poliwag, swimming is easier than walking. The swirl pattern on its belly is actually part of the Pokémon's innards showing through the skin.",
                    60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Razor Fin", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-060 - Poliwhirl
     */
    public static final PokemonCard POLIWHIRL = PokemonCard.evolution(
                    "A1-060", "Poliwhirl", "Staring at the swirl on its belly causes drowsiness. This trait of Poliwhirl's has been used in place of lullabies to get children to go to sleep.",
                    1, "Poliwag", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Knuckle Punch", "", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-061 - Poliwrath
     */
    public static final PokemonCard POLIWRATH = PokemonCard.evolution(
                    "A1-061", "Poliwrath", "Its body is solid muscle. When swimming through cold seas, Poliwrath uses its impressive arms to smash through drift ice and plow forward.",
                    2, "Poliwhirl", 150, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Mega Punch", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(80), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-062 - Tentacool
     */
    public static final PokemonCard TENTACOOL = PokemonCard.basic(
                    "A1-062", "Tentacool", "Tentacool is not a particularly strong swimmer. It drifts across the surface of shallow seas as it searches for prey.",
                    60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Gentle Slap", "", EnergyCost.of(Type.WATER, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-063 - Tentacruel
     */
    public static final PokemonCard TENTACRUEL = PokemonCard.evolution(
                    "A1-063", "Tentacruel", "When the red orbs on Tentacruel's head glow brightly, watch out. The Pokémon is about to fire off a burst of ultrasonic waves.",
                    1, "Tentacool", 110, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Poison Tentacles", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(50), new OpponentActive()),
                                    new AddStatus(new PoisonStatus(), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-064 - Seel
     */
    public static final PokemonCard SEEL = PokemonCard.basic(
                    "A1-064", "Seel", "Thanks to its thick fat, cold seas don't bother it at all, but it gets tired pretty easily in warm waters.",
                    80, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Headbutt", "", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-065 - Dewgong
     */
    public static final PokemonCard DEWGONG = PokemonCard.evolution(
                    "A1-065", "Dewgong", "It sunbathes on the beach after meals. The rise in its body temperature helps its digestion.",
                    1, "Seel", 120, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Surf", "", EnergyCost.of(Type.WATER, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(90), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-066 - Shellder
     */
    public static final PokemonCard SHELLDER = PokemonCard.basic(
                    "A1-066", "Shellder", "It is encased in a shell that is harder than diamond. Inside, however, it is surprisingly tender.",
                    60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Tongue Slap", "", EnergyCost.of(Type.WATER, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-067 - Cloyster
     */
    public static final PokemonCard CLOYSTER = PokemonCard.evolution(
                    "A1-067", "Cloyster", "Cloyster that live in seas with harsh tidal currents grow large, sharp spikes on their shells.",
                    1, "Shellder", 120, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Surf", "", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-068 - Krabby
     */
    public static final PokemonCard KRABBY = PokemonCard.basic(
                    "A1-068", "Krabby", "It can be found near the sea. The large pincers grow back if they are torn out of their sockets.",
                    70, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Vise Grip", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-069 - Kingler
     */
    public static final PokemonCard KINGLER = PokemonCard.evolution(
                    "A1-069", "Kingler", "Its large and hard pincer has 10,000-horsepower strength. However, being so big, it is unwieldy to move.",
                    1, "Krabby", 120, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "KO Crab", "Flip 2 coins. If both of them are heads, this attack does 80 more damage.", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new FlipN(new Literal(2)),
                                    new DealDamage(new Branch(
                                            List.of(new Branch.Case(
                                                    new EqualTo(new NumberHeads(), new Literal(2)),
                                                    new Literal(160))),
                                            new Literal(80)), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-070 - Horsea
     */
    public static final PokemonCard HORSEA = PokemonCard.basic(
                    "A1-070", "Horsea", "Horsea makes its home in oceans with gentle currents. If this Pokémon is under attack, it spits out pitch-black ink and escapes.",
                    60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Water Gun", "", EnergyCost.of(Type.WATER, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-071 - Seadra
     */
    public static final PokemonCard SEADRA = PokemonCard.evolution(
                    "A1-071", "Seadra", "It's the males that raise the offspring. While Seadra are raising young, the spines on their backs secrete thicker and stronger poison.",
                    1, "Horsea", 70, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Water Arrow", "This attack does 50 damage to 1 of your opponent's Pokémon.", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(50), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target"))
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-072 - Goldeen
     */
    public static final PokemonCard GOLDEEN = PokemonCard.basic(
                    "A1-072", "Goldeen", "Its dorsal, pectoral, and tail fins wave elegantly in water. That is why it is known as the Water Dancer.",
                    60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Flop", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-073 - Seaking
     */
    public static final PokemonCard SEAKING = PokemonCard.evolution(
                    "A1-073", "Seaking", "In autumn, its body becomes more fatty in preparing to propose to a mate. It takes on beautiful colors.",
                    1, "Goldeen", 100, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Horn Hazard", "Flip a coin. If tails, this attack does nothing.", EnergyCost.of(Type.WATER, 1),
                            new Attempt(List.of(
                                    new FlipN(new Literal(1)),
                                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new Fail()),
                                    new DealDamage(new Literal(80), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-074 - Staryu
     */
    public static final PokemonCard STARYU = PokemonCard.basic(
                    "A1-074", "Staryu", "If you visit a beach at the end of summer, you'll be able to see groups of Staryu lighting up in a steady rhythm.",
                    50, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Smack", "", EnergyCost.of(Type.WATER, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-075 - Starmie
     */
    public static final PokemonCard STARMIE = PokemonCard.evolution(
                    "A1-075", "Starmie", "This Pokémon has an organ known as its core. The organ glows in seven colors when Starmie is unleashing its potent psychic powers.",
                    1, "Staryu", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                            "Wave Splash", "", EnergyCost.of(Type.WATER, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-076 - Starmie ex
     */
    public static final PokemonCard STARMIE_EX = PokemonCard.evolution(
                    "A1-076", "Starmie ex", "",
                    1, "Staryu", 130, Type.WATER, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                            "Hydro Splash", "", EnergyCost.of(Type.WATER, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(90), new OpponentActive())
                            )))), CardRarity.DOUBLE_RARE)
            .withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A1-077 - Magikarp
     */
    public static final PokemonCard MAGIKARP = PokemonCard.basic(
                    "A1-077", "Magikarp", "An underpowered, pathetic Pokémon. It may jump high on rare occasions but never more than seven feet.",
                    30, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Splash", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-078 - Gyarados
     */
    public static final PokemonCard GYARADOS = PokemonCard.evolution(
                    "A1-078", "Gyarados", "Once it appears, it goes on a rampage. It remains enraged until it demolishes everything around it.",
                    1, "Magikarp", 150, Type.WATER, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                            "Hyper Beam", "Discard a random Energy from your opponent's Active Pokémon.", EnergyCost.of(Type.WATER, 4),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(100), new OpponentActive()),
                                    new DiscardRandomEnergy(new Literal(1), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-079 - Lapras
     */
    public static final PokemonCard LAPRAS = PokemonCard.basic(
                    "A1-079", "Lapras", "A smart and kindhearted Pokémon, it glides across the surface of the sea while its beautiful song echoes around it.",
                    100, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Hydro Pump", "If this Pokémon has at least 3 extra Water Energy attached, this attack does 70 more damage.", EnergyCost.of(Type.WATER, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Branch(
                                            List.of(new Branch.Case(new GreaterThan(
                                                    new EnergyOn(new Self(), Type.WATER),
                                                    new Literal(4)),
                                                    new Literal(90))),
                                            new Literal(20)
                                    ), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-080 - Vaporeon
     */
    public static final PokemonCard VAPOREON = PokemonCard.evolution(
                    "A1-080", "Vaporeon", "It lives close to water. Its long tail is ridged with a fin, which is often mistaken for a mermaid's.",
                    1, "Eevee", 130, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Bubble Drain", "Heal 30 damage from this Pokémon.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(60), new OpponentActive()),
                                    new HealDamage(new Literal(30), new Self())
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-081 - Omanyte
     */
    public static final PokemonCard OMANYTE = PokemonCard.evolution(
                    "A1-081", "Omanyte", "Because some Omanyte manage to escape after being restored or are released into the wild by people, this species is becoming a problem.",
                    1, "Helix Fossil", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Water Gun", "", EnergyCost.of(Type.WATER, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-082 - Omastar
     */
    public static final PokemonCard OMASTAR = PokemonCard.evolution(
                    "A1-082", "Omastar", "Weighed down by a large and heavy shell, Omastar couldn't move very fast. Some say it went extinct because it was unable to catch food.",
                    2, "Omanyte", 140, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Ancient Whirlpool", "During your opponent's next turn, the Defending Pokémon can't attack.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive()),
                                    new PreventAttack(new OpponentActive(), new Literal(1))
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-083 - Articuno
     */
    public static final PokemonCard ARTICUNO = PokemonCard.basic(
                    "A1-083", "Articuno", "It's said that this Pokémon's beautiful blue wings are made of ice. Articuno flies over snowy mountains, its long tail fluttering along behind it.",
                    100, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Ice Beam", "Flip a coin. If heads, your opponent's Active Pokémon is now Paralyzed.", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(60), new OpponentActive()),
                                    new FlipN(new Literal(1)),
                                    new ConditionalEffect(new LastCoinTossHeads(),
                                            new AddStatus(new ParalysisStatus(), new OpponentActive()))
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-084 - Articuno ex
     */
    public static final PokemonCard ARTICUNO_EX = PokemonCard.basic(
                    "A1-084", "Articuno ex", "",
                    140, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Ice Wing", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            ))), new Action(
                            "Blizzard", "This attack also does 10 damage to each of your opponent's Benched Pokémon.", EnergyCost.of(Type.WATER, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(100), new OpponentActive()),
                                    new DamageEach(new Literal(10), new OpponentBench()))
                            ))), CardRarity.DOUBLE_RARE)
            .withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A1-085 - Ducklett
     */
    public static final PokemonCard DUCKLETT = PokemonCard.basic(
                    "A1-085", "Ducklett", "When attacked, it uses its feathers to splash water, escaping under cover of the spray.",
                    50, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Flap", "", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-086 - Swanna
     */
    public static final PokemonCard SWANNA = PokemonCard.evolution(
                    "A1-086", "Swanna", "Despite their elegant appearance, they can flap their wings strongly and fly for thousands of miles.",
                    1, "Ducklett", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Wing Attack", "", EnergyCost.of(Type.COLORLESS, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-087 - Froakie
     */
    public static final PokemonCard FROAKIE = PokemonCard.basic(
                    "A1-087", "Froakie", "It secretes flexible bubbles from its chest and back. The bubbles reduce the damage it would otherwise take when attacked.",
                    60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Flop", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-088 - Frogadier
     */
    public static final PokemonCard FROGADIER = PokemonCard.evolution(
                    "A1-088", "Frogadier", "It can throw bubble-covered pebbles with precise control, hitting empty cans up to a hundred feet away.",
                    1, "Froakie", 80, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Water Drip", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-089 - Greninja
     */
    public static final PokemonCard GRENINJA = PokemonCard.evolution(
                    "A1-089", "Greninja", "It creates throwing stars out of compressed water. When it spins them and throws them at high speed, these stars can split metal in two.",
                    2, "Frogadier", 120, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Mist Slash", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(60), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING)
            .withAbility(new ActivatedAbility(
                    "Water Shuriken", "Once during your turn, you may do 20 damage to 1 of your opponent's Pokémon.",
                    new PlainAction("", new Attempt(List.of(
                            new DealDamage(new Literal(20), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target"))
                    ))), true, new Always<>()
            ));

    /**
     * A1-090 - Pyukumuku
     */
    public static final PokemonCard PYUKUMUKU = PokemonCard.basic(
                    "A1-090", "Pyukumuku", "It lives in warm, shallow waters. If it encounters a foe, it will spit out its internal organs as a means to punch them.",
                    70, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Rain Splash", "", EnergyCost.of(Type.WATER, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-091 - Bruxish
     */
    public static final PokemonCard BRUXISH = PokemonCard.basic(
                    "A1-091", "Bruxish", "It grinds its teeth with great force to stimulate its brain. It fires the psychic energy created by this process from the protuberance on its head.",
                    90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Second Strike", "If your opponent's Active Pokémon has damage on it, this attack does 60 more damage.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Branch(List.of(
                                            new Branch.Case(new For(new IsDamaged(), new OpponentActive()), new Literal(70))
                                    ), new Literal(10)), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-092 - Snom
     */
    public static final PokemonCard SNOM = PokemonCard.basic(
                    "A1-092", "Snom", "It eats snow that has accumulated on the ground. It prefers soft, freshly fallen snow, so it will eat its way up a mountain, aiming for the peak.",
                    50, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Ram", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.METAL);

    /**
     * A1-093 - Frosmoth
     */
    public static final PokemonCard FROSMOTH = PokemonCard.evolution(
                    "A1-093", "Frosmoth", "Frosmoth senses air currents with its antennae. It sends its scales drifting on frigid air, making them fall like snow.",
                    1, "Snom", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Powder Snow", "Your opponent's Active Pokémon is now Asleep.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive()),
                                    new AddStatus(new SleepStatus(), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.METAL);

    static final List<PokemonCard> CARDS = List.of(SQUIRTLE, WARTORTLE, BLASTOISE, BLASTOISE_EX, 
            PSYDUCK, GOLDUCK, POLIWAG, POLIWHIRL, POLIWRATH, TENTACOOL, TENTACRUEL, SEEL, DEWGONG, SHELLDER, CLOYSTER, 
            KRABBY, KINGLER, HORSEA, SEADRA, GOLDEEN, SEAKING, STARYU, STARMIE, STARMIE_EX, MAGIKARP, GYARADOS, LAPRAS, 
            VAPOREON, OMANYTE, OMASTAR, ARTICUNO, ARTICUNO_EX, DUCKLETT, SWANNA, FROAKIE, FROGADIER, GRENINJA, 
            PYUKUMUKU, BRUXISH, SNOM, FROSMOTH);

}