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
 * Blocks the effects of the other side's attacks on the target, for a number
 * of turns — the "and effects of" half of Dig and Dive.
 *
 * <p>A node of its own rather than a flag on {@link PreventDamage}: the two
 * halves are separate card text elsewhere ("prevent all effects of attacks,
 * except damage"), and composing them costs one more line on the cards that
 * print both. The effects that honour it ask {@code ResolutionContext.shields}.
 */
public record PreventEffects(ITarget target, INumber duration) implements IDurationEffect {

    public PreventEffects {
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
        resolved.get().addModifier(new ActiveModifier(ModifierKind.PREVENT_EFFECTS, 0, expiry));
        return EffectOutcome.APPLIED;
    }
}
