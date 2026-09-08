package com.tcgpocket.state;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.energy.Type;
import com.tcgpocket.status.IStatus;
import com.tcgpocket.status.StatusCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A Pokemon on the board, carrying all the battle state that the printed card
 * cannot: damage taken, energy attached, and when it was played.
 *
 * <p>Damage is tracked rather than current HP, so that raising max HP mid-game
 * never has to reconcile two numbers.
 */
public final class PokemonInPlay extends CardInstance {

    private final Map<Type, Integer> attachedEnergy = new EnumMap<>(Type.class);
    private final Set<IStatus> statuses = new LinkedHashSet<>();
    private final List<ActiveModifier> modifiers = new ArrayList<>();
    /** What this used to be, oldest first; a Stage 1 keeps its Basic underneath. */
    private final List<PokemonCard> evolutionStack = new ArrayList<>();

    /**
     * The card currently on top. Held separately from the inherited definition
     * because evolving changes it, and the instance persists across the change
     * so damage and energy carry over.
     */
    private PokemonCard current;

    private CardInstance tool;
    private int damage;
    private int turnPlayed;
    private boolean abilityUsedThisTurn;

    public PokemonInPlay(int instanceId, PokemonCard definition, Side owner, Zone zone, int turnPlayed) {
        super(instanceId, definition, owner, zone);
        this.current = definition;
        this.turnPlayed = turnPlayed;
    }

    @Override
    public PokemonCard definition() {
        return current;
    }

    public List<PokemonCard> evolutionStack() {
        return Collections.unmodifiableList(evolutionStack);
    }

    /**
     * Evolves onto this Pokemon.
     *
     * <p>Damage, attached energy and the Tool all carry over; statuses and
     * temporary modifiers do not, which is why evolving is a way out of a
     * special condition. The turn counter resets, so the evolution cannot
     * itself evolve on the same turn.
     */
    public void evolveInto(PokemonCard evolution, int currentTurn) {
        evolutionStack.add(current);
        current = evolution;
        turnPlayed = currentTurn;
        statuses.clear();
        modifiers.clear();
    }

    /** Whether this Pokemon has already used its ability this turn. */
    public boolean abilityUsedThisTurn() {
        return abilityUsedThisTurn;
    }

    public void markAbilityUsed() {
        abilityUsedThisTurn = true;
    }

    public void resetTurnFlags() {
        abilityUsedThisTurn = false;
    }

    public int maxHp() {
        return definition().maxHp();
    }

    /** Damage counters on this Pokemon, never negative and never above max HP. */
    public int damage() {
        return damage;
    }

    public int currentHp() {
        return maxHp() - damage;
    }

    public boolean isKnockedOut() {
        return damage >= maxHp();
    }

    /** Turn number on which this Pokemon was put into play; gates evolution. */
    public int turnPlayed() {
        return turnPlayed;
    }

    public void setTurnPlayed(int turn) {
        this.turnPlayed = turn;
    }

    /** Caps at max HP, so damage never runs away past a knockout. */
    public void takeDamage(int amount) {
        requireNonNegative(amount, "damage");
        damage = Math.min(maxHp(), damage + amount);
    }

    public void heal(int amount) {
        requireNonNegative(amount, "heal");
        damage = Math.max(0, damage - amount);
    }

    public Map<Type, Integer> attachedEnergy() {
        return Collections.unmodifiableMap(attachedEnergy);
    }

    public int energyOf(Type type) {
        return attachedEnergy.getOrDefault(type, 0);
    }

    public int totalEnergy() {
        return attachedEnergy.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void attachEnergy(Type type, int count) {
        requireNonNegative(count, "energy count");
        if (count > 0) {
            attachedEnergy.merge(type, count, Integer::sum);
        }
    }

    /** Removes up to {@code count} energy of a type; returns how many actually went. */
    public int discardEnergy(Type type, int count) {
        requireNonNegative(count, "energy count");
        int removed = Math.min(count, energyOf(type));
        if (removed > 0) {
            int remaining = energyOf(type) - removed;
            if (remaining == 0) {
                attachedEnergy.remove(type);
            } else {
                attachedEnergy.put(type, remaining);
            }
        }
        return removed;
    }

    public Set<IStatus> statuses() {
        return Collections.unmodifiableSet(statuses);
    }

    public boolean hasStatus(IStatus status) {
        return statuses.contains(status);
    }

    /**
     * Applies a status, enforcing the stacking rule: Poisoned and Burned may
     * coexist with each other and with one special condition, but a second
     * special condition replaces the first rather than stacking.
     */
    public void addStatus(IStatus status) {
        if (status.category() == StatusCategory.SPECIAL) {
            statuses.removeIf(existing -> existing.category() == StatusCategory.SPECIAL);
        }
        statuses.add(status);
    }

    public boolean removeStatus(IStatus status) {
        return statuses.remove(status);
    }

    /** Statuses come off when a Pokemon leaves the active spot or evolves. */
    public void clearStatuses() {
        statuses.clear();
    }

    /** Statuses and temporary modifiers both come off when leaving the active spot. */
    public void clearTemporaryState() {
        statuses.clear();
        modifiers.clear();
    }

    /** Temporary buffs and debuffs; read by the damage pipeline. */
    public List<ActiveModifier> modifiers() {
        return Collections.unmodifiableList(modifiers);
    }

    public void addModifier(ActiveModifier modifier) {
        modifiers.add(modifier);
    }

    /**
     * Whether a modifier of that kind is live on the given turn.
     *
     * <p>Asks the modifiers directly rather than trusting
     * {@link #expireModifiers} to have run, because legality is checked
     * mid-turn and expiry happens between turns.
     */
    public boolean hasModifier(ModifierKind kind, int currentTurn) {
        return modifiers.stream()
                .anyMatch(modifier -> modifier.kind() == kind && modifier.isActiveOn(currentTurn));
    }

    /** Drops modifiers whose duration has run out. Called between turns. */
    public void expireModifiers(int currentTurn) {
        modifiers.removeIf(modifier -> !modifier.isActiveOn(currentTurn));
    }

    public Optional<CardInstance> tool() {
        return Optional.ofNullable(tool);
    }

    public boolean hasTool() {
        return tool != null;
    }

    public void attachTool(CardInstance toolCard) {
        if (tool != null) {
            throw new IllegalStateException(
                    definition().name() + " already holds " + tool.definition().name());
        }
        tool = toolCard;
        toolCard.moveTo(Zone.ATTACHED);
    }

    public Optional<CardInstance> removeTool() {
        Optional<CardInstance> removed = Optional.ofNullable(tool);
        tool = null;
        return removed;
    }

    private static void requireNonNegative(int amount, String what) {
        if (amount < 0) {
            throw new IllegalArgumentException(what + " must not be negative, was " + amount);
        }
    }
}
