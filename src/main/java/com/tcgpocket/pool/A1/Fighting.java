package com.tcgpocket.pool.A1;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.OpponentActive;

import java.util.List;

/**
 * Genetic Apex — Fighting.
 */
public final class Fighting {

    /**
     * A1-137 - Sandshrew
     */
    public static final PokemonCard SANDSHREW = PokemonCard.basic(
                    "A1-137", "Sandshrew", "It loves to bathe in the grit of dry, sandy areas. By sand bathing, the Pokémon rids itself of dirt and moisture clinging to its body.",
                    70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Scratch", "", EnergyCost.of(Type.FIGHTING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.GRASS);

    static final List<PokemonCard> CARDS = List.of(SANDSHREW);

    private Fighting() {
    }
}
