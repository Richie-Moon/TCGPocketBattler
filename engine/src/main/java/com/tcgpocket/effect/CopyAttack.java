package com.tcgpocket.effect;

import com.tcgpocket.action.Action;
import com.tcgpocket.condition.HasEnergy;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.List;
import java.util.Optional;

/**
 * Uses one of the opponent's Pokemon's attacks as this one — "Choose 1 of your
 * opponent's Pokemon's attacks and use it as this attack".
 *
 * <p>The controller picks; the copied attack's {@link IAttempt} then resolves
 * with the same context, so {@code Self} is still the copying Pokemon. Its cost
 * is checked against the copier's energy and, unpaid, the attack does nothing:
 * a {@link EffectOutcome#NO_OP}, as is an opponent with nothing to copy.
 *
 * <p>Other copy attacks are never offered, so two copiers cannot copy each
 * other forever.
 */
public record CopyAttack() implements IEffect {

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> copier = context.source();
        if (copier.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        List<Action> attacks = context.opponent().inPlay().stream()
                .flatMap(pokemon -> pokemon.definition().actions().stream())
                .filter(Action.class::isInstance)
                .map(Action.class::cast)
                .filter(attack -> !(attack.attempt() instanceof Attempt(List<IEffect> effects)
                        && effects.stream().anyMatch(CopyAttack.class::isInstance)))
                .toList();
        if (attacks.isEmpty()) {
            return EffectOutcome.NO_OP;
        }

        Action chosen = attacks.size() == 1 ? attacks.get(0) : context.controller().player().choose(
                new Decision<>("Choose an attack to copy", attacks, context.controller(), context));
        if (!new HasEnergy(chosen.cost()).evaluate(copier.get())) {
            return EffectOutcome.NO_OP;
        }

        // A copied attempt that failed after changing the board still changed
        // it, so it reports APPLIED rather than break the all-or-nothing rule.
        AttemptResult result = chosen.attempt().execute(context);
        if (result.changedTheBoard()) {
            return EffectOutcome.APPLIED;
        }
        return result.failed() ? EffectOutcome.FAILED : EffectOutcome.NO_OP;
    }
}
