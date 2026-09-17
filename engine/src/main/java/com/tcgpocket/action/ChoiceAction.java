package com.tcgpocket.action;

import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;

import java.util.List;
import java.util.Objects;

/**
 * "Choose one:" — the branch is picked when the card resolves, not when it was
 * built.
 *
 * <p>The chooser is a {@link ISideTarget} rather than always the controller,
 * because plenty of cards make the <em>opponent</em> pick.
 *
 * <p>Only legal branches are offered, so a choice whose every branch is
 * unavailable is itself illegal rather than presenting a dead menu.
 */
public record ChoiceAction(List<IAction> choices, ISideTarget chooser, String prompt)
        implements IAction {

    public ChoiceAction {
        Objects.requireNonNull(chooser, "chooser");
        Objects.requireNonNull(prompt, "prompt");
        choices = List.copyOf(choices);
        if (choices.isEmpty()) {
            throw new IllegalArgumentException("a choice needs at least one branch: " + prompt);
        }
    }

    @Override
    public boolean isLegal(ResolutionContext context) {
        return !legalChoices(context).isEmpty();
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        List<IAction> available = legalChoices(context);
        if (available.isEmpty()) {
            return AttemptResult.failure("no branch of \"" + prompt + "\" is available", List.of());
        }

        Side deciding = chooser.resolve(context);
        IAction chosen = deciding.player()
                .choose(new Decision<>(prompt, available, deciding, context));

        return chosen.execute(context);
    }

    private List<IAction> legalChoices(ResolutionContext context) {
        return choices.stream().filter(choice -> choice.isLegal(context)).toList();
    }
}
