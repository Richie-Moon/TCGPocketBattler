package com.tcgpocket.pool.a1;

import com.tcgpocket.card.ICard;

import java.util.ArrayList;
import java.util.List;

/**
 * Genetic Apex (A1), assembled in set order.
 *
 * <p>One class per energy type below, because that is the order the set is
 * printed in: transcribing it goes top to bottom, and a file stays at roughly
 * twenty-five cards instead of one file at two hundred.
 *
 * <p><b>This is a sample, not the set.</b> Seven cards are here to establish
 * the pattern. Every id, HP, retreat cost and weakness needs checking against
 * the printed card before it is trusted — {@code CardPoolTest} asserts the
 * shape of the card text, which is a different thing from asserting the
 * numbers are right.
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
        // TODO: Fire, Water once transcribed.
        cards.addAll(Lightning.CARDS);
        cards.addAll(Psychic.CARDS);
        // TODO: Fighting.
        cards.addAll(Darkness.CARDS);
        // TODO: Metal, Dragon, Colorless.
        cards.addAll(Trainers.CARDS);
        return List.copyOf(cards);
    }
}
