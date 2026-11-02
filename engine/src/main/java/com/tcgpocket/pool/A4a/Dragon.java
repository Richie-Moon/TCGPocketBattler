package com.tcgpocket.pool.A4a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.EnergyTypesOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Secluded Springs (A4a): the Dragon cards, in printed set order.
 */
public final class Dragon {

    /**
     * A4a-055 - Altaria
     */
    public static final PokemonCard ALTARIA = PokemonCard.evolution(
            "A4a-055", "Altaria", "If it bonds with a person, it will gently envelop the friend with its soft wings, then hum.",
            1, "Swablu", 120, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Dragon Arcana", "If this Pokémon has 2 or more different types of Energy attached, this attack does 60 more damage.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(new GreaterThan(new EnergyTypesOn(new Self()), new Literal(1)), new Literal(100))), new Literal(40)), new OpponentActive())))
            )), CardRarity.RARE
    );

    static final List<PokemonCard> CARDS = List.of(
            ALTARIA);

    private Dragon() {
    }
}
