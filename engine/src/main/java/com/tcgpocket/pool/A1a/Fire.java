package com.tcgpocket.pool.A1a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsPoisoned;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.*;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Mythical Island — Fire.
 */
public final class Fire {
    /**
     * A1a-010 - Ponyta
     */
    public static final PokemonCard PONYTA = PokemonCard.basic(
            "A1a-010", "Ponyta", "It can't run properly when it's newly born. As it races around with others of its kind, its legs grow stronger.",
            60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Stomp", "Flip a coin. If heads, this attack does 30 more damage.",
                    EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(
                            new Literal(10),
                            new Product(new NumberHeads(), new Literal(30))),
                            new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A1a-011 - Rapidash
     */
    public static final PokemonCard RAPIDASH = PokemonCard.evolution(
            "A1a-011", "Rapidash", "This Pokémon can be seen galloping through fields at speeds of up to 150 mph, its fiery mane fluttering in the wind.",
            1, "Ponyta", 100, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Rising Lunge", "Flip a coin. If heads, this attack does 60 more damage.",
                    EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(
                            new Literal(40),
                            new Product(new NumberHeads(), new Literal(60))),
                            new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A1a-012 - Magmar
     */
    public static final PokemonCard MAGMAR = PokemonCard.basic(
            "A1a-012", "Magmar", "Magmar dispatches its prey with fire. But it regrets this habit once it realizes that it has burned its intended prey to a charred crisp.",
            80, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Fire Blast", "Discard 2 Fire Energy from this Pokémon.",
                    EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIRE, new Literal(2), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A1a-013 - Larvesta
     */
    public static final PokemonCard LARVESTA = PokemonCard.basic(
            "A1a-013", "Larvesta", "This Pokémon was called the Larva That Stole the Sun. The fire Larvesta spouts from its horns can cut right through a sheet of iron.",
            80, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Combustion", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A1a-014 - Volcarona — Volcanic Ash: the discard comes first, as printed.
     * The cost guarantees two Fire, so it cannot fail and swallow the damage.
     */
    public static final PokemonCard VOLCARONA = PokemonCard.evolution(
            "A1a-014", "Volcarona", "Its burning body causes it to be unpopular in hot parts of the world, but in cold ones, Volcarona is revered as an embodiment of the sun.",
            1, "Larvesta", 120, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Volcanic Ash", "Discard 2 Fire Energy from this Pokémon. This attack does 80 damage to 1 of your opponent's Pokémon.",
                    EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DiscardTypeEnergy(Type.FIRE, new Literal(2), new Self()),
                    new DealDamage(new Literal(80), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"))))
            )), CardRarity.RARE
    ).withWeakness(Type.WATER);

    /**
     * A1a-015 - Salandit
     */
    public static final PokemonCard SALANDIT = PokemonCard.basic(
            "A1a-015", "Salandit", "It taunts its prey and lures them into narrow, rocky areas where it then sprays them with toxic gas to make them dizzy and take them down.",
            60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Venoshock", "If your opponent's Active Pokémon is Poisoned, this attack does 40 more damage.",
                    EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new For(new IsPoisoned(), new OpponentActive()), new Literal(50))),
                            new Literal(10)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A1a-016 - Salazzle
     */
    public static final PokemonCard SALAZZLE = PokemonCard.evolution(
            "A1a-016", "Salazzle", "Salazzle makes its opponents light-headed with poisonous gas, then captivates them with alluring movements to turn them into loyal servants.",
            1, "Salandit", 80, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Poison Claws", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /** A1a-071 · Salandit (alternate art of A1a-015) */
    public static final PokemonCard SALANDIT_IR = SALANDIT.withId("A1a-071", CardRarity.ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            PONYTA, RAPIDASH, MAGMAR, LARVESTA, VOLCARONA, SALANDIT, SALAZZLE,
            SALANDIT_IR);

    private Fire() {
    }
}
