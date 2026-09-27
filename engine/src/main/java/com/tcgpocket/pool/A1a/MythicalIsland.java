package com.tcgpocket.pool.A1a;

import com.tcgpocket.card.ICard;

import java.util.ArrayList;
import java.util.List;

/**
 * Mythical Island (A1a), assembled in set order. Same layout as
 * {@link com.tcgpocket.pool.A1.GeneticApex}: one class per energy type.
 */
public final class MythicalIsland {

    /** The set code, which prefixes every id in it. */
    public static final String CODE = "A1a";

    private MythicalIsland() {
    }

    /** Every card in the set, in printed order. */
    public static final List<ICard> CARDS = assemble();

    private static List<ICard> assemble() {
        List<ICard> cards = new ArrayList<>();
        cards.addAll(Grass.CARDS);
        cards.addAll(Fire.CARDS);
        cards.addAll(Water.CARDS);
        return List.copyOf(cards);
    }
}
