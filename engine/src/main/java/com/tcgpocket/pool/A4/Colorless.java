package com.tcgpocket.pool.A4;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.card.StatusImmunity;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.EnergyFromZone;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.HasTool;
import com.tcgpocket.condition.HasType;
import com.tcgpocket.condition.IsBasic;
import com.tcgpocket.condition.IsPokemon;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ChangeRandomEnergy;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardTypeEnergy;
import com.tcgpocket.effect.DrawCard;
import com.tcgpocket.effect.EvolveFromDeck;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.FlipUntilTails;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.effect.IncreaseAttackCost;
import com.tcgpocket.effect.PreventDamage;
import com.tcgpocket.effect.PreventEffects;
import com.tcgpocket.effect.SearchDeck;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Difference;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.EnergyAttached;
import com.tcgpocket.trigger.Trigger;

import java.util.List;
import java.util.Optional;

/**
 * Wisdom of Sea and Sky — Colorless.
 */
public final class Colorless {

    /**
     * A4-129 - Spearow
     */
    public static final PokemonCard SPEAROW = PokemonCard.basic(
            "A4-129", "Spearow", "Its reckless nature leads it to stand up to others—even large Pokémon—if it has to protect its territory.",
            70, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Glide", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-130 - Fearow
     */
    public static final PokemonCard FEAROW = PokemonCard.evolution(
            "A4-130", "Fearow", "Carrying food through Fearow's territory is dangerous. It will snatch the food away from you in a flash!",
            1, "Spearow", 90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Peck Bugs", "If your opponent's Active Pokémon is a Grass Pokémon, this attack does 40 more damage.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new For(new HasType(Type.GRASS), new OpponentActive()), new Literal(70))),
                            new Literal(30)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-131 - Chansey
     */
    public static final PokemonCard CHANSEY = PokemonCard.basic(
            "A4-131", "Chansey", "This kindly Pokémon lays highly nutritious eggs and shares them with injured Pokémon or people.",
            100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Scrunch", "Flip a coin. If heads, during your opponent's next turn, prevent all damage done to this Pokémon by attacks.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventDamage(new Self(), new Literal(1)))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-132 - Blissey
     */
    public static final PokemonCard BLISSEY = PokemonCard.evolution(
            "A4-132", "Blissey", "Anyone who takes even one taste of Blissey's egg becomes unfailingly caring and pleasant to everyone.",
            1, "Chansey", 150, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Energy Blow", "This attack does 20 more damage for each Energy attached to this Pokémon.", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Sum(new Literal(20), new Product(new Literal(20), new EnergyOn(new Self()))), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-133 - Kangaskhan
     */
    public static final PokemonCard KANGASKHAN = PokemonCard.basic(
            "A4-133", "Kangaskhan", "Although it's carrying its baby in a pouch on its belly, Kangaskhan is swift on its feet. It intimidates its opponents with quick jabs.",
            100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Cross-Cut", "If your opponent's Active Pokémon is an Evolution Pokémon, this attack does 40 more damage.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new For(new Not<>(new IsBasic()), new OpponentActive()), new Literal(60))),
                            new Literal(20)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-134 - Eevee
     */
    public static final PokemonCard EEVEE = PokemonCard.basic(
            "A4-134", "Eevee", "Its ability to evolve into many forms allows it to adapt smoothly and perfectly to any environment.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Find a Friend", "Put a random Pokémon from your deck into your hand.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new SearchDeck(new AttackerSide(), new Literal(1), new IsPokemon())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-135 - Porygon
     */
    public static final PokemonCard PORYGON = PokemonCard.basic(
            "A4-135", "Porygon", "State-of-the-art technology was used to create Porygon. It was the first artificial Pokémon to be created via computer programming.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-136 - Porygon2
     */
    public static final PokemonCard PORYGON2 = PokemonCard.evolution(
            "A4-136", "Porygon2", "This is a Porygon that was updated with special data. Porygon2 develops itself by learning about many different subjects all on its own.",
            1, "Porygon", 80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Spinning Attack", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING).withAbility(new PassiveAbility("Buggy Evolution",
            "Whenever you attach an Energy from your Energy Zone to this Pokémon, put a random card from your deck that evolves from this Pokémon onto this Pokémon to evolve it.",
            List.of(new Trigger(EnergyAttached.class,
                    new And<>(new EventConcerns(new Self()), new EnergyFromZone()),
                    new Attempt(List.of(new EvolveFromDeck(new Self())))))));

    /**
     * A4-137 - Porygon-Z
     */
    public static final PokemonCard PORYGON_Z = PokemonCard.evolution(
            "A4-137", "Porygon-Z", "Porygon-Z had a program installed to allow it to move between dimensions, but the program also caused instability in Porygon-Z's behavior.",
            2, "Porygon2", 130, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Slowing Beam", "During your opponent's next turn, attacks used by the Defending Pokémon cost 1 Colorless more.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new IncreaseAttackCost(new Literal(1), new OpponentActive(), new Literal(1))))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-138 - Sentret
     */
    public static final PokemonCard SENTRET = PokemonCard.basic(
            "A4-138", "Sentret", "When acting as a lookout, it warns others of danger by screeching and hitting the ground with its tail.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Scratch", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-139 - Furret
     */
    public static final PokemonCard FURRET = PokemonCard.evolution(
            "A4-139", "Furret", "The mother puts its offspring to sleep by curling up around them. It corners foes with speed.",
            1, "Sentret", 90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tail Smash", "Flip a coin. If tails, this attack does nothing.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new DealDamage(new Literal(80), new OpponentActive()))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-140 - Hoothoot
     */
    public static final PokemonCard HOOTHOOT = PokemonCard.basic(
            "A4-140", "Hoothoot", "It always stands on one foot. It changes feet so fast, the movement can rarely be seen.",
            50, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Wing Attack", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING).withAbility(new PassiveAbility("Insomnia",
            "This Pokémon can't be Asleep.",
            new StatusImmunity(new IsSpecies("Hoothoot"), Optional.of(new SleepStatus()))));

    /**
     * A4-141 - Noctowl
     */
    public static final PokemonCard NOCTOWL = PokemonCard.evolution(
            "A4-141", "Noctowl", "Its eyes are specially developed to enable it to see clearly even in murky darkness and minimal light.",
            1, "Hoothoot", 100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Fly", "Flip a coin. If tails, this attack does nothing. If heads, during your opponent's next turn, prevent all damage from—and effects of—attacks done to this Pokémon.", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new DealDamage(new Literal(90), new OpponentActive())),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventDamage(new Self(), new Literal(1))),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventEffects(new Self(), new Literal(1)))))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-142 - Aipom — a hand already as large draws nothing, as the difference is not positive.
     */
    public static final PokemonCard AIPOM = PokemonCard.basic(
            "A4-142", "Aipom", "As it did more and more with its tail, its hands became clumsy. It makes its nest high in the treetops.",
            50, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Imitate", "Draw cards until you have the same number of cards in your hand as your opponent.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DrawCard(new Difference(new CountCards(new OpponentSide(), Zone.HAND), new CountCards(new AttackerSide(), Zone.HAND)), new AttackerSide())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-143 - Ambipom
     */
    public static final PokemonCard AMBIPOM = PokemonCard.evolution(
            "A4-143", "Ambipom", "It uses its tails for everything. If it wraps both of its tails around you and gives you a squeeze, that's proof it really likes you.",
            1, "Aipom", 100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tail Jab", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-144 - Dunsparce
     */
    public static final PokemonCard DUNSPARCE = PokemonCard.basic(
            "A4-144", "Dunsparce", "It creates mazes in dark locations. When spotted, it flees into the ground by digging with its tail.",
            50, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sudden Flash", "Flip a coin. If heads, your opponent's Active Pokémon is now Paralyzed.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new AddStatus(new ParalysisStatus(), new OpponentActive()))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-145 - Teddiursa
     */
    public static final PokemonCard TEDDIURSA = PokemonCard.basic(
            "A4-145", "Teddiursa", "Its paws are soaked in oodles of honey. When nervous, Teddiursa will lick its paws and soon have a smile back on its face.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Honey Snack", "Heal 20 damage from this Pokémon.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new HealDamage(new Literal(20), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-146 - Ursaring
     */
    public static final PokemonCard URSARING = PokemonCard.evolution(
            "A4-146", "Ursaring", "It usually wears a hardened expression, but when it's licking up honey—which it loves—the joy it feels will cause it to break into a wide grin.",
            1, "Teddiursa", 120, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Swing Around", "Flip 2 coins. This attack does 20 more damage for each heads.", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Sum(new Literal(60), new Product(new Literal(20), new NumberHeads())), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-147 - Stantler
     */
    public static final PokemonCard STANTLER = PokemonCard.basic(
            "A4-147", "Stantler", "This Pokémon apparently used to live in much harsher environments, and thus it once had stronger psychic powers than it does now.",
            80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Enhanced Horns", "If this Pokémon has a Pokémon Tool attached, this attack does 50 more damage.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new For(new HasTool(), new Self()), new Literal(70))),
                            new Literal(20)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-148 - Smeargle
     */
    public static final PokemonCard SMEARGLE = PokemonCard.basic(
            "A4-148", "Smeargle", "The fluid of Smeargle's tail secretions changes in the intensity of its hue as the Pokémon's emotions change.",
            70, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Splatter Coating", "Change the type of a random Energy attached to your opponent's Active Pokémon to 1 of the following at random: Grass, Fire, Water, Lightning, Psychic, Fighting, Darkness, or Metal.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new ChangeRandomEnergy(new OpponentActive(), List.of(
                            Type.GRASS, Type.FIRE, Type.WATER, Type.LIGHTNING,
                            Type.PSYCHIC, Type.FIGHTING, Type.DARKNESS, Type.METAL))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-149 - Lugia ex
     */
    public static final PokemonCard LUGIA_EX = PokemonCard.basic(
            "A4-149", "Lugia ex", "",
            150, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Elemental Blast", "Discard a Fire, Water, and Lightning Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 1, Type.WATER, 1, Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(180), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIRE, new Literal(1), new Self()),
                    new DiscardTypeEnergy(Type.WATER, new Literal(1), new Self()),
                    new DiscardTypeEnergy(Type.LIGHTNING, new Literal(1), new Self())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A4-150 - Bouffalant
     */
    public static final PokemonCard BOUFFALANT = PokemonCard.basic(
            "A4-150", "Bouffalant", "These Pokémon can crush a car with no more than a headbutt. Bouffalant with more hair on their heads hold higher positions within the herd.",
            100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Continuous Headbutt", "Flip a coin until you get tails. This attack does 70 damage for each heads.", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new FlipUntilTails(),
                    new DealDamage(new Product(new Literal(70), new NumberHeads()), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /** A4-181 · Sentret (alternate art of A4-138) */
    public static final PokemonCard SENTRET_IR = SENTRET.withId("A4-181", CardRarity.ILLUSTRATION_RARE);

    /** A4-182 · Hoothoot (alternate art of A4-140) */
    public static final PokemonCard HOOTHOOT_IR = HOOTHOOT.withId("A4-182", CardRarity.ILLUSTRATION_RARE);

    /** A4-183 · Stantler (alternate art of A4-147) */
    public static final PokemonCard STANTLER_IR = STANTLER.withId("A4-183", CardRarity.ILLUSTRATION_RARE);

    /** A4-184 · Smeargle (alternate art of A4-148) */
    public static final PokemonCard SMEARGLE_IR = SMEARGLE.withId("A4-184", CardRarity.ILLUSTRATION_RARE);

    /** A4-185 · Blissey (alternate art of A4-132) */
    public static final PokemonCard BLISSEY_IR = BLISSEY.withId("A4-185", CardRarity.ILLUSTRATION_RARE);

    /** A4-195 · Lugia ex (alternate art of A4-149) */
    public static final PokemonCard LUGIA_EX_UR = LUGIA_EX.withId("A4-195", CardRarity.ULTRA_RARE);

    /** A4-211 · Lugia ex (alternate art of A4-149) */
    public static final PokemonCard LUGIA_EX_IM = LUGIA_EX.withId("A4-211", CardRarity.IMMERSIVE);

    /** A4-230 · Lickitung (shiny of A2-124) */
    public static final PokemonCard LICKITUNG_SR = com.tcgpocket.pool.A2.Colorless.LICKITUNG.withId("A4-230", CardRarity.SHINY_RARE);

    /** A4-231 · Eevee (shiny of A4-134) */
    public static final PokemonCard EEVEE_SR = EEVEE.withId("A4-231", CardRarity.SHINY_RARE);

    /** A4-239 · Lickilicky ex (shiny of A2-125) */
    public static final PokemonCard LICKILICKY_EX_SUR = com.tcgpocket.pool.A2.Colorless.LICKILICKY_EX.withId("A4-239", CardRarity.SHINY_ULTRA_RARE);

    /** A4-241 · Lugia ex (alternate art of A4-149) */
    public static final PokemonCard LUGIA_EX_HR = LUGIA_EX.withId("A4-241", CardRarity.HYPER_RARE);

    static final List<PokemonCard> CARDS = List.of(
            SPEAROW, FEAROW, CHANSEY, BLISSEY, KANGASKHAN, EEVEE, PORYGON, PORYGON2, PORYGON_Z,
            SENTRET, FURRET, HOOTHOOT, NOCTOWL, AIPOM, AMBIPOM, DUNSPARCE, TEDDIURSA, URSARING,
            STANTLER, SMEARGLE, LUGIA_EX, BOUFFALANT,
            SENTRET_IR, HOOTHOOT_IR, STANTLER_IR, SMEARGLE_IR, BLISSEY_IR, LUGIA_EX_UR,
            LUGIA_EX_IM, LICKITUNG_SR, EEVEE_SR, LICKILICKY_EX_SUR, LUGIA_EX_HR);

    private Colorless() {
    }
}
