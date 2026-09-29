package com.tcgpocket.pool.A1a;

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
import com.tcgpocket.condition.HasType;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Mythical Island — Water.
 */
public final class Water {
    /**
     * A1a-017 - Magikarp
     */
    public static final PokemonCard MAGIKARP = PokemonCard.basic(
            "A1a-017", "Magikarp", "An underpowered, pathetic Pokémon. It may jump high on rare occasions but never more than seven feet.",
            30, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leap Out", "Switch this Pokémon with 1 of your Benched Pokémon.", EnergyCost.of(Type.WATER, 1),
                    new Attempt(List.of(
                            new SwitchActive(new SelfSide())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A1a-018 - Gyarados ex
     */
    public static final PokemonCard GYARADOS_EX = PokemonCard.evolution(
            "A1a-018", "Gyarados ex", "",
            1, "Magikarp", 180, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Rampaging Whirlpool", "Discard a random Energy from among the Energy attached to all Pokémon (both yours and your opponent's).",
                    EnergyCost.of(Type.WATER, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(140), new OpponentActive()),
                    new DiscardRandomEnergyAmong(new Literal(1), new AllInPlay())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /** Wash Out's source: a Benched Water Pokémon with a Water Energy to give. */
    private static final ICondition<PokemonInPlay> BENCHED_WATER_WITH_WATER =
            new And<>(new HasType(Type.WATER), new HasEnergy(EnergyCost.of(Type.WATER, 1)));

    /**
     * A1a-019 - Vaporeon
     */
    public static final PokemonCard VAPOREON = PokemonCard.evolution(
            "A1a-019", "Vaporeon", "It lives close to water. Its long tail is ridged with a fin, which is often mistaken for a mermaid's.",
            1, "Eevee", 120, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Wave Splash", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.RARE)
            .withAbility(new ActivatedAbility("Wash Out",
                    "As often as you like during your turn, you may move a Water Energy from 1 of your Benched Water Pokémon to your Active Water Pokémon.",
                    new PlainAction("", new Attempt(List.of(
                            new MoveEnergy(new ChosenFrom(new AttackerBench(), new AttackerSide(), BENCHED_WATER_WITH_WATER,
                                    "Choose a Benched Water Pokémon to move a Water Energy from"),
                                    new AttackerActive(), new Literal(1), Type.WATER)))),
                    false,
                    new And<>(new For(new HasType(Type.WATER), new AttackerActive()),
                            new ForAny(BENCHED_WATER_WITH_WATER, new AttackerBench()))))
            .withWeakness(Type.LIGHTNING);

    /**
     * A1a-020 - Finneon
     */
    public static final PokemonCard FINNEON = PokemonCard.basic(
            "A1a-020", "Finneon", "The line running down its side can store sunlight. It shines vividly at night.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Water Gun", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A1a-021 - Lumineon
     */
    public static final PokemonCard LUMINEON = PokemonCard.evolution(
            "A1a-021", "Lumineon", "With its shining light, it lures its prey close. However, the light also happens to attract ferocious fish Pokémon—its natural predators.",
            1, "Finneon", 80, Type.WATER, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                    "Aqua Liner", "This attack does 50 damage to 1 of your opponent's Benched Pokémon.", EnergyCost.of(Type.WATER, 2),
                    new Attempt(List.of(
                            new DealDamage(new Literal(50), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Select a target:"))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A1a-022 - Chewtle
     */
    public static final PokemonCard CHEWTLE = PokemonCard.basic(
            "A1a-022", "Chewtle", "Its large front tooth is still growing in. When the tooth itches, this Pokémon will bite another Chewtle's horn, and the two Pokémon will tussle.",
            80, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A1a-023 - Drednaw
     */
    public static final PokemonCard DREDNAW = PokemonCard.evolution(
            "A1a-023", "Drednaw", "Its massive, jagged teeth can crush a boulder in a single bite. This Pokémon has an extremely vicious disposition.",
            1, "Chewtle", 130, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Crunch", "Flip a coin. If heads, discard a random Energy from your opponent's Active Pokémon.",
                    EnergyCost.of(Type.WATER, 3), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(),
                            new DiscardRandomEnergy(new Literal(1), new OpponentActive()))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A1a-024 - Cramorant — Dive: built like A1-140 Dugtrio's Dig.
     */
    public static final PokemonCard CRAMORANT = PokemonCard.basic(
            "A1a-024", "Cramorant", "It's so strong that it can knock out some opponents in a single hit, but it also may forget what it's battling midfight.",
            80, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Dive", "Flip a coin. If heads, during your opponent's next turn, prevent all damage from—and effects of—attacks done to this Pokémon.",
                    EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventDamage(new Self(), new Literal(1))),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventEffects(new Self(), new Literal(1)))))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    static final List<PokemonCard> CARDS = List.of(
            MAGIKARP, GYARADOS_EX, VAPOREON, FINNEON, LUMINEON, CHEWTLE, DREDNAW, CRAMORANT);

    private Water() {
    }
}
