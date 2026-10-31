package com.tcgpocket.pool.A4;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.IsBasic;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.AttachEnergy;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentBench;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Wisdom of Sea and Sky — Lightning.
 */
public final class Lightning {

    /**
     * A4-064 - Chinchou
     */
    public static final PokemonCard CHINCHOU = PokemonCard.basic(
            "A4-064", "Chinchou", "Its antennae, which evolved from a fin, have both positive and negative charges flowing through them.",
            70, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Lightning Ball", "", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-065 - Lanturn ex
     */
    public static final PokemonCard LANTURN_EX = PokemonCard.evolution(
            "A4-065", "Lanturn ex", "",
            1, "Chinchou", 150, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Flashing Signal", "Flip a coin. If heads, your opponent's Active Pokémon is now Paralyzed. If tails, your opponent's Active Pokémon is now Confused.", EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new AddStatus(new ParalysisStatus(), new OpponentActive())),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new AddStatus(new ConfusionStatus(), new OpponentActive()))))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX);

    /**
     * A4-066 - Pichu — Baby Pokémon: a free attack, no Retreat Cost and no Weakness.
     */
    public static final PokemonCard PICHU = PokemonCard.basic(
            "A4-066", "Pichu", "It is unskilled at storing electric power. Any kind of shock causes it to discharge energy spontaneously.",
            30, Type.LIGHTNING, EnergyCost.free(), List.of(new Action(
                    "Crackly Toss", "Take a Lightning Energy from your Energy Zone and attach it to 1 of your Benched Basic Pokémon.", EnergyCost.free(), new Attempt(List.of(
                    new AttachEnergy(Type.LIGHTNING, new Literal(1), new ChosenFrom(new AttackerBench(), new AttackerSide(), new IsBasic(), "Choose a Benched Basic Pokémon to attach a Lightning Energy to."))))
            )), CardRarity.RARE
    );

    /**
     * A4-067 - Mareep
     */
    public static final PokemonCard MAREEP = PokemonCard.basic(
            "A4-067", "Mareep", "If static electricity builds in its body, its fleece doubles in volume. Touching it will shock you.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Static Shock", "", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-068 - Flaaffy
     */
    public static final PokemonCard FLAAFFY = PokemonCard.evolution(
            "A4-068", "Flaaffy", "As a result of storing too much electricity, it developed patches where even downy wool won't grow.",
            1, "Mareep", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Zap Kick", "", EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-069 - Ampharos
     */
    public static final PokemonCard AMPHAROS = PokemonCard.evolution(
            "A4-069", "Ampharos", "The bright light on its tail can be seen far away. It has been treasured since ancient times as a beacon.",
            2, "Flaaffy", 150, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Thunder Punch", "Flip a coin. If heads, this attack does 40 more damage. If tails, this Pokémon also does 20 damage to itself.", EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(new Literal(80), new Product(new Literal(40), new NumberHeads())), new OpponentActive()),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new DealDamage(new Literal(20), new Self()))))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-070 - Elekid — Baby Pokémon: a free attack, no Retreat Cost and no Weakness.
     */
    public static final PokemonCard ELEKID = PokemonCard.basic(
            "A4-070", "Elekid", "When a storm approaches, this Pokémon gets restless. Once Elekid hears the sound of thunder, it gets full-on rowdy.",
            30, Type.LIGHTNING, EnergyCost.free(), List.of(new Action(
                    "Zappy Shot", "This attack does 20 damage to 1 of your opponent's Benched Pokémon.", EnergyCost.free(), new Attempt(List.of(
                    new DealDamage(new Literal(20), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Choose 1 of your opponent's Benched Pokémon."))))
            )), CardRarity.RARE
    );

    /**
     * A4-071 - Raikou
     */
    public static final PokemonCard RAIKOU = PokemonCard.basic(
            "A4-071", "Raikou", "The rain clouds it carries let it fire thunderbolts at will. They say that it descended with lightning.",
            90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Thunder", "This Pokémon also does 20 damage to itself.", EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new DealDamage(new Literal(20), new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A4-072 - Emolga
     */
    public static final PokemonCard EMOLGA = PokemonCard.basic(
            "A4-072", "Emolga", "As Emolga flutters through the air, it crackles with electricity. This Pokémon is cute, but it can cause a lot of trouble.",
            70, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Raging Thunder", "This attack also does 10 damage to 1 of your Benched Pokémon.", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new DealDamage(new Literal(10), new ChosenFrom(new AttackerBench(), new AttackerSide(), "Choose 1 of your Benched Pokémon to take 10 damage."))))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /** A4-171 · Pichu (alternate art of A4-066) */
    public static final PokemonCard PICHU_IR = PICHU.withId("A4-171", CardRarity.ILLUSTRATION_RARE);

    /** A4-172 · Ampharos (alternate art of A4-069) */
    public static final PokemonCard AMPHAROS_IR = AMPHAROS.withId("A4-172", CardRarity.ILLUSTRATION_RARE);

    /** A4-189 · Lanturn ex (alternate art of A4-065) */
    public static final PokemonCard LANTURN_EX_UR = LANTURN_EX.withId("A4-189", CardRarity.ULTRA_RARE);

    /** A4-204 · Lanturn ex (alternate art of A4-065) */
    public static final PokemonCard LANTURN_EX_SIR = LANTURN_EX.withId("A4-204", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    /** A4-217 · Magnemite (shiny of A1-097) */
    public static final PokemonCard MAGNEMITE_SR = com.tcgpocket.pool.A1.Lightning.MAGNEMITE.withId("A4-217", CardRarity.SHINY_RARE);

    /** A4-218 · Magneton (shiny of A1-098) */
    public static final PokemonCard MAGNETON_SR = com.tcgpocket.pool.A1.Lightning.MAGNETON.withId("A4-218", CardRarity.SHINY_RARE);

    /** A4-219 · Jolteon (shiny of A3b-025) */
    public static final PokemonCard JOLTEON_SR = com.tcgpocket.pool.A3b.Lightning.JOLTEON.withId("A4-219", CardRarity.SHINY_RARE);

    /** A4-236 · Pachirisu ex (shiny of A2-061) */
    public static final PokemonCard PACHIRISU_EX_SUR = com.tcgpocket.pool.A2.Lightning.PACHIRISU_EX.withId("A4-236", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            CHINCHOU, LANTURN_EX, PICHU, MAREEP, FLAAFFY, AMPHAROS,
            ELEKID, RAIKOU, EMOLGA,
            PICHU_IR, AMPHAROS_IR, LANTURN_EX_UR,
            LANTURN_EX_SIR, MAGNEMITE_SR, MAGNETON_SR, JOLTEON_SR, PACHIRISU_EX_SUR);

    private Lightning() {
    }
}
