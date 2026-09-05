package com.tcgpocket.action;

import com.tcgpocket.condition.HasEnergy;
import com.tcgpocket.condition.IsAsleep;
import com.tcgpocket.condition.IsParalyzed;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.IAttempt;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

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

    @Override
    public AttemptResult execute(ResolutionContext context) {
        return attempt.execute(context);
    }
}
