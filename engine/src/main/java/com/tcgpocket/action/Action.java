package com.tcgpocket.action;

import com.tcgpocket.card.AttackCostIncrease;
import com.tcgpocket.card.AttackCostReduction;
import com.tcgpocket.condition.HasEnergy;
import com.tcgpocket.condition.IsAsleep;
import com.tcgpocket.condition.IsParalyzed;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.EffectOutcome;
import com.tcgpocket.effect.IAttempt;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.ModifierKind;
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
     * is neither held down by a special condition nor under a
     * {@code CANNOT_ATTACK} modifier, against the printed cost adjusted by
     * {@link #colorlessAdjustment}.
     */
    @Override
    public boolean isLegal(ResolutionContext context) {
        Optional<PokemonInPlay> attacker = context.source();
        if (attacker.isEmpty()) {
            return false;
        }

        PokemonInPlay pokemon = attacker.get();
        boolean heldDown = new IsAsleep().evaluate(pokemon)
                || new IsParalyzed().evaluate(pokemon)
                || pokemon.hasModifier(ModifierKind.CANNOT_ATTACK, context.battle().turn())
                || pokemon.modifiers().stream().anyMatch(modifier -> modifier.kind() == ModifierKind.CANNOT_USE_ATTACK
                        && modifier.isActiveOn(context.battle().turn())
                        && modifier.attackName().filter(name::equals).isPresent());

        return !heldDown
                && context.controller().active().filter(pokemon::equals).isPresent()
                && new HasEnergy(cost.plusColorless(colorlessAdjustment(context, pokemon))).evaluate(pokemon);
    }

    /**
     * Colorless added to the printed cost: any {@code INCREASE_ATTACK_COST} and
     * the opponent's {@link AttackCostIncrease} (Guard Dog Visage),
     * less the side's {@code ReduceSideAttackCost} (Barry) and the attacker's
     * own {@link AttackCostReduction} (Vigor Link).
     */
    private static int colorlessAdjustment(ResolutionContext context, PokemonInPlay pokemon) {
        int turn = context.battle().turn();
        int ruleReduction = pokemon.owner().standingRules().stream()
                .filter(held -> held.holder() == pokemon
                        && held.rule() instanceof AttackCostReduction rule && rule.whileHolder().evaluate(pokemon))
                .mapToInt(held -> ((AttackCostReduction) held.rule()).colorless())
                .sum();
        int ruleIncrease = context.opponent().standingRules().stream()
                .filter(held -> held.rule() instanceof AttackCostIncrease rule && rule.whileHolder().evaluate(held.holder()))
                .mapToInt(held -> ((AttackCostIncrease) held.rule()).colorless())
                .sum();
        return pokemon.modifierTotal(ModifierKind.INCREASE_ATTACK_COST, turn)
                + ruleIncrease
                - pokemon.owner().attackCostReductionFor(pokemon, turn)
                - ruleReduction;
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

        context.scope().markAttack();
        return attempt.execute(context);
    }
}
