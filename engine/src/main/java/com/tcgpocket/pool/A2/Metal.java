package com.tcgpocket.pool.A2;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
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
import com.tcgpocket.trigger.DamageIncoming;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * Space-Time Smackdown — Metal.
 */
public final class Metal {

    /**
     * A2-111 - Skarmory
     */
    public static final PokemonCard SKARMORY = PokemonCard.basic(
            "A2-111", "Skarmory", "People fashion swords from Skarmory's shed feathers, so this Pokémon is a popular element in heraldic designs.",
            80, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Metal Arms", "If this Pokémon has a Pokémon Tool attached, this attack does 30 more damage.", EnergyCost.of(Type.METAL, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(
                            new Branch.Case(new For(new HasTool(), new Self()), new Literal(50))
                    ), new Literal(20)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-112 - Registeel
     */
    public static final PokemonCard REGISTEEL = PokemonCard.basic(
            "A2-112", "Registeel", "Registeel's body is made of a strange material that is flexible enough to stretch and shrink but also more durable than any metal.",
            110, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Metal Claw", "", EnergyCost.of(Type.METAL, 3), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-113 - Shieldon
     */
    public static final PokemonCard SHIELDON = PokemonCard.evolution(
            "A2-113", "Shieldon", "A mild-mannered, herbivorous Pokémon, it used its face to dig up tree roots to eat. The skin on its face was plenty tough.",
            1, "Armor Fossil", 100, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Headbutt", "", EnergyCost.of(Type.METAL, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-114 - Bastiodon — Guarded Grill: flips on {@link DamageIncoming}, so
     * only when an attack would actually damage it. The reduction lasts the
     * rest of the turn; an attack hitting it twice would flip twice, which no
     * attack in Pocket does yet.
     */
    public static final PokemonCard BASTIODON = PokemonCard.evolution(
            "A2-114", "Bastiodon", "The bones of its face are huge and hard, so they were mistaken for its spine until after this Pokémon was successfully restored.",
            2, "Shieldon", 160, Type.METAL, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Headbang", "", EnergyCost.of(Type.METAL, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.RARE
    ).withAbility(new PassiveAbility("Guarded Grill",
            "If any damage is done to this Pokémon by attacks, flip a coin. If heads, this Pokémon takes -100 damage from that attack.",
            List.of(new Trigger(DamageIncoming.class, new EventConcerns(new Self()),
                    new Attempt(List.of(
                            new FlipN(new Literal(1)),
                            new ConditionalEffect(new LastCoinTossHeads(),
                                    new ReduceDamageTaken(new Literal(100), new Self(), new Literal(0)))))))))
            .withWeakness(Type.FIRE);

    /**
     * A2-115 - Wormadam
     */
    public static final PokemonCard WORMADAM = PokemonCard.evolution(
            "A2-115", "Wormadam", "Its appearance changes depending on where it evolved. The materials on hand become a part of its body.",
            1, "Burmy", 110, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Iron Head", "Flip a coin until you get tails. This attack does 30 more damage for each heads.", EnergyCost.of(Type.METAL, 2), new Attempt(List.of(
                    new FlipUntilTails(),
                    new DealDamage(new Sum(
                            new Literal(50),
                            new Product(new NumberHeads(), new Literal(30))), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-116 - Bronzor
     */
    public static final PokemonCard BRONZOR = PokemonCard.basic(
            "A2-116", "Bronzor", "Ancient people believed that the pattern on Bronzor's back contained a mysterious power.",
            60, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.METAL, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-117 - Bronzong
     */
    public static final PokemonCard BRONZONG = PokemonCard.evolution(
            "A2-117", "Bronzong", "In ages past, this Pokémon was revered as a bringer of rain. It was found buried in the ground.",
            1, "Bronzor", 120, Type.METAL, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Guard Press", "During your opponent's next turn, this Pokémon takes -20 damage from attacks.",
                    EnergyCost.of(Type.METAL, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new ReduceDamageTaken(new Literal(20), new Self(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-118 - Probopass
     */
    public static final PokemonCard PROBOPASS = PokemonCard.evolution(
            "A2-118", "Probopass", "Although it can control its units known as Mini-Noses, they sometimes get lost and don't come back.",
            1, "Nosepass", 130, Type.METAL, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Triple Nose", "Flip 3 coins. This attack does 50 more damage for each heads.",
                    EnergyCost.of(Type.METAL, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(3)),
                    new DealDamage(new Sum(
                            new Literal(30),
                            new Product(new NumberHeads(), new Literal(50))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2-119 - Dialga ex
     */
    public static final PokemonCard DIALGA_EX = PokemonCard.basic(
            "A2-119", "Dialga ex", "",
            150, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Metallic Turbo", "Take 2 Metal Energy from your Energy Zone and attach it to 1 of your Benched Pokémon.",
                    EnergyCost.of(Type.METAL, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new AttachEnergy(Type.METAL, new Literal(2), new ChosenFrom(
                            new AttackerBench(), new AttackerSide(), "Choose a Benched Pokémon to attach 2 Metal Energy to."))))
            ), new Action(
                    "Heavy Impact", "", EnergyCost.of(Type.METAL, 2, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(100), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX);

    /**
     * A2-120 - Heatran
     */
    public static final PokemonCard HEATRAN = PokemonCard.basic(
            "A2-120", "Heatran", "It dwells in volcanic caves. It digs in with its cross-shaped feet to crawl on ceilings and walls.",
            120, Type.METAL, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Steel Tackle", "This Pokémon also does 20 damage to itself.", EnergyCost.of(Type.METAL, 3), new Attempt(List.of(
                    new DealDamage(new Literal(110), new OpponentActive()),
                    new DealDamage(new Literal(20), new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    static final List<PokemonCard> CARDS = List.of(
            SKARMORY, REGISTEEL, SHIELDON, BASTIODON, WORMADAM, BRONZOR, BRONZONG, PROBOPASS,
            DIALGA_EX, HEATRAN);

    private Metal() {
    }
}
