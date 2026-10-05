package com.tcgpocket.pool.A1a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.EvolutionLock;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.KnockedOutLastTurn;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Mythical Island — Fighting.
 */
public final class Fighting {
    /**
     * A1a-041 - Mankey
     */
    public static final PokemonCard MANKEY = PokemonCard.basic(
            "A1a-041", "Mankey", "It lives in groups in the treetops. If it loses sight of its group, it becomes infuriated by its loneliness.",
            50, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Focus Fist", "Flip a coin. If tails, this attack does nothing.", EnergyCost.of(Type.FIGHTING, 1),
                    new Attempt(List.of(
                            new FlipN(new Literal(1)),
                            new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new Fail()),
                            new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A1a-042 - Primeape
     */
    public static final PokemonCard PRIMEAPE = PokemonCard.evolution(
            "A1a-042", "Primeape", "It becomes wildly furious if it even senses someone looking at it. It chases anyone that meets its glare.",
            1, "Mankey", 80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Punch", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A1a-043 - Geodude
     */
    public static final PokemonCard GEODUDE = PokemonCard.basic(
            "A1a-043", "Geodude", "Geodude that have lived a long life have had all their edges smoothed out until they're totally round. They also have a calm, quiet disposition.",
            70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Light Punch", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A1a-044 - Graveler
     */
    public static final PokemonCard GRAVELER = PokemonCard.evolution(
            "A1a-044", "Graveler", "It climbs up cliffs as it heads toward the peak of a mountain. As soon as it reaches the summit, it rolls back down the way it came.",
            1, "Geodude", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Lunge Out", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A1a-045 - Golem
     */
    public static final PokemonCard GOLEM = PokemonCard.evolution(
            "A1a-045", "Golem", "When Golem grow old, they stop shedding their shells. Those that have lived a long, long time have shells green with moss.",
            2, "Graveler", 160, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Guard Press", "During your opponent's next turn, this Pokémon takes -30 damage from attacks.",
                    EnergyCost.of(Type.FIGHTING, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive()),
                    new ReduceDamageTaken(new Literal(30), new Self(), new Literal(1))))
            )), CardRarity.RARE
    ).withWeakness(Type.GRASS);

    /**
     * A1a-046 - Aerodactyl ex
     */
    public static final PokemonCard AERODACTYL_EX = PokemonCard.evolution(
            "A1a-046", "Aerodactyl ex", "",
            1, "Old Amber", 140, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Land Crush", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE)
            .withAbility(new PassiveAbility("Primeval Law", 
                    "Your opponent can't play any Pokémon from their hand to evolve their Active Pokémon.",
                    new EvolutionLock(new IsActive())))
            .withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A1a-047 - Marshadow
     */
    public static final PokemonCard MARSHADOW = PokemonCard.basic(
            "A1a-047", "Marshadow", "It slips into the shadows of others and mimics their powers and movements. As it improves, it becomes stronger than those it's imitating.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Revenge", "If any of your Pokémon were Knocked Out by damage from an attack during your opponent's last turn, this attack does 60 more damage.",
                    EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new KnockedOutLastTurn(), new Literal(100))),
                            new Literal(40)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.PSYCHIC);

    /**
     * A1a-048 - Stonjourner
     */
    public static final PokemonCard STONJOURNER = PokemonCard.basic(
            "A1a-048", "Stonjourner", "The elemental composition of the rocks that form its body were found to match the bedrock of a land far away from this Pokémon's habitat.",
            120, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Mega Kick", "", EnergyCost.of(Type.FIGHTING, 3), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /** A1a-074 · Marshadow (alternate art of A1a-047) */
    public static final PokemonCard MARSHADOW_IR = MARSHADOW.withId("A1a-074", CardRarity.ILLUSTRATION_RARE);
    /** A1a-078 · Aerodactyl ex (alternate art of A1a-046) */
    public static final PokemonCard AERODACTYL_EX_UR = AERODACTYL_EX.withId("A1a-078", CardRarity.ULTRA_RARE);
    /** A1a-084 · Aerodactyl ex (alternate art of A1a-046) */
    public static final PokemonCard AERODACTYL_EX_SIR = AERODACTYL_EX.withId("A1a-084", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            MANKEY, PRIMEAPE, GEODUDE, GRAVELER, GOLEM, AERODACTYL_EX, MARSHADOW, STONJOURNER,
            MARSHADOW_IR, AERODACTYL_EX_UR, AERODACTYL_EX_SIR);

    private Fighting() {
    }
}
