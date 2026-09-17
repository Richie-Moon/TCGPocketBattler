package com.tcgpocket.effect;

import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.ActiveModifier;
import com.tcgpocket.state.ModifierKind;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;

import java.util.Objects;
import java.util.Optional;

/**
 * Stops the target declaring an attack, for a number of turns — "the Defending
 * Pokemon can't attack during your opponent's next turn".
 *
 * <h2>Why this is not a status</h2>
 *
 * <p>Paralysis would be the obvious home and is the wrong one. It also forbids
 * retreating, it is one of the five special conditions and so competes for the
 * single {@code SPECIAL} slot, and it cures itself at the end of its victim's
 * own turn. This forbids attacking and nothing else: the victim can still
 * retreat, attach, play Trainers and use abilities.
 *
 * <p>So it installs an {@code ActiveModifier} instead, the same way
 * {@link PreventDamage} does — which also gets the two behaviours that are
 * right for free: it expires on a turn count rather than on an event, and
 * leaving the active spot sheds it, so retreating out is the way around it.
 */
public record PreventAttack(ITarget target, INumber duration) implements IDurationEffect {

    public PreventAttack {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(duration, "duration");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        int expiry = context.battle().turn() + Math.max(0, duration.evaluate(context));
        resolved.get().addModifier(new ActiveModifier(ModifierKind.CANNOT_ATTACK, 0, expiry));
        return EffectOutcome.APPLIED;
    }
}
