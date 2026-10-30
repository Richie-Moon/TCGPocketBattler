package com.tcgpocket.pool.A3b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.CounterDamage;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Eevee Grove — Metal.
 */
public final class Metal {

    /**
     * A3b-047 - Mawile
     */
    public static final PokemonCard MAWILE = PokemonCard.basic(
            "A3b-047", "Mawile", "It uses its docile-looking face to lull foes into complacency, then bites with its huge, relentless jaws.",
            70, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Powerful Vise", "Flip a coin. If heads, your opponent's Active Pokémon is now Paralyzed.", EnergyCost.of(Type.METAL, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new AddStatus(new ParalysisStatus(), new OpponentActive()))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3b-048 - Togedemaru
     */
    public static final PokemonCard TOGEDEMARU = PokemonCard.basic(
            "A3b-048", "Togedemaru", "With the long hairs on its back, this Pokémon takes in electricity from other electric Pokémon. It stores what it absorbs in an electric sac.",
            80, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bristling Spikes", "During your opponent's next turn, if this Pokémon is damaged by an attack, do 30 damage to the Attacking Pokémon.", EnergyCost.of(Type.METAL, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new CounterDamage(new Literal(30), new Self(), new Literal(1))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3b-049 - Meltan
     */
    public static final PokemonCard MELTAN = PokemonCard.basic(
            "A3b-049", "Meltan", "It dissolves and eats metal. Circulating liquid metal within its body is how it generates energy.",
            60, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Headbutt", "", EnergyCost.of(Type.METAL, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3b-050 - Melmetal
     */
    public static final PokemonCard MELMETAL = PokemonCard.evolution(
            "A3b-050", "Melmetal", "At the end of its life-span, Melmetal will rust and fall apart. The small shards left behind will eventually be reborn as Meltan.",
            1, "Meltan", 140, Type.METAL, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Mega Punch", "", EnergyCost.of(Type.METAL, 3), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    static final List<PokemonCard> CARDS = List.of(
            MAWILE, TOGEDEMARU, MELTAN, MELMETAL);

    private Metal() {
    }
}
