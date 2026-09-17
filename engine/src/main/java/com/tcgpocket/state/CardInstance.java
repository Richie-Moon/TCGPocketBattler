package com.tcgpocket.state;

import com.tcgpocket.card.ICard;

import java.util.Objects;

/**
 * One physical copy of a card within one battle.
 *
 * <p>A deck holds duplicates: two copies of Pikachu share a single immutable
 * {@link ICard} definition but are distinct instances that take damage
 * independently.
 *
 * <p>Because an instance knows its own {@link #zone()} and {@link #owner()},
 * card-level conditions such as {@code IsActive} can be answered from the
 * instance alone — which is why {@code ICondition} needs no resolution context.
 */
public sealed class CardInstance permits PokemonInPlay {

    private final int instanceId;
    private final ICard definition;
    private final Side owner;
    private Zone zone;

    public CardInstance(int instanceId, ICard definition, Side owner, Zone zone) {
        this.instanceId = instanceId;
        this.definition = Objects.requireNonNull(definition, "definition");
        this.owner = Objects.requireNonNull(owner, "owner");
        this.zone = Objects.requireNonNull(zone, "zone");
    }

    /** Unique within a battle. Two copies of the same card differ here. */
    public int instanceId() {
        return instanceId;
    }

    public ICard definition() {
        return definition;
    }

    public Side owner() {
        return owner;
    }

    public Zone zone() {
        return zone;
    }

    public void moveTo(Zone destination) {
        this.zone = Objects.requireNonNull(destination, "destination");
    }

    @Override
    public String toString() {
        return definition.name() + "#" + instanceId + "(" + zone + ")";
    }
}
