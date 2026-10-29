package com.tcgpocket.pool.A2b;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.*;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Shining Revelry — Darkness.
 */
public final class Darkness {

    /**
     * A2b-045 - Ekans
     */
    public static final PokemonCard EKANS = PokemonCard.basic(
            "A2b-045", "Ekans", "By dislocating its jaw, it can swallow prey larger than itself. After a meal, it curls up and rests.",
            70, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-046 - Arbok
     */
    public static final PokemonCard ARBOK = PokemonCard.evolution(
            "A2b-046", "Arbok", "After stunning its opponents with the pattern on its stomach, it quickly wraps them up in its body and waits for them to stop moving.",
            1, "Ekans", 110, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Venomous Fang", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.DARKNESS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-047 - Paldean Wooper
     */
    public static final PokemonCard PALDEAN_WOOPER = PokemonCard.basic(
            "A2b-047", "Paldean Wooper", "After losing a territorial struggle, Wooper began living on land. The Pokémon changed over time, developing a poisonous film to protect its body.",
            50, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Poison Jab", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-048 - Paldean Clodsire ex
     */
    public static final PokemonCard PALDEAN_CLODSIRE_EX = PokemonCard.evolution(
            "A2b-048", "Paldean Clodsire ex", "",
            1, "Paldean Wooper", 160, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Venoshock", "If your opponent's Active Pokémon is Poisoned, this attack does 60 more damage.",
                    EnergyCost.of(Type.DARKNESS, 2), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new For(new IsPoisoned(), new OpponentActive()), new Literal(120))),
                            new Literal(60)), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX);

    /**
     * A2b-049 - Spiritomb
     */
    public static final PokemonCard SPIRITOMB = PokemonCard.basic(
            "A2b-049", "Spiritomb", "Its constant mischief and misdeeds resulted in it being bound to an Odd Keystone by a mysterious spell.",
            80, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Confuse Ray", "Flip a coin. If heads, your opponent's Active Pokémon is now Confused.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new ConditionalEffect(new LastCoinTossHeads(), new AddStatus(new ConfusionStatus(), new OpponentActive()))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2b-050 - Shroodle
     */
    public static final PokemonCard SHROODLE = PokemonCard.basic(
            "A2b-050", "Shroodle", "Though usually a mellow Pokémon, it will sink its sharp, poison-soaked front teeth into any that anger it, causing paralysis in the object of its ire.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Gnaw", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-051 - Grafaiai. Not offered while the Active is already Poisoned, as
     * A1-117 Hypno's Sleep Pendulum is not while it is Asleep.
     */
    public static final PokemonCard GRAFAIAI = PokemonCard.evolution(
            "A2b-051", "Grafaiai", "The color of the poisonous saliva depends on what the Pokémon eats. Grafaiai covers its fingers in its saliva and draws patterns on trees in forests.",
            1, "Shroodle", 90, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.DARKNESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING).withAbility(new ActivatedAbility("Poison Coating",
            "Once during your turn, you may flip a coin. If heads, your opponent's Active Pokémon is now Poisoned.",
            new PlainAction("", new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new AddStatus(new PoisonStatus(), new OpponentActive()))))),
            true, new Not<>(new For(new IsPoisoned(), new OpponentActive()))));

    /** A2b-076 · Grafaiai (alternate art of A2b-051) */
    public static final PokemonCard GRAFAIAI_IR = GRAFAIAI.withId("A2b-076", CardRarity.ILLUSTRATION_RARE);
    /** A2b-085 · Paldean Clodsire ex (alternate art of A2b-048) */
    public static final PokemonCard PALDEAN_CLODSIRE_EX_UR = PALDEAN_CLODSIRE_EX.withId("A2b-085", CardRarity.ULTRA_RARE);
    /** A2b-093 · Paldean Clodsire ex (alternate art of A2b-048) */
    public static final PokemonCard PALDEAN_CLODSIRE_EX_SIR = PALDEAN_CLODSIRE_EX.withId("A2b-093", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            EKANS, ARBOK, PALDEAN_WOOPER, PALDEAN_CLODSIRE_EX, SPIRITOMB, SHROODLE, GRAFAIAI,
            GRAFAIAI_IR, PALDEAN_CLODSIRE_EX_UR, PALDEAN_CLODSIRE_EX_SIR);

    private Darkness() {
    }
}
