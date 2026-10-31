package com.tcgpocket.pool.A4;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DamageEach;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.ReduceDamageTaken;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentBench;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Wisdom of Sea and Sky — Fighting.
 */
public final class Fighting {

    /**
     * A4-092 - Onix
     */
    public static final PokemonCard ONIX = PokemonCard.basic(
            "A4-092", "Onix", "As it digs through the ground, it absorbs many hard objects. This is what makes its body so solid.",
            100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Headbutt", "", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-093 - Sudowoodo
     */
    public static final PokemonCard SUDOWOODO = PokemonCard.basic(
            "A4-093", "Sudowoodo", "Although it always pretends to be a tree, its composition appears more similar to rock than to vegetation.",
            90, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Rock Throw", "", EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-094 - Gligar
     */
    public static final PokemonCard GLIGAR = PokemonCard.basic(
            "A4-094", "Gligar", "It usually clings to cliffs. When it spots its prey, it spreads its wings and glides down to attack.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Glide", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-095 - Gliscor
     */
    public static final PokemonCard GLISCOR = PokemonCard.evolution(
            "A4-095", "Gliscor", "It observes prey while hanging inverted from branches. When the chance presents itself, it swoops!",
            1, "Gligar", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sharp Fang", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-096 - Swinub
     */
    public static final PokemonCard SWINUB = PokemonCard.basic(
            "A4-096", "Swinub", "It rubs its snout on the ground to find and dig up food. It sometimes discovers hot springs.",
            70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Mud-Slap", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-097 - Piloswine
     */
    public static final PokemonCard PILOSWINE = PokemonCard.evolution(
            "A4-097", "Piloswine", "If it charges at an enemy, the hairs on its back stand up straight. It is very sensitive to sound.",
            1, "Swinub", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Headbutt Bounce", "", EnergyCost.of(Type.FIGHTING, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-098 - Mamoswine
     */
    public static final PokemonCard MAMOSWINE = PokemonCard.evolution(
            "A4-098", "Mamoswine", "This Pokémon can be spotted in wall paintings from as far back as 10,000 years ago. For a while, it was thought to have gone extinct.",
            2, "Piloswine", 160, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Earthquake", "This attack also does 20 damage to each of your Benched Pokémon.", EnergyCost.of(Type.FIGHTING, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(160), new OpponentActive()),
                    new DamageEach(new Literal(20), new AttackerBench())))
            )), CardRarity.RARE
    ).withWeakness(Type.GRASS);

    /**
     * A4-099 - Phanpy
     */
    public static final PokemonCard PHANPY = PokemonCard.basic(
            "A4-099", "Phanpy", "This Pokémon lives and nests on a riverbank. After playing in the mud, it won't be able to settle down unless it washes its body.",
            70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Reckless Charge", "This Pokémon also does 10 damage to itself.", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new DealDamage(new Literal(10), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-100 - Donphan ex
     */
    public static final PokemonCard DONPHAN_EX = PokemonCard.evolution(
            "A4-100", "Donphan ex", "",
            1, "Phanpy", 160, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Gigantic Press", "If this Pokémon has at least 2 extra Fighting Energy attached, this attack does 60 more damage.", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(
                                    new GreaterThan(new EnergyOn(new Self(), Type.FIGHTING), new Literal(2)),
                                    new Literal(110))),
                            new Literal(50)), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.GRASS).withTags(CardTag.EX);

    /**
     * A4-101 - Tyrogue — Baby Pokémon: a free attack, no Retreat Cost and no Weakness.
     */
    public static final PokemonCard TYROGUE = PokemonCard.basic(
            "A4-101", "Tyrogue", "It is always bursting with energy. To make itself stronger, it keeps on fighting even if it loses.",
            30, Type.FIGHTING, EnergyCost.free(), List.of(new Action(
                    "Slappy Knuckle", "", EnergyCost.free(), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.RARE
    );

    /**
     * A4-102 - Hitmontop
     */
    public static final PokemonCard HITMONTOP = PokemonCard.basic(
            "A4-102", "Hitmontop", "It launches kicks while spinning. If it spins at high speed, it may bore its way into the ground.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Piercing Spin", "This attack also does 20 damage to 1 of your opponent's Benched Pokémon.", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new DealDamage(new Literal(20), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Choose 1 of your opponent's Benched Pokémon."))))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A4-103 - Larvitar
     */
    public static final PokemonCard LARVITAR = PokemonCard.basic(
            "A4-103", "Larvitar", "Born deep underground, this Pokémon becomes a pupa after eating enough dirt to make a mountain.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-104 - Pupitar
     */
    public static final PokemonCard PUPITAR = PokemonCard.evolution(
            "A4-104", "Pupitar", "This pupa flies around wildly by venting with great force the gas pressurized inside its body.",
            1, "Larvitar", 80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Guard Press", "During your opponent's next turn, this Pokémon takes -30 damage from attacks.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new ReduceDamageTaken(new Literal(30), new Self(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-105 - Binacle
     */
    public static final PokemonCard BINACLE = PokemonCard.basic(
            "A4-105", "Binacle", "After two Binacle find a suitably sized rock, they adhere themselves to it and live together. They cooperate to gather food during high tide.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Dual Chop", "Flip 2 coins. This attack does 30 damage for each heads.", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(30)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4-106 - Barbaracle
     */
    public static final PokemonCard BARBARACLE = PokemonCard.evolution(
            "A4-106", "Barbaracle", "Seven Binacle come together to form one Barbaracle. The Binacle that serves as the head gives orders to those serving as the limbs.",
            1, "Binacle", 120, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Dynamic Chop", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /** A4-176 · Gligar (alternate art of A4-094) */
    public static final PokemonCard GLIGAR_IR = GLIGAR.withId("A4-176", CardRarity.ILLUSTRATION_RARE);

    /** A4-191 · Donphan ex (alternate art of A4-100) */
    public static final PokemonCard DONPHAN_EX_UR = DONPHAN_EX.withId("A4-191", CardRarity.ULTRA_RARE);

    /** A4-206 · Donphan ex (alternate art of A4-100) */
    public static final PokemonCard DONPHAN_EX_SIR = DONPHAN_EX.withId("A4-206", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    /** A4-221 · Mankey (shiny of A1a-041) */
    public static final PokemonCard MANKEY_SR = com.tcgpocket.pool.A1a.Fighting.MANKEY.withId("A4-221", CardRarity.SHINY_RARE);

    /** A4-222 · Primeape (shiny of A1-142) */
    public static final PokemonCard PRIMEAPE_SR = com.tcgpocket.pool.A1.Fighting.PRIMEAPE.withId("A4-222", CardRarity.SHINY_RARE);

    static final List<PokemonCard> CARDS = List.of(
            ONIX, SUDOWOODO, GLIGAR, GLISCOR, SWINUB, PILOSWINE, MAMOSWINE, PHANPY, DONPHAN_EX,
            TYROGUE, HITMONTOP, LARVITAR, PUPITAR, BINACLE, BARBARACLE,
            GLIGAR_IR, DONPHAN_EX_UR,
            DONPHAN_EX_SIR, MANKEY_SR, PRIMEAPE_SR);

    private Fighting() {
    }
}
