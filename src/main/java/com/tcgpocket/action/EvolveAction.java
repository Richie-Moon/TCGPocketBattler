package com.tcgpocket.action;

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
                .map(species -> species.equals(pokemon.definition().name()))
                .orElse(false);

        return rightSpecies && pokemon.turnPlayed() < context.battle().turn();
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        if (!isLegal(context)) {
            return AttemptResult.failure("cannot evolve", List.of());
        }

        PokemonInPlay pokemon = onto.resolve(context).orElseThrow();
        PokemonCard was = pokemon.definition();
        context.controller().removeFromHand(evolution);
        pokemon.evolveInto((PokemonCard) evolution.definition(), context.battle().turn());

        TriggerDispatcher.dispatch(context.battle(), new Evolved(was, pokemon));
        return AttemptResult.success(List.of(EffectOutcome.APPLIED));
    }
}
