package com.tcgpocket.pool.A4;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.DamagedWhileActiveLastTurn;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.condition.EventSideIs;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.ForAny;
import com.tcgpocket.condition.HasAbility;
import com.tcgpocket.condition.HasAbilityNamed;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.IsDamaged;
import com.tcgpocket.condition.IsPokemon;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.condition.OwnerHasInPlay;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.effect.IncreaseSideDamage;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.effect.PreventDamage;
import com.tcgpocket.effect.ReduceDamageDealt;
import com.tcgpocket.effect.ReduceSideDamageTaken;
import com.tcgpocket.effect.SearchDeck;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.CurrentHP;
import com.tcgpocket.number.Difference;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.AttackerAll;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.AttackDeclared;
import com.tcgpocket.trigger.DamageIncoming;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * Wisdom of Sea and Sky — Psychic.
 */
public final class Psychic {

    /** "This Ability works if you have any Unown in play with an Ability other than {@code ability}." */
    private static ICondition<ResolutionContext> otherUnownThan(String ability) {
        return new For(new OwnerHasInPlay(new And<>(new IsSpecies("Unown"),
                new And<>(new HasAbility(), new Not<>(new HasAbilityNamed(ability))))), new Self());
    }

    /**
     * A4-073 - Slowpoke
     */
    public static final PokemonCard SLOWPOKE = PokemonCard.basic(
            "A4-073", "Slowpoke", "It is incredibly slow and dopey. It takes five seconds for it to feel pain when under attack.",
            70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Rest", "This Pokémon is now Asleep. Heal 30 damage from it.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new AddStatus(new SleepStatus(), new Self()),
                    new HealDamage(new Literal(30), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A4-074 - Slowking
     */
    public static final PokemonCard SLOWKING = PokemonCard.evolution(
            "A4-074", "Slowking", "When its head was bitten, toxins entered Slowpoke's head and unlocked an extraordinary power.",
            1, "Slowpoke", 110, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Psychic", "This attack does 30 more damage for each Energy attached to your opponent's Active Pokémon.", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Sum(new Literal(10), new Product(new Literal(30), new EnergyOn(new OpponentActive()))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A4-075 - Smoochum — Baby Pokémon: a free attack, no Retreat Cost and no Weakness.
     */
    public static final PokemonCard SMOOCHUM = PokemonCard.basic(
            "A4-075", "Smoochum", "If its face gets even slightly dirty, Smoochum will bathe immediately. But if its body gets dirty, Smoochum doesn't really seem to care.",
            30, Type.PSYCHIC, EnergyCost.free(), List.of(new Action(
                    "Shivery Wave", "This attack does 20 damage for each Energy attached to your opponent's Active Pokémon.", EnergyCost.free(), new Attempt(List.of(
                    new DealDamage(new Product(new Literal(20), new EnergyOn(new OpponentActive())), new OpponentActive())))
            )), CardRarity.RARE
    );

    /**
     * A4-076 - Jynx
     */
    public static final PokemonCard JYNX = PokemonCard.basic(
            "A4-076", "Jynx", "Its strange cries sound like human language. There are some musicians who compose songs for Jynx to sing.",
            80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Attract Smack", "Flip a coin. If heads, your opponent's Active Pokémon is now Paralyzed.", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new AddStatus(new ParalysisStatus(), new OpponentActive()))))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A4-077 - Cleffa — Baby Pokémon: a free attack, no Retreat Cost and no Weakness.
     */
    public static final PokemonCard CLEFFA = PokemonCard.basic(
            "A4-077", "Cleffa", "According to local rumors, Cleffa are often seen in places where shooting stars have fallen.",
            30, Type.PSYCHIC, EnergyCost.free(), List.of(new Action(
                    "Twinkly Call", "Put a random Pokémon from your deck into your hand.", EnergyCost.free(), new Attempt(List.of(
                    new SearchDeck(new AttackerSide(), new Literal(1), new IsPokemon())))
            )), CardRarity.RARE
    );

    /**
     * A4-078 - Togepi
     */
    public static final PokemonCard TOGEPI = PokemonCard.basic(
            "A4-078", "Togepi", "The shell seems to be filled with joy. It is said that it will share good luck when treated kindly.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Charm", "During your opponent's next turn, attacks used by the Defending Pokémon do -20 damage.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new ReduceDamageDealt(new Literal(20), new OpponentActive(), new Literal(1))))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A4-079 - Togetic
     */
    public static final PokemonCard TOGETIC = PokemonCard.evolution(
            "A4-079", "Togetic", "They say that it will appear before kindhearted, caring people and shower them with happiness.",
            1, "Togepi", 90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Speed Dive", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /**
     * A4-080 - Togekiss — Celestial Blessing: like Bastiodon's Guarded Grill,
     * flips on {@link DamageIncoming}, and the block lasts the rest of the
     * turn, which no attack in Pocket hits twice within.
     */
    public static final PokemonCard TOGEKISS = PokemonCard.evolution(
            "A4-080", "Togekiss", "These Pokémon are never seen anywhere near conflict or turmoil. In recent times, they've hardly been seen at all.",
            2, "Togetic", 140, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Magical Shot", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.METAL).withAbility(new PassiveAbility("Celestial Blessing",
            "If any damage is done to this Pokémon by attacks, flip a coin. If heads, prevent that damage.",
            List.of(new Trigger(DamageIncoming.class, new EventConcerns(new Self()),
                    new Attempt(List.of(
                            new FlipN(new Literal(1)),
                            new ConditionalEffect(new LastCoinTossHeads(), new PreventDamage(new Self(), new Literal(0)))))))));

    /**
     * A4-081 - Natu
     */
    public static final PokemonCard NATU = PokemonCard.basic(
            "A4-081", "Natu", "It is extremely good at climbing tree trunks and likes to eat the new sprouts on the trees.",
            50, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Peck", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A4-082 - Xatu — Life Drain places the difference, so a target already at
     * 10 HP or less is left alone rather than healed.
     */
    public static final PokemonCard XATU = PokemonCard.evolution(
            "A4-082", "Xatu", "They say that it stays still and quiet because it is seeing both the past and future at the same time.",
            1, "Natu", 80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Life Drain", "Flip a coin. If heads, your opponent's Active Pokémon's remaining HP is now 10.", EnergyCost.of(Type.PSYCHIC, 2), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(),
                            new PlaceDamage(new Difference(new CurrentHP(new OpponentActive()), new Literal(10)), new OpponentActive()))))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS);

    /**
     * A4-083 - Espeon ex
     */
    public static final PokemonCard ESPEON_EX = PokemonCard.evolution(
            "A4-083", "Espeon ex", "",
            1, "Eevee", 140, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Super Psy Bolt", "", EnergyCost.of(Type.PSYCHIC, 2), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.DARKNESS).withTags(CardTag.EX).withAbility(new ActivatedAbility("Psychic Healing",
            "Once during your turn, if this Pokémon is in the Active Spot, you may heal 30 damage from 1 of your Pokémon.",
            new PlainAction("", new Attempt(List.of(
                    new HealDamage(new Literal(30), new ChosenFrom(
                            new AttackerAll(), new AttackerSide(), new IsDamaged(), "Choose a Pokémon to heal"))))),
            true, new And<>(new For(new IsActive(), new Self()), new ForAny(new IsDamaged(), new AttackerAll()))));

    /**
     * A4-084 - Unown GUARD — answers the opponent's {@link AttackDeclared} from
     * anywhere in play, so the reduction covers whichever Pokémon is hit.
     */
    public static final PokemonCard UNOWN_GUARD = PokemonCard.basic(
            "A4-084", "Unown", "Its flat, thin body is always stuck on walls. Its shape appears to have some meaning.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Hidden Power", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS).withAbility(new PassiveAbility("GUARD",
            "This Ability works if you have any Unown in play with an Ability other than GUARD. All of your Pokémon take -10 damage from attacks from your opponent's Pokémon.", List.of(
            new Trigger(AttackDeclared.class,
                    new And<>(new Not<>(new EventSideIs(new SelfSide())), otherUnownThan("GUARD")),
                    new Attempt(List.of(new ReduceSideDamageTaken(new Literal(10), new Literal(0))))))));

    /**
     * A4-085 - Unown POWER
     */
    public static final PokemonCard UNOWN_POWER = PokemonCard.basic(
            "A4-085", "Unown", "Its flat, thin body is always stuck on walls. Its shape appears to have some meaning.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Hidden Power", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS).withAbility(new PassiveAbility("POWER",
            "This Ability works if you have any Unown in play with an Ability other than POWER. Attacks used by your Pokémon do +10 damage to your opponent's Active Pokémon.", List.of(
            new Trigger(AttackDeclared.class,
                    new And<>(new EventSideIs(new SelfSide()), otherUnownThan("POWER")),
                    new Attempt(List.of(new IncreaseSideDamage(new Literal(10), new Literal(0))))))));

    /**
     * A4-086 - Wobbuffet
     */
    public static final PokemonCard WOBBUFFET = PokemonCard.basic(
            "A4-086", "Wobbuffet", "It hates light and shock. If attacked, it inflates its body to pump up its counterstrike.",
            90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Reply Strongly", "If this Pokémon was damaged by an attack during your opponent's last turn while it was in the Active Spot, this attack does 50 more damage.", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new DamagedWhileActiveLastTurn(new Self()), new Literal(80))),
                            new Literal(30)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A4-087 - Girafarig
     */
    public static final PokemonCard GIRAFARIG = PokemonCard.basic(
            "A4-087", "Girafarig", "Girafarig's tail has a small head. It instinctively bites at any foe that approaches the Pokémon from behind.",
            80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Psybeam", "Your opponent's Active Pokémon is now Confused.", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new AddStatus(new ConfusionStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A4-088 - Snubbull
     */
    public static final PokemonCard SNUBBULL = PokemonCard.basic(
            "A4-088", "Snubbull", "It grows close to others easily and is also easily spoiled. The disparity between its face and its actions makes many young people wild about it.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A4-089 - Granbull
     */
    public static final PokemonCard GRANBULL = PokemonCard.evolution(
            "A4-089", "Granbull", "While it has powerful jaws, it doesn't care for disputes, so it rarely has a chance to display their might.",
            1, "Snubbull", 120, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Wild Tackle", "This Pokémon also does 20 damage to itself.", EnergyCost.of(Type.PSYCHIC, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(100), new OpponentActive()),
                    new DealDamage(new Literal(20), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /**
     * A4-090 - Munna
     */
    public static final PokemonCard MUNNA = PokemonCard.basic(
            "A4-090", "Munna", "Late at night, it appears beside people's pillows. As it feeds on dreams, the patterns on its body give off a faint glow.",
            70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Psyshot", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A4-091 - Musharna
     */
    public static final PokemonCard MUSHARNA = PokemonCard.evolution(
            "A4-091", "Musharna", "When dark mists emanate from its body, don't get too near. If you do, your nightmares will become reality.",
            1, "Munna", 110, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Dream Dance", "Both Active Pokémon are now Asleep.", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new AddStatus(new SleepStatus(), new OpponentActive()),
                    new AddStatus(new SleepStatus(), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /** A4-173 · Togepi (alternate art of A4-078) */
    public static final PokemonCard TOGEPI_IR = TOGEPI.withId("A4-173", CardRarity.ILLUSTRATION_RARE);

    /** A4-174 · Xatu (alternate art of A4-082) */
    public static final PokemonCard XATU_IR = XATU.withId("A4-174", CardRarity.ILLUSTRATION_RARE);

    /** A4-175 · Wobbuffet (alternate art of A4-086) */
    public static final PokemonCard WOBBUFFET_IR = WOBBUFFET.withId("A4-175", CardRarity.ILLUSTRATION_RARE);

    /** A4-190 · Espeon ex (alternate art of A4-083) */
    public static final PokemonCard ESPEON_EX_UR = ESPEON_EX.withId("A4-190", CardRarity.ULTRA_RARE);

    /** A4-205 · Espeon ex (alternate art of A4-083) */
    public static final PokemonCard ESPEON_EX_SIR = ESPEON_EX.withId("A4-205", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    /** A4-220 · Misdreavus (shiny of A2-066) */
    public static final PokemonCard MISDREAVUS_SR = com.tcgpocket.pool.A2.Psychic.MISDREAVUS.withId("A4-220", CardRarity.SHINY_RARE);

    /** A4-237 · Mismagius ex (shiny of A2-067) */
    public static final PokemonCard MISMAGIUS_EX_SUR = com.tcgpocket.pool.A2.Psychic.MISMAGIUS_EX.withId("A4-237", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            SLOWPOKE, SLOWKING, SMOOCHUM, JYNX, CLEFFA, TOGEPI, TOGETIC, TOGEKISS, NATU, XATU,
            ESPEON_EX, UNOWN_GUARD, UNOWN_POWER, WOBBUFFET, GIRAFARIG, SNUBBULL, GRANBULL, MUNNA, MUSHARNA,
            TOGEPI_IR, XATU_IR, WOBBUFFET_IR, ESPEON_EX_UR,
            ESPEON_EX_SIR, MISDREAVUS_SR, MISMAGIUS_EX_SUR);

    private Psychic() {
    }
}
