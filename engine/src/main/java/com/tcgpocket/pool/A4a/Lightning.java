package com.tcgpocket.pool.A4a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.IsDamaged;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentBench;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Secluded Springs (A4a): the Lightning cards, in printed set order.
 */
public final class Lightning {

    /**
     * A4a-025 - Raikou ex
     */
    public static final PokemonCard RAIKOU_EX = PokemonCard.basic(
            "A4a-025", "Raikou ex", "",
            130, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Voltaic Bullet", "This attack also does 10 damage to 1 of your opponent's Benched Pokémon.", EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new DealDamage(new Literal(10), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Select a Benched Pokémon"))))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX).withAbility(LegendaryPulse.ABILITY);

    /**
     * A4a-026 - Tynamo
     */
    public static final PokemonCard TYNAMO = PokemonCard.basic(
            "A4a-026", "Tynamo", "While one alone doesn't have much power, a chain of many Tynamo can be as powerful as lightning.",
            40, Type.LIGHTNING, EnergyCost.free(), List.of(new Action(
                    "Tiny Bolt", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4a-027 - Eelektrik
     */
    public static final PokemonCard EELEKTRIK = PokemonCard.evolution(
            "A4a-027", "Eelektrik", "They coil around foes and shock them with electricity-generating organs that seem simply to be circular patterns.",
            1, "Tynamo", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Electro Ball", "", EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4a-028 - Eelektross
     */
    public static final PokemonCard EELEKTROSS = PokemonCard.evolution(
            "A4a-028", "Eelektross", "They crawl out of the ocean using their arms. They will attack prey on shore and immediately drag it into the ocean.",
            2, "Eelektrik", 150, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Wild Charge", "This Pokémon also does 20 damage to itself.", EnergyCost.of(Type.LIGHTNING, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive()),
                    new DealDamage(new Literal(20), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4a-029 - Stunfisk
     */
    public static final PokemonCard STUNFISK = PokemonCard.basic(
            "A4a-029", "Stunfisk", "Thanks to bacteria that lived in the mud flats with it, this Pokémon developed the organs it uses to generate electricity.",
            90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Static Shock", "", EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4a-030 - Yamper
     */
    public static final PokemonCard YAMPER = PokemonCard.basic(
            "A4a-030", "Yamper", "This gluttonous Pokémon only assists people with their work because it wants treats. As it runs, it crackles with electricity.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Zap Kick", "", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4a-031 - Boltund — the printed cost still works while damaged; the cheaper one is an alternative.
     */
    public static final PokemonCard BOLTUND = PokemonCard.evolution(
            "A4a-031", "Boltund", "This Pokémon generates electricity and channels it into its legs to keep them going strong. Boltund can run nonstop for three full days.",
            1, "Yamper", 120, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Defiant Spark", "If this Pokémon has damage on it, this attack can be used for 1 Lightning Energy.", EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            ).withAlternativeCost(EnergyCost.of(Type.LIGHTNING, 1), new IsDamaged())), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /** A4a-073 · Stunfisk (alternate art of A4a-029) */
    public static final PokemonCard STUNFISK_IR = STUNFISK.withId("A4a-073", CardRarity.ILLUSTRATION_RARE);

    /** A4a-074 · Yamper (alternate art of A4a-030) */
    public static final PokemonCard YAMPER_IR = YAMPER.withId("A4a-074", CardRarity.ILLUSTRATION_RARE);

    /** A4a-081 · Raikou ex (alternate art of A4a-025) */
    public static final PokemonCard RAIKOU_EX_UR = RAIKOU_EX.withId("A4a-081", CardRarity.ULTRA_RARE);

    /** A4a-088 · Raikou ex (alternate art of A4a-025) */
    public static final PokemonCard RAIKOU_EX_SIR = RAIKOU_EX.withId("A4a-088", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            RAIKOU_EX, TYNAMO, EELEKTRIK, EELEKTROSS, STUNFISK, YAMPER,
            BOLTUND, STUNFISK_IR, YAMPER_IR, RAIKOU_EX_UR, RAIKOU_EX_SIR);

    private Lightning() {
    }
}
