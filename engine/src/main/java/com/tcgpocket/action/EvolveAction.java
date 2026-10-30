package com.tcgpocket.action;

import com.tcgpocket.card.EvolutionLock;
import com.tcgpocket.card.EvolvesAs;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.EffectOutcome;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;
import com.tcgpocket.trigger.Evolved;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Plays an evolution from hand onto a Pokemon already in play.
 *
 * <p>Gated on the target having been played on an <em>earlier</em> turn, which
 * is the rule that stops a Basic being played and evolved in one go.
 *
 * @param evolution the card in hand; a runtime instance, because legal-move
 *                  generation builds this action per candidate card
 */
public record EvolveAction(CardInstance evolution, ITarget onto) implements IAction {

    public EvolveAction {
        Objects.requireNonNull(evolution, "evolution");
        Objects.requireNonNull(onto, "onto");
    }

    @Override
    public boolean isLegal(ResolutionContext context) {
        if (!(evolution.definition() instanceof PokemonCard evolving) || evolving.isBasic()) {
            return false;
        }
        if (!context.controller().hand().contains(evolution)) {
            return false;
        }

        Optional<PokemonInPlay> target = onto.resolve(context);
        if (target.isEmpty()) {
            return false;
        }

        PokemonInPlay pokemon = target.get();
        boolean rightSpecies = evolving.evolvesFrom()
                .map(species -> species.equals(pokemon.definition().name()) || evolvesAs(pokemon, species, context))
                .orElse(false);

        boolean locked = context.opponent().standingRules().stream()
                .anyMatch(held -> held.rule() instanceof EvolutionLock lock && lock.appliesTo().evaluate(pokemon));

        return rightSpecies && !locked && pokemon.turnPlayed() < context.battle().turn();
    }

    private static boolean evolvesAs(PokemonInPlay pokemon, String species, ResolutionContext context) {
        return context.controller().standingRules().stream()
                .anyMatch(held -> held.holder() == pokemon
                        && held.rule() instanceof EvolvesAs rule && rule.species().equals(species));
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        if (!isLegal(context)) {
            return AttemptResult.failure("cannot evolve", List.of());
        }

        PokemonInPlay pokemon = onto.resolve(context).orElseThrow();
        PokemonCard was = pokemon.definition();
        context.controller().removeFromHand(evolution);
        pokemon.evolveInto(evolution, context.battle().turn());

        TriggerDispatcher.dispatch(context.battle(), new Evolved(was, pokemon));
        return AttemptResult.success(List.of(EffectOutcome.APPLIED));
    }
}
