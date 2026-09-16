package com.tcgpocket.action;

import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.EffectOutcome;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.Zone;
import com.tcgpocket.trigger.CardPlayed;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Plays a card from hand.
 *
 * <p>A Basic Pokemon goes to the bench; anything else runs its printed
 * actions and is discarded.
 *
 * <p>A card printed with {@link PlayedOnto} is dragged onto a Pokemon, and
 * {@code onto} is where it was dropped. So "Misty onto Lapras" and "Misty onto
 * Starmie" are two moves, and the choice is settled before anything resolves;
 * {@link #all} lists them.
 *
 * @param onto the Pokemon this was dragged onto; present exactly when the card
 *             is played that way
 */
public record PlayCardAction(CardInstance card, Optional<PokemonInPlay> onto) implements IAction {

    public PlayCardAction {
        Objects.requireNonNull(card, "card");
        Objects.requireNonNull(onto, "onto");
    }

    /** Played without dragging it onto anything. */
    public PlayCardAction(CardInstance card) {
        this(card, Optional.empty());
    }

    /** Dragged onto a Pokemon. */
    public PlayCardAction(CardInstance card, PokemonInPlay onto) {
        this(card, Optional.of(onto));
    }

    /**
     * Every legal way to play this card right now: one move per Pokemon it can
     * be dragged onto, or the single move for a card that is not dragged.
     */
    public static List<PlayCardAction> all(CardInstance card, ResolutionContext context) {
        List<PlayCardAction> moves = playedOnto(card)
                .map(dragged -> dragged.candidates(context).stream()
                        .map(pokemon -> new PlayCardAction(card, pokemon))
                        .toList())
                .orElseGet(() -> List.of(new PlayCardAction(card)));
        return moves.stream().filter(move -> move.isLegal(context)).toList();
    }

    @Override
    public boolean isLegal(ResolutionContext context) {
        Side side = context.controller();
        if (!side.hand().contains(card)) {
            return false;
        }

        if (card.definition() instanceof PokemonCard pokemon) {
            return onto.isEmpty() && pokemon.isBasic() && !side.benchIsFull();
        }

        if (card.definition() instanceof SupporterCard
                && (side.supporterPlayedThisTurn() || side.supportersLocked(context.battle().turn()))) {
            return false;
        }

        // Dropping a card that is not dragged onto a Pokemon, or not dropping
        // one that is, is not a move. The printed actions then answer for the
        // rest: a PlayedOnto checks the drop, a WithPrecondition its condition.
        if (playedOnto(card).isPresent() != onto.isPresent()) {
            return false;
        }
        ResolutionContext played = context.withPlayTarget(onto);
        return card.definition().actions().stream().allMatch(action -> action.isLegal(played));
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        if (!isLegal(context)) {
            return AttemptResult.failure("cannot play " + card.definition().name(), List.of());
        }

        Side side = context.controller();
        side.removeFromHand(card);
        TriggerDispatcher.dispatch(context.battle(), new CardPlayed(side, card));

        if (card.definition() instanceof PokemonCard pokemon) {
            side.addToBench(new PokemonInPlay(
                    card.instanceId(), pokemon, side, Zone.BENCH, context.battle().turn()));
            return AttemptResult.success(List.of(EffectOutcome.APPLIED));
        }

        if (card.definition() instanceof SupporterCard) {
            side.markSupporterPlayed();
        }

        ResolutionContext played = context.withPlayTarget(onto);
        AttemptResult last = AttemptResult.success(List.of());
        for (IAction action : card.definition().actions()) {
            last = action.execute(played);
        }
        side.addToDiscard(card);
        return last;
    }

    private static Optional<PlayedOnto> playedOnto(CardInstance card) {
        return card.definition().actions().stream()
                .filter(PlayedOnto.class::isInstance)
                .map(PlayedOnto.class::cast)
                .findFirst();
    }
}
