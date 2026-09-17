package com.tcgpocket.action;

import com.tcgpocket.card.CardTag;
import com.tcgpocket.condition.IsAsleep;
import com.tcgpocket.condition.IsParalyzed;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.EffectOutcome;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.ActiveModifier;
import com.tcgpocket.state.ModifierKind;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ITarget;
import com.tcgpocket.trigger.Retreated;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Swaps the active Pokemon out, paying its retreat cost in energy.
 *
 * <p>The retreating Pokemon sheds its statuses on the way to the bench, which
 * is what makes retreating a way out of a special condition — and, in turn,
 * why Asleep and Paralyzed forbid retreating in the first place.
 *
 * <p>A Fossil never retreats: "This card can't retreat" is printed on every
 * one, so it is read off the {@link CardTag#FOSSIL} tag rather than repeated
 * as a modifier on each card.
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
        if (retreating.definition().tags().contains(CardTag.FOSSIL)
                || new IsAsleep().evaluate(retreating) || new IsParalyzed().evaluate(retreating)
                || retreating.hasModifier(ModifierKind.CANNOT_RETREAT, context.battle().turn())) {
            return false;
        }

        return retreatCostOf(context, retreating).isSatisfiedBy(retreating.attachedEnergy())
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
     *
     * <p>Less any live {@link ModifierKind#REDUCE_RETREAT_COST}, taken off the
     * colorless part and never below zero. Every printed retreat cost is
     * colorless, so that is all of it.
     */
    private static EnergyCost retreatCostOf(ResolutionContext context, PokemonInPlay retreating) {
        int turn = context.battle().turn();
        int reduction = retreating.modifiers().stream()
                .filter(modifier -> modifier.kind() == ModifierKind.REDUCE_RETREAT_COST)
                .filter(modifier -> modifier.isActiveOn(turn))
                .mapToInt(ActiveModifier::amount)
                .sum();
        Map<Type, Integer> requirements = new HashMap<>(retreating.definition().retreatCost().requirements());
        requirements.computeIfPresent(Type.COLORLESS, (type, count) -> Math.max(0, count - reduction));
        return new EnergyCost(requirements);
    }

    private static void payRetreatCost(ResolutionContext context, PokemonInPlay retreating) {
        int cost = retreatCostOf(context, retreating).total();
        for (int i = 0; i < cost; i++) {
            List<Type> units = retreating.attachedEnergy().entrySet().stream()
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
