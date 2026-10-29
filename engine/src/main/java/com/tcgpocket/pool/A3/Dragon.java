package com.tcgpocket.pool.A3;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Celestial Guardians — Dragon.
 */
public final class Dragon {

    /**
     * A3-124 - Drampa
     */
    public static final PokemonCard DRAMPA = PokemonCard.basic(
            "A3-124", "Drampa", "The mountains it calls home are nearly two miles in height. On rare occasions, it descends to play with the children living in the towns below.",
            100, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Berserk", "If any of your Benched Pokémon have damage on them, this attack does 50 more damage.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(
                            new Branch.Case(new ForAny(new IsDamaged(), new AttackerBench()), new Literal(70))
                    ), new Literal(20)), new OpponentActive())))
            )), CardRarity.RARE
    );

    /**
     * A3-125 - Jangmo-o
     */
    public static final PokemonCard JANGMO_O = PokemonCard.basic(
            "A3-125", "Jangmo-o", "They learn to fight by smashing their head scales together. The dueling strengthens both their skills and their spirits.",
            60, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Headbutt", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    );

    /**
     * A3-126 - Hakamo-o
     */
    public static final PokemonCard HAKAMO_O = PokemonCard.evolution(
            "A3-126", "Hakamo-o", "The scaleless, scarred parts of its body are signs of its strength. It shows them off to defeated opponents.",
            1, "Jangmo-o", 100, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Dragon Claw", "", EnergyCost.of(Type.LIGHTNING, 1, Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    );

    /**
     * A3-127 - Kommo-o
     */
    public static final PokemonCard KOMMO_O = PokemonCard.evolution(
            "A3-127", "Kommo-o", "It clatters its tail scales to unnerve opponents. This Pokémon will battle only those who stand steadfast in the face of this display.",
            2, "Hakamo-o", 150, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Clanging Scales", "During your opponent's next turn, this Pokémon takes +30 damage from attacks.", EnergyCost.of(Type.LIGHTNING, 1, Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(130), new OpponentActive()),
                    new IncreaseDamageTaken(new Literal(30), new Self(), new Literal(1))))
            )), CardRarity.RARE
    );

    /** A3-176 · Drampa (alternate art of A3-124) */
    public static final PokemonCard DRAMPA_IR = DRAMPA.withId("A3-176", CardRarity.ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(DRAMPA, JANGMO_O, HAKAMO_O, KOMMO_O,
            DRAMPA_IR);

    private Dragon() {
    }
}
