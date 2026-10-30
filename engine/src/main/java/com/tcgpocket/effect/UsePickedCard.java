package com.tcgpocket.effect;

import com.tcgpocket.action.IAction;
import com.tcgpocket.action.PlayCardAction;
import com.tcgpocket.player.Decision;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;

import java.util.List;
import java.util.Optional;

/**
 * Uses the effect of the card {@link LookAtRandomCard} picked as this card's own
 * — the second half of Penny.
 *
 * <p>The printed actions resolve with this context, so "your" still means the
 * player of this card. A card that would be dragged onto a Pokemon asks for the
 * target here, among those it could legally be dragged onto. No picked card, or
 * one whose effect can't be used right now, is a {@link EffectOutcome#NO_OP}.
 */
public record UsePickedCard() implements IEffect {

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<CardInstance> picked = context.scope().pickedCard();
        if (picked.isEmpty()) {
            return EffectOutcome.NO_OP;
        }

        List<IAction> actions = picked.get().definition().actions();
        List<Optional<PokemonInPlay>> drops = PlayCardAction.playedOnto(picked.get())
                .map(dragged -> dragged.candidates(context).stream().map(Optional::of).toList())
                .orElseGet(() -> List.of(Optional.empty()));
        List<Optional<PokemonInPlay>> legal = drops.stream()
                .filter(drop -> actions.stream().allMatch(action -> action.isLegal(context.withPlayTarget(drop))))
                .toList();
        if (legal.isEmpty()) {
            return EffectOutcome.NO_OP;
        }

        Optional<PokemonInPlay> drop = legal.size() == 1 ? legal.get(0)
                : Optional.of(context.controller().player().choose(new Decision<>(
                        "Choose a Pokémon for " + picked.get().definition().name(),
                        legal.stream().map(Optional::orElseThrow).toList(), context.controller(), context)));

        // As in CopyAttack: a used card that failed after changing the board still changed it.
        boolean changed = false;
        boolean failed = false;
        for (IAction action : actions) {
            AttemptResult result = action.execute(context.withPlayTarget(drop));
            changed |= result.changedTheBoard();
            failed |= result.failed();
        }
        if (changed) {
            return EffectOutcome.APPLIED;
        }
        return failed ? EffectOutcome.FAILED : EffectOutcome.NO_OP;
    }
}
