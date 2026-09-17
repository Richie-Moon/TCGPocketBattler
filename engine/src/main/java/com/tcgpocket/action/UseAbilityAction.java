package com.tcgpocket.action;

import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.IAbility;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.PokemonInPlay;

import java.util.List;
import java.util.Objects;

/**
 * Uses an activated ability.
 *
 * <p>The ability resolves with its holder as the source, so {@code Self}
 * inside it means the Pokemon whose ability it is rather than whoever is
 * attacking.
 *
 * <p>Once-per-turn is tracked on the Pokemon, not the card, so two copies of
 * the same species each get a use.
 */
public record UseAbilityAction(PokemonInPlay source, IAbility ability) implements IAction {

    public UseAbilityAction {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(ability, "ability");
    }

    @Override
    public boolean isLegal(ResolutionContext context) {
        if (!(ability instanceof ActivatedAbility activated)) {
            return false;
        }
        if (source.definition().ability().filter(ability::equals).isEmpty()) {
            return false;
        }
        if (activated.oncePerTurn() && source.abilityUsedThisTurn()) {
            return false;
        }

        ResolutionContext held = context.withSource(source);
        return activated.usableWhen().evaluate(held) && activated.action().isLegal(held);
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        if (!isLegal(context)) {
            return AttemptResult.failure("cannot use " + ability.name(), List.of());
        }

        ActivatedAbility activated = (ActivatedAbility) ability;
        AttemptResult result = activated.action().execute(context.withSource(source));

        if (activated.oncePerTurn()) {
            source.markAbilityUsed();
        }
        return result;
    }
}
