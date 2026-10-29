package com.tcgpocket.pool.A2b;

import com.tcgpocket.card.ICard;

import java.util.ArrayList;
import java.util.List;

/**
 * Shining Revelry (A2b), assembled in set order. Same layout as
 * {@link com.tcgpocket.pool.A1.GeneticApex}: one class per energy type.
 */
public final class ShiningRevelry {

    /** The set code, which prefixes every id in it. */
    public static final String CODE = "A2b";

    private ShiningRevelry() {
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
