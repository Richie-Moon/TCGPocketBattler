package com.tcgpocket.pool;

import com.tcgpocket.card.ICard;
import com.tcgpocket.pool.a1.GeneticApex;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Every printed card, looked up by its id.
 *
 * <h2>Why this exists</h2>
 *
 * <p>The cards are written in Java today and are meant to become data later.
 * That swap is only cheap if nothing outside this package ever names a card
 * class: decks, tests and self-play runs all say {@code CardPool.get("A1-096")},
 * so replacing the constants with a JSON loader changes this one file rather
 * than every file.
 *
 * <p>The whole {@code pool} package is a leaf. It depends on the model; nothing
 * in the model depends on it. That is the one part of the project that could
 * become a Maven module of its own — unlike the interpreter core, which is a
 * single strongly-connected component and cannot be split.
 */
public final class CardPool {

    private static final Map<String, ICard> BY_ID = index(GeneticApex.CARDS);

    private CardPool() {
    }

    /** Every card in the pool, in set order. */
    public static List<ICard> all() {
        return List.copyOf(BY_ID.values());
    }

    /**
     * The card with that printed id, e.g. {@code "A1-096"}.
     *
     * @throws IllegalArgumentException if no such card exists. A typo in a deck
     *         list is a mistake, not a legal empty result.
     */
    public static ICard get(String id) {
        ICard card = BY_ID.get(id);
        if (card == null) {
            throw new IllegalArgumentException("no card with id " + id);
        }
        return card;
    }

    /** For callers that would rather test than catch — a deck-list validator. */
    public static Optional<ICard> find(String id) {
        return Optional.ofNullable(BY_ID.get(id));
    }

    public static int size() {
        return BY_ID.size();
    }

    /**
     * Builds the index, refusing duplicates.
     *
     * <p>Transcribing a set by hand means copying an entry and editing it, and
     * the id is the field most easily forgotten. Catching that at class-load
     * time turns a silently wrong card pool into an immediate failure.
     */
    private static Map<String, ICard> index(List<ICard> cards) {
        Map<String, ICard> byId = new LinkedHashMap<>();
        for (ICard card : cards) {
            ICard clash = byId.put(card.id(), card);
            if (clash != null) {
                throw new IllegalStateException(
                        "duplicate card id " + card.id()
                                + ": " + clash.name() + " and " + card.name());
            }
        }
        // Insertion-ordered, not Map.copyOf: set order is how a printed list
        // reads, and how a diff of a half-transcribed set stays legible.
        return Collections.unmodifiableMap(byId);
    }
}
