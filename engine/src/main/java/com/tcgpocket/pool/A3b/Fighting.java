package com.tcgpocket.pool.A3b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DamageEach;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.Fail;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.PreventDamage;
import com.tcgpocket.effect.PreventEffects;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Eevee Grove — Fighting.
 */
public final class Fighting {

    /**
     * A3b-038 - Barboach
     */
    public static final PokemonCard BARBOACH = PokemonCard.basic(
            "A3b-038", "Barboach", "Its two whiskers provide a sensitive radar. Even in muddy waters, it can detect its prey's location.",
            70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Mud Shot", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3b-039 - Whiscash
     */
    public static final PokemonCard WHISCASH = PokemonCard.evolution(
            "A3b-039", "Whiscash", "It is extremely protective of its territory. If any foe approaches, it attacks using vicious tremors.",
            1, "Barboach", 130, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Earthquake", "This attack also does 10 damage to each of your Benched Pokémon.", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(130), new OpponentActive()),
                    new DamageEach(new Literal(10), new AttackerBench())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3b-040 - Mienfoo
     */
    public static final PokemonCard MIENFOO = PokemonCard.basic(
            "A3b-040", "Mienfoo", "In one minute, a well-trained Mienfoo can chop with its arms more than 100 times.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Kick Shot", "Flip a coin. If tails, this attack does nothing.", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new Fail()),
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A3b-041 - Mienshao
     */
    public static final PokemonCard MIENSHAO = PokemonCard.evolution(
            "A3b-041", "Mienshao", "When Mienshao comes across a truly challenging opponent, it will lighten itself by biting off the fur on its arms.",
            1, "Mienfoo", 90, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Low Kick", "", EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A3b-042 - Carbink
     */
    public static final PokemonCard CARBINK = PokemonCard.basic(
            "A3b-042", "Carbink", "When beset by attackers, Carbink wipes them all out by firing high-energy beams from the gems embedded in its body.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Hard Roll", "Flip a coin. If heads, during your opponent's next turn, prevent all damage from—and effects of—attacks done to this Pokémon.", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventDamage(new Self(), new Literal(1))),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventEffects(new Self(), new Literal(1)))))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /** A3b-106 · Gallade ex (shiny of A2-095) */
    public static final PokemonCard GALLADE_EX_SUR = com.tcgpocket.pool.A2.Fighting.GALLADE_EX.withId("A3b-106", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            BARBOACH, WHISCASH, MIENFOO, MIENSHAO, CARBINK,
            GALLADE_EX_SUR);

    private Fighting() {
    }
}
