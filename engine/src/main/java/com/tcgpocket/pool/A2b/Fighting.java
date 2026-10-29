package com.tcgpocket.pool.A2b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.*;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Shining Revelry — Fighting.
 */
public final class Fighting {

    /**
     * A2b-037 - Machop
     */
    public static final PokemonCard MACHOP = PokemonCard.basic(
            "A2b-037", "Machop", "Its whole body is composed of muscles. Even though it's the size of a human child, it can hurl 100 grown-ups.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Low Kick", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A2b-038 - Machoke
     */
    public static final PokemonCard MACHOKE = PokemonCard.evolution(
            "A2b-038", "Machoke", "Its muscular body is so powerful, it must wear a power-save belt to be able to regulate its motions.",
            1, "Machop", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Pummel", "Flip a coin. If heads, this attack does 30 more damage.", EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(new Literal(30), new Product(new NumberHeads(), new Literal(30))), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A2b-039 - Machamp. "2 extra" counts beyond the attack's own 2 Fighting,
     * as A1-055 Blastoise's Hydro Pump does.
     */
    public static final PokemonCard MACHAMP = PokemonCard.evolution(
            "A2b-039", "Machamp", "It quickly swings its four arms to rock its opponents with ceaseless punches and chops from all angles.",
            2, "Machoke", 150, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Power Press", "If this Pokémon has at least 2 extra Fighting Energy attached, this attack does 50 more damage.",
                    EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(
                                    new GreaterThan(new EnergyOn(new Self(), Type.FIGHTING), new Literal(3)),
                                    new Literal(120))),
                            new Literal(70)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.PSYCHIC);

    /**
     * A2b-040 - Hitmonlee
     */
    public static final PokemonCard HITMONLEE = PokemonCard.basic(
            "A2b-040", "Hitmonlee", "This amazing Pokémon has an awesome sense of balance. It can kick in succession from any position.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Kick", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A2b-041 - Hitmonchan
     */
    public static final PokemonCard HITMONCHAN = PokemonCard.basic(
            "A2b-041", "Hitmonchan", "Its punches slice the air. They are launched at such high speed, even a slight graze could cause a burn.",
            90, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Magnum Punch", "", EnergyCost.of(Type.FIGHTING, 3), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A2b-042 - Riolu
     */
    public static final PokemonCard RIOLU = PokemonCard.basic(
            "A2b-042", "Riolu", "They communicate with one another using their auras. They are able to run all through the night.",
            70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Punch", "", EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A2b-043 - Lucario ex
     */
    public static final PokemonCard LUCARIO_EX = PokemonCard.evolution(
            "A2b-043", "Lucario ex", "",
            1, "Riolu", 150, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Aura Sphere", "This attack also does 30 damage to 1 of your opponent's Benched Pokémon.",
                    EnergyCost.of(Type.FIGHTING, 3), new Attempt(List.of(
                    new DealDamage(new Literal(100), new OpponentActive()),
                    new DealDamage(new Literal(30), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Select a Benched Pokémon:"))))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.PSYCHIC).withTags(CardTag.EX);

    /**
     * A2b-044 - Flamigo
     */
    public static final PokemonCard FLAMIGO = PokemonCard.basic(
            "A2b-044", "Flamigo", "This Pokémon apparently ties the base of its neck into a knot so that energy stored in its belly does not escape from its beak.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Double Kick", "Flip 2 coins. This attack does 50 damage for each heads.", EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(50)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.PSYCHIC);

    /** A2b-084 · Lucario ex (alternate art of A2b-043) */
    public static final PokemonCard LUCARIO_EX_UR = LUCARIO_EX.withId("A2b-084", CardRarity.ULTRA_RARE);
    /** A2b-104 · Riolu (alternate art of A2b-042) */
    public static final PokemonCard RIOLU_SR = RIOLU.withId("A2b-104", CardRarity.SHINY_RARE);
    /** A2b-110 · Lucario ex (alternate art of A2b-043) */
    public static final PokemonCard LUCARIO_EX_SUR = LUCARIO_EX.withId("A2b-110", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            MACHOP, MACHOKE, MACHAMP, HITMONLEE, HITMONCHAN, RIOLU, LUCARIO_EX, FLAMIGO,
            LUCARIO_EX_UR, RIOLU_SR, LUCARIO_EX_SUR);

    private Fighting() {
    }
}
