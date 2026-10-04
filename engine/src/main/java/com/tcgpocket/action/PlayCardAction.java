package com.tcgpocket.action;

import com.tcgpocket.card.IPlayableCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.card.SupporterLock;
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
import java.util.OptionalInt;

/**
 * Plays a card from hand.
 *
 * <p>A Basic Pokemon goes to the bench, and so does a Fossil, which plays as
 * one; anything else runs its printed actions and is discarded.
 *
 * <p>A card printed with {@link PlayedOnto} is dragged onto a Pokemon, and
 * {@code onto} is where it was dropped. So "Misty onto Lapras" and "Misty onto
 * Starmie" are two moves, and the choice is settled before anything resolves;
 * {@link #all} lists them. A Basic is likewise dropped into a Bench slot, one
 * move per empty slot.
 *
 * @param onto      the Pokemon this was dragged onto; present exactly when the
 *                  card is played that way
 * @param benchSlot the Bench slot a Basic goes into; present exactly when the
 *                  card is one
 */
public record PlayCardAction(CardInstance card, Optional<PokemonInPlay> onto, OptionalInt benchSlot) implements IAction {

    public PlayCardAction {
        Objects.requireNonNull(card, "card");
        Objects.requireNonNull(onto, "onto");
        Objects.requireNonNull(benchSlot, "benchSlot");
    }

    /** Played without dragging it onto anything. */
    public PlayCardAction(CardInstance card) {
        this(card, Optional.empty(), OptionalInt.empty());
    }

    /** Dragged onto a Pokemon. */
    public PlayCardAction(CardInstance card, PokemonInPlay onto) {
        this(card, Optional.of(onto), OptionalInt.empty());
    }

    /** A Basic, into a Bench slot. */
    public PlayCardAction(CardInstance card, int benchSlot) {
        this(card, Optional.empty(), OptionalInt.of(benchSlot));
    }

    /**
     * Every legal way to play this card right now: one move per Pokemon it can
     * be dragged onto, one per empty Bench slot for a Basic, or the single move
     * for a card that is neither.
     */
    public static List<PlayCardAction> all(CardInstance card, ResolutionContext context) {
        List<PlayCardAction> moves = card.definition() instanceof IPlayableCard
                ? context.controller().emptyBenchSlots().stream().map(slot -> new PlayCardAction(card, slot)).toList()
                : playedOnto(card)
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

        if (card.definition() instanceof IPlayableCard playable) {
            return onto.isEmpty() && playable.asPokemon().isBasic()
                    && benchSlot.isPresent() && side.emptyBenchSlots().contains(benchSlot.getAsInt());
        }
        if (benchSlot.isPresent()) {
            return false;
        }

        if (card.definition() instanceof SupporterCard
                && (side.supporterPlayedThisTurn() || side.supportersLocked(context.battle().turn())
                        || supportersLockedByRule(context))) {
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

        if (card.definition() instanceof IPlayableCard playable) {
            side.addToBench(new PokemonInPlay(
                    card.instanceId(), playable, side, Zone.BENCH, context.battle().turn()), benchSlot.getAsInt());
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

    /** A {@link SupporterLock} the opponent holds, whose holder currently meets its condition. */
    private static boolean supportersLockedByRule(ResolutionContext context) {
        return context.opponent().standingRules().stream()
                .anyMatch(held -> held.rule() instanceof SupporterLock lock && lock.whileHolder().evaluate(held.holder()));
    }
}
