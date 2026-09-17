package com.tcgpocket.player;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.Side;

import java.util.List;
import java.util.Objects;

/**
 * A question put to a player, with every legal answer.
 *
 * @param chooser who is being asked. Not always the card's controller — plenty
 *                of cards make the <em>opponent</em> choose.
 * @param context the board as it stands, so a thinking player can evaluate the
 *                options rather than picking blind
 * @param <T>     what is being chosen: an action, a Pokemon, a card
 */
public record Decision<T>(String prompt, List<T> options, Side chooser, ResolutionContext context) {

    public Decision {
        Objects.requireNonNull(prompt, "prompt");
        Objects.requireNonNull(chooser, "chooser");
        Objects.requireNonNull(context, "context");
        options = List.copyOf(options);
        if (options.isEmpty()) {
            throw new IllegalArgumentException("a decision needs at least one option: " + prompt);
        }
    }

    public boolean isForced() {
        return options.size() == 1;
    }
}
