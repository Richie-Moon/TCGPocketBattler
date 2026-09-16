package com.tcgpocket.action;

import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.IAttempt;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.IMultiTarget;

import java.util.List;
import java.util.Objects;

/**
 * A Trainer played by dragging it onto one of your Pokemon — Misty, Erika,
 * Brock.
 *
 * <p>The target is part of the move, not a question asked mid-resolution:
 * {@link PlayCardAction} carries it, one move per candidate, and the attempt
 * reads it back through {@code PlayTarget}. That is what puts Misty's choice
 * before her coin flips, and what makes her unplayable with no Water Pokemon
 * in play rather than flipping and then failing.
 *
 * @param candidates what the card may be dragged onto
 */
public record PlayedOnto(IMultiTarget candidates, String description, IAttempt attempt) implements IAction {

    public PlayedOnto {
        Objects.requireNonNull(candidates, "candidates");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(attempt, "attempt");
    }

    /** What it could be dragged onto right now. Free of side effects, as {@code isLegal} needs. */
    public List<PokemonInPlay> candidates(ResolutionContext context) {
        return candidates.resolve(context);
    }

    /** Legal only once dragged onto one of the candidates. */
    @Override
    public boolean isLegal(ResolutionContext context) {
        return context.playTarget().filter(candidates(context)::contains).isPresent();
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        if (!isLegal(context)) {
            return AttemptResult.failure("not played onto a valid target", List.of());
        }
        return attempt.execute(context);
    }
}
