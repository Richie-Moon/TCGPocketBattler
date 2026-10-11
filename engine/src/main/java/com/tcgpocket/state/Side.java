package com.tcgpocket.state;

import com.tcgpocket.card.IRule;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.energy.Type;
import com.tcgpocket.player.IPlayer;
import com.tcgpocket.resolve.RandomSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * One player's half of the board.
 *
 * <p>Mutable by design: alongside {@link Battle} this is the game state that
 * effects write to. Everything reachable from a card's text is immutable.
 */
public final class Side {

    /** How many Pokemon may sit on the bench at once. */
    public static final int BENCH_LIMIT = 3;

    private final String name;
    private final IPlayer player;
    /** By slot; null is an empty slot. A Pokemon keeps its slot until it leaves the Bench. */
    private final PokemonInPlay[] bench = new PokemonInPlay[BENCH_LIMIT];
    private final List<CardInstance> hand = new ArrayList<>();
    private final List<CardInstance> deck = new ArrayList<>();
    private final List<CardInstance> discardPile = new ArrayList<>();
    private List<CardInstance> revealed = List.of();
    private List<CardInstance> seenTop = List.of();
    /** Energy is not a card, so its discard pile is a count per type. */
    private final Map<Type, Integer> discardedEnergy = new EnumMap<>(Type.class);

    /** Types this deck registered; the Energy Zone draws from these. */
    private final Set<Type> registeredTypes = EnumSet.noneOf(Type.class);

    private PokemonInPlay active;
    private int points;

    private Type currentEnergy;
    private Type nextEnergy;
    private boolean energyAttachedThisTurn;
    private boolean supporterPlayedThisTurn;
    private int supportersLockedUntilTurn = -1;
    private boolean retreatedThisTurn;
    private int knockedOutByAttackOnTurn = -1;
    private final List<AttackBonus> attackBonuses = new ArrayList<>();
    private final List<DamageReduction> damageReductions = new ArrayList<>();

    public Side(String name, IPlayer player) {
        this.name = name;
        this.player = java.util.Objects.requireNonNull(player, "player");
    }

    public String name() {
        return name;
    }

    /** Who decides for this side when the rules ask. */
    public IPlayer player() {
        return player;
    }

