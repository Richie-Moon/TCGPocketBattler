package com.tcgpocket.pool.A4;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsBasic;
import com.tcgpocket.condition.IsBurned;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.AttachEnergy;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardRandomEnergy;
import com.tcgpocket.effect.DistributeMixedEnergy;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.PreventAttack;
import com.tcgpocket.effect.RepeatEffect;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.status.BurnStatus;
import com.tcgpocket.target.AllInPlay;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.Except;
import com.tcgpocket.target.Matching;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.RandomFrom;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Wisdom of Sea and Sky — Fire.
 */
public final class Fire {

    /**
     * A4-025 - Vulpix
     */
    public static final PokemonCard VULPIX = PokemonCard.basic(
            "A4-025", "Vulpix", "While young, it has six gorgeous tails. When it grows, several new tails are sprouted.",
            60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Live Coal", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A4-026 - Ninetales
     */
    public static final PokemonCard NINETALES = PokemonCard.evolution(
            "A4-026", "Ninetales", "It is said to live 1,000 years, and each of its tails is loaded with supernatural powers.",
            1, "Vulpix", 100, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Scorching Breath", "During your next turn, this Pokémon can't attack.", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive()),
                    new PreventAttack(new Self(), new Literal(2))))
            )), CardRarity.RARE
    ).withWeakness(Type.WATER);

    /**
     * A4-027 - Cyndaquil
     */
    public static final PokemonCard CYNDAQUIL = PokemonCard.basic(
            "A4-027", "Cyndaquil", "It is timid and always curls itself up in a ball. If attacked, it flares up its back for protection.",
            60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flare", "", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A4-028 - Quilava
     */
    public static final PokemonCard QUILAVA = PokemonCard.evolution(
            "A4-028", "Quilava", "Before battle, it turns its back on its opponent to demonstrate how ferociously its fire blazes.",
            1, "Cyndaquil", 80, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Combustion", "", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A4-029 - Typhlosion
     */
    public static final PokemonCard TYPHLOSION = PokemonCard.evolution(
            "A4-029", "Typhlosion", "If its rage peaks, it becomes so hot that anything that touches it will instantly go up in flames.",
            2, "Quilava", 140, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Magma Punch", "", EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.WATER).withAbility(new ActivatedAbility("Fire Breath",
            "Once during your turn, you may make your opponent's Active Pokémon Burned.",
            new PlainAction("", new Attempt(List.of(new AddStatus(new BurnStatus(), new OpponentActive())))),
            true, new Always<>()));

    /**
     * A4-030 - Slugma
     */
    public static final PokemonCard SLUGMA = PokemonCard.basic(
            "A4-030", "Slugma", "A common sight in volcanic areas, it slowly slithers around in a constant search for warm places.",
            70, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Stampede", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A4-031 - Magcargo — each of the 3 hits picks again, so one Pokémon can be chosen more than once.
     */
    public static final PokemonCard MAGCARGO = PokemonCard.evolution(
            "A4-031", "Magcargo", "Its brittle shell occasionally spouts intense flames that circulate throughout its body.",
            1, "Slugma", 120, Type.FIRE, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Spurt Fire", "1 other Pokémon (either yours or your opponent's) is chosen at random 3 times. For each time a Pokémon was chosen, do 50 damage to it.", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new RepeatEffect(new Literal(3), new DealDamage(new Literal(50), new RandomFrom(new Except(new AllInPlay(), new Self()))))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A4-032 - Magby — Baby Pokémon: a free attack, no Retreat Cost and no Weakness.
     */
    public static final PokemonCard MAGBY = PokemonCard.basic(
            "A4-032", "Magby", "This Pokémon is still small and timid. Whenever Magby gets excited or surprised, flames leak from its mouth and its nose.",
            30, Type.FIRE, EnergyCost.free(), List.of(new Action(
                    "Toasty Toss", "Take a Fire Energy from your Energy Zone and attach it to 1 of your Benched Basic Pokémon.", EnergyCost.free(), new Attempt(List.of(
                    new AttachEnergy(Type.FIRE, new Literal(1), new ChosenFrom(new AttackerBench(), new AttackerSide(), new IsBasic(), "Choose a Benched Basic Pokémon to attach a Fire Energy to."))))
            )), CardRarity.RARE
    );

    /**
     * A4-033 - Entei
     */
    public static final PokemonCard ENTEI = PokemonCard.basic(
            "A4-033", "Entei", "A Pokémon that races across the land. It is said that one is born every time a new volcano appears.",
            110, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Strong Flare", "Flip a coin. If tails, discard 2 random Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(110), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new DiscardRandomEnergy(new Literal(2), new Self()))))
            )), CardRarity.RARE
    ).withWeakness(Type.WATER);

    /**
     * A4-034 - Ho-Oh ex — one decision over the whole placement, as Inferno Dance.
     */
    public static final PokemonCard HO_OH_EX = PokemonCard.basic(
            "A4-034", "Ho-Oh ex", "",
            150, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Phoenix Turbo", "Take a Fire, Water, and Lightning Energy from your Energy Zone and attach them to your Benched Basic Pokémon in any way you like.", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new DistributeMixedEnergy(List.of(Type.FIRE, Type.WATER, Type.LIGHTNING), new Matching(new AttackerBench(), new IsBasic()), "Place the Fire, Water and Lightning Energy on your Benched Basic Pokémon.")))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A4-035 - Darumaka
     */
    public static final PokemonCard DARUMAKA = PokemonCard.basic(
            "A4-035", "Darumaka", "It derives its power from fire burning inside its body. If the fire dwindles, this Pokémon will immediately fall asleep.",
            70, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flop", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A4-036 - Darmanitan
     */
    public static final PokemonCard DARMANITAN = PokemonCard.evolution(
            "A4-036", "Darmanitan", "The thick arms of this hot-blooded Pokémon can deliver punches capable of obliterating a dump truck.",
            1, "Darumaka", 120, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Heat Blast", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A4-037 - Heatmor
     */
    public static final PokemonCard HEATMOR = PokemonCard.basic(
            "A4-037", "Heatmor", "There's a hole in its tail that allows it to draw in the air it needs to keep its fire burning. If the hole gets blocked, this Pokémon will fall ill.",
            90, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Roasting Heat", "If your opponent's Active Pokémon is Burned, this attack does 60 more damage.", EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(new For(new IsBurned(), new OpponentActive()), new Literal(90))), new Literal(30)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /** A4-165 · Cyndaquil (alternate art of A4-027) */
    public static final PokemonCard CYNDAQUIL_IR = CYNDAQUIL.withId("A4-165", CardRarity.ILLUSTRATION_RARE);

    /** A4-166 · Magby (alternate art of A4-032) */
    public static final PokemonCard MAGBY_IR = MAGBY.withId("A4-166", CardRarity.ILLUSTRATION_RARE);

    /** A4-187 · Ho-Oh ex (alternate art of A4-034) */
    public static final PokemonCard HO_OH_EX_UR = HO_OH_EX.withId("A4-187", CardRarity.ULTRA_RARE);

    /** A4-210 · Ho-Oh ex (alternate art of A4-034) */
    public static final PokemonCard HO_OH_EX_IM = HO_OH_EX.withId("A4-210", CardRarity.IMMERSIVE);

    /** A4-213 · Flareon (shiny of A3b-008) */
    public static final PokemonCard FLAREON_SR = com.tcgpocket.pool.A3b.Fire.FLAREON.withId("A4-213", CardRarity.SHINY_RARE);

    /** A4-240 · Ho-Oh ex (alternate art of A4-034) */
    public static final PokemonCard HO_OH_EX_HR = HO_OH_EX.withId("A4-240", CardRarity.HYPER_RARE);

    static final List<PokemonCard> CARDS = List.of(
            VULPIX, NINETALES, CYNDAQUIL, QUILAVA, TYPHLOSION, SLUGMA,
            MAGCARGO, MAGBY, ENTEI, HO_OH_EX, DARUMAKA, DARMANITAN,
            HEATMOR,
            CYNDAQUIL_IR, MAGBY_IR, HO_OH_EX_UR,
            HO_OH_EX_IM, FLAREON_SR, HO_OH_EX_HR);

    private Fire() {
    }
}
