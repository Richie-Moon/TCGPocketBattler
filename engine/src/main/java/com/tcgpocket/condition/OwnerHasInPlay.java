package com.tcgpocket.condition;

import com.tcgpocket.state.PokemonInPlay;

import java.util.Objects;

/**
 * Whether the Pokemon's owner has a Pokemon in play that meets
 * {@code condition} — the Link abilities' "if you have Arceus or Arceus ex in
 * play".
 *
 * <p>Subjected on the Pokemon rather than the context, so a standing rule
 * ({@code NoRetreatCost}, {@code AttackCostReduction}) can ask it of its
 * holder, and it answers for the holder's side whoever's turn it is. A trigger
 * lifts it with {@code For(..., Self)}. The subject itself counts.
 */
public record OwnerHasInPlay(ICondition<? super PokemonInPlay> condition) implements ICondition<PokemonInPlay> {

    public OwnerHasInPlay {
        Objects.requireNonNull(condition, "condition");
    }

    @Override
    public boolean evaluate(PokemonInPlay subject) {
        return subject.owner().inPlay().stream().anyMatch(condition::evaluate);
    }
}
