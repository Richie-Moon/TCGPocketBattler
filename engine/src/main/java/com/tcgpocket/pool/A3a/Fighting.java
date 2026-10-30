package com.tcgpocket.pool.A3a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.HealBlock;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.ForAny;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardTypeEnergy;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.PreventRetreat;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

import static com.tcgpocket.pool.A1a.Fighting.AERODACTYL_EX;

/**
 * Extradimensional Crisis — Fighting.
 */
public final class Fighting {

    /**
     * A3a-028 - Meditite
     */
    public static final PokemonCard MEDITITE = PokemonCard.basic(
            "A3a-028", "Meditite", "It never skips its daily yoga training. It heightens its inner strength through meditation.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Trip Over", "Flip a coin. If heads, this attack does 20 more damage.", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(
                            new Literal(10),
                            new Product(new NumberHeads(), new Literal(20))), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A3a-029 - Medicham
     */
    public static final PokemonCard MEDICHAM = PokemonCard.evolution(
            "A3a-029", "Medicham", "Through yoga training, it gained the psychic power to predict its foe's next move.",
            1, "Meditite", 90, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "High Jump Kick", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A3a-030 - Baltoy
     */
    public static final PokemonCard BALTOY = PokemonCard.basic(
            "A3a-030", "Baltoy", "It moves while spinning around on its single foot. Some Baltoy have been seen spinning on their heads.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Spinning Attack", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3a-031 - Claydol
     */
    public static final PokemonCard CLAYDOL = PokemonCard.evolution(
            "A3a-031", "Claydol", "This mysterious Pokémon started life as an ancient clay figurine made over 20,000 years ago.",
            1, "Baltoy", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Power Beam", "", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS).withAbility(new PassiveAbility("Heal Block",
            "Pokémon (both yours and your opponent's) can't be healed.",
            new HealBlock()));

    /**
     * A3a-032 - Rockruff
     */
    public static final PokemonCard ROCKRUFF = PokemonCard.basic(
            "A3a-032", "Rockruff", "This Pokémon is very friendly when it's young. Its disposition becomes vicious once it matures, but it never forgets the kindness of its master.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Rock Throw", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3a-033 - Lycanroc ex
     */
    public static final PokemonCard LYCANROC_EX = PokemonCard.evolution(
            "A3a-033", "Lycanroc ex", "",
            1, "Rockruff", 150, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Lycanfang", "Discard a Fighting Energy from this Pokémon.", EnergyCost.of(Type.FIGHTING, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(130), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIGHTING, new Literal(1), new Self())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.GRASS).withTags(CardTag.EX);

    /**
     * A3a-034 - Passimian
     */
    public static final PokemonCard PASSIMIAN = PokemonCard.basic(
            "A3a-034", "Passimian", "This Pokémon battles by throwing hard berries. It won't obey a Trainer who throws Poké Balls without skill.",
            100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Let's Throw", "If Passimian is on your Bench, this attack does 40 more damage.", EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                    new DealDamage(new Sum(
                            new Literal(40),
                            new Branch(List.of(new Branch.Case(
                                    new ForAny(new IsSpecies("Passimian"), new AttackerBench()), new Literal(40))),
                                    new Literal(0))), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A3a-035 - Sandygast
     */
    public static final PokemonCard SANDYGAST = PokemonCard.basic(
            "A3a-035", "Sandygast", "If it loses its shovel, it will stick something else—like a branch—in its head to make do until it finds another shovel.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Sandy Shot", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3a-036 - Palossand
     */
    public static final PokemonCard PALOSSAND = PokemonCard.evolution(
            "A3a-036", "Palossand", "The terrifying Palossand drags smaller Pokémon into its sandy body. Once its victims are trapped, it drains them of their vitality whenever it pleases.",
            1, "Sandygast", 130, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Sand Tomb", "During your opponent's next turn, the Defending Pokémon can't retreat.", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive()),
                    new PreventRetreat(new OpponentActive(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /** A3a-078 · Lycanroc ex (alternate art of A3a-033) */
    public static final PokemonCard LYCANROC_EX_UR = LYCANROC_EX.withId("A3a-078", CardRarity.ULTRA_RARE);

    /** A3a-085 · Lycanroc ex (alternate art of A3a-033) */
    public static final PokemonCard LYCANROC_EX_SIR = LYCANROC_EX.withId("A3a-085", CardRarity.SPECIAL_ILLUSTRATION_RARE);
    /** A3a-101 · Aerodactyl ex (alternate art of A1a-046) */
    public static final PokemonCard AERODACTYL_EX_SUR = AERODACTYL_EX.withId("A3a-101", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            MEDITITE, MEDICHAM, BALTOY, CLAYDOL, ROCKRUFF, LYCANROC_EX,
            PASSIMIAN, SANDYGAST, PALOSSAND,
            LYCANROC_EX_UR,
            LYCANROC_EX_SIR, AERODACTYL_EX_SUR);

    private Fighting() {
    }
}
