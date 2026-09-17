package com.tcgpocket.pool.A1;

import com.tcgpocket.card.ICard;

import java.util.ArrayList;
import java.util.List;

/**
 * Genetic Apex (A1), assembled in set order.
 *
 * <p>One class per energy type below, because that is the order the set is
 * printed in: transcribing it goes top to bottom, and a file stays at roughly
 * twenty-five cards instead of one file at two hundred.
 */
public final class GeneticApex {

    /** The set code, which prefixes every id in it. */
    public static final String CODE = "A1";

    private GeneticApex() {
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
        cards.addAll(Dragon.CARDS);
        cards.addAll(Colorless.CARDS);
        cards.addAll(Trainers.CARDS);
        return List.copyOf(cards);
    }
}
