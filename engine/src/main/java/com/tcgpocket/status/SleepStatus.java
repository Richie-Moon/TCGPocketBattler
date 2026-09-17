package com.tcgpocket.status;

import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.RemoveStatus;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.ITrigger;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnEnd;

import java.util.List;

/**
 * Asleep: cannot attack or retreat, and flips at the end of every turn to wake
 * up.
 *
 * <p>The half that stops it attacking is not here — it is the legality check on
 * {@code Action} and {@code RetreatAction}. A rule about what a player may
 * <em>choose</em> belongs to legal-move generation, not to a reaction.
 */
public record SleepStatus() implements IStatus {

    @Override
    public StatusCategory category() {
        return StatusCategory.SPECIAL;
    }

    @Override
    public List<ITrigger> triggers() {
        return List.of(new Trigger(
                TurnEnd.class,
                new For(new IsActive(), new Self()),
                new Attempt(List.of(
                        new FlipN(new Literal(1)),
                        new ConditionalEffect(
                                new LastCoinTossHeads(),
                                new RemoveStatus(new Self(), new SleepStatus()))))));
    }
}
