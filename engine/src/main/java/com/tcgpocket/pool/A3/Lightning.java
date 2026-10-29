package com.tcgpocket.pool.A3;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.target.*;
import com.tcgpocket.trigger.DamageIncoming;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * Celestial Guardians — Lightning.
 */
public final class Lightning {

    /**
     * A3-057 - Pikachu
     */
    public static final PokemonCard PIKACHU = PokemonCard.basic(
            "A3-057", "Pikachu", "When it is angered, it immediately discharges the energy stored in the pouches in its cheeks.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tail Smack", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-058 - Alolan Raichu ex
     */
    public static final PokemonCard ALOLAN_RAICHU_EX = PokemonCard.evolution(
            "A3-058", "Alolan Raichu ex", "",
            1, "Pikachu", 140, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Psychic", "This attack does 30 more damage for each Energy attached to your opponent's Active Pokémon.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Sum(
                            new Literal(60),
                            new Product(new Literal(30), new EnergyOn(new OpponentActive()))), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX);

    /**
     * A3-059 - Alolan Geodude
     */
    public static final PokemonCard ALOLAN_GEODUDE = PokemonCard.basic(
            "A3-059", "Alolan Geodude", "If you mistake it for a rock and step on it, it will headbutt you in anger. In addition to the pain, it will also zap you with a shock.",
            70, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Knuckle Punch", "", EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-060 - Alolan Graveler
     */
    public static final PokemonCard ALOLAN_GRAVELER = PokemonCard.evolution(
            "A3-060", "Alolan Graveler", "When two Graveler fight each other, it fills the surroundings with flashes of light and sound. People call it the “fireworks of the earth.”",
            1, "Alolan Geodude", 110, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Heavy Impact", "", EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-061 - Alolan Golem
     */
    public static final PokemonCard ALOLAN_GOLEM = PokemonCard.evolution(
            "A3-061", "Alolan Golem", "It's grumpy and stubborn. If you upset it, it discharges electricity from the surface of its body and growls with a voice like thunder.",
            2, "Alolan Graveler", 160, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Super Zap Cannon", "Discard 2 Lightning Energy from this Pokémon.", EnergyCost.of(Type.LIGHTNING, 2, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(150), new OpponentActive()),
                    new DiscardTypeEnergy(Type.LIGHTNING, new Literal(2), new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-062 - Helioptile
     */
    public static final PokemonCard HELIOPTILE = PokemonCard.basic(
            "A3-062", "Helioptile", "When spread, the frills on its head act like solar panels, generating the power behind this Pokémon's electric moves.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Smash Kick", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-063 - Heliolisk
     */
    public static final PokemonCard HELIOLISK = PokemonCard.evolution(
            "A3-063", "Heliolisk", "One Heliolisk basking in the sun with its frill outspread is all it would take to produce enough electricity to power a city.",
            1, "Helioptile", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Rear Kick", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-064 - Charjabug
     */
    public static final PokemonCard CHARJABUG = PokemonCard.evolution(
            "A3-064", "Charjabug", "While its durable shell protects it from attacks, Charjabug strikes at enemies with jolts of electricity discharged from the tips of its jaws.",
            1, "Grubbin", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Vise Grip", "", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-065 - Vikavolt
     * 
     * TODO Double check whether Fossils are playable under the debuff. 
     */
    public static final PokemonCard VIKAVOLT = PokemonCard.evolution(
            "A3-065", "Vikavolt", "It builds up electricity in its abdomen, focuses it through its jaws, and then fires the electricity off in concentrated beams.",
            2, "Charjabug", 140, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Disconnect", "During your opponent's next turn, they can't play any Item cards from their hand.", EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new PreventItem(new Literal(1))))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-066 - Oricorio — Safeguard answers {@link DamageIncoming}, which is
     * only ever announced for attack damage, by zeroing it for the rest of the
     * turn. The attacker is the event's source, so a non-ex attacker after an
     * ex one in the same turn would still be blocked; no attack in Pocket hits
     * twice from two Pokémon.
     */
    public static final PokemonCard ORICORIO = PokemonCard.basic(
            "A3-066", "Oricorio", "This form of Oricorio has sipped yellow nectar. It uses nimble steps to approach opponents, then knocks them out with electric punches.",
            70, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Zzzap", "", EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING).withAbility(new PassiveAbility("Safeguard",
            "Prevent all damage done to this Pokémon by attacks from your opponent's Pokémon ex.",
            List.of(new Trigger(DamageIncoming.class,
                    new And<>(new EventConcerns(new Self()), new For(new HasTag(CardTag.EX), new EventSource())),
                    new Attempt(List.of(new PreventDamage(new Self(), new Literal(0))))))));

    /**
     * A3-067 - Togedemaru
     */
    public static final PokemonCard TOGEDEMARU = PokemonCard.basic(
            "A3-067", "Togedemaru", "With the long hairs on its back, this Pokémon takes in electricity from other electric Pokémon. It stores what it absorbs in an electric sac.",
            70, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Electrosmash", "Flip a coin. If heads, this attack does 30 more damage.", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(
                            new Literal(20),
                            new Product(new NumberHeads(), new Literal(30))), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-068 - Tapu Koko
     */
    public static final PokemonCard TAPU_KOKO = PokemonCard.basic(
            "A3-068", "Tapu Koko", "Although it's called a guardian deity, if a person or Pokémon puts it in a bad mood, it will become a malevolent deity and attack.",
            100, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Volt Switch", "Switch this Pokémon with 1 of your Benched Lightning Pokémon.", EnergyCost.of(Type.LIGHTNING, 3), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new SwitchActive(new SelfSide(), new ChosenFrom(
                            new AttackerBench(), new AttackerSide(), new IsType(Type.LIGHTNING), "Choose a Benched Lightning Pokémon to switch in"))))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /** A3-165 · Oricorio (alternate art of A3-066) */
    public static final PokemonCard ORICORIO_IR = ORICORIO.withId("A3-165", CardRarity.ILLUSTRATION_RARE);
    /** A3-166 · Tapu Koko (alternate art of A3-068) */
    public static final PokemonCard TAPU_KOKO_IR = TAPU_KOKO.withId("A3-166", CardRarity.ILLUSTRATION_RARE);
    /** A3-185 · Alolan Raichu ex (alternate art of A3-058) */
    public static final PokemonCard ALOLAN_RAICHU_EX_UR = ALOLAN_RAICHU_EX.withId("A3-185", CardRarity.ULTRA_RARE);
    /** A3-203 · Alolan Raichu ex (alternate art of A3-058) */
    public static final PokemonCard ALOLAN_RAICHU_EX_SIR = ALOLAN_RAICHU_EX.withId("A3-203", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            PIKACHU, ALOLAN_RAICHU_EX, ALOLAN_GEODUDE, ALOLAN_GRAVELER, ALOLAN_GOLEM, HELIOPTILE, HELIOLISK,
            CHARJABUG, VIKAVOLT, ORICORIO, TOGEDEMARU, TAPU_KOKO,
            ORICORIO_IR, TAPU_KOKO_IR, ALOLAN_RAICHU_EX_UR, ALOLAN_RAICHU_EX_SIR);

    private Lightning() {
    }
}
