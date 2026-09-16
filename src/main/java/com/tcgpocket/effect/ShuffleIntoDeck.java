package com.tcgpocket.effect;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.ITarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Shuffles a Pokemon in play, with every card under it and its Tool, into its
 * owner's deck — "your opponent shuffles their Active Pokemon into their deck".
 *
 * <p>Energy is not a card in Pocket, so it simply goes. An Active that leaves
 * is replaced from the Bench by its owner, as after {@link SwitchActive}; with
 * nothing on the Bench to replace it this {@link EffectOutcome#FAILED fails}
 * rather than leave a side without an Active.
 */
public record ShuffleIntoDeck(ITarget target) implements IEffect {

    public ShuffleIntoDeck {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        PokemonInPlay pokemon = resolved.get();
        Side owner = pokemon.owner();
        boolean wasActive = owner.active().filter(pokemon::equals).isPresent();
        if (wasActive && owner.bench().isEmpty()) {
            return EffectOutcome.FAILED;
        }

        if (wasActive) {
            List<PokemonInPlay> bench = owner.bench();
            PokemonInPlay promoted = bench.size() == 1 ? bench.get(0) : owner.player().choose(
                    new Decision<>("Choose a Pokemon to bring up", bench, owner, context));
            owner.removeFromBench(promoted);
            owner.setActive(promoted);
        } else {
            owner.removeFromBench(pokemon);
        }

        // ponytail: evolved cards lost their instances in EvolveAction, so the whole stack
        //           shares this Pokemon's id. Keep CardInstances on the stack if ids must stay unique.
        List<PokemonCard> cards = new ArrayList<>(pokemon.evolutionStack());
        cards.add(pokemon.definition());
        for (PokemonCard card : cards) {
            owner.addToDeck(new CardInstance(pokemon.instanceId(), card, owner, Zone.DECK));
        }
        pokemon.removeTool().ifPresent(owner::addToDeck);
        owner.shuffleDeck(context.battle().rng());
        return EffectOutcome.APPLIED;
    }
}
