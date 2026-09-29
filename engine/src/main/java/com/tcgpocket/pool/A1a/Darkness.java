package com.tcgpocket.pool.A1a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsPoisoned;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;

import java.util.List;

/**
 * Mythical Island — Darkness.
 */
public final class Darkness {
    /**
     * A1a-049 - Koffing
     */
    public static final PokemonCard KOFFING = PokemonCard.basic(
            "A1a-049", "Koffing", "Its body is full of poisonous gas. It floats into garbage dumps, seeking out the fumes of raw, rotting trash.",
            70, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Division", "Put 1 random Koffing from your deck onto your Bench.", EnergyCost.of(Type.DARKNESS, 1),
                    new Attempt(List.of(
                            new BenchFromDeck(new AttackerSide(), new IsSpecies("Koffing"))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A1a-050 - Weezing
     */
    public static final PokemonCard WEEZING = PokemonCard.evolution(
            "A1a-050", "Weezing", "If one of the twin Koffing inflates, the other one deflates. It constantly mixes its poisonous gases.",
            1, "Koffing", 110, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Smokescreen", "During your opponent's next turn, if the Defending Pokémon tries to use an attack, your opponent flips a coin. If tails, that attack doesn't happen.",
                    EnergyCost.of(Type.DARKNESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new HinderAttack(new OpponentActive(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A1a-051 - Purrloin
     */
    public static final PokemonCard PURRLOIN = PokemonCard.basic(
            "A1a-051", "Purrloin", "It steals things from people just to amuse itself with their frustration. A rivalry exists between this Pokémon and Nickit.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Scratch", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A1a-052 - Liepard
     */
    public static final PokemonCard LIEPARD = PokemonCard.evolution(
            "A1a-052", "Liepard", "Don't be fooled by its gorgeous fur and elegant figure. This is a moody and vicious Pokémon.",
            1, "Purrloin", 90, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Slash", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A1a-053 - Venipede
     */
    public static final PokemonCard VENIPEDE = PokemonCard.basic(
            "A1a-053", "Venipede", "Venipede and Sizzlipede are similar species, but when the two meet, a huge fight ensues.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A1a-054 - Whirlipede
     */
    public static final PokemonCard WHIRLIPEDE = PokemonCard.evolution(
            "A1a-054", "Whirlipede", "This Pokémon spins itself rapidly and charges into its opponents. Its top speed is just over 60 mph.",
            1, "Venipede", 90, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Poison Sting", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.DARKNESS, 1),
                    new Attempt(List.of(
                            new DealDamage(new Literal(20), new OpponentActive()),
                            new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A1a-055 - Scolipede
     */
    public static final PokemonCard SCOLIPEDE = PokemonCard.evolution(
            "A1a-055", "Scolipede", "Scolipede latches on to its prey with the claws on its neck before slamming them into the ground and jabbing them with its claws' toxic spikes.",
            2, "Whirlipede", 140, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Venoshock", "If your opponent's Active Pokémon is Poisoned, this attack does 50 more damage.",
                    EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new For(new IsPoisoned(), new OpponentActive()), new Literal(120))),
                            new Literal(70)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    static final List<PokemonCard> CARDS = List.of(
            KOFFING, WEEZING, PURRLOIN, LIEPARD, VENIPEDE, WHIRLIPEDE, SCOLIPEDE);

    private Darkness() {
    }
}
