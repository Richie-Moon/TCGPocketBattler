package com.tcgpocket.pool.A4;

import com.tcgpocket.card.ICard;

import java.util.ArrayList;
import java.util.List;

/**
 * Wisdom of Sea and Sky (A4), assembled in set order. Same layout as
 * {@link com.tcgpocket.pool.A1.GeneticApex}: one class per energy type.
 *
 * <p>A4-001 to A4-241: every type, the Trainers, their alternate arts and the shinies.
 */
public final class WisdomOfSeaAndSky {

    /** The set code, which prefixes every id in it. */
    public static final String CODE = "A4";

    private WisdomOfSeaAndSky() {
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
