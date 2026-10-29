package com.tcgpocket.pool.A2a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Triumphant Light — Fighting.
 */
public final class Fighting {

    /**
     * A2a-036 - Sudowoodo
     */
    public static final PokemonCard SUDOWOODO = PokemonCard.basic(
            "A2a-036", "Sudowoodo", "Although it always pretends to be a tree, its composition appears more similar to rock than to vegetation.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Fighting Headbutt", "If your opponent's Active Pokémon is a Pokémon ex, this attack does 30 more damage.",
                    EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new For(new HasTag(CardTag.EX), new OpponentActive()), new Literal(50))),
                            new Literal(20)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2a-037 - Phanpy
     */
    public static final PokemonCard PHANPY = PokemonCard.basic(
            "A2a-037", "Phanpy", "This Pokémon lives and nests on a riverbank. After playing in the mud, it won't be able to settle down unless it washes its body.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Rollout", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2a-038 - Donphan. Rolling Spin is its only attack, so boosting all of
     * its attacks is exact.
     */
    public static final PokemonCard DONPHAN = PokemonCard.evolution(
            "A2a-038", "Donphan", "Donphan is covered in tough hide, so even being hit by a car won't faze this Pokémon. However, it is extremely susceptible to rain.",
            1, "Phanpy", 120, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Rolling Spin", "During your next turn, this Pokémon's Rolling Spin attack does +60 damage.",
                    EnergyCost.of(Type.FIGHTING, 3), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new IncreaseDamage(new Literal(60), new Self(), new Literal(2))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2a-039 - Larvitar
     */
    public static final PokemonCard LARVITAR = PokemonCard.basic(
            "A2a-039", "Larvitar", "Born deep underground, this Pokémon becomes a pupa after eating enough dirt to make a mountain.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Corkscrew Punch", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2a-040 - Pupitar
     */
    public static final PokemonCard PUPITAR = PokemonCard.evolution(
            "A2a-040", "Pupitar", "Its shell is as hard as bedrock, and it is also very strong. Its thrashing can topple a mountain.",
            1, "Larvitar", 80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Speed Attack", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2a-041 - Tyranitar
     */
    public static final PokemonCard TYRANITAR = PokemonCard.evolution(
            "A2a-041", "Tyranitar", "Extremely strong, it can change the landscape. It is so insolent that it doesn't care about others.",
            2, "Pupitar", 160, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Land Crush", "", EnergyCost.of(Type.FIGHTING, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(130), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.GRASS).withAbility(Links.powerLink());

    /**
     * A2a-042 - Nosepass
     */
    public static final PokemonCard NOSEPASS = PokemonCard.basic(
            "A2a-042", "Nosepass", "It moves less than an inch a year, but when it's in a jam, it will spin and drill down into the ground in a split second.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2a-043 - Meditite
     */
    public static final PokemonCard MEDITITE = PokemonCard.basic(
            "A2a-043", "Meditite", "It never skips its daily yoga training. It heightens its inner strength through meditation.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Kick", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A2a-044 - Medicham
     */
    public static final PokemonCard MEDICHAM = PokemonCard.evolution(
            "A2a-044", "Medicham", "Through yoga training, it gained the psychic power to predict its foe's next move.",
            1, "Meditite", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Kick Shot", "Flip a coin. If tails, this attack does nothing.", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new Fail()),
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A2a-045 - Gible
     */
    public static final PokemonCard GIBLE = PokemonCard.basic(
            "A2a-045", "Gible", "It skulks in caves, and when prey or an enemy passes by, it leaps out and chomps them. The force of its attack sometimes chips its teeth.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2a-046 - Gabite
     */
    public static final PokemonCard GABITE = PokemonCard.evolution(
            "A2a-046", "Gabite", "In rare cases, it molts and sheds its scales. Medicine containing its scales as an ingredient will make a weary body feel invigorated.",
            1, "Gible", 80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sharp Scythe", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2a-047 - Garchomp ex
     */
    public static final PokemonCard GARCHOMP_EX = PokemonCard.evolution(
            "A2a-047", "Garchomp ex", "",
            2, "Gabite", 170, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(
                    new Action(
                            "Linear Attack", "This attack does 50 damage to 1 of your opponent's Pokémon.",
                            EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                            new DealDamage(new Literal(50), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"))))),
                    new Action(
                            "Dragon Claw", "", EnergyCost.of(Type.FIGHTING, 2, Type.COLORLESS, 1), new Attempt(List.of(
                            new DealDamage(new Literal(100), new OpponentActive()))))
            ), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.GRASS).withTags(CardTag.EX);

    /** A2a-079 · Sudowoodo (alternate art of A2a-036) */
    public static final PokemonCard SUDOWOODO_IR = SUDOWOODO.withId("A2a-079", CardRarity.ILLUSTRATION_RARE);
    /** A2a-084 · Garchomp ex (alternate art of A2a-047) */
    public static final PokemonCard GARCHOMP_EX_UR = GARCHOMP_EX.withId("A2a-084", CardRarity.ULTRA_RARE);
    /** A2a-093 · Garchomp ex (alternate art of A2a-047) */
    public static final PokemonCard GARCHOMP_EX_SIR = GARCHOMP_EX.withId("A2a-093", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            SUDOWOODO, PHANPY, DONPHAN, LARVITAR, PUPITAR, TYRANITAR, NOSEPASS, MEDITITE, MEDICHAM,
            GIBLE, GABITE, GARCHOMP_EX,
            SUDOWOODO_IR, GARCHOMP_EX_UR, GARCHOMP_EX_SIR);

    private Fighting() {
    }
}
