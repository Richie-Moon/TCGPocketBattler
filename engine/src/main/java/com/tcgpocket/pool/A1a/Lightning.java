package com.tcgpocket.pool.A1a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.IsType;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Product;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.target.*;

import java.util.List;
import java.util.Optional;

/**
 * Mythical Island — Lightning.
 */
public final class Lightning {
    /**
     * A1a-025 - Pikachu — Circle Circuit: built like A1-096 Pikachu ex's.
     */
    public static final PokemonCard PIKACHU = PokemonCard.basic(
            "A1a-025", "Pikachu", "When it is angered, it immediately discharges the energy stored in the pouches in its cheeks.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Circle Circuit", "This attack does 10 damage for each of your Benched Lightning Pokémon.",
                    EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Product(
                            new Literal(10), new CountCards(new AttackerSide(), Zone.BENCH, Optional.of(new IsType(Type.LIGHTNING)))
                    ), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A1a-026 - Raichu
     */
    public static final PokemonCard RAICHU = PokemonCard.evolution(
            "A1a-026", "Raichu", "Its tail discharges electricity into the ground, protecting it from getting shocked.",
            1, "Pikachu", 120, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Gigashock", "This attack also does 20 damage to each of your opponent's Benched Pokémon.",
                    EnergyCost.of(Type.LIGHTNING, 3), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new DamageEach(new Literal(20), new OpponentBench())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A1a-027 - Electabuzz
     */
    public static final PokemonCard ELECTABUZZ = PokemonCard.basic(
            "A1a-027", "Electabuzz", "Many power plants keep Ground-type Pokémon around as a defense against Electabuzz that come seeking electricity.",
            70, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Thunder Spear", "This attack does 40 damage to 1 of your opponent's Pokémon.",
                    EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A1a-028 - Joltik
     */
    public static final PokemonCard JOLTIK = PokemonCard.basic(
            "A1a-028", "Joltik", "Joltik can be found clinging to other Pokémon. It's soaking up static electricity because it can't produce a charge on its own.",
            40, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bug Bite", "", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A1a-029 - Galvantula
     */
    public static final PokemonCard GALVANTULA = PokemonCard.evolution(
            "A1a-029", "Galvantula", "It launches electrified fur from its abdomen as its means of attack. Opponents hit by the fur could be in for three full days and nights of paralysis.",
            1, "Joltik", 80, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Electroweb", "During your opponent's next turn, the Defending Pokémon can't retreat.",
                    EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new PreventRetreat(new OpponentActive(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A1a-030 - Dedenne
     */
    public static final PokemonCard DEDENNE = PokemonCard.basic(
            "A1a-030", "Dedenne", "It's small and its electricity-generating organ is not fully developed, so it uses its tail to absorb electricity from people's homes and charge itself.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Thunder Shock", "Flip a coin. If heads, your opponent's Active Pokémon is now Paralyzed.",
                    EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new ConditionalEffect(new LastCoinTossHeads(),
                            new AddStatus(new ParalysisStatus(), new OpponentActive()))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /** A1a-073 · Dedenne (alternate art of A1a-030) */
    public static final PokemonCard DEDENNE_IR = DEDENNE.withId("A1a-073", CardRarity.ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            PIKACHU, RAICHU, ELECTABUZZ, JOLTIK, GALVANTULA, DEDENNE,
            DEDENNE_IR);

    private Lightning() {
    }
}
