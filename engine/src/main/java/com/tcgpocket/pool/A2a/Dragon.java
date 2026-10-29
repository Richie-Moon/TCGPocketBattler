package com.tcgpocket.pool.A2a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Triumphant Light — Dragon.
 */
public final class Dragon {

    /**
     * A2a-061 - Giratina
     */
    public static final PokemonCard GIRATINA = PokemonCard.basic(
            "A2a-061", "Giratina", "This Pokémon is said to live in a world on the reverse side of ours, where common knowledge is distorted and strange.",
            120, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Crisis Dive", "Discard 2 random Energy from this Pokémon.",
                    EnergyCost.of(Type.GRASS, 1, Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive()),
                    new DiscardRandomEnergy(new Literal(2), new Self())))
            )), CardRarity.RARE
    );

    static final List<PokemonCard> CARDS = List.of(GIRATINA);

    private Dragon() {
    }
}
