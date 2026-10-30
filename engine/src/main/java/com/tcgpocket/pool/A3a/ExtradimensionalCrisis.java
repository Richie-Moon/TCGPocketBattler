package com.tcgpocket.pool.A3a;

import com.tcgpocket.card.ICard;

import java.util.ArrayList;
import java.util.List;

/**
 * Extradimensional Crisis (A3a), assembled in set order. Same layout as
 * {@link com.tcgpocket.pool.A1.GeneticApex}: one class per energy type.
 */
public final class ExtradimensionalCrisis {

    /** The set code, which prefixes every id in it. */
    public static final String CODE = "A3a";

    private ExtradimensionalCrisis() {
    }

    /** Every card in the set, in printed order. */
    public static final List<ICard> CARDS = assemble();

    private static List<ICard> assemble() {
        List<ICard> cards = new ArrayList<>();
        cards.addAll(Grass.CARDS);
        cards.addAll(Fire.CARDS);
        cards.addAll(Water.CARDS);
        cards.addAll(Lightning.CARDS);
        cards.addAll(Psychic.CARDS);
        cards.addAll(Fighting.CARDS);
        cards.addAll(Darkness.CARDS);
        cards.addAll(Metal.CARDS);
        cards.addAll(Colorless.CARDS);
        cards.addAll(Trainers.CARDS);
        return List.copyOf(cards);
    }
}
