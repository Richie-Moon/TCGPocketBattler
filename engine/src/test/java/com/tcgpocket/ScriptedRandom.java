package com.tcgpocket;

import com.tcgpocket.resolve.RandomSource;

import java.util.List;

/**
 * A {@link RandomSource} whose coin flips are written down in advance, so a
 * test about Burn going out can say "heads" instead of hunting for a seed that
 * happens to produce one.
 *
 * <p>Once the script runs out the last value repeats, rather than throwing. A
 * test that cares about the third flip should not have to know how many flips
 * came before it.
 */
public final class ScriptedRandom implements RandomSource {

    private final List<Boolean> script;
    private int position;

    private ScriptedRandom(List<Boolean> script) {
        this.script = List.copyOf(script);
    }

    public static ScriptedRandom flipping(boolean... results) {
        List<Boolean> script = new java.util.ArrayList<>(results.length);
        for (boolean result : results) {
            script.add(result);
        }
        return new ScriptedRandom(script);
    }

    public static ScriptedRandom alwaysHeads() {
        return flipping(true);
    }

    public static ScriptedRandom alwaysTails() {
        return flipping(false);
    }

    /** How many flips have been taken; lets a test assert nothing flipped at all. */
    public int flipsTaken() {
        return position;
    }

    @Override
    public boolean nextBoolean() {
        boolean result = script.get(Math.min(position, script.size() - 1));
        position++;
        return result;
    }

    /** Always the first candidate, so random targeting is reproducible too. */
    @Override
    public int nextInt(int bound) {
        return 0;
    }

    @Override
    public void shuffle(List<?> list) {
        // Deliberately nothing: a test that shuffles wants a known order.
    }
}
