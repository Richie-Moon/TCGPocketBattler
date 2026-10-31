package com.tcgpocket.pool.A4;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.condition.EventSideIs;
import com.tcgpocket.condition.EvolvesFrom;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.condition.HasType;
import com.tcgpocket.condition.IsBenched;
import com.tcgpocket.condition.IsPokemon;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.condition.Or;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardAnyFromPlay;
import com.tcgpocket.effect.DiscardRandomEnergy;
import com.tcgpocket.effect.DrawCard;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.HinderAttack;
import com.tcgpocket.effect.IncreaseSideDamage;
import com.tcgpocket.effect.MoveAllEnergy;
import com.tcgpocket.effect.PreventDamage;
import com.tcgpocket.effect.PreventEffects;
import com.tcgpocket.effect.PreventRetreat;
import com.tcgpocket.effect.ReduceDamageDealt;
import com.tcgpocket.effect.ReduceDamageTaken;
import com.tcgpocket.effect.SearchDeck;
import com.tcgpocket.effect.YouMay;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.PokemonDiscarded;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.Matching;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.AttackDeclared;
import com.tcgpocket.trigger.Evolved;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * Wisdom of Sea and Sky — Water.
 */
public final class Water {

    /** 
     * Longer than any game, so only leaving the Active Spot (or evolving) ends it. 
     * Could abstract in INumber (Maybe to something like INumber.Infinite) 
     */
    private static final Literal UNTIL_IT_LEAVES_THE_ACTIVE_SPOT = new Literal(1000);

