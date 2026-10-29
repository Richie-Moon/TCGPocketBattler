package com.tcgpocket.pool.A2a;

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
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Triumphant Light — Metal.
 */
public final class Metal {

    /**
     * A2a-053 - Magnemite
     */
    public static final PokemonCard MAGNEMITE = PokemonCard.basic(
            "A2a-053", "Magnemite", "The electromagnetic waves emitted by the units at the sides of its head expel antigravity, which allows it to float.",
            60, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.METAL, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2a-054 - Magneton
     */
    public static final PokemonCard MAGNETON = PokemonCard.evolution(
            "A2a-054", "Magneton", "Three Magnemite are linked by a strong magnetic force. Earaches will occur if you get too close.",
            1, "Magnemite", 90, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Rolling Attack", "", EnergyCost.of(Type.METAL, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2a-055 - Magnezone
     */
    public static final PokemonCard MAGNEZONE = PokemonCard.evolution(
            "A2a-055", "Magnezone", "As it zooms through the sky, this Pokémon seems to be receiving signals of unknown origin while transmitting signals of unknown purpose.",
            2, "Magneton", 140, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Power Beam", "", EnergyCost.of(Type.METAL, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE).withAbility(Links.resilienceLink());

    /**
     * A2a-056 - Mawile
     */
    public static final PokemonCard MAWILE = PokemonCard.basic(
            "A2a-056", "Mawile", "It uses its docile-looking face to lull foes into complacency, then bites with its huge, relentless jaws.",
            70, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Iron Beam Breaker", "If your opponent's Active Pokémon is a Metal Pokémon, this attack does 30 more damage.",
                    EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new For(new HasType(Type.METAL), new OpponentActive()), new Literal(50))),
                            new Literal(20)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2a-057 - Probopass ex
     */
    public static final PokemonCard PROBOPASS_EX = PokemonCard.evolution(
            "A2a-057", "Probopass ex", "",
            1, "Nosepass", 160, Type.METAL, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Defensive Unit", "During your opponent's next turn, this Pokémon takes −20 damage from attacks.",
                    EnergyCost.of(Type.METAL, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive()),
                    new ReduceDamageTaken(new Literal(20), new Self(), new Literal(1))))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX);

    /**
     * A2a-058 - Bronzor
     */
    public static final PokemonCard BRONZOR = PokemonCard.basic(
            "A2a-058", "Bronzor", "Ancient people believed that the pattern on Bronzor's back contained a mysterious power.",
            60, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2a-059 - Bronzong
     */
    public static final PokemonCard BRONZONG = PokemonCard.evolution(
            "A2a-059", "Bronzong", "In ages past, this Pokémon was revered as a bringer of rain. It was found buried in the ground.",
            1, "Bronzor", 120, Type.METAL, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Psychic", "This attack does 20 more damage for each Energy attached to your opponent's Active Pokémon.",
                    EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Sum(new Literal(50), new Product(new Literal(20), new EnergyOn(new OpponentActive()))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2a-060 - Origin Forme Dialga
     */
    public static final PokemonCard ORIGIN_FORME_DIALGA = PokemonCard.basic(
            "A2a-060", "Origin Forme Dialga", "Radiant light caused Dialga to take on a form bearing a striking resemblance to the creator Pokémon. Dialga now wields such colossal strength that one must conclude this is its true form.",
            120, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Time Mash", "Flip a coin. If tails, during your next turn, this Pokémon can't attack.",
                    EnergyCost.of(Type.METAL, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(100), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new PreventAttack(new Self(), new Literal(2)))))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    static final List<PokemonCard> CARDS = List.of(
            MAGNEMITE, MAGNETON, MAGNEZONE, MAWILE, PROBOPASS_EX, BRONZOR, BRONZONG, ORIGIN_FORME_DIALGA);

    private Metal() {
    }
}
