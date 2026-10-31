package com.tcgpocket.pool.A4;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.AttackCostReduction;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.NoRetreatCost;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.AttackUsedLastTurn;
import com.tcgpocket.condition.EventSideIs;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.HasTool;
import com.tcgpocket.condition.IsDamaged;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.effect.HealEach;
import com.tcgpocket.effect.ReduceDamageTaken;
import com.tcgpocket.effect.SwitchActive;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.RetreatCost;
import com.tcgpocket.number.Sum;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.AttackerAll;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnStart;

import java.util.List;
import java.util.Optional;

/**
 * Wisdom of Sea and Sky — Grass.
 */
public final class Grass {

    /**
     * A4-001 - Oddish
     */
    public static final PokemonCard ODDISH = PokemonCard.basic(
            "A4-001", "Oddish", "If exposed to moonlight, it starts to move. It roams far and wide at night to scatter its seeds.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Poison Powder", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-002 - Gloom
     */
    public static final PokemonCard GLOOM = PokemonCard.evolution(
            "A4-002", "Gloom", "Its pistils exude an incredibly foul odor. The horrid stench can cause fainting at a distance of 1.25 miles.",
            1, "Oddish", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leaf Step", "", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-003 - Bellossom
     */
    public static final PokemonCard BELLOSSOM = PokemonCard.evolution(
            "A4-003", "Bellossom", "Plentiful in the tropics. When it dances, its petals rub together and make a pleasant ringing sound.",
            2, "Gloom", 130, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Petal Dance", "Flip 3 coins. This attack does 60 damage for each heads. This Pokémon is now Confused.", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new FlipN(new Literal(3)),
                    new DealDamage(new Product(new Literal(60), new NumberHeads()), new OpponentActive()),
                    new AddStatus(new ConfusionStatus(), new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A4-004 - Tangela
     */
    public static final PokemonCard TANGELA = PokemonCard.basic(
            "A4-004", "Tangela", "Hidden beneath a tangle of vines that grows nonstop even if the vines are torn off, this Pokémon's true appearance remains a mystery.",
            80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Sleep Powder", "Your opponent's Active Pokémon is now Asleep.", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new AddStatus(new SleepStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-005 - Tangrowth
     */
    public static final PokemonCard TANGROWTH = PokemonCard.evolution(
            "A4-005", "Tangrowth", "Tangrowth has two arms that it can extend as it pleases. Recent research has shown that these arms are, in fact, bundles of vines.",
            1, "Tangela", 130, Type.GRASS, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Grass Knot", "This attack does 40 more damage for each Energy in your opponent's Active Pokémon's Retreat Cost.", EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Sum(new Literal(10), new Product(new RetreatCost(new OpponentActive()), new Literal(40))), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A4-006 - Scyther
     */
    public static final PokemonCard SCYTHER = PokemonCard.basic(
            "A4-006", "Scyther", "It slashes through grass with its sharp scythes, moving too fast for the human eye to track.",
            70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Slash", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-007 - Pinsir
     */
    public static final PokemonCard PINSIR = PokemonCard.basic(
            "A4-007", "Pinsir", "These Pokémon judge one another based on pincers. Thicker, more impressive pincers make for more popularity with the opposite gender.",
            90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Guillotine", "", EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-008 - Chikorita
     */
    public static final PokemonCard CHIKORITA = PokemonCard.basic(
            "A4-008", "Chikorita", "A sweet aroma gently wafts from the leaf on its head. It is docile and loves to soak up sun rays.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Razor Leaf", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-009 - Bayleef
     */
    public static final PokemonCard BAYLEEF = PokemonCard.evolution(
            "A4-009", "Bayleef", "A spicy aroma emanates from around its neck. The aroma acts as a stimulant to restore health.",
            1, "Chikorita", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Vine Whip", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-010 - Meganium
     */
    public static final PokemonCard MEGANIUM = PokemonCard.evolution(
            "A4-010", "Meganium", "The aroma that rises from its petals contains a substance that calms aggressive feelings.",
            2, "Bayleef", 150, Type.GRASS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Bloomshine", "Heal 20 damage from each of your Pokémon.", EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new HealEach(new Literal(20), new AttackerAll())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A4-011 - Ledyba
     */
    public static final PokemonCard LEDYBA = PokemonCard.basic(
            "A4-011", "Ledyba", "These very cowardly Pokémon join together and use Reflect to protect their nest.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bug Bite", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-012 - Ledian
     */
    public static final PokemonCard LEDIAN = PokemonCard.evolution(
            "A4-012", "Ledian", "It's said that the patterns on its back are related to the stars in the night sky, but the details of that relationship remain unclear.",
            1, "Ledyba", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Punch", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-013 - Hoppip
     */
    public static final PokemonCard HOPPIP = PokemonCard.basic(
            "A4-013", "Hoppip", "Outside of cold regions, Hoppip can be found practically everywhere. This is because the wind carries this species all over the world.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-014 - Skiploom
     */
    public static final PokemonCard SKIPLOOM = PokemonCard.evolution(
            "A4-014", "Skiploom", "Skiploom enthusiasts can apparently tell where a Skiploom was born by the scent drifting from the flower on the Pokémon's head.",
            1, "Hoppip", 70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Seed Bomb", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-015 - Jumpluff
     */
    public static final PokemonCard JUMPLUFF = PokemonCard.evolution(
            "A4-015", "Jumpluff", "Jumpluff travels on seasonal winds. Once its cotton spores run out, its journey ends, as does its life.",
            2, "Skiploom", 120, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Spinning Attack", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE).withAbility(new PassiveAbility("Fluffy Flight",
            "Your Active Pokémon has no Retreat Cost.",
            new NoRetreatCost(Optional.of(new Always<>()), new Always<>())));

    /**
     * A4-016 - Sunkern
     */
    public static final PokemonCard SUNKERN = PokemonCard.basic(
            "A4-016", "Sunkern", "It suddenly falls out of the sky in the morning. Knowing it's weak, it simply feeds until it evolves.",
            40, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Blot", "Heal 10 damage from this Pokémon.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new HealDamage(new Literal(10), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-017 - Sunflora
     */
    public static final PokemonCard SUNFLORA = PokemonCard.evolution(
            "A4-017", "Sunflora", "In the daytime, it rushes about in a hectic manner, but it comes to a complete stop when the sun sets.",
            1, "Sunkern", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Solar Beam", "", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-018 - Yanma
     */
    public static final PokemonCard YANMA = PokemonCard.basic(
            "A4-018", "Yanma", "Its eyes can see 360 degrees without moving its head. It won't miss prey—even those behind it.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "U-turn", "Switch this Pokémon with 1 of your Benched Pokémon.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new SwitchActive(new AttackerSide(), new ChosenFrom(new AttackerBench(), new AttackerSide(), "Choose a Benched Pokémon to switch with."))))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-019 - Yanmega
     */
    public static final PokemonCard YANMEGA = PokemonCard.evolution(
            "A4-019", "Yanmega", "It prefers to battle by biting apart foes' heads instantly while flying by at high speed.",
            1, "Yanma", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bug Buzz", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-020 - Pineco
     */
    public static final PokemonCard PINECO = PokemonCard.basic(
            "A4-020", "Pineco", "It looks just like a pinecone. Its shell protects it from bird Pokémon that peck it by mistake.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-021 - Shuckle ex
     */
    public static final PokemonCard SHUCKLE_EX = PokemonCard.basic(
            "A4-021", "Shuckle ex", "",
            120, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Triple Slap", "Flip 3 coins. This attack does 20 damage for each heads.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new FlipN(new Literal(3)),
                    new DealDamage(new Product(new Literal(20), new NumberHeads()), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX).withAbility(new PassiveAbility("Solid Shell", "This Pokémon takes −20 damage from attacks.", List.of(
            new Trigger(TurnStart.class, new Not<>(new EventSideIs(new SelfSide())),
                    new Attempt(List.of(new ReduceDamageTaken(new Literal(20), new Self(), new Literal(0))))))));

    /**
     * A4-022 - Heracross
     */
    public static final PokemonCard HERACROSS = PokemonCard.basic(
            "A4-022", "Heracross", "It loves sweet nectar. To keep all the nectar to itself, it hurls rivals away with its prized horn.",
            90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Single Lunge", "If this Pokémon has no damage on it, this attack does 40 more damage.", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(new Not<>(new For(new IsDamaged(), new Self())), new Literal(80))), new Literal(40)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A4-023 - Cherubi
     */
    public static final PokemonCard CHERUBI = PokemonCard.basic(
            "A4-023", "Cherubi", "It nimbly dashes about to avoid getting pecked by bird Pokémon that would love to make off with its small, nutrient-rich storage ball.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sweets Relay", "If 1 of your Pokémon used Sweets Relay during your last turn, this attack does 20 more damage.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(new AttackUsedLastTurn("Sweets Relay"), new Literal(30))), new Literal(10)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE).withAbility(new PassiveAbility("En-fruits-iastic",
            "If this Pokémon has a Pokémon Tool attached, attacks used by this Pokémon cost 1 less Grass Energy.",
            new AttackCostReduction(Type.GRASS, 1, new HasTool())));

    /**
     * A4-024 - Cherrim
     */
    public static final PokemonCard CHERRIM = PokemonCard.evolution(
            "A4-024", "Cherrim", "After absorbing plenty of sunlight, Cherrim takes this form. It's full of energy while it's like this, and its liveliness will go on until sundown.",
            1, "Cherubi", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Seed Bomb", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /** A4-162 · Chikorita (alternate art of A4-008) */
    public static final PokemonCard CHIKORITA_IR = CHIKORITA.withId("A4-162", CardRarity.ILLUSTRATION_RARE);

    /** A4-163 · Bellossom (alternate art of A4-003) */
    public static final PokemonCard BELLOSSOM_IR = BELLOSSOM.withId("A4-163", CardRarity.ILLUSTRATION_RARE);

    /** A4-164 · Heracross (alternate art of A4-022) */
    public static final PokemonCard HERACROSS_IR = HERACROSS.withId("A4-164", CardRarity.ILLUSTRATION_RARE);

    /** A4-186 · Shuckle ex (alternate art of A4-021) */
    public static final PokemonCard SHUCKLE_EX_UR = SHUCKLE_EX.withId("A4-186", CardRarity.ULTRA_RARE);

    /** A4-202 · Shuckle ex (alternate art of A4-021) */
    public static final PokemonCard SHUCKLE_EX_SIR = SHUCKLE_EX.withId("A4-202", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    /** A4-212 · Yanma (shiny of A4-018) */
    public static final PokemonCard YANMA_SR = YANMA.withId("A4-212", CardRarity.SHINY_RARE);

    /** A4-232 · Yanmega ex (shiny of A2-007) */
    public static final PokemonCard YANMEGA_EX_SUR = com.tcgpocket.pool.A2.Grass.YANMEGA_EX.withId("A4-232", CardRarity.SHINY_ULTRA_RARE);

    /** A4-233 · Leafeon ex (shiny of A2a-010) */
    public static final PokemonCard LEAFEON_EX_SUR = com.tcgpocket.pool.A2a.Grass.LEAFEON_EX.withId("A4-233", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            ODDISH, GLOOM, BELLOSSOM, TANGELA, TANGROWTH, SCYTHER,
            PINSIR, CHIKORITA, BAYLEEF, MEGANIUM, LEDYBA, LEDIAN,
            HOPPIP, SKIPLOOM, JUMPLUFF, SUNKERN, SUNFLORA, YANMA,
            YANMEGA, PINECO, SHUCKLE_EX, HERACROSS, CHERUBI, CHERRIM,
            CHIKORITA_IR, BELLOSSOM_IR, HERACROSS_IR, SHUCKLE_EX_UR,
            SHUCKLE_EX_SIR, YANMA_SR, YANMEGA_EX_SUR, LEAFEON_EX_SUR);

    private Grass() {
    }
}
