package com.tcgpocket.effect;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.EvolvesFrom;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ITarget;
import com.tcgpocket.trigger.Evolved;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Puts a random card that evolves from the target from its owner's deck onto
 * it — Porygon2's Buggy Evolution.
 *
 * <p>The card text is the permission, so the turn rules on evolving do not
 * apply. With no such card in the deck it is a {@link EffectOutcome#NO_OP}.
 */
public record EvolveFromDeck(ITarget onto) implements IEffect {

    public EvolveFromDeck {
        Objects.requireNonNull(onto, "onto");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = onto.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        PokemonInPlay pokemon = resolved.get();
        Side owner = pokemon.owner();
        EvolvesFrom evolvesFromIt = new EvolvesFrom(pokemon.definition().name());
        List<CardInstance> candidates = owner.deck().stream().filter(evolvesFromIt::evaluate).toList();
        if (candidates.isEmpty()) {
            return EffectOutcome.NO_OP;
        }

        CardInstance chosen = candidates.get(context.battle().rng().nextInt(candidates.size()));
        PokemonCard was = pokemon.definition();
        owner.removeFromDeck(chosen);
        pokemon.evolveInto(chosen, context.battle().turn());
        TriggerDispatcher.dispatch(context.battle(), new Evolved(was, pokemon));
        return EffectOutcome.APPLIED;
    }
}
