package com.tcgpocket.resolve;

import java.util.List;

/**
 * The outcome of one coin-flipping effect.
 *
 * <p>Holds every flip rather than just a count, because card text asks
 * different questions of the same flips: "damage per heads", "if all heads",
 * "if the last was heads".
 *
 * @param results one entry per flip, {@code true} for heads
 */
public record FlipResult(List<Boolean> results) {

    public FlipResult {
        results = List.copyOf(results);
    }

    public static FlipResult of(boolean... flips) {
        List<Boolean> list = new java.util.ArrayList<>(flips.length);
        for (boolean flip : flips) {
            list.add(flip);
        }
        return new FlipResult(list);
    }

    public int flips() {
        return results.size();
    }

    public int heads() {
        return (int) results.stream().filter(Boolean::booleanValue).count();
    }

    public int tails() {
        return flips() - heads();
    }

    /** Vacuously true when nothing was flipped. */
    public boolean allHeads() {
        return results.stream().allMatch(Boolean::booleanValue);
    }

    public boolean lastWasHeads() {
        return !results.isEmpty() && results.get(results.size() - 1);
    }
}
