package com.tcgpocket.status;

import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.ITrigger;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnEnd;

import java.util.List;

/** Poisoned: 10 damage at the end of every turn, and it never wears off. */
public record PoisonStatus() implements IStatus {

    /** Damage a poisoned Pokemon takes each turn. */
    public static final int DAMAGE = 10;

    @Override
    public StatusCategory category() {
        return StatusCategory.DAMAGE_OVER_TIME;
    }

    @Override
    public List<ITrigger> triggers() {
        return List.of(new Trigger(
                TurnEnd.class,
                // Conditions only ever sit on the Active Pokemon, but an effect
                // can target the bench directly, and a tick there would be wrong.
                new For(new IsActive(), new Self()),
                new Attempt(List.of(new PlaceDamage(new Literal(DAMAGE), new Self())))));
    }
}
