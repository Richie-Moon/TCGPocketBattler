package com.tcgpocket.effect;

import com.tcgpocket.energy.Type;
import com.tcgpocket.number.INumber;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ITarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Attaches energy drawn at random from the controller's discard pile —
 * Lusamine's "attach 2 random Energy from your discard pile to that Pokemon".
 *
 * <p>The sibling of {@link AttachFromDiscard}, which names a type. Takes what
 * is there, up to the amount; an empty pile is {@link EffectOutcome#NO_OP}.
 */
public record AttachRandomFromDiscard(INumber amount, ITarget target) implements IEffect {

    public AttachRandomFromDiscard {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        Side side = context.controller();
        List<Type> pile = new ArrayList<>();
        side.discardedEnergy().forEach((type, count) -> pile.addAll(Collections.nCopies(count, type)));
        int taken = Math.min(amount.evaluate(context), pile.size());
        if (taken <= 0) {
            return EffectOutcome.NO_OP;
        }

        for (int i = 0; i < taken; i++) {
            Type type = pile.remove(context.battle().rng().nextInt(pile.size()));
            side.takeDiscardedEnergy(type, 1);
            Energies.attachMoved(context, resolved.get(), type, 1);
        }
        return EffectOutcome.APPLIED;
    }
}
