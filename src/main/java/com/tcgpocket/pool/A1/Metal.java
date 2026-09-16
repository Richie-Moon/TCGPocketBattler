package com.tcgpocket.pool.A1;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.AttachEnergy;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardRandomEnergy;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/** Genetic Apex — Metal. */
public final class Metal {

    private Metal() {
    }

    /**
     * A1-178 - Mawile
     */
    public static final PokemonCard MAWILE = PokemonCard.basic(
                    "A1-178", "Mawile", "It uses its docile-looking face to lull foes into complacency, then bites with its huge, relentless jaws.",
                    70, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Crunch", "Flip a coin. If heads, discard a random Energy from your opponent's Active Pokémon.", EnergyCost.of(Type.METAL, 1),
                            new Attempt(List.of(
                                    new FlipN(new Literal(1)),
                                    new DealDamage(new Literal(20), new OpponentActive()),
                                    new ConditionalEffect(
                                            new LastCoinTossHeads(),
                                            new DiscardRandomEnergy(new Literal(1), new OpponentActive()))
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIRE);

    /**
     * A1-179 - Pawniard
     */
    public static final PokemonCard PAWNIARD = PokemonCard.basic(
                    "A1-179", "Pawniard", "Pawniard will fearlessly challenge even powerful foes. In a pinch, it will cling to opponents and pierce them with the blades all over its body.",
                    50, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Pierce", "", EnergyCost.of(Type.METAL, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIRE);

    /**
     * A1-180 - Bisharp
     */
    public static final PokemonCard BISHARP = PokemonCard.evolution(
                    "A1-180", "Bisharp", "This Pokémon commands a group of several Pawniard. Groups that are defeated in territorial disputes are absorbed by the winning side.",
                    1, "Pawniard", 90, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Metal Claw", "", EnergyCost.of(Type.METAL, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIRE);

    /**
     * A1-181 - Meltan
     */
    public static final PokemonCard MELTAN = PokemonCard.basic(
                    "A1-181", "Meltan", "It dissolves and eats metal. Circulating liquid metal within its body is how it generates energy.",
                    60, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Amass", "Take 1 Metal Energy from your Energy Zone and attach it to this Pokémon.", EnergyCost.of(Type.METAL, 1),
                            new Attempt(List.of(
                                    new AttachEnergy(Type.METAL, new Self())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIRE);

    /**
     * A1-182 - Melmetal
     */
    public static final PokemonCard MELMETAL = PokemonCard.evolution(
                    "A1-182", "Melmetal", "At the end of its life-span, Melmetal will rust and fall apart. The small shards left behind will eventually be reborn as Meltan.",
                    1, "Meltan", 130, Type.METAL, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Heavy Impact", "", EnergyCost.of(Type.METAL, 3, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(120), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.FIRE);

    static final List<PokemonCard> CARDS = List.of(MAWILE, PAWNIARD, BISHARP, MELTAN, MELMETAL);
}
