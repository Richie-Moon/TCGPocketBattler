package com.tcgpocket.status;

import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.Fail;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.number.Literal;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.AttackDeclared;
import com.tcgpocket.trigger.ITrigger;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * Confused: flip when attacking, and on tails the attack does nothing.
 *
 * <p>The one status that cancels something rather than adding to it, and the
 * reason a trigger runs an {@code IAttempt} rather than a bare effect. The
 * failure propagates out through {@code DispatchResult.vetoed()} and
 * {@code Action.execute} abandons the attack — no engine special case anywhere.
 */
public record ConfusionStatus() implements IStatus {

    @Override
    public StatusCategory category() {
        return StatusCategory.SPECIAL;
    }

    @Override
    public List<ITrigger> triggers() {
        return List.of(new Trigger(
                AttackDeclared.class,
                new EventConcerns(new Self()),
                new Attempt(List.of(
                        new FlipN(new Literal(1)),
                        new ConditionalEffect(
                                new Not<ResolutionContext>(new LastCoinTossHeads()),
                                new Fail())))));
    }
}
