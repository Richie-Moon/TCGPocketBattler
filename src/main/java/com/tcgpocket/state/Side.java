package com.tcgpocket.state;

import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.RandomSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * One player's half of the board.
 *
 * <p>Mutable by design: alongside {@link Battle} this is the game state that
 * effects write to. Everything reachable from a card's text is immutable.
 */
public final class Side {

    // TODO: IPlayer player — awaits the agent seam.

    private final String name;
    private final List<PokemonInPlay> bench = new ArrayList<>();
    private final List<CardInstance> hand = new ArrayList<>();
    private final List<CardInstance> deck = new ArrayList<>();
    private final List<CardInstance> discardPile = new ArrayList<>();

    /** Types this deck registered; the Energy Zone draws from these. */
    private final Set<Type> registeredTypes = EnumSet.noneOf(Type.class);

    private PokemonInPlay active;
    private int points;

    private Type currentEnergy;
    private Type nextEnergy;
    private boolean energyAttachedThisTurn;
    private boolean supporterPlayedThisTurn;

    public Side(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    /* ------------------------------------------------------------------ */
    /* Board                                                               */
    /* ------------------------------------------------------------------ */

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

    public boolean removeFromBench(PokemonInPlay pokemon) {
        return bench.remove(pokemon);
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

    /* ------------------------------------------------------------------ */
    /* Piles                                                               */
    /* ------------------------------------------------------------------ */

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

    public boolean removeFromHand(CardInstance card) {
        return hand.remove(card);
    }

    /**
     * Draws the top card, or empty when the deck is out.
     *
     * <p>Running out of cards is not a loss in Pocket, so this reports nothing
     * rather than ending the game.
     */
    public Optional<CardInstance> drawCard() {
        if (deck.isEmpty()) {
            return Optional.empty();
        }
        CardInstance drawn = deck.remove(0);
        addToHand(drawn);
        return Optional.of(drawn);
    }

    public void shuffleDeck(RandomSource rng) {
        rng.shuffle(deck);
    }

    /**
     * The cards this side holds in a given zone.
     *
     * <p>Keyed by {@link Zone} rather than exposing one getter per pile,
     * because that is the lookup {@code CountCards} is built on.
     */
    public List<CardInstance> cardsIn(Zone zone) {
        return switch (zone) {
            case DECK -> deck();
            case HAND -> hand();
            case DISCARD -> discardPile();
            case ACTIVE -> active == null ? List.of() : List.of(active);
            case BENCH -> List.copyOf(bench);
            case ATTACHED -> inPlay().stream()
                    .flatMap(pokemon -> pokemon.tool().stream())
                    .toList();
        };
    }

    /* ------------------------------------------------------------------ */
    /* Energy Zone                                                         */
    /* ------------------------------------------------------------------ */

    public Set<Type> registeredTypes() {
        return Collections.unmodifiableSet(registeredTypes);
    }

    public void registerTypes(Type... types) {
        for (Type type : types) {
            if (!type.isGeneratable()) {
                throw new IllegalArgumentException(type + " cannot be generated by an Energy Zone");
            }
            registeredTypes.add(type);
        }
    }

    /** The energy available to attach right now. */
    public Optional<Type> currentEnergy() {
        return Optional.ofNullable(currentEnergy);
    }

    /** Previewed to both players, so an opponent can plan against it. */
    public Optional<Type> nextEnergy() {
        return Optional.ofNullable(nextEnergy);
    }

    /** Takes the current energy out of the zone, or empty if there is none. */
    public Optional<Type> consumeCurrentEnergy() {
        Optional<Type> taken = currentEnergy();
        currentEnergy = null;
        return taken;
    }

    /**
     * Advances the zone at the start of a turn: what was previewed becomes
     * available, and a fresh type is previewed.
     *
     * <p>Unused energy does not accumulate — whatever was current is dropped.
     */
    public void generateEnergy(RandomSource rng) {
        if (registeredTypes.isEmpty()) {
            return;
        }
        currentEnergy = nextEnergy;
        nextEnergy = randomRegisteredType(rng);
        if (currentEnergy == null) {
            currentEnergy = nextEnergy;
            nextEnergy = randomRegisteredType(rng);
        }
    }

    private Type randomRegisteredType(RandomSource rng) {
        List<Type> types = List.copyOf(registeredTypes);
        return types.get(rng.nextInt(types.size()));
    }

    public boolean energyAttachedThisTurn() {
        return energyAttachedThisTurn;
    }

    public void markEnergyAttached() {
        energyAttachedThisTurn = true;
    }

    public boolean supporterPlayedThisTurn() {
        return supporterPlayedThisTurn;
    }

    public void markSupporterPlayed() {
        supporterPlayedThisTurn = true;
    }

    public void resetTurnFlags() {
        energyAttachedThisTurn = false;
        supporterPlayedThisTurn = false;
    }

    /* ------------------------------------------------------------------ */
    /* Scoring                                                             */
    /* ------------------------------------------------------------------ */

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
