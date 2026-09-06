package com.tcgpocket.status;

import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.effect.RemoveStatus;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.ITrigger;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnEnd;

import java.util.List;

/**
 * Burned: 20 damage at the end of every turn, then a coin flip — heads and it
 * goes out.
 *
 * <p>The damage comes first and unconditionally, so a Pokemon the burn knocks
 * out is knocked out whether or not the flip would have cured it.
 */
public record BurnStatus() implements IStatus {

    /** Damage a burned Pokemon takes each turn. */
    public static final int DAMAGE = 20;

    @Override
    public StatusCategory category() {
        return StatusCategory.DAMAGE_OVER_TIME;
    }

    @Override
    public List<ITrigger> triggers() {
        return List.of(new Trigger(
                TurnEnd.class,
                new For(new IsActive(), new Self()),
                new Attempt(List.of(
                        new PlaceDamage(new Literal(DAMAGE), new Self()),
                        new FlipN(new Literal(1)),
                        new ConditionalEffect(
                                new LastCoinTossHeads(),
                                new RemoveStatus(new Self(), new BurnStatus()))))));
    }
}
