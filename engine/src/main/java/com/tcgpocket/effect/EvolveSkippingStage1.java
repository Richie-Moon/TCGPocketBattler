package com.tcgpocket.effect;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.player.Decision;
import com.tcgpocket.pool.CardPool;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;
import com.tcgpocket.trigger.Evolved;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Puts a Stage 2 from the controller's hand straight onto a Basic, skipping the
 * Stage 1 — Rare Candy.
 *
 * <p>A Stage 2 names only its Stage 1, so the Basic underneath is found by
 * looking that Stage 1 up in {@link CardPool} by name. That is the one place
 * the model reaches into the pool; a Stage 2 carrying its Basic's name would
 * remove it. The turn rules ("not on your first turn", "not on a Basic put into
 * play this turn") are legality, and live in {@code CanSkipStage1}.
 *
 * <p>FAILED with no such Stage 2 in hand, as nothing could happen.
 */
public record EvolveSkippingStage1(ITarget onto) implements IEffect {

    public EvolveSkippingStage1 {
        Objects.requireNonNull(onto, "onto");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = onto.resolve(context);
        if (resolved.isEmpty()) {
            return EffectOutcome.FAILED;
        }
        PokemonInPlay basic = resolved.get();
        List<CardInstance> candidates = stage2sInHand(basic);
        if (candidates.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        CardInstance chosen = context.controller().player().choose(
                new Decision<>("Choose a Stage 2 Pokémon to evolve into", candidates, context.controller(), context));
        PokemonCard was = basic.definition();
        basic.owner().removeFromHand(chosen);
        basic.evolveInto(chosen, context.battle().turn());
        TriggerDispatcher.dispatch(context.battle(), new Evolved(was, basic));
        return EffectOutcome.APPLIED;
    }

    /** The Stage 2 cards in the Basic's owner's hand that evolve from it by way of a Stage 1. */
    public static List<CardInstance> stage2sInHand(PokemonInPlay basic) {
        if (!basic.definition().isBasic()) {
            return List.of();
        }
        String species = basic.definition().name();
        return basic.owner().hand().stream()
                .filter(card -> card.definition() instanceof PokemonCard pokemon && pokemon.stage() == 2
                        && pokemon.evolvesFrom().flatMap(EvolveSkippingStage1::basicUnder).equals(Optional.of(species)))
                .toList();
    }

    /** What the named Stage 1 evolves from. */
    private static Optional<String> basicUnder(String stage1) {
        return CardPool.all().stream()
                .flatMap(card -> card instanceof PokemonCard pokemon && pokemon.stage() == 1 && pokemon.name().equals(stage1)
                        ? pokemon.evolvesFrom().stream()
                        : Stream.empty())
                .findFirst();
    }
}
