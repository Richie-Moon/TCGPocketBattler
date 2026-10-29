package com.tcgpocket.pool.A2a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Triumphant Light — Lightning.
 */
public final class Lightning {

    /**
     * A2a-025 - Pikachu
     */
    public static final PokemonCard PIKACHU = PokemonCard.basic(
            "A2a-025", "Pikachu", "When it is angered, it immediately discharges the energy stored in the pouches in its cheeks.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Spark", "This attack also does 10 damage to 1 of your opponent's Benched Pokémon.",
                    EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new DealDamage(new Literal(10), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Select a Benched Pokémon"))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2a-026 - Raichu
     */
    public static final PokemonCard RAICHU = PokemonCard.evolution(
            "A2a-026", "Raichu", "Its tail discharges electricity into the ground, protecting it from getting shocked.",
            1, "Pikachu", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Spark", "This attack also does 20 damage to 1 of your opponent's Benched Pokémon.",
                    EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new DealDamage(new Literal(20), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Select a Benched Pokémon"))))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING).withAbility(Links.resilienceLink());

    /**
     * A2a-027 - Electrike
     */
    public static final PokemonCard ELECTRIKE = PokemonCard.basic(
            "A2a-027", "Electrike", "It stores static electricity in its fur for discharging. It gives off sparks if a storm approaches.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Zap Kick", "", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2a-028 - Manectric
     */
    public static final PokemonCard MANECTRIC = PokemonCard.evolution(
            "A2a-028", "Manectric", "It stimulates its own muscles with electricity, so it can move quickly. It eases its soreness with electricity, too, so it can recover quickly as well.",
            1, "Electrike", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flash", "During your opponent's next turn, if the Defending Pokémon tries to use an attack, your opponent flips a coin. If tails, that attack doesn't happen.",
                    EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new HinderAttack(new OpponentActive(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    static final List<PokemonCard> CARDS = List.of(PIKACHU, RAICHU, ELECTRIKE, MANECTRIC);

    private Lightning() {
    }
}
