package com.tcgpocket.action;

import com.tcgpocket.condition.HasEnergy;
import com.tcgpocket.condition.IsAsleep;
import com.tcgpocket.condition.IsParalyzed;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.EffectOutcome;
import com.tcgpocket.effect.IAttempt;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.trigger.AttackDeclared;
import com.tcgpocket.trigger.DispatchResult;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An attack, as printed on a Pokemon.
 *
 * <p>The {@code cost} is what gates it — before this existed nothing in the
 * model could stop a Pokemon attacking with no energy attached.
 */
public record Action(String name, String description, EnergyCost cost, IAttempt attempt)
        implements IAction {

    public Action {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(cost, "cost");
        Objects.requireNonNull(attempt, "attempt");
    }

    public Action(String name, EnergyCost cost, IAttempt attempt) {
        this(name, "", cost, attempt);
    }

    /**
     * Legal when the attacking Pokemon is the active one, has the energy, and
     * is not held down by a special condition.
     */
    @Override
    public boolean isLegal(ResolutionContext context) {
        Optional<PokemonInPlay> attacker = context.source();
        if (attacker.isEmpty()) {
            return false;
        }

        PokemonInPlay pokemon = attacker.get();
        boolean heldDown = new IsAsleep().evaluate(pokemon) || new IsParalyzed().evaluate(pokemon);

        return !heldDown
                && context.controller().active().filter(pokemon::equals).isPresent()
                && new HasEnergy(cost).evaluate(pokemon);
    }

    /**
     * Announces the attack, then resolves it unless a trigger cancelled it.
     *
     * <p>The announcement happens here rather than in the turn loop so that an
     * attack is self-contained: executing one runs the whole of it, Confusion
     * included, without an engine to sequence the two halves.
     */
    @Override
    public AttemptResult execute(ResolutionContext context) {
        Optional<PokemonInPlay> attacker = context.source();
        if (attacker.isPresent()) {
            DispatchResult declared = TriggerDispatcher.dispatch(
                    context.battle(), new AttackDeclared(attacker.get(), this));
            if (declared.vetoed()) {
                return AttemptResult.failure(
                        name + " was cancelled", List.of(EffectOutcome.FAILED));
            }
        }

        return attempt.execute(context);
    }
}
