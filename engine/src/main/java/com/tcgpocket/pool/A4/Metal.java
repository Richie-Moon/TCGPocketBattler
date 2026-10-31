package com.tcgpocket.pool.A4;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.MovedToActiveThisTurn;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.IncreaseDamage;
import com.tcgpocket.effect.ReduceDamageTaken;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Wisdom of Sea and Sky — Metal.
 */
public final class Metal {

    /**
     * A4-121 - Forretress
     */
    public static final PokemonCard FORRETRESS = PokemonCard.evolution(
            "A4-121", "Forretress", "It remains immovably rooted to its tree. It scatters pieces of its hard shell to drive its enemies away.",
            1, "Pineco", 130, Type.METAL, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Shell Crash", "", EnergyCost.of(Type.METAL, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-122 - Steelix
     */
    public static final PokemonCard STEELIX = PokemonCard.evolution(
            "A4-122", "Steelix", "It is said that if an Onix lives for over 100 years, its composition changes to become diamond-like.",
            1, "Onix", 150, Type.METAL, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Heavy Impact", "", EnergyCost.of(Type.METAL, 1, Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(110), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A4-123 - Scizor
     */
    public static final PokemonCard SCIZOR = PokemonCard.evolution(
            "A4-123", "Scizor", "This Pokémon's pincers, which contain steel, can crush any hard object they get ahold of into bits.",
            1, "Scyther", 100, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Gale Thrust", "If this Pokémon moved from your Bench to the Active Spot this turn, this attack does 50 more damage.", EnergyCost.of(Type.METAL, 2), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new For(new MovedToActiveThisTurn(), new Self()), new Literal(100))),
                            new Literal(50)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A4-124 - Skarmory ex
     */
    public static final PokemonCard SKARMORY_EX = PokemonCard.basic(
            "A4-124", "Skarmory ex", "",
            140, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Steel Wing", "During your opponent's next turn, this Pokémon takes -20 damage from attacks.", EnergyCost.of(Type.METAL, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new ReduceDamageTaken(new Literal(20), new Self(), new Literal(1))))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX);

    /**
     * A4-125 - Mawile
     */
    public static final PokemonCard MAWILE = PokemonCard.basic(
            "A4-125", "Mawile", "It uses its docile-looking face to lull foes into complacency, then bites with its huge, relentless jaws.",
            70, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.METAL, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-126 - Klink
     */
    public static final PokemonCard KLINK = PokemonCard.basic(
            "A4-126", "Klink", "The two minigears that compose this Pokémon are closer than twins. They mesh well only with each other.",
            60, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Vise Grip", "", EnergyCost.of(Type.METAL, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-127 - Klang
     */
    public static final PokemonCard KLANG = PokemonCard.evolution(
            "A4-127", "Klang", "When Klang goes all out, the minigear links up perfectly with the outer part of the big gear, and this Pokémon's rotation speed increases sharply.",
            1, "Klink", 80, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Gear Cutter", "", EnergyCost.of(Type.METAL, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4-128 - Klinklang
     */
    public static final PokemonCard KLINKLANG = PokemonCard.evolution(
            "A4-128", "Klinklang", "From its spikes, it launches powerful blasts of electricity. Its red core contains an enormous amount of energy.",
            2, "Klang", 150, Type.METAL, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Gear Spinner", "During your next turn, this Pokémon's Gear Spinner attack does +70 damage.", EnergyCost.of(Type.METAL, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new IncreaseDamage(new Literal(70), new Self(), new Literal(2))))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /** A4-180 · Scizor (alternate art of A4-123) */
    public static final PokemonCard SCIZOR_IR = SCIZOR.withId("A4-180", CardRarity.ILLUSTRATION_RARE);

    /** A4-194 · Skarmory ex (alternate art of A4-124) */
    public static final PokemonCard SKARMORY_EX_UR = SKARMORY_EX.withId("A4-194", CardRarity.ULTRA_RARE);

    /** A4-209 · Skarmory ex (alternate art of A4-124) */
    public static final PokemonCard SKARMORY_EX_SIR = SKARMORY_EX.withId("A4-209", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            FORRETRESS, STEELIX, SCIZOR, SKARMORY_EX, MAWILE, KLINK, KLANG, KLINKLANG,
            SCIZOR_IR, SKARMORY_EX_UR,
            SKARMORY_EX_SIR);

    private Metal() {
    }
}