    public boolean benchIsFull() {
        return emptyBenchSlots().isEmpty();
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

    /** The Benched Pokemon in slot order, without the empty slots. */
    public List<PokemonInPlay> bench() {
        return Arrays.stream(bench).filter(Objects::nonNull).toList();
    }

    /** The Pokemon in a Bench slot; empty for an empty slot or one past the end. */
    public Optional<PokemonInPlay> benchAt(int slot) {
        return slot < 0 || slot >= BENCH_LIMIT ? Optional.empty() : Optional.ofNullable(bench[slot]);
    }

    public List<Integer> emptyBenchSlots() {
        return IntStream.range(0, BENCH_LIMIT).filter(slot -> bench[slot] == null).boxed().toList();
    }

    /** Into the first empty slot, for text that puts a Pokemon on the Bench without saying where. */
    public void addToBench(PokemonInPlay pokemon) {
        addToBench(pokemon, emptyBenchSlots().getFirst());
    }

    public void addToBench(PokemonInPlay pokemon, int slot) {
        if (bench[slot] != null) {
            throw new IllegalStateException("bench slot " + slot + " is taken by " + bench[slot]);
        }
        bench[slot] = pokemon;
        pokemon.moveTo(Zone.BENCH);
    }

    /** Empties its slot; the others stay where they are. */
    public boolean removeFromBench(PokemonInPlay pokemon) {
        int slot = Arrays.asList(bench).indexOf(pokemon);
        if (slot < 0) {
            return false;
        }
        bench[slot] = null;
        return true;
    }

    /** Brings a Benched Pokemon up; the old Active, if any, takes the slot it left. */
    public void switchIn(PokemonInPlay benched) {
        int slot = Arrays.asList(bench).indexOf(benched);
        if (slot < 0) {
            throw new IllegalArgumentException(benched + " is not on the bench");
        }
        bench[slot] = null;
        if (active != null) {
            addToBench(active, slot);
        }
        setActive(benched);
    }

    /** Active plus bench, in that order. */
    public List<PokemonInPlay> inPlay() {
        List<PokemonInPlay> all = new ArrayList<>();
        active().ifPresent(all::add);
        all.addAll(bench());
        return Collections.unmodifiableList(all);
    }

    /** A standing rule and the Pokemon holding it, for rules that ask about their holder. */
    public record HeldRule(IRule rule, PokemonInPlay holder) {
    }

    /** The standing rules this side's Pokemon in play hold right now; see {@link IRule}. */
    public List<HeldRule> standingRules() {
        List<HeldRule> held = new ArrayList<>();
        for (PokemonInPlay pokemon : inPlay()) {
            if (pokemon.definition().ability().orElse(null) instanceof PassiveAbility passive) {
                passive.rule().ifPresent(rule -> held.add(new HeldRule(rule, pokemon)));
            }
        }
        return held;
    }

    public boolean hasPokemonInPlay() {
        return active != null || !bench().isEmpty();
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

    /** Shows the hand as it is now to the opponent; see {@link #revealedHand()}. */
    public void revealHand() {
        revealed = List.copyOf(hand);
    }

    /**
     * The revealed cards still in hand. A card drawn since was never shown, so
     * it stays hidden.
     */
    public List<CardInstance> revealedHand() {
        return revealed.stream().filter(hand::contains).toList();
    }

    public void concealHand() {
        revealed = List.of();
    }

    /** Shows this side's owner the top {@code count} cards of their deck; see {@link #seenTopCards()}. */
    public void lookAtTopCards(int count) {
        seenTop = List.copyOf(deck.subList(0, Math.min(count, deck.size())));
    }

    /**
     * The top cards this side's owner has looked at, top first. Drawing one
     * leaves the rest known; anything else that disturbs the top of the deck
     * forgets them all.
     */
    public List<CardInstance> seenTopCards() {
        List<CardInstance> still = seenTop.stream().filter(deck::contains).toList();
        return deck.subList(0, still.size()).equals(still) ? still : List.of();
    }

    public boolean removeFromHand(CardInstance card) {
        return hand.remove(card);
    }

    /**
     * Takes a specific card out of the deck.
     *
     * <p>Distinct from {@link #drawCard()}, which takes the top one. Card text
     * that reaches into the deck for something particular needs to name it.
     */
    public boolean removeFromDeck(CardInstance card) {
        return deck.remove(card);
    }

    public boolean removeFromDiscard(CardInstance card) {
        return discardPile.remove(card);
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
        // A shuffle that happens to leave the same card on top must not tell the owner so.
        seenTop = List.of();
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
            case BENCH -> List.copyOf(bench());
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

    public Map<Type, Integer> discardedEnergy() {
        return Collections.unmodifiableMap(discardedEnergy);
    }

    public void addDiscardedEnergy(Type type, int count) {
        if (count > 0) {
            discardedEnergy.merge(type, count, Integer::sum);
        }
    }

    /** Takes up to {@code count} of a type back out of the discard pile; returns how many. */
    public int takeDiscardedEnergy(Type type, int count) {
        int taken = Math.min(Math.max(0, count), discardedEnergy.getOrDefault(type, 0));
        if (taken > 0) {
            discardedEnergy.merge(type, -taken, Integer::sum);
            discardedEnergy.remove(type, 0);
        }
        return taken;
    }

    /** Replaces the previewed energy; the zone still generates normally after it. */
    public void setNextEnergy(Type type) {
        nextEnergy = type;
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

    /**
     * Whether an opponent's effect has shut this side out of Supporters.
     *
     * <p>A turn stamp rather than a flag, and deliberately not one of the
     * {@link #resetTurnFlags} set: the lock is imposed on the opponent's turn
     * and has to survive the reset that starts this one. Its Pokemon-level
     * cousin is {@code ActiveModifier}, which cannot serve here because the
     * restriction outlives any Pokemon on the board.
     */
    public boolean supportersLocked(int currentTurn) {
        return currentTurn <= supportersLockedUntilTurn;
    }

    /** Extends the lock; a shorter one never shortens a longer one already set. */
    public void lockSupportersUntil(int turn) {
        supportersLockedUntilTurn = Math.max(supportersLockedUntilTurn, turn);
    }

    /**
     * A damage bonus for this side's attacks — Giovanni, Blaine.
     *
     * <p>On the {@code Side} for the reason {@link #supportersLocked} is: it
     * belongs to the player, so it must reach a Pokemon that retreats, switches
     * in or is benched after the card was played. A {@link Predicate} rather
     * than an {@code ICondition} keeps {@code state} out of the model's
     * dependency cycle, as {@code ActiveModifier} does.
     */
    public record AttackBonus(int amount, Predicate<PokemonInPlay> appliesTo, int expiresOnTurn) {
    }

    public void addAttackBonus(int amount, Predicate<PokemonInPlay> appliesTo, int expiresOnTurn) {
        attackBonuses.add(new AttackBonus(amount, appliesTo, expiresOnTurn));
    }

    /** The bonus one of this side's Pokemon attacks with this turn. */
    public int attackBonusFor(PokemonInPlay attacker, int currentTurn) {
        // ponytail: expired bonuses are never pruned; a handful per game, prune if that changes.
        return attackBonuses.stream()
                .filter(bonus -> currentTurn <= bonus.expiresOnTurn())
                .filter(bonus -> bonus.appliesTo().test(attacker))
                .mapToInt(AttackBonus::amount)
                .sum();
    }

    /**
     * Less damage from the opponent's attacks for every one of this side's
     * Pokemon — Blue. On the {@code Side} for the same reason as
     * {@link AttackBonus}: it covers whichever Pokemon is hit, including one
     * benched after the card was played.
     */
    public record DamageReduction(int amount, int expiresOnTurn) {
    }

    public void addDamageReduction(int amount, int expiresOnTurn) {
        damageReductions.add(new DamageReduction(amount, expiresOnTurn));
    }

    /** How much less damage this side's Pokemon take from the opponent's attacks this turn. */
    public int damageReductionOn(int currentTurn) {
        // ponytail: never pruned, like attackBonuses.
        return damageReductions.stream()
                .filter(reduction -> currentTurn <= reduction.expiresOnTurn())
                .mapToInt(DamageReduction::amount)
                .sum();
    }

    /**
     * Whether one of this side's Pokemon was Knocked Out by an attack on {@code turn} — Marshadow's
     * Revenge. A turn stamp, like {@link #supportersLocked}, because it is read a turn after it is set.
     */
    public boolean knockedOutByAttackOn(int turn) {
        return knockedOutByAttackOnTurn == turn;
    }

    public void markKnockedOutByAttack(int turn) {
        knockedOutByAttackOnTurn = turn;
    }

    public boolean retreatedThisTurn() {
        return retreatedThisTurn;
    }

    public void markRetreated() {
        retreatedThisTurn = true;
    }

    public void resetTurnFlags() {
        energyAttachedThisTurn = false;
        supporterPlayedThisTurn = false;
        retreatedThisTurn = false;
        inPlay().forEach(PokemonInPlay::resetTurnFlags);
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
