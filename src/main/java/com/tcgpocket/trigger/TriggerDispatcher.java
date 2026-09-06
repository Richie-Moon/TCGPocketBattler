package com.tcgpocket.trigger;

import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.StadiumCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.state.Battle;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.status.IStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds every trigger on the board and fires the ones that match.
 *
 * <h2>Collection, not registration</h2>
 *
 * <p>The board is walked afresh on every dispatch instead of cards subscribing
 * as they enter play and unsubscribing as they leave. A board is about ten
 * cards, so the cost is irrelevant, and it makes the whole class of
 * stale-listener bug impossible: a Tool that was just discarded cannot fire,
 * and a Pokemon that just evolved brings its new ability along for free.
 *
 * <p>The walk is a snapshot. A trigger that changes the board — Burn removing
 * itself is the ordinary case — cannot disturb the iteration, and cannot add a
 * listener that then fires for the event already in flight.
 *
 * <h2>Order</h2>
 *
 * <p>Fixed, so a seeded game replays identically: attacker's side then
 * defender's, active before bench, and per Pokemon its statuses (in the order
 * they were applied), then its Tool, then its passive ability. The Stadium
 * goes last, as it belongs to neither board.
 */
public final class TriggerDispatcher {

    /**
     * How deep a chain of triggers firing triggers may go.
     *
     * <p>Two cards that retaliate against damage will otherwise volley
     * for ever. Real chains are one or two deep, so hitting this means a loop.
     */
    private static final int MAX_DEPTH = 16;

    /**
     * Re-entrancy depth. Static and unsynchronised: a battle is single-threaded
     * by construction — the seeded {@code RandomSource} would not be
     * reproducible otherwise.
     */
    private static int depth;

    private TriggerDispatcher() {
    }

    /**
     * Announces an event and runs whatever listens for it.
     *
     * <p>Callers that can be cancelled check {@link DispatchResult#vetoed()};
     * everyone else may ignore the result entirely.
     */
    public static DispatchResult dispatch(Battle battle, GameEvent event) {
        if (depth >= MAX_DEPTH) {
            return DispatchResult.nothing(event);
        }

        depth++;
        try {
            List<AttemptResult> results = new ArrayList<>();
            for (TriggerSource source : collect(battle)) {
                if (source.trigger().appliesTo(event)) {
                    results.add(source.trigger().fire(source.contextFor(battle, event)));
                }
            }
            return new DispatchResult(event, results);
        } finally {
            depth--;
        }
    }

    /** Every trigger currently on the board, paired with where it came from. */
    public static List<TriggerSource> collect(Battle battle) {
        List<TriggerSource> found = new ArrayList<>();

        for (Side side : List.of(battle.attacker(), battle.defender())) {
            for (PokemonInPlay pokemon : side.inPlay()) {
                collectFrom(found, pokemon);
            }
        }

        battle.stadium().ifPresent(stadium -> {
            if (stadium.definition() instanceof StadiumCard card) {
                card.triggers().forEach(
                        trigger -> found.add(TriggerSource.unheld(trigger, stadium.owner())));
            }
        });

        return found;
    }

    private static void collectFrom(List<TriggerSource> found, PokemonInPlay pokemon) {
        for (IStatus status : pokemon.statuses()) {
            held(found, status.triggers(), pokemon);
        }

        pokemon.tool().ifPresent(tool -> {
            if (tool.definition() instanceof ToolCard card) {
                held(found, card.triggers(), pokemon);
            }
        });

        if (pokemon.definition().ability().orElse(null) instanceof PassiveAbility passive) {
            held(found, passive.triggers(), pokemon);
        }
    }

    private static void held(List<TriggerSource> found, List<ITrigger> triggers, PokemonInPlay holder) {
        triggers.forEach(trigger -> found.add(TriggerSource.heldBy(trigger, holder)));
    }
}
