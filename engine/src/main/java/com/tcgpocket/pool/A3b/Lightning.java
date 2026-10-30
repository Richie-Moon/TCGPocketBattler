package com.tcgpocket.pool.A3b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.EvolvedThisTurn;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardAllEnergy;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Eevee Grove — Lightning.
 */
public final class Lightning {

    /**
     * A3b-025 - Jolteon
     */
    public static final PokemonCard JOLTEON = PokemonCard.evolution(
            "A3b-025", "Jolteon", "It concentrates the weak electric charges emitted by its cells and launches wicked lightning bolts.",
            1, "Eevee", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Beginning Bolt", "If this Pokémon evolved during this turn, this attack does 20 more damage.", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(
                            new For(new EvolvedThisTurn(), new Self()),
                            new Literal(60))
                    ), new Literal(40)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A3b-026 - Joltik
     */
    public static final PokemonCard JOLTIK = PokemonCard.basic(
            "A3b-026", "Joltik", "Joltik can be found clinging to other Pokémon. It's soaking up static electricity because it can't produce a charge on its own.",
            40, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Jolt", "Flip a coin. If heads, your opponent's Active Pokémon is now Paralyzed.", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new AddStatus(new ParalysisStatus(), new OpponentActive()))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3b-027 - Galvantula
     */
    public static final PokemonCard GALVANTULA = PokemonCard.evolution(
            "A3b-027", "Galvantula", "It launches electrified fur from its abdomen as its means of attack. Opponents hit by the fur could be in for three full days and nights of paralysis.",
            1, "Joltik", 80, Type.LIGHTNING, EnergyCost.free(), List.of(new Action(
                    "Electric Shock", "Discard all Energy attached to this Pokémon. Your opponent's Active Pokémon is now Paralyzed.", EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new DiscardAllEnergy(new Self()),
                    new AddStatus(new ParalysisStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /** A3b-074 · Jolteon (alternate art of A3b-025) */
    public static final PokemonCard JOLTEON_IR = JOLTEON.withId("A3b-074", CardRarity.ILLUSTRATION_RARE);

    /** A3b-095 · Voltorb (shiny of A2-054) */
    public static final PokemonCard VOLTORB_SR = com.tcgpocket.pool.A2.Lightning.VOLTORB.withId("A3b-095", CardRarity.SHINY_RARE);

    /** A3b-096 · Electrode (shiny of A2-055) */
    public static final PokemonCard ELECTRODE_SR = com.tcgpocket.pool.A2.Lightning.ELECTRODE.withId("A3b-096", CardRarity.SHINY_RARE);

    /** A3b-105 · Zapdos ex (shiny of A1-104) */
    public static final PokemonCard ZAPDOS_EX_SUR = com.tcgpocket.pool.A1.Lightning.ZAPDOS_EX.withId("A3b-105", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            JOLTEON, JOLTIK, GALVANTULA, JOLTEON_IR,
            VOLTORB_SR, ELECTRODE_SR, ZAPDOS_EX_SUR);

    private Lightning() {
    }
}
