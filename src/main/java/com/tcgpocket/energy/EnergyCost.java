package com.tcgpocket.energy;

import java.util.EnumMap;
import java.util.Map;

/**
 * What an attack costs, as a multiset of energy types.
 *
 * <p>{@link Type#COLORLESS} in a cost is a wildcard: it is paid by any leftover
 * energy of any type, once the typed requirements have been met. So a cost of
 * one Lightning and one Colorless is satisfied by two Lightning, or by a
 * Lightning and a Water, but not by two Water.
 */
public record EnergyCost(Map<Type, Integer> requirements) {

    public EnergyCost {
        Map<Type, Integer> copy = new EnumMap<>(Type.class);
        requirements.forEach((type, count) -> {
            if (count == null || count < 0) {
                throw new IllegalArgumentException("cost for " + type + " must not be negative");
            }
            if (count > 0) {
                copy.put(type, count);
            }
        });
        requirements = Map.copyOf(copy);
    }

    public static EnergyCost free() {
        return new EnergyCost(Map.of());
    }

    public static EnergyCost of(Type type, int count) {
        return new EnergyCost(Map.of(type, count));
    }

    public static EnergyCost of(Type type, int count, Type otherType, int otherCount) {
        return new EnergyCost(Map.of(type, count, otherType, otherCount));
    }
    
    public static EnergyCost of(Type type, int count, Type secondType, int secondCount, Type thirdType, int thirdCount) {
        return new EnergyCost(Map.of(type, count, secondType, secondCount, thirdType, thirdCount));
    }

    /** Total energy needed, typed and colorless together. */
    public int total() {
        return requirements.values().stream().mapToInt(Integer::intValue).sum();
    }

    public boolean isFree() {
        return requirements.isEmpty();
    }

    public int requirementFor(Type type) {
        return requirements.getOrDefault(type, 0);
    }

    /**
     * Whether the given attached energy pays this cost.
     *
     * <p>Typed requirements are matched against their own type first; whatever
     * is left over — of any type — then pays the colorless part.
     */
    public boolean isSatisfiedBy(Map<Type, Integer> attached) {
        int typedNeeded = 0;

        for (Map.Entry<Type, Integer> requirement : requirements.entrySet()) {
            Type type = requirement.getKey();
            int needed = requirement.getValue();
            if (type == Type.COLORLESS) {
                continue;
            }
            if (attached.getOrDefault(type, 0) < needed) {
                return false;
            }
            typedNeeded += needed;
        }

        int totalAttached = attached.values().stream().mapToInt(Integer::intValue).sum();
        int leftover = totalAttached - typedNeeded;

        return leftover >= requirementFor(Type.COLORLESS);
    }
}
