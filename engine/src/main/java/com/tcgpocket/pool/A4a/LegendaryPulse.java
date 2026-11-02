package com.tcgpocket.pool.A4a;

import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.EventSideIs;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DrawCard;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnEnd;

import java.util.List;

/**
 * The ability shared by the set's three legendary beasts.
 */
final class LegendaryPulse {

    /** Entei ex, Suicune ex, Raikou ex. */
    static final PassiveAbility ABILITY = new PassiveAbility("Legendary Pulse",
            "At the end of your turn, if this Pokémon is in the Active Spot, draw a card.",
            List.of(new Trigger(TurnEnd.class,
                    new And<>(new EventSideIs(new SelfSide()), new For(new IsActive(), new Self())),
                    new Attempt(List.of(new DrawCard(new Literal(1), new SelfSide()))))));

    private LegendaryPulse() {
    }
}
