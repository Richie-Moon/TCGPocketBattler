package com.tcgpocket.card;

import com.tcgpocket.condition.ICondition;
import com.tcgpocket.state.PokemonInPlay;

import com.tcgpocket.status.IStatus;
import java.util.Objects;
import java.util.Optional;

/**
 * Each of the holder's side's Pokemon that meets {@code protects} recovers from
 * all Special Conditions and can't be affected by any — Comfey's Flower
 * Shield, with "has any Psychic Energy attached".
 *
 * <p>The one rule read in two places, because the text says two things.
 * {@code AddStatus} refuses a new status on a protected Pokemon ("can't be
 * affected"), and {@code Side.recoverProtected}, which {@code TurnEngine} runs
 * after every action, clears ones it already had ("recovers") — the moment
 * Psychic Energy is attached, or Comfey comes into play. Recovery is for good:
 * losing the Energy later brings nothing back.
 *
 * <p>On a Tool it covers only its holder, as a Tool's rules do.
 *
 * @param only narrows it to one status — Insomnia's "can't be Asleep"; empty is
 *             every Special Condition
 */
public record StatusImmunity(ICondition<? super PokemonInPlay> protects, Optional<IStatus> only) implements IRule {

    public StatusImmunity {
        Objects.requireNonNull(protects, "protects");
        Objects.requireNonNull(only, "only");
    }

    /** Every Special Condition. */
    public StatusImmunity(ICondition<? super PokemonInPlay> protects) {
        this(protects, Optional.empty());
    }

    public boolean covers(IStatus status) {
        return only.map(status::equals).orElse(true);
    }
}
