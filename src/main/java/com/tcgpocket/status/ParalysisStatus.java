package com.tcgpocket.status;

import com.tcgpocket.condition.And;
import com.tcgpocket.condition.EventSideIs;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.RemoveStatus;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.ITrigger;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnEnd;

import java.util.List;

/**
 * Paralyzed: cannot attack or retreat, and wears off at the end of its victim's
 * own turn.
 *
 * <p>Hence the {@link EventSideIs} gate, which is the only thing separating
 * this from {@link SleepStatus}. Paralysis inflicted on your turn has to
 * survive the rest of it and cost the opponent their whole next turn; a plain
 * {@code TurnEnd} listener would cure it at once and make the condition
 * worthless.
 */
public record ParalysisStatus() implements IStatus {

    @Override
    public StatusCategory category() {
        return StatusCategory.SPECIAL;
    }

    @Override
    public List<ITrigger> triggers() {
        return List.of(new Trigger(
                TurnEnd.class,
                new And<ResolutionContext>(
                        new EventSideIs(new SelfSide()),
                        new For(new IsActive(), new Self())),
                new Attempt(List.of(new RemoveStatus(new Self(), new ParalysisStatus())))));
    }
}
