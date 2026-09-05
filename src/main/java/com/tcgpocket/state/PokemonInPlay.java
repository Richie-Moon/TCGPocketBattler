package com.tcgpocket.state;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.energy.Type;
import com.tcgpocket.status.IStatus;
import com.tcgpocket.status.StatusCategory;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashSet;
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

    // TODO: List<PokemonCard> evolutionStack, List<ActiveModifier> modifiers —
    //       await evolution and the damage pipeline.

    private final Map<Type, Integer> attachedEnergy = new EnumMap<>(Type.class);
    private final Set<IStatus> statuses = new LinkedHashSet<>();
    private CardInstance tool;
    private int damage;
    private int turnPlayed;

    public PokemonInPlay(int instanceId, PokemonCard definition, Side owner, Zone zone, int turnPlayed) {
        super(instanceId, definition, owner, zone);
        this.turnPlayed = turnPlayed;
    }

    @Override
    public PokemonCard definition() {
        return (PokemonCard) super.definition();
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
