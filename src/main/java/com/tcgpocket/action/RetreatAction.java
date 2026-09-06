package com.tcgpocket.action;

import com.tcgpocket.condition.IsAsleep;
import com.tcgpocket.condition.IsParalyzed;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.EffectOutcome;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ITarget;
import com.tcgpocket.trigger.Retreated;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Swaps the active Pokemon out, paying its retreat cost in energy.
 *
 * <p>The retreating Pokemon sheds its statuses on the way to the bench, which
 * is what makes retreating a way out of a special condition — and, in turn,
 * why Asleep and Paralyzed forbid retreating in the first place.
 */
public record RetreatAction(ITarget replacement) implements IAction {

    public RetreatAction {
        Objects.requireNonNull(replacement, "replacement");
    }

    @Override
    public boolean isLegal(ResolutionContext context) {
        Side side = context.controller();
        Optional<PokemonInPlay> active = side.active();
        if (active.isEmpty() || side.bench().isEmpty() || side.retreatedThisTurn()) {
            return false;
        }

        PokemonInPlay retreating = active.get();
        if (new IsAsleep().evaluate(retreating) || new IsParalyzed().evaluate(retreating)) {
            return false;
        }

        return retreatCostOf(retreating).isSatisfiedBy(retreating.attachedEnergy())
                && replacement.resolve(context).filter(side.bench()::contains).isPresent();
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        if (!isLegal(context)) {
            return AttemptResult.failure("cannot retreat", List.of());
        }

        Side side = context.controller();
        PokemonInPlay retreating = side.active().orElseThrow();
        PokemonInPlay incoming = replacement.resolve(context).orElseThrow();

        payRetreatCost(context, retreating);

        side.removeFromBench(incoming);
        retreating.clearTemporaryState();
        side.addToBench(retreating);
        side.setActive(incoming);
        side.markRetreated();

        TriggerDispatcher.dispatch(context.battle(), new Retreated(retreating, incoming));
        return AttemptResult.success(List.of(EffectOutcome.APPLIED));
    }

    /**
     * The printed retreat cost, read as an {@link EnergyCost}.
     *
     * <p>A retreat cost is a cost like any other — colorless in every Pocket
     * printing so far, but the same wildcard rule applies either way, so it is
     * worth going through the type that already knows the rule rather than
     * comparing totals.
     */
    private static EnergyCost retreatCostOf(PokemonInPlay retreating) {
        return retreating.definition().retreatCost();
    }

    private static void payRetreatCost(ResolutionContext context, PokemonInPlay retreating) {
        int cost = retreatCostOf(retreating).total();
        for (int i = 0; i < cost; i++) {
            List<com.tcgpocket.energy.Type> units = retreating.attachedEnergy().entrySet().stream()
                    .flatMap(entry -> java.util.stream.IntStream.range(0, entry.getValue())
                            .mapToObj(ignored -> entry.getKey()))
                    .toList();
            if (units.isEmpty()) {
                return;
            }
            retreating.discardEnergy(units.get(context.battle().rng().nextInt(units.size())), 1);
        }
    }
}
