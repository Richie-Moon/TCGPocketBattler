package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

/**
 * Locks the opponent out of Supporter cards for a number of turns — "your
 * opponent can't use any Supporter cards from their hand during their next
 * turn".
 *
 * <h2>Why this is not an {@code ActiveModifier}</h2>
 *
 * <p>Every other {@link IDurationEffect} installs one on a Pokemon, and this
 * one cannot: the restriction belongs to a <em>player</em>. Parking it on the
 * Pokemon that dealt it would make it die with a knockout, an evolution or a
 * retreat, and would point at the wrong side entirely. So it sets a turn stamp
 * on the {@code Side}, which is where the sibling once-per-turn Supporter rule
 * already lives, and {@code PlayCardAction.isLegal} reads both together.
 *
 * <p>There is no {@code ITarget} because there is nothing to choose: card text
 * says "your opponent", which is {@code context.opponent()} — the side opposing
 * whoever controls the resolving card, not necessarily the defender.
 */
public record PreventSupporter(INumber duration) implements IDurationEffect {

    public PreventSupporter {
        Objects.requireNonNull(duration, "duration");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        context.opponent().lockSupportersUntil(expiry);
        return EffectOutcome.APPLIED;
    }
}
