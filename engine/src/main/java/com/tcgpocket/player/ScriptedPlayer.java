package com.tcgpocket.player;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * Replays a fixed list of answers, given as indices into the offered options.
 *
 * <p>This is what makes rule tests deterministic and readable. It fails loudly
 * when the game asks a question the script did not anticipate, rather than
 * silently defaulting — a surprise question means the test is not exercising
 * what its author thought it was.
 */
public final class ScriptedPlayer implements IPlayer {

    private final String name;
    private final Deque<Integer> answers;

    public ScriptedPlayer(String name, List<Integer> answers) {
        this.name = Objects.requireNonNull(name, "name");
        this.answers = new ArrayDeque<>(answers);
    }

    public ScriptedPlayer(String name, Integer... answers) {
        this(name, List.of(answers));
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public <T> T choose(Decision<T> decision) {
        if (answers.isEmpty()) {
            throw new IllegalStateException(
                    name + " was asked \"" + decision.prompt() + "\" but the script has run out");
        }

        int index = answers.removeFirst();
        if (index < 0 || index >= decision.options().size()) {
            throw new IllegalStateException(
                    name + " was scripted to pick option " + index + " of \"" + decision.prompt()
                            + "\" but only " + decision.options().size() + " were offered");
        }
        return decision.options().get(index);
    }

    /** How many scripted answers are left unused. */
    public int remaining() {
        return answers.size();
    }
}
