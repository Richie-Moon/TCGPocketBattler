package com.tcgpocket.pool.A4;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.ForAny;
import com.tcgpocket.condition.HasEnergy;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.IsDamaged;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardRandomFromHand;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.MoveTypeEnergy;
import com.tcgpocket.effect.PreventRetreat;
import com.tcgpocket.effect.ShuffleRandomFromHand;
import com.tcgpocket.effect.SwitchActive;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.AttackerAll;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.Except;
import com.tcgpocket.target.IMultiTarget;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentBench;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Wisdom of Sea and Sky — Darkness.
 */
public final class Darkness {

    private static final IMultiTarget YOUR_OTHER_POKEMON = new Except(new AttackerAll(), new Self());

    /**
     * A4-107 - Zubat
     */
    public static final PokemonCard ZUBAT = PokemonCard.basic(
            "A4-107", "Zubat", "It emits ultrasonic waves from its mouth to check its surroundings. Even in tight caves, Zubat flies around with skill.",
            50, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Venomous Fang", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-108 - Golbat
     */
    public static final PokemonCard GOLBAT = PokemonCard.evolution(
            "A4-108", "Golbat", "It loves to drink other creatures' blood. It's said that if it finds others of its kind going hungry, it sometimes shares the blood it's gathered.",
            1, "Zubat", 70, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Venomous Fang", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-109 - Crobat ex
     */
    public static final PokemonCard CROBAT_EX = PokemonCard.evolution(
            "A4-109", "Crobat ex", "",
            2, "Golbat", 170, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Venomous Slash", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX);

    /**
     * A4-110 - Spinarak
     */
    public static final PokemonCard SPINARAK = PokemonCard.basic(
            "A4-110", "Spinarak", "It lies still in the same pose for days in its web, waiting for its unsuspecting prey to wander close.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Pierce", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-111 - Ariados
     */
    public static final PokemonCard ARIADOS = PokemonCard.evolution(
            "A4-111", "Ariados", "Every night, it wanders around in search of prey, whose movements it restrains by spewing threads before it bites into them with its fangs.",
            1, "Spinarak", 80, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bind Down", "During your opponent's next turn, the Defending Pokémon can't retreat.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new PreventRetreat(new OpponentActive(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-112 - Umbreon ex
     */
    public static final PokemonCard UMBREON_EX = PokemonCard.evolution(
            "A4-112", "Umbreon ex", "",
            1, "Eevee", 140, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Darkness Fang", "", EnergyCost.of(Type.DARKNESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.GRASS).withTags(CardTag.EX).withAbility(new ActivatedAbility("Dark Chase",
            "Once during your turn, if this Pokémon is in the Active Spot, you may switch in 1 of your opponent's Benched Pokémon that has damage on it to the Active Spot.",
            new PlainAction("", new Attempt(List.of(
                    new SwitchActive(new OpponentSide(), new ChosenFrom(
                            new OpponentBench(), new AttackerSide(), new IsDamaged(),
                            "Choose a damaged Benched Pokémon to switch in."))))),
            true, new And<>(new For(new IsActive(), new Self()), new ForAny(new IsDamaged(), new OpponentBench()))));

    /**
     * A4-113 - Murkrow
     */
    public static final PokemonCard MURKROW = PokemonCard.basic(
            "A4-113", "Murkrow", "Feared and loathed by many, it is believed to bring misfortune to all those who see it at night.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Astonish", "Flip a coin. If heads, your opponent reveals a random card from their hand and shuffles it into their deck.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new ShuffleRandomFromHand(new OpponentSide()))))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-114 - Honchkrow
     */
    public static final PokemonCard HONCHKROW = PokemonCard.evolution(
            "A4-114", "Honchkrow", "It is merciless by nature. It is said that it never forgives the mistakes of its Murkrow followers.",
            1, "Murkrow", 120, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Dark Cutter", "", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4-115 - Sneasel
     */
    public static final PokemonCard SNEASEL = PokemonCard.basic(
            "A4-115", "Sneasel", "This cunning Pokémon hides under the cover of darkness, waiting to attack its prey.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Scratch", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-116 - Weavile
     */
    public static final PokemonCard WEAVILE = PokemonCard.evolution(
            "A4-116", "Weavile", "Evolution made it even more devious. It communicates by clawing signs in boulders.",
            1, "Sneasel", 80, Type.DARKNESS, EnergyCost.free(), List.of(new Action(
                    "Slash", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-117 - Houndour
     */
    public static final PokemonCard HOUNDOUR = PokemonCard.basic(
            "A4-117", "Houndour", "It is smart enough to hunt in packs. It uses a variety of cries for communicating with others.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.DARKNESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-118 - Houndoom
     */
    public static final PokemonCard HOUNDOOM = PokemonCard.evolution(
            "A4-118", "Houndoom", "If you are burned by the flames it shoots from its mouth, the pain will never go away.",
            1, "Houndour", 110, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Diving Swipe", "Discard a random card from your opponent's hand.", EnergyCost.of(Type.DARKNESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new DiscardRandomFromHand(new OpponentSide(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-119 - Tyranitar
     */
    public static final PokemonCard TYRANITAR = PokemonCard.evolution(
            "A4-119", "Tyranitar", "Extremely strong, it can change the landscape. It is so insolent that it doesn't care about others.",
            2, "Pupitar", 160, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Buster Tail", "", EnergyCost.of(Type.DARKNESS, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(130), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.GRASS).withAbility(new ActivatedAbility("Energy Plunder",
            "Once during your turn, you may move all Darkness Energy from each of your Pokémon to this Pokémon.",
            new PlainAction("", new Attempt(List.of(
                    new MoveTypeEnergy(Type.DARKNESS, YOUR_OTHER_POKEMON, new Self())))),
            true, new ForAny(new HasEnergy(EnergyCost.of(Type.DARKNESS, 1)), YOUR_OTHER_POKEMON)));

    /**
     * A4-120 - Absol
     */
    public static final PokemonCard ABSOL = PokemonCard.basic(
            "A4-120", "Absol", "Swift as the wind, Absol races through fields and mountains. Its curved, bow-like horn is acutely sensitive to the warning signs of natural disasters.",
            80, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leap Over", "This attack does 30 damage to 1 of your opponent's Benched Pokémon.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Choose 1 of your opponent's Benched Pokémon."))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /** A4-177 · Spinarak (alternate art of A4-110) */
    public static final PokemonCard SPINARAK_IR = SPINARAK.withId("A4-177", CardRarity.ILLUSTRATION_RARE);

    /** A4-178 · Murkrow (alternate art of A4-113) */
    public static final PokemonCard MURKROW_IR = MURKROW.withId("A4-178", CardRarity.ILLUSTRATION_RARE);

    /** A4-179 · Tyranitar (alternate art of A4-119) */
    public static final PokemonCard TYRANITAR_IR = TYRANITAR.withId("A4-179", CardRarity.ILLUSTRATION_RARE);

    /** A4-192 · Crobat ex (alternate art of A4-109) */
    public static final PokemonCard CROBAT_EX_UR = CROBAT_EX.withId("A4-192", CardRarity.ULTRA_RARE);

    /** A4-193 · Umbreon ex (alternate art of A4-112) */
    public static final PokemonCard UMBREON_EX_UR = UMBREON_EX.withId("A4-193", CardRarity.ULTRA_RARE);

    /** A4-207 · Crobat ex (alternate art of A4-109) */
    public static final PokemonCard CROBAT_EX_SIR = CROBAT_EX.withId("A4-207", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    /** A4-208 · Umbreon ex (alternate art of A4-112) */
    public static final PokemonCard UMBREON_EX_SIR = UMBREON_EX.withId("A4-208", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    /** A4-223 · Nidoran♀ (shiny of A1-166) */
    public static final PokemonCard NIDORAN_F_SR = com.tcgpocket.pool.A1.Darkness.NIDORAN_F.withId("A4-223", CardRarity.SHINY_RARE);

    /** A4-224 · Nidorina (shiny of A1-167) */
    public static final PokemonCard NIDORINA_SR = com.tcgpocket.pool.A1.Darkness.NIDORINA.withId("A4-224", CardRarity.SHINY_RARE);

    /** A4-225 · Nidoqueen (shiny of A1-168) */
    public static final PokemonCard NIDOQUEEN_SR = com.tcgpocket.pool.A1.Darkness.NIDOQUEEN.withId("A4-225", CardRarity.SHINY_RARE);

    /** A4-226 · Nidoran♂ (shiny of A1-169) */
    public static final PokemonCard NIDORAN_M_SR = com.tcgpocket.pool.A1.Darkness.NIDORAN_M.withId("A4-226", CardRarity.SHINY_RARE);

    /** A4-227 · Nidorino (shiny of A1-170) */
    public static final PokemonCard NIDORINO_SR = com.tcgpocket.pool.A1.Darkness.NIDORINO.withId("A4-227", CardRarity.SHINY_RARE);

    /** A4-228 · Nidoking (shiny of A1-171) */
    public static final PokemonCard NIDOKING_SR = com.tcgpocket.pool.A1.Darkness.NIDOKING.withId("A4-228", CardRarity.SHINY_RARE);

    /** A4-229 · Sneasel (shiny of A4-115) */
    public static final PokemonCard SNEASEL_SR = SNEASEL.withId("A4-229", CardRarity.SHINY_RARE);

    /** A4-238 · Weavile ex (shiny of A2-099) */
    public static final PokemonCard WEAVILE_EX_SUR = com.tcgpocket.pool.A2.Darkness.WEAVILE_EX.withId("A4-238", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            ZUBAT, GOLBAT, CROBAT_EX, SPINARAK, ARIADOS, UMBREON_EX, MURKROW, HONCHKROW,
            SNEASEL, WEAVILE, HOUNDOUR, HOUNDOOM, TYRANITAR, ABSOL,
            SPINARAK_IR, MURKROW_IR, TYRANITAR_IR, CROBAT_EX_UR, UMBREON_EX_UR,
            CROBAT_EX_SIR, UMBREON_EX_SIR, NIDORAN_F_SR, NIDORINA_SR, NIDOQUEEN_SR, NIDORAN_M_SR, NIDORINO_SR, NIDOKING_SR, SNEASEL_SR, WEAVILE_EX_SUR);

    private Darkness() {
    }
}
