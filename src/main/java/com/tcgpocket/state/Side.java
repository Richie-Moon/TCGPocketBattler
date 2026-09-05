package com.tcgpocket.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * One player's half of the board.
 *
 * <p>Mutable by design: alongside {@link Battle} this is the game state that
 * effects write to. Everything reachable from a card's text is immutable.
 */
public final class Side {

    // TODO: IPlayer player, the Energy Zone (registeredTypes, currentEnergy,
    //       nextEnergy, energyAttachedThisTurn) and per-turn flags.

    private final String name;
    private final List<PokemonInPlay> bench = new ArrayList<>();
    private final List<CardInstance> hand = new ArrayList<>();
    private final List<CardInstance> deck = new ArrayList<>();
    private final List<CardInstance> discardPile = new ArrayList<>();

    private PokemonInPlay active;
    private int points;

    public Side(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public Optional<PokemonInPlay> active() {
        return Optional.ofNullable(active);
    }

    public void setActive(PokemonInPlay pokemon) {
        this.active = pokemon;
        if (pokemon != null) {
            pokemon.moveTo(Zone.ACTIVE);
        }
    }

    public List<PokemonInPlay> bench() {
        return Collections.unmodifiableList(bench);
    }

    public void addToBench(PokemonInPlay pokemon) {
        bench.add(pokemon);
        pokemon.moveTo(Zone.BENCH);
    }

    public List<CardInstance> hand() {
        return Collections.unmodifiableList(hand);
    }

    public List<CardInstance> deck() {
        return Collections.unmodifiableList(deck);
    }

    public List<CardInstance> discardPile() {
        return Collections.unmodifiableList(discardPile);
    }

    public void addToHand(CardInstance card) {
        hand.add(card);
        card.moveTo(Zone.HAND);
    }

    public void addToDeck(CardInstance card) {
        deck.add(card);
        card.moveTo(Zone.DECK);
    }

    public void addToDiscard(CardInstance card) {
        discardPile.add(card);
        card.moveTo(Zone.DISCARD);
    }

    /** Active plus bench, in that order. */
    public List<PokemonInPlay> inPlay() {
        List<PokemonInPlay> all = new ArrayList<>();
        active().ifPresent(all::add);
        all.addAll(bench);
        return Collections.unmodifiableList(all);
    }

    public boolean hasPokemonInPlay() {
        return active != null || !bench.isEmpty();
    }

    /**
     * The cards this side holds in a given zone.
     *
     * <p>This is the lookup {@code CountCards} is built on, which is why it is
     * keyed by {@link Zone} rather than exposing one getter per pile.
     */
    public List<CardInstance> cardsIn(Zone zone) {
        return switch (zone) {
            case DECK -> deck();
            case HAND -> hand();
            case DISCARD -> discardPile();
            case ACTIVE -> active == null ? List.of() : List.of(active);
            case BENCH -> List.copyOf(bench);
            // TODO: tools, once attachment is modelled.
            case ATTACHED -> List.of();
        };
    }

    public int points() {
        return points;
    }

    public void awardPoints(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("points must not be negative, was " + amount);
        }
        points += amount;
    }

    @Override
    public String toString() {
        return "Side[" + name + ", points=" + points + "]";
    }
}
