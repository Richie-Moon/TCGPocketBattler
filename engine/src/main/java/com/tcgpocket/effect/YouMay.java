package com.tcgpocket.effect;

import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.List;
import java.util.Objects;

/**
 * Asks the controller whether to apply an effect — Sylveon ex's "you may draw 2
 * cards".
 *
 * <p>An effect because a {@code Trigger} runs an {@code IAttempt}, so the action
 * level's {@code ChoiceAction} can't be reached from one. Declining is a
 * {@link EffectOutcome#NO_OP}.
 *
 * @param prompt the yes-or-no question put to the controller
 */
public record YouMay(String prompt, IEffect effect) implements IEffect {

    private static final String YES = "Yes";
    private static final String NO = "No";

    public YouMay {
        Objects.requireNonNull(prompt, "prompt");
        Objects.requireNonNull(effect, "effect");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        String chosen = context.controller().player().choose(
                new Decision<>(prompt, List.of(YES, NO), context.controller(), context));
        return chosen.equals(YES) ? effect.apply(context) : EffectOutcome.NO_OP;
    }
}
