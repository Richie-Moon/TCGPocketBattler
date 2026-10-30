package com.tcgpocket.pool.A3b;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.EvolvesAs;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.EventSideIs;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.HasTool;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.IsTool;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DrawCard;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.effect.SearchDeck;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnEnd;

import java.util.List;

/**
 * Eevee Grove — Colorless.
 */
public final class Colorless {

    /**
     * A3b-055 - Eevee
     */
    public static final PokemonCard EEVEE = PokemonCard.basic(
            "A3b-055", "Eevee", "Its ability to evolve into many forms allows it to adapt smoothly and perfectly to any environment.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Collect", "Draw a card.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DrawCard(new Literal(1), new SelfSide())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3b-056 - Eevee ex
     */
    public static final PokemonCard EEVEE_EX = PokemonCard.basic(
            "A3b-056", "Eevee ex", "",
            90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX).withAbility(new PassiveAbility(
            "Veevee 'volve",
            "This Pokémon can evolve into any Pokémon that evolves from Eevee if you play it from your hand onto this Pokémon. (This Pokémon can't evolve during your first turn or the turn you play it.)",
            new EvolvesAs("Eevee")));

    /**
     * A3b-057 - Snorlax ex
     */
    public static final PokemonCard SNORLAX_EX = PokemonCard.basic(
            "A3b-057", "Snorlax ex", "",
            160, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Flop-Down Punch", "This Pokémon is now Asleep.", EnergyCost.of(Type.COLORLESS, 4), new Attempt(List.of(
                    new DealDamage(new Literal(130), new OpponentActive()),
                    new AddStatus(new SleepStatus(), new Self())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX).withAbility(new PassiveAbility(
            "Full-Mouth Manner",
            "At the end of your turn, if this Pokémon is in the Active Spot, heal 20 damage from it.",
            List.of(new Trigger(TurnEnd.class,
                    new And<>(new EventSideIs(new SelfSide()), new For(new IsActive(), new Self())),
                    new Attempt(List.of(new HealDamage(new Literal(20), new Self())))))));

    /**
     * A3b-058 - Aipom
     */
    public static final PokemonCard AIPOM = PokemonCard.basic(
            "A3b-058", "Aipom", "As it did more and more with its tail, its hands became clumsy. It makes its nest high in the treetops.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Double Hit", "Flip 2 coins. This attack does 20 damage for each heads.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(20)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3b-059 - Ambipom
     */
    public static final PokemonCard AMBIPOM = PokemonCard.evolution(
            "A3b-059", "Ambipom", "It uses its tails for everything. If it wraps both of its tails around you and gives you a squeeze, that's proof it really likes you.",
            1, "Aipom", 90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Corkscrew Punch", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING).withAbility(new ActivatedAbility(
            "Catching Tail",
            "Once during your turn, you may put a random Pokémon Tool card from your deck into your hand.",
            new PlainAction("", new Attempt(List.of(
                    new SearchDeck(new SelfSide(), new Literal(1), new IsTool())))),
            true, new Always<>()));

    /**
     * A3b-060 - Chatot
     */
    public static final PokemonCard CHATOT = PokemonCard.basic(
            "A3b-060", "Chatot", "It mimics the cries of other Pokémon to trick them into thinking it's one of them. This way they won't attack it.",
            70, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tone-Deaf", "Your opponent's Active Pokémon is now Confused.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new AddStatus(new ConfusionStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3b-061 - Audino
     */
    public static final PokemonCard AUDINO = PokemonCard.basic(
            "A3b-061", "Audino", "This Pokémon has a kind heart. By touching with its feelers, Audino can gauge other creatures' feelings and physical conditions.",
            90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Do the Wave", "This attack does 20 damage for each of your Benched Pokémon.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Product(new CountCards(new AttackerSide(), Zone.BENCH), new Literal(20)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3b-062 - Minccino
     */
    public static final PokemonCard MINCCINO = PokemonCard.basic(
            "A3b-062", "Minccino", "The way it brushes away grime with its tail can be helpful when cleaning. But its focus on spotlessness can make cleaning more of a hassle.",
            70, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Pound", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3b-063 - Cinccino
     */
    public static final PokemonCard CINCCINO = PokemonCard.evolution(
            "A3b-063", "Cinccino", "Its body secretes oil that this Pokémon spreads over its nest as a coating to protect it from dust. Cinccino won't tolerate even a speck of the stuff.",
            1, "Minccino", 90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Slap", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3b-064 - Skwovet
     */
    public static final PokemonCard SKWOVET = PokemonCard.basic(
            "A3b-064", "Skwovet", "It stores berries in its cheeks. When there are no berries to be found, Skwovet will stuff pebbles into its cheeks to stave off its cravings.",
            70, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3b-065 - Greedent
     */
    public static final PokemonCard GREEDENT = PokemonCard.evolution(
            "A3b-065", "Greedent", "This Pokémon makes off with heaps of fallen berries by wrapping them in its tail, which is roughly twice the length of its body.",
            1, "Skwovet", 120, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Enhanced Fang", "If this Pokémon has a Pokémon Tool attached, this attack does 50 more damage.", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(
                            new For(new HasTool(), new Self()),
                            new Literal(100))
                    ), new Literal(50)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /** A3b-078 · Eevee (alternate art of A3b-055) */
    public static final PokemonCard EEVEE_IR = EEVEE.withId("A3b-078", CardRarity.ILLUSTRATION_RARE);

    /** A3b-083 · Eevee ex (alternate art of A3b-056) */
    public static final PokemonCard EEVEE_EX_UR = EEVEE_EX.withId("A3b-083", CardRarity.ULTRA_RARE);

    /** A3b-084 · Snorlax ex (alternate art of A3b-057) */
    public static final PokemonCard SNORLAX_EX_UR = SNORLAX_EX.withId("A3b-084", CardRarity.ULTRA_RARE);

    /** A3b-091 · Snorlax ex (alternate art of A3b-057) */
    public static final PokemonCard SNORLAX_EX_SIR = SNORLAX_EX.withId("A3b-091", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    /** A3b-092 · Eevee ex (alternate art of A3b-056) */
    public static final PokemonCard EEVEE_EX_IM = EEVEE_EX.withId("A3b-092", CardRarity.IMMERSIVE);

    /** A3b-102 · Farfetch'd (shiny of A1-198) */
    public static final PokemonCard FARFETCHD_SR = com.tcgpocket.pool.A1.Colorless.FARFETCHD.withId("A3b-102", CardRarity.SHINY_RARE);

    static final List<PokemonCard> CARDS = List.of(
            EEVEE, EEVEE_EX, SNORLAX_EX, AIPOM, AMBIPOM, CHATOT,
            AUDINO, MINCCINO, CINCCINO, SKWOVET, GREEDENT, EEVEE_IR,
            EEVEE_EX_UR, SNORLAX_EX_UR,
            SNORLAX_EX_SIR, EEVEE_EX_IM, FARFETCHD_SR);

    private Colorless() {
    }
}
