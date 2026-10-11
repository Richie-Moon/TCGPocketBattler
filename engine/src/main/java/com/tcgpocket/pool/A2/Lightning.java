package com.tcgpocket.pool.A2;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Space-Time Smackdown — Lightning.
 */
public final class Lightning {

    /**
     * A2-051 - Magnemite
     */
    public static final PokemonCard MAGNEMITE = PokemonCard.basic(
            "A2-051", "Magnemite", "The electromagnetic waves emitted by the units at the sides of its head expel antigravity, which allows it to float.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-052 - Magneton
     */
    public static final PokemonCard MAGNETON = PokemonCard.evolution(
            "A2-052", "Magneton", "Three Magnemite are linked by a strong magnetic force. Earaches will occur if you get too close.",
            1, "Magnemite", 80, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Lightning Ball", "", EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-053 - Magnezone
     */
    public static final PokemonCard MAGNEZONE = PokemonCard.evolution(
            "A2-053", "Magnezone", "As it zooms through the sky, this Pokémon seems to be receiving signals of unknown origin while transmitting signals of unknown purpose.",
            2, "Magneton", 140, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Thunder Blast", "Discard a Lightning Energy from this Pokémon.", EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(110), new OpponentActive()),
                    new DiscardTypeEnergy(Type.LIGHTNING, new Literal(1), new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-054 - Voltorb
     */
    public static final PokemonCard VOLTORB = PokemonCard.basic(
            "A2-054", "Voltorb", "It rolls to move. If the ground is uneven, a sudden jolt from hitting a bump can cause it to explode.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Big Explosion", "This Pokémon also does 10 damage to itself.", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new DealDamage(new Literal(10), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-055 - Electrode
     */
    public static final PokemonCard ELECTRODE = PokemonCard.evolution(
            "A2-055", "Electrode", "The more energy it charges up, the faster it gets. But this also makes it more likely to explode.",
            1, "Voltorb", 80, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Rolling Attack", "", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-056 - Electabuzz
     */
    public static final PokemonCard ELECTABUZZ = PokemonCard.basic(
            "A2-056", "Electabuzz", "Many power plants keep Ground-type Pokémon around as a defense against Electabuzz that come seeking electricity.",
            80, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Charge", "Take a Lightning Energy from your Energy Zone and attach it to this Pokémon.", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new AttachEnergy(Type.LIGHTNING, new Literal(1), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-057 - Electivire — "2 extra" is beyond the 2 the attack costs, as
     * A1-055 Blastoise's Hydro Pump reads it.
     */
    public static final PokemonCard ELECTIVIRE = PokemonCard.evolution(
            "A2-057", "Electivire", "The amount of electrical energy this Pokémon produces is proportional to the rate of its pulse. The voltage jumps while Electivire is battling.",
            1, "Electabuzz", 120, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Exciting Voltage", "If this Pokémon has at least 2 extra Lightning Energy attached, this attack does 80 more damage.",
                    EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(
                            new GreaterThan(new EnergyOn(new Self(), Type.LIGHTNING), new Literal(3)),
                            new Literal(120))
                    ), new Literal(40)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-058 - Shinx — Hide: Dig without the damage.
     */
    public static final PokemonCard SHINX = PokemonCard.basic(
            "A2-058", "Shinx", "The extension and contraction of its muscles generates electricity. It glows when in trouble.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Hide", "Flip a coin. If heads, during your opponent's next turn, prevent all damage from—and effects of—attacks done to this Pokémon.",
                    EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventDamage(new Self(), new Literal(1))),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventEffects(new Self(), new Literal(1)))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-059 - Luxio
     */
    public static final PokemonCard LUXIO = PokemonCard.evolution(
            "A2-059", "Luxio", "Strong electricity courses through the tips of its sharp claws. A light scratch causes fainting in foes.",
            1, "Shinx", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Electric Claws", "", EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-060 - Luxray — "all" is however many Lightning Energy are attached
     * when the attack resolves.
     */
    public static final PokemonCard LUXRAY = PokemonCard.evolution(
            "A2-060", "Luxray", "It can see clearly through walls to track down its prey and seek its lost young.",
            2, "Luxio", 130, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                    "Volt Bolt", "Discard all Lightning Energy from this Pokémon. This attack does 120 damage to 1 of your opponent's Pokémon.",
                    EnergyCost.of(Type.LIGHTNING, 3), new Attempt(List.of(
                    new DealDamage(new Literal(120), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:")),
                    new DiscardTypeEnergy(Type.LIGHTNING, new EnergyOn(new Self(), Type.LIGHTNING), new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-061 - Pachirisu ex
     */
    public static final PokemonCard PACHIRISU_EX = PokemonCard.basic(
            "A2-061", "Pachirisu ex", "",
            120, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sparking Gadget", "If this Pokémon has a Pokémon Tool attached, this attack does 40 more damage.",
                    EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(
                            new Branch.Case(new For(new HasTool(), new Self()), new Literal(80))
                    ), new Literal(40)), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX);

    /**
     * A2-062 - Rotom
     */
    public static final PokemonCard ROTOM = PokemonCard.basic(
            "A2-062", "Rotom", "Its electricity-like body can enter some kinds of machines and take control in order to make mischief.",
            70, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Assault Laser", "If your opponent's Active Pokémon has a Pokémon Tool attached, this attack does 30 more damage.",
                    EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(
                            new Branch.Case(new For(new HasTool(), new OpponentActive()), new Literal(50))
                    ), new Literal(20)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    static final List<PokemonCard> CARDS = List.of(
            MAGNEMITE, MAGNETON, MAGNEZONE, VOLTORB, ELECTRODE, ELECTABUZZ, ELECTIVIRE,
            SHINX, LUXIO, LUXRAY, PACHIRISU_EX, ROTOM);

    private Lightning() {
    }
}
