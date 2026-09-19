package com.tcgpocket.engine;

import com.tcgpocket.card.ICard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.energy.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The deck-building rules: exactly {@value #DECK_SIZE} cards, at least one
 * Basic Pokemon, at most {@value #MAX_COPIES} cards sharing a name, and
 * {@value #MIN_ENERGY_TYPES}–{@value #MAX_ENERGY_TYPES} Energy Zone types.
 *
 * <p>Copies are counted by <em>name</em>, not id: two Bulbasaur from different
 * sets are still two Bulbasaur. "Pikachu ex" is a different name from
 * "Pikachu", so an ex and its non-ex counterpart are counted apart for free.
 * There are no Energy cards to count — energy is generated, so a deck only
 * names the types its Energy Zone produces.
 */
public final class DeckValidator {

    public static final int DECK_SIZE = 20;
    public static final int MAX_COPIES = 2;
    public static final int MIN_ENERGY_TYPES = 1;
    public static final int MAX_ENERGY_TYPES = 3;

    private DeckValidator() {
    }

    /** Every rule the deck breaks, as a sentence for the player; empty when it is legal. */
    public static List<String> problems(List<? extends ICard> cards, Set<Type> energy) {
        List<String> problems = new ArrayList<>();
        if (cards.size() != DECK_SIZE) {
            problems.add("A deck must have exactly " + DECK_SIZE + " cards, not " + cards.size());
        }
        if (cards.stream().noneMatch(card -> card instanceof PokemonCard pokemon && pokemon.isBasic())) {
            problems.add("A deck must have at least one Basic Pokemon");
        }
        Map<String, Long> copies = cards.stream()
                .collect(Collectors.groupingBy(ICard::name, Collectors.counting()));
        copies.forEach((name, count) -> {
            if (count > MAX_COPIES) {
                problems.add("At most " + MAX_COPIES + " cards named " + name + ", not " + count);
            }
        });
        if (energy.size() < MIN_ENERGY_TYPES || energy.size() > MAX_ENERGY_TYPES) {
            problems.add("Choose " + MIN_ENERGY_TYPES + " to " + MAX_ENERGY_TYPES
                    + " energy types, not " + energy.size());
        }
        energy.stream().filter(type -> !type.isGeneratable())
                .forEach(type -> problems.add(type + " is not an energy type a deck can choose"));
        return problems;
    }

    /** Throws listing every broken rule, for callers that cannot go on with an illegal deck. */
    public static void requireValid(String owner, List<? extends ICard> cards, Set<Type> energy) {
        List<String> problems = problems(cards, energy);
        if (!problems.isEmpty()) {
            throw new IllegalStateException(owner + "'s deck is illegal: " + String.join("; ", problems));
        }
    }
}