    /**
     * A4-038 - Poliwag
     */
    public static final PokemonCard POLIWAG = PokemonCard.basic(
            "A4-038", "Poliwag", "For Poliwag, swimming is easier than walking. The swirl pattern on its belly is actually part of the Pokémon's innards showing through the skin.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Water Gun", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-039 - Poliwhirl
     */
    public static final PokemonCard POLIWHIRL = PokemonCard.evolution(
            "A4-039", "Poliwhirl", "Staring at the swirl on its belly causes drowsiness. This trait of Poliwhirl's has been used in place of lullabies to get children to go to sleep.",
            1, "Poliwag", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Wave Splash", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-040 - Politoed
     */
    public static final PokemonCard POLITOED = PokemonCard.evolution(
            "A4-040", "Politoed", "At nightfall, these Pokémon appear on the shores of lakes. They announce their territorial claims by letting out cries that sound like shouting.",
            2, "Poliwhirl", 140, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Hyper Voice", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING).withAbility(new PassiveAbility("Lordly Cheering",
            "As long as this Pokémon is on your Bench, attacks used by your Pokémon that evolve from Poliwhirl do +40 damage to your opponent's Active Pokémon.", List.of(
            new Trigger(AttackDeclared.class,
                    new And<>(new EventSideIs(new SelfSide()),
                            new And<>(new For(new IsBenched(), new Self()), new For(new EvolvesFrom("Poliwhirl"), new AttackerActive()))),
                    new Attempt(List.of(new IncreaseSideDamage(new Literal(40), new Literal(0))))))));

    /**
     * A4-041 - Horsea
     */
    public static final PokemonCard HORSEA = PokemonCard.basic(
            "A4-041", "Horsea", "Horsea makes its home in oceans with gentle currents. If this Pokémon is under attack, it spits out pitch-black ink and escapes.",
            50, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Hook", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-042 - Seadra
     */
    public static final PokemonCard SEADRA = PokemonCard.evolution(
            "A4-042", "Seadra", "It's the males that raise the offspring. While Seadra are raising young, the spines on their backs secrete thicker and stronger poison.",
            1, "Horsea", 80, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Razor Fin", "", EnergyCost.of(Type.WATER, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-043 - Kingdra ex
     */
    public static final PokemonCard KINGDRA_EX = PokemonCard.evolution(
            "A4-043", "Kingdra ex", "",
            2, "Seadra", 170, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Stormy Prison", "During your opponent's next turn, the Defending Pokémon can't retreat.", EnergyCost.of(Type.WATER, 2), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive()),
                    new PreventRetreat(new OpponentActive(), new Literal(1))))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A4-044 - Magikarp
     */
    public static final PokemonCard MAGIKARP = PokemonCard.basic(
            "A4-044", "Magikarp", "An underpowered, pathetic Pokémon. It may jump high on rare occasions but never more than seven feet.",
            30, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Splashing Dodge", "Flip a coin. If heads, during your opponent's next turn, prevent all damage from—and effects of—attacks done to this Pokémon.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventDamage(new Self(), new Literal(1))),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventEffects(new Self(), new Literal(1)))))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-045 - Gyarados
     */
    public static final PokemonCard GYARADOS = PokemonCard.evolution(
            "A4-045", "Gyarados", "Once it appears, it goes on a rampage. It remains enraged until it demolishes everything around it.",
            1, "Magikarp", 130, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Wild Swing", "You may discard any number of your Benched Water Pokémon. This attack does 40 more damage for each Benched Pokémon you discarded in this way.", EnergyCost.of(Type.WATER, 2), new Attempt(List.of(
                    new DiscardAnyFromPlay(new Matching(new AttackerBench(), new HasType(Type.WATER)), "Choose any number of your Benched Water Pokémon to discard."),
                    new DealDamage(new Sum(new Literal(20), new Product(new PokemonDiscarded(), new Literal(40))), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-046 - Totodile
     */
    public static final PokemonCard TOTODILE = PokemonCard.basic(
            "A4-046", "Totodile", "It is small but rough and tough. It won't hesitate to take a bite out of anything that moves.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Wave Splash", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-047 - Croconaw
     */
    public static final PokemonCard CROCONAW = PokemonCard.evolution(
            "A4-047", "Croconaw", "If it loses a fang, a new one grows back in its place. There are always 48 fangs lining its mouth.",
            1, "Totodile", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-048 - Feraligatr
     */
    public static final PokemonCard FERALIGATR = PokemonCard.evolution(
            "A4-048", "Feraligatr", "It usually moves slowly, but it goes at blinding speed when it attacks and bites prey.",
            2, "Croconaw", 150, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Destructive Whirlpool", "Discard a random Energy from your opponent's Active Pokémon.", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive()),
                    new DiscardRandomEnergy(new Literal(1), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-049 - Marill
     */
    public static final PokemonCard MARILL = PokemonCard.basic(
            "A4-049", "Marill", "The fur on its body naturally repels water. It can stay dry even when it plays in the water.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-050 - Azumarill
     */
    public static final PokemonCard AZUMARILL = PokemonCard.evolution(
            "A4-050", "Azumarill", "Its long ears are superb sensors. It can distinguish the movements of things in water and tell what they are.",
            1, "Marill", 110, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Tail Smack", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING).withAbility(new PassiveAbility("Thick Fat",
            "This Pokémon takes −30 damage from attacks from Fire or Water Pokémon.",
            List.of(new Trigger(AttackDeclared.class,
                    new And<>(new Not<>(new EventSideIs(new SelfSide())),
                            new For(new Or<>(new HasType(Type.FIRE), new HasType(Type.WATER)), new AttackerActive())),
                    new Attempt(List.of(new ReduceDamageTaken(new Literal(30), new Self(), new Literal(0))))))));

    /**
     * A4-051 - Wooper
     */
    public static final PokemonCard WOOPER = PokemonCard.basic(
            "A4-051", "Wooper", "This Pokémon lives in cold water. It will leave the water to search for food when it gets cold outside.",
            70, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Rain Splash", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-052 - Quagsire
     */
    public static final PokemonCard QUAGSIRE = PokemonCard.evolution(
            "A4-052", "Quagsire", "It has an easygoing nature. It doesn't care if it bumps its head on boats and boulders while swimming.",
            1, "Wooper", 130, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Surf", "", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-053 - Qwilfish
     */
    public static final PokemonCard QWILFISH = PokemonCard.basic(
            "A4-053", "Qwilfish", "Be cautious if this Pokémon starts sucking in water—it will soon attack by scattering the toxic spikes that grow all over its body.",
            80, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Poison Sting", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-054 - Corsola
     */
    public static final PokemonCard CORSOLA = PokemonCard.basic(
            "A4-054", "Corsola", "It will regrow any branches that break off its head. People keep particularly beautiful Corsola branches as charms to promote safe childbirth.",
            80, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Find a Friend", "Put a random Pokémon from your deck into your hand.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new SearchDeck(new AttackerSide(), new Literal(1), new IsPokemon())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-055 - Remoraid
     */
    public static final PokemonCard REMORAID = PokemonCard.basic(
            "A4-055", "Remoraid", "The water they shoot from their mouths can hit moving prey from more than 300 feet away.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Water Gun", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-056 - Octillery — evolving sheds it, as it does every attack effect; a second Octazooka adds no second coin, because the modifier is only asked whether it is present.
     */
    public static final PokemonCard OCTILLERY = PokemonCard.evolution(
            "A4-056", "Octillery", "Its instinct is to bury itself in holes. It often steals the nesting holes of others to sleep in them.",
            1, "Remoraid", 100, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Octazooka", "If the Defending Pokémon tries to use an attack, your opponent flips a coin. If tails, that attack doesn't happen. This effect lasts until the Defending Pokémon leaves the Active Spot, and it doesn't stack.", EnergyCost.of(Type.WATER, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new HinderAttack(new OpponentActive(), UNTIL_IT_LEAVES_THE_ACTIVE_SPOT)))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-057 - Delibird
     */
    public static final PokemonCard DELIBIRD = PokemonCard.basic(
            "A4-057", "Delibird", "It carries food all day long. There are tales about lost people who were saved by the food it had.",
            70, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Stumbling Draw", "Draw a card.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new DrawCard(new Literal(1), new AttackerSide())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /**
     * A4-058 - Mantine
     */
    public static final PokemonCard MANTINE = PokemonCard.basic(
            "A4-058", "Mantine", "While elegantly swimming in the sea, it ignores Remoraid that cling to its fins seeking food scraps.",
            100, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Dive", "Flip a coin. If heads, during your opponent's next turn, prevent all damage from—and effects of—attacks done to this Pokémon.", EnergyCost.of(Type.WATER, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventDamage(new Self(), new Literal(1))),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventEffects(new Self(), new Literal(1)))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-059 - Suicune
     */
    public static final PokemonCard SUICUNE = PokemonCard.basic(
            "A4-059", "Suicune", "It races around the world to purify fouled water. It dashes away with the north wind.",
            100, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Cure Stream", "During your opponent's next turn, attacks used by the Defending Pokémon do −20 damage.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new ReduceDamageDealt(new Literal(20), new OpponentActive(), new Literal(1))))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-060 - Corphish
     */
    public static final PokemonCard CORPHISH = PokemonCard.basic(
            "A4-060", "Corphish", "No matter how dirty the water in the river, it will adapt and thrive. It has a strong will to survive.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Vise Grip", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-061 - Crawdaunt
     */
    public static final PokemonCard CRAWDAUNT = PokemonCard.evolution(
            "A4-061", "Crawdaunt", "A rough customer that wildly flails its giant claws. It is said to be extremely hard to raise.",
            1, "Corphish", 100, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Crabhammer", "", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING).withAbility(new PassiveAbility("Unruly Claw",
            "Once during your turn, when you play this Pokémon from your hand to evolve 1 of your Pokémon, you may discard a random Energy from your opponent's Active Pokémon.",
            List.of(new Trigger(Evolved.class,
                    new And<>(new EventConcerns(new Self()), new GreaterThan(new EnergyOn(new OpponentActive()), new Literal(0))),
                    new Attempt(List.of(new YouMay("Unruly Claw: discard a random Energy from your opponent's Active Pokémon?",
                            new DiscardRandomEnergy(new Literal(1), new OpponentActive()))))))));

    /**
     * A4-062 - Ducklett
     */
    public static final PokemonCard DUCKLETT = PokemonCard.basic(
            "A4-062", "Ducklett", "When attacked, it uses its feathers to splash water, escaping under cover of the spray.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Rain Splash", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-063 - Swanna
     */
    public static final PokemonCard SWANNA = PokemonCard.evolution(
            "A4-063", "Swanna", "Despite their elegant appearance, they can flap their wings strongly and fly for thousands of miles.",
            1, "Ducklett", 80, Type.WATER, EnergyCost.free(), List.of(new Action(
                    "Feathery Cyclone", "Move all Energy from this Pokémon to 1 of your Benched Pokémon.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new MoveAllEnergy(new Self(), new ChosenFrom(new AttackerBench(), new AttackerSide(), "Choose a Benched Pokémon to move all Energy to."))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /** A4-167 · Totodile (alternate art of A4-046) */
    public static final PokemonCard TOTODILE_IR = TOTODILE.withId("A4-167", CardRarity.ILLUSTRATION_RARE);

    /** A4-168 · Qwilfish (alternate art of A4-053) */
    public static final PokemonCard QWILFISH_IR = QWILFISH.withId("A4-168", CardRarity.ILLUSTRATION_RARE);

    /** A4-169 · Octillery (alternate art of A4-056) */
    public static final PokemonCard OCTILLERY_IR = OCTILLERY.withId("A4-169", CardRarity.ILLUSTRATION_RARE);

    /** A4-170 · Delibird (alternate art of A4-057) */
    public static final PokemonCard DELIBIRD_IR = DELIBIRD.withId("A4-170", CardRarity.ILLUSTRATION_RARE);

    /** A4-188 · Kingdra ex (alternate art of A4-043) */
    public static final PokemonCard KINGDRA_EX_UR = KINGDRA_EX.withId("A4-188", CardRarity.ULTRA_RARE);

    /** A4-203 · Kingdra ex (alternate art of A4-043) */
    public static final PokemonCard KINGDRA_EX_SIR = KINGDRA_EX.withId("A4-203", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    /** A4-214 · Magikarp (shiny of A1a-017) */
    public static final PokemonCard MAGIKARP_SR = com.tcgpocket.pool.A1a.Water.MAGIKARP.withId("A4-214", CardRarity.SHINY_RARE);

    /** A4-215 · Gyarados (shiny of A4-045) */
    public static final PokemonCard GYARADOS_SR = GYARADOS.withId("A4-215", CardRarity.SHINY_RARE);

    /** A4-216 · Vaporeon (shiny of A1-080) */
    public static final PokemonCard VAPOREON_SR = com.tcgpocket.pool.A1.Water.VAPOREON.withId("A4-216", CardRarity.SHINY_RARE);

    /** A4-234 · Gyarados ex (shiny of A1a-018) */
    public static final PokemonCard GYARADOS_EX_SUR = com.tcgpocket.pool.A1a.Water.GYARADOS_EX.withId("A4-234", CardRarity.SHINY_ULTRA_RARE);

    /** A4-235 · Glaceon ex (shiny of A2a-022) */
    public static final PokemonCard GLACEON_EX_SUR = com.tcgpocket.pool.A2a.Water.GLACEON_EX.withId("A4-235", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            POLIWAG, POLIWHIRL, POLITOED, HORSEA, SEADRA, KINGDRA_EX,
            MAGIKARP, GYARADOS, TOTODILE, CROCONAW, FERALIGATR, MARILL,
            AZUMARILL, WOOPER, QUAGSIRE, QWILFISH, CORSOLA, REMORAID,
            OCTILLERY, DELIBIRD, MANTINE, SUICUNE, CORPHISH, CRAWDAUNT,
            DUCKLETT, SWANNA,
            TOTODILE_IR, QWILFISH_IR, OCTILLERY_IR, DELIBIRD_IR, KINGDRA_EX_UR,
            KINGDRA_EX_SIR, MAGIKARP_SR, GYARADOS_SR, VAPOREON_SR, GYARADOS_EX_SUR, GLACEON_EX_SUR);

    private Water() {
    }
}
