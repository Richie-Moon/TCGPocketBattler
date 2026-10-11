package com.tcgpocket.pool.A2;

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
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Space-Time Smackdown — Grass.
 *
 * <p>"During your next turn" is a duration of 2: the modifier lives while the
 * turn number is at most {@code turn + duration}, and your next turn is two
 * turns on.
 */
public final class Grass {

    /**
     * A2-001 - Oddish
     */
    public static final PokemonCard ODDISH = PokemonCard.basic(
            "A2-001", "Oddish", "If exposed to moonlight, it starts to move. It roams far and wide at night to scatter its seeds.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Blot", "Heal 10 damage from this Pokémon.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new HealDamage(new Literal(10), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-002 - Gloom
     */
    public static final PokemonCard GLOOM = PokemonCard.evolution(
            "A2-002", "Gloom", "Its pistils exude an incredibly foul odor. The horrid stench can cause fainting at a distance of 1.25 miles.",
            1, "Oddish", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Razor Leaf", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-003 - Bellossom
     */
    public static final PokemonCard BELLOSSOM = PokemonCard.evolution(
            "A2-003", "Bellossom", "Plentiful in the tropics. When it dances, its petals rub together and make a pleasant ringing sound.",
            2, "Gloom", 130, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leaf Step", "", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-004 - Tangela
     */
    public static final PokemonCard TANGELA = PokemonCard.basic(
            "A2-004", "Tangela", "Hidden beneath a tangle of vines that grows nonstop even if the vines are torn off, this Pokémon's true appearance remains a mystery.",
            70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Vine Whip", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-005 - Tangrowth
     */
    public static final PokemonCard TANGROWTH = PokemonCard.evolution(
            "A2-005", "Tangrowth", "Tangrowth has two arms that it can extend as it pleases. Recent research has shown that these arms are, in fact, bundles of vines.",
            1, "Tangela", 130, Type.GRASS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Mega Drain", "Heal 30 damage from this Pokémon.", EnergyCost.of(Type.GRASS, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive()),
                    new HealDamage(new Literal(30), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-006 - Yanma
     */
    public static final PokemonCard YANMA = PokemonCard.basic(
            "A2-006", "Yanma", "Its eyes can see 360 degrees without moving its head. It won't miss prey—even those behind it.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flap", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-007 - Yanmega ex
     */
    public static final PokemonCard YANMEGA_EX = PokemonCard.evolution(
            "A2-007", "Yanmega ex", "",
            1, "Yanma", 140, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Air Slash", "Discard a random Energy from this Pokémon.", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive()),
                    new DiscardRandomEnergy(new Literal(1), new Self())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A2-008 - Roselia
     */
    public static final PokemonCard ROSELIA = PokemonCard.basic(
            "A2-008", "Roselia", "Its flowers give off a relaxing fragrance. The stronger its aroma, the healthier the Roselia is.",
            70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sting", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-009 - Roserade
     */
    public static final PokemonCard ROSERADE = PokemonCard.evolution(
            "A2-009", "Roserade", "After captivating opponents with its sweet scent, it lashes them with its thorny whips.",
            1, "Roselia", 100, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Poisonous Whip", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-010 - Turtwig
     */
    public static final PokemonCard TURTWIG = PokemonCard.basic(
            "A2-010", "Turtwig", "It uses its whole body to photosynthesize when exposed to sunlight. Its shell is made from hardened soil.",
            80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-011 - Grotle
     */
    public static final PokemonCard GROTLE = PokemonCard.evolution(
            "A2-011", "Grotle", "It lives along water in forests. In the daytime, it leaves the forest to sunbathe its treed shell.",
            1, "Turtwig", 100, Type.GRASS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Razor Leaf", "", EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-012 - Torterra — Frenzy Plant locks only itself, but it is Torterra's
     * one attack, so "can't attack" is exactly the same restriction.
     */
    public static final PokemonCard TORTERRA = PokemonCard.evolution(
            "A2-012", "Torterra", "Ancient people imagined that beneath the ground dwelt a gigantic Torterra.",
            2, "Grotle", 160, Type.GRASS, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Frenzy Plant", "During your next turn, this Pokémon can't use Frenzy Plant.", EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(150), new OpponentActive()),
                    new PreventAttack(new Self(), new Literal(2))))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A2-013 - Kricketot
     */
    public static final PokemonCard KRICKETOT = PokemonCard.basic(
            "A2-013", "Kricketot", "Its legs are short. Whenever it stumbles, its stiff antennae clack with a xylophone-like sound.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bug Bite", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-014 - Kricketune
     */
    public static final PokemonCard KRICKETUNE = PokemonCard.evolution(
            "A2-014", "Kricketune", "By allowing its cry to resonate in the hollow of its belly, it produces a captivating sound.",
            1, "Kricketot", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Entrancing Melody", "Your opponent's Active Pokémon is now Confused.", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new AddStatus(new ConfusionStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-015 - Burmy
     */
    public static final PokemonCard BURMY = PokemonCard.basic(
            "A2-015", "Burmy", "To shelter itself from cold, wintry winds, it covers itself with a cloak made of twigs and leaves.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-016 - Wormadam
     */
    public static final PokemonCard WORMADAM = PokemonCard.evolution(
            "A2-016", "Wormadam", "Its appearance changes depending on where it evolved. The materials on hand become a part of its body.",
            1, "Burmy", 120, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Leaf Cutter", "Flip a coin. If heads, this attack does 30 more damage.", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(
                            new Literal(60),
                            new Product(new NumberHeads(), new Literal(30))), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-017 - Combee
     */
    public static final PokemonCard COMBEE = PokemonCard.basic(
            "A2-017", "Combee", "At night, Combee sleep in a group of about a hundred, packed closely together in a lump.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Call for Family", "Put 1 random Basic Pokémon from your deck onto your Bench.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new BenchFromDeck(new AttackerSide(), new IsBasic())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-018 - Vespiquen
     */
    public static final PokemonCard VESPIQUEN = PokemonCard.evolution(
            "A2-018", "Vespiquen", "It houses its colony in cells in its body and releases various pheromones to make those grubs do its bidding.",
            1, "Combee", 100, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Pierce", "", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-019 - Carnivine
     */
    public static final PokemonCard CARNIVINE = PokemonCard.basic(
            "A2-019", "Carnivine", "It attracts prey with its sweet-smelling saliva, then chomps down. It takes a whole day to eat prey.",
            90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flog", "Flip a coin. If heads, this attack does 50 more damage.", EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(
                            new Literal(40),
                            new Product(new NumberHeads(), new Literal(50))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-020 - Leafeon
     */
    public static final PokemonCard LEAFEON = PokemonCard.evolution(
            "A2-020", "Leafeon", "When you see Leafeon asleep in a patch of sunshine, you'll know it is using photosynthesis to produce clean air.",
            1, "Eevee", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leafy Cyclone", "During your next turn, this Pokémon can't attack.", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive()),
                    new PreventAttack(new Self(), new Literal(2))))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A2-021 - Mow Rotom
     */
    public static final PokemonCard MOW_ROTOM = PokemonCard.basic(
            "A2-021", "Mow Rotom", "The lawn mower is one of the household appliances that led to the development of the Rotom Dex.",
            80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Energy Cutoff", "Flip a coin. If heads, discard a random Energy from your opponent's Active Pokémon.", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(
                            new LastCoinTossHeads(),
                            new DiscardRandomEnergy(new Literal(1), new OpponentActive()))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-022 - Shaymin
     */
    public static final PokemonCard SHAYMIN = PokemonCard.basic(
            "A2-022", "Shaymin", "It can dissolve toxins in the air to instantly transform ruined land into a lush field of flowers.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flop", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE).withAbility(new ActivatedAbility(
            "Fragrant Flower Garden",
            "Once during your turn, you may heal 10 damage from each of your Pokémon.",
            new PlainAction("", new Attempt(List.of(
                    new HealEach(new Literal(10), new AttackerAll())))),
            true,
            new ForAny(new IsDamaged(), new AttackerAll())
    ));

    static final List<PokemonCard> CARDS = List.of(
            ODDISH, GLOOM, BELLOSSOM, TANGELA, TANGROWTH, YANMA, YANMEGA_EX, ROSELIA, ROSERADE,
            TURTWIG, GROTLE, TORTERRA, KRICKETOT, KRICKETUNE, BURMY, WORMADAM, COMBEE, VESPIQUEN,
            CARNIVINE, LEAFEON, MOW_ROTOM, SHAYMIN);

    private Grass() {
    }
}
