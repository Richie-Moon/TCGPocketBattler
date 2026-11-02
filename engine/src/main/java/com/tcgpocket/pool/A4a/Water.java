package com.tcgpocket.pool.A4a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.ForAny;
import com.tcgpocket.condition.HasType;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.condition.IsBasic;
import com.tcgpocket.condition.IsDamaged;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.condition.IsTool;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.AttachEnergy;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.BenchFromDeck;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardUpToFromHand;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.effect.SwitchActive;
import com.tcgpocket.effect.YouMay;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CardsDiscarded;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.AttackerAll;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentAll;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.Evolved;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * Secluded Springs (A4a): the Water cards, in printed set order.
 */
public final class Water {

    private static final ICondition<PokemonInPlay> DAMAGED_WATER = new And<>(new HasType(Type.WATER), new IsDamaged());

    /**
     * A4a-013 - Poliwag
     */
    public static final PokemonCard POLIWAG = PokemonCard.basic(
            "A4a-013", "Poliwag", "For Poliwag, swimming is easier than walking. The swirl pattern on its belly is actually part of the Pokémon's innards showing through the skin.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Call for Family", "Put 1 random Poliwag from your deck onto your Bench.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new BenchFromDeck(new AttackerSide(), new IsSpecies("Poliwag"))))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4a-014 - Poliwhirl
     */
    public static final PokemonCard POLIWHIRL = PokemonCard.evolution(
            "A4a-014", "Poliwhirl", "Staring at the swirl on its belly causes drowsiness. This trait of Poliwhirl's has been used in place of lullabies to get children to go to sleep.",
            1, "Poliwag", 80, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4a-015 - Tentacool
     */
    public static final PokemonCard TENTACOOL = PokemonCard.basic(
            "A4a-015", "Tentacool", "Tentacool is not a particularly strong swimmer. It drifts across the surface of shallow seas as it searches for prey.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Poison Sting", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4a-016 - Tentacruel
     */
    public static final PokemonCard TENTACRUEL = PokemonCard.evolution(
            "A4a-016", "Tentacruel", "When the red orbs on Tentacruel's head glow brightly, watch out. The Pokémon is about to fire off a burst of ultrasonic waves.",
            1, "Tentacool", 110, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Stun Poison", "Flip a coin. If heads, your opponent's Active Pokémon is now Poisoned and Paralyzed.", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new AddStatus(new PoisonStatus(), new OpponentActive())),
                    new ConditionalEffect(new LastCoinTossHeads(), new AddStatus(new ParalysisStatus(), new OpponentActive()))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4a-017 - Slowpoke
     */
    public static final PokemonCard SLOWPOKE = PokemonCard.basic(
            "A4a-017", "Slowpoke", "It is incredibly slow and dopey. It takes five seconds for it to feel pain when under attack.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Water Gun", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4a-018 - Slowking
     */
    public static final PokemonCard SLOWKING = PokemonCard.evolution(
            "A4a-018", "Slowking", "When its head was bitten, toxins entered Slowpoke's head and unlocked an extraordinary power.",
            1, "Slowpoke", 130, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Litter", "Discard up to 2 Pokémon Tool cards from your hand. This attack does 50 damage for each card you discarded in this way.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DiscardUpToFromHand(new AttackerSide(), new Literal(2), new IsTool()),
                    new DealDamage(new Product(new CardsDiscarded(), new Literal(50)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4a-019 - Jynx
     */
    public static final PokemonCard JYNX = PokemonCard.basic(
            "A4a-019", "Jynx", "Its strange cries sound like human language. There are some musicians who compose songs for Jynx to sing.",
            70, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Dazzle Dance", "Your opponent's Active Pokémon is now Confused.", EnergyCost.of(Type.WATER, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new AddStatus(new ConfusionStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A4a-020 - Suicune ex
     */
    public static final PokemonCard SUICUNE_EX = PokemonCard.basic(
            "A4a-020", "Suicune ex", "",
            140, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Crystal Waltz", "This attack does 20 damage for each Benched Pokémon (both yours and your opponent's).", EnergyCost.of(Type.WATER, 2), new Attempt(List.of(
                    new DealDamage(new Product(new Sum(new CountCards(new AttackerSide(), Zone.BENCH), new CountCards(new OpponentSide(), Zone.BENCH)), new Literal(20)), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.LIGHTNING).withTags(CardTag.EX).withAbility(LegendaryPulse.ABILITY);

    /**
     * A4a-021 - Feebas
     */
    public static final PokemonCard FEEBAS = PokemonCard.basic(
            "A4a-021", "Feebas", "Although unattractive and unpopular, this Pokémon's marvelous vitality has made it a subject of research.",
            30, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leap Out", "Switch this Pokémon with 1 of your Benched Pokémon.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new SwitchActive(new SelfSide())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4a-022 - Milotic
     */
    public static final PokemonCard MILOTIC = PokemonCard.evolution(
            "A4a-022", "Milotic", "Milotic has provided inspiration to many artists. It has even been referred to as the most beautiful Pokémon of all.",
            1, "Feebas", 120, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Aqua Edge", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING).withAbility(new PassiveAbility("Healing Ripples",
            "Once during your turn, when you play this Pokémon from your hand to evolve 1 of your Pokémon, you may heal 60 damage from 1 of your Water Pokémon.",
            List.of(new Trigger(Evolved.class,
                    new And<>(new EventConcerns(new Self()), new ForAny(DAMAGED_WATER, new AttackerAll())),
                    new Attempt(List.of(new YouMay("Healing Ripples: heal 60 damage from 1 of your Water Pokémon?",
                            new HealDamage(new Literal(60), new ChosenFrom(new AttackerAll(), new AttackerSide(), DAMAGED_WATER, "Choose a Water Pokémon to heal.")))))))));

    /**
     * A4a-023 - Mantyke
     */
    public static final PokemonCard MANTYKE = PokemonCard.basic(
            "A4a-023", "Mantyke", "It's highly friendly and easily tamed. Tours that take people swimming with Mantyke are a super-popular beach activity.",
            30, Type.WATER, EnergyCost.free(), List.of(new Action(
                    "Splashy Toss", "Take a Water Energy from your Energy Zone and attach it to 1 of your Benched Basic Pokémon.", EnergyCost.free(), new Attempt(List.of(
                    new AttachEnergy(Type.WATER, new Literal(1), new ChosenFrom(new AttackerBench(), new AttackerSide(), new IsBasic(), "Choose a Benched Basic Pokémon to attach a Water Energy to."))))
            )), CardRarity.RARE
    );

    /**
     * A4a-024 - Cryogonal
     */
    public static final PokemonCard CRYOGONAL = PokemonCard.basic(
            "A4a-024", "Cryogonal", "Cryogonal appear during cold seasons. It is said that people and Pokémon who die on snowy mountains are reborn into these Pokémon.",
            80, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ice Blade", "This attack does 40 damage to 1 of your opponent's Pokémon.", EnergyCost.of(Type.WATER, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"))))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /** A4a-072 · Milotic (alternate art of A4a-022) */
    public static final PokemonCard MILOTIC_IR = MILOTIC.withId("A4a-072", CardRarity.ILLUSTRATION_RARE);

    /** A4a-080 · Suicune ex (alternate art of A4a-020) */
    public static final PokemonCard SUICUNE_EX_UR = SUICUNE_EX.withId("A4a-080", CardRarity.ULTRA_RARE);

    /** A4a-090 · Suicune ex (alternate art of A4a-020) */
    public static final PokemonCard SUICUNE_EX_IM = SUICUNE_EX.withId("A4a-090", CardRarity.IMMERSIVE);

    /** A4a-093 · Psyduck (shiny of A1-057) */
    public static final PokemonCard PSYDUCK_SR = com.tcgpocket.pool.A1.Water.PSYDUCK.withId("A4a-093", CardRarity.SHINY_RARE);

    /** A4a-094 · Golduck (shiny of A1-058) */
    public static final PokemonCard GOLDUCK_SR = com.tcgpocket.pool.A1.Water.GOLDUCK.withId("A4a-094", CardRarity.SHINY_RARE);

    /** A4a-095 · Krabby (shiny of A1-068) */
    public static final PokemonCard KRABBY_SR = com.tcgpocket.pool.A1.Water.KRABBY.withId("A4a-095", CardRarity.SHINY_RARE);

    /** A4a-096 · Kingler (shiny of A1-069) */
    public static final PokemonCard KINGLER_SR = com.tcgpocket.pool.A1.Water.KINGLER.withId("A4a-096", CardRarity.SHINY_RARE);

    /** A4a-097 · Pyukumuku (shiny of A3-054) */
    public static final PokemonCard PYUKUMUKU_SR = com.tcgpocket.pool.A3.Water.PYUKUMUKU.withId("A4a-097", CardRarity.SHINY_RARE);

    /** A4a-105 · Mantyke (alternate art of A4a-023) */
    public static final PokemonCard MANTYKE_HR = MANTYKE.withId("A4a-105", CardRarity.HYPER_RARE);

    static final List<PokemonCard> CARDS = List.of(
            POLIWAG, POLIWHIRL, TENTACOOL, TENTACRUEL, SLOWPOKE, SLOWKING,
            JYNX, SUICUNE_EX, FEEBAS, MILOTIC, MANTYKE, CRYOGONAL,
            MILOTIC_IR, SUICUNE_EX_UR, SUICUNE_EX_IM, PSYDUCK_SR, GOLDUCK_SR, KRABBY_SR,
            KINGLER_SR, PYUKUMUKU_SR, MANTYKE_HR);

    private Water() {
    }
}
