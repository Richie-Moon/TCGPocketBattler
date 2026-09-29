package com.tcgpocket.pool.A1a;

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
 * Mythical Island — Dragon.
 */
public final class Dragon {
    /**
     * A1a-056 - Druddigon
     */
    public static final PokemonCard DRUDDIGON = PokemonCard.basic(
            "A1a-056", "Druddigon", "Druddigon lives in caves, but it never skips sunbathing—it won't be able to move if its body gets too cold.",
            100, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Dragon Claw", "", EnergyCost.of(Type.FIRE, 1, Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive())))
            )), CardRarity.UNCOMMON
    );

    static final List<PokemonCard> CARDS = List.of(DRUDDIGON);

    private Dragon() {
    }
}
