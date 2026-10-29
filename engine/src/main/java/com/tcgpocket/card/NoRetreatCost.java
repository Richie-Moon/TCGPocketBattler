package com.tcgpocket.card;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;
import java.util.Optional;

/**
 * The holder has no Retreat Cost while it satisfies {@code whileHolder} —
 * Giratina's Levitate, with "has any Energy attached".
 *
 * <p>Read by {@code RetreatAction}. A rule rather than a turn-start stamp
 * because the condition changes mid-turn: attaching an Energy and then
 * retreating is the whole point of the card.
 *
 * @param retreating frees another of the holder's side's Pokemon instead —
 *                   Tatsugiri's "your Active Dondozo has no Retreat Cost";
 *                   empty frees only the holder
 */
public record NoRetreatCost(
        Optional<ICondition<? super PokemonInPlay>> retreating,
        ICondition<PokemonInPlay> whileHolder) implements IRule {

    public NoRetreatCost {
        Objects.requireNonNull(retreating, "retreating");
        Objects.requireNonNull(whileHolder, "whileHolder");
    }

    /** Only the holder's own cost. */
    public NoRetreatCost(ICondition<PokemonInPlay> whileHolder) {
        this(Optional.empty(), whileHolder);
    }

    /** Whether this frees {@code pokemon}, given who holds it. */
    public boolean frees(PokemonInPlay holder, PokemonInPlay pokemon) {
        return retreating.map(condition -> condition.evaluate(pokemon)).orElse(holder == pokemon)
                && whileHolder.evaluate(holder);
    }
}
