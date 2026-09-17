package com.tcgpocket.server;

import com.tcgpocket.player.Decision;
import com.tcgpocket.player.IPlayer;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * A player in a browser.
 *
 * <p>{@link #choose} sends the options as labels and blocks the game thread
 * until the browser answers with an index. The browser never sends a move of
 * its own, only a position in a list the engine already checked, so an answer
 * is either legal or rejected.
 *
 * <p>Each decision has an id and an answer must repeat it. That way a
 * double-click or a delayed message can't be taken as the answer to the next
 * question.
 */
final class RemotePlayer implements IPlayer {

    /** Thrown on the game thread once the game is abandoned; ends the game. */
    static final class Left extends RuntimeException {
        Left() {
            super("a player left", null, false, false);
        }
    }

    record DecisionMessage(String type, int id, String prompt, List<String> options) {
        DecisionMessage(int id, String prompt, List<String> options) {
            this("decision", id, prompt, options);
        }
    }

    private static final int LEFT = -1;

    private final String name;
    private final Consumer<Object> send;
    private final Runnable beforeEachDecision;
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final BlockingQueue<Integer> answers = new LinkedBlockingQueue<>();

    private volatile int awaiting;      // id of the open decision, 0 when none
    private volatile int optionCount;
    private volatile boolean left;

    /**
     * @param beforeEachDecision runs on the game thread before every question,
     *                           so both players see the board it is asked about
     */
    RemotePlayer(String name, Consumer<Object> send, Runnable beforeEachDecision) {
        this.name = name;
        this.send = send;
        this.beforeEachDecision = beforeEachDecision;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public <T> T choose(Decision<T> decision) {
        beforeEachDecision.run();

        List<String> labels = decision.options().stream()
                .map(option -> Labels.of(option, decision))
                .toList();
        answers.clear();
        int id = nextId.getAndIncrement();
        optionCount = labels.size();
        awaiting = id;
        if (left) {
            throw new Left();
        }
        send.accept(new DecisionMessage(id, decision.prompt(), labels));

        // ponytail: waits forever for an answer; add a turn timer that picks a default when games stall.
        int index = take();
        awaiting = 0;
        if (index == LEFT) {
            throw new Left();
        }
        return decision.options().get(index);
    }

    /**
     * Takes an answer from the browser. Called on a socket thread.
     *
     * @return why the answer was rejected, or empty if it was accepted
     */
    Optional<String> answer(int decisionId, int option) {
        int open = awaiting;
        if (open == 0 || decisionId != open) {
            return Optional.of("decision " + decisionId + " is not open");
        }
        if (option < 0 || option >= optionCount) {
            return Optional.of("option " + option + " is out of range");
        }
        answers.offer(option);
        return Optional.empty();
    }

    /** Wakes the game thread if it is waiting on this player, and makes every later question end the game. */
    void leave() {
        left = true;
        answers.offer(LEFT);
    }

    private int take() {
        try {
            return answers.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new Left();
        }
    }
}
