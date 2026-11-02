package com.tcgpocket.pool.A4a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.status.BurnStatus;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Secluded Springs (A4a): the Fire cards, in printed set order.
 */
public final class Fire {

    /**
     * A4a-008 - Slugma
     */
    public static final PokemonCard SLUGMA = PokemonCard.basic(
            "A4a-008", "Slugma", "A common sight in volcanic areas, it slowly slithers around in a constant search for warm places.",
            80, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Combustion", "", EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A4a-009 - Magcargo
     */
    public static final PokemonCard MAGCARGO = PokemonCard.evolution(
            "A4a-009", "Magcargo", "Its brittle shell occasionally spouts intense flames that circulate throughout its body.",
            1, "Slugma", 120, Type.FIRE, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Heat Blast", "", EnergyCost.of(Type.FIRE, 3), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A4a-010 - Entei ex — "extra" is beyond the printed 2 Fire, as in Hydro Pump, so 4 Fire in all.
     */
    public static final PokemonCard ENTEI_EX = PokemonCard.basic(
            "A4a-010", "Entei ex", "",
            140, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Blazing Beatdown", "If this Pokémon has at least 2 extra Fire Energy attached, this attack does 60 more damage.", EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(new GreaterThan(new EnergyOn(new Self(), Type.FIRE), new Literal(3)), new Literal(120))), new Literal(60)), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.WATER).withTags(CardTag.EX).withAbility(LegendaryPulse.ABILITY);

    /**
     * A4a-011 - Fletchinder
     */
    public static final PokemonCard FLETCHINDER = PokemonCard.evolution(
            "A4a-011", "Fletchinder", "Fletchinder scatters embers in tall grass where bug Pokémon might be hiding and then catches them as they come leaping out.",
            1, "Fletchling", 70, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Fire Wing", "", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4a-012 - Talonflame
     */
    public static final PokemonCard TALONFLAME = PokemonCard.evolution(
            "A4a-012", "Talonflame", "It has top-notch flying capabilities. It flies around easily, even while carrying prey that weighs more than 220 lbs.",
            2, "Fletchinder", 130, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Searing Flame", "Your opponent's Active Pokémon is now Burned.", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new AddStatus(new BurnStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /** A4a-079 · Entei ex (alternate art of A4a-010) */
    public static final PokemonCard ENTEI_EX_UR = ENTEI_EX.withId("A4a-079", CardRarity.ULTRA_RARE);

    /** A4a-087 · Entei ex (alternate art of A4a-010) */
    public static final PokemonCard ENTEI_EX_SIR = ENTEI_EX.withId("A4a-087", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    /** A4a-091 · Chimchar (shiny of A2-027) */
    public static final PokemonCard CHIMCHAR_SR = com.tcgpocket.pool.A2.Fire.CHIMCHAR.withId("A4a-091", CardRarity.SHINY_RARE);

    /** A4a-092 · Monferno (shiny of A2-028) */
    public static final PokemonCard MONFERNO_SR = com.tcgpocket.pool.A2.Fire.MONFERNO.withId("A4a-092", CardRarity.SHINY_RARE);

    /** A4a-101 · Infernape ex (shiny of A2-029) */
    public static final PokemonCard INFERNAPE_EX_SUR = com.tcgpocket.pool.A2.Fire.INFERNAPE_EX.withId("A4a-101", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            SLUGMA, MAGCARGO, ENTEI_EX, FLETCHINDER, TALONFLAME, ENTEI_EX_UR,
            ENTEI_EX_SIR, CHIMCHAR_SR, MONFERNO_SR, INFERNAPE_EX_SUR);

    private Fire() {
    }
}
