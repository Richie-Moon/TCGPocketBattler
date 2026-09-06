package com.tcgpocket.trigger;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.resolve.ResolutionScope;
import com.tcgpocket.state.Battle;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;

import java.util.Objects;
import java.util.Optional;

/**
 * A trigger together with where it came from.
 *
 * <p>The holder is what makes {@code Self} mean the right thing. A Giant Cape's
 * trigger says "this Pokemon", and "this" is whichever Pokemon happens to be
 * wearing the Cape — not the attacker, and not the card that dispatched the
 * event.
 *
 * @param holder     the Pokemon supplying the trigger; empty for a Stadium,
 *                   which sits on the board attached to nobody
 * @param controller whose card it is. A defender's Tool fires during the
 *                   attacker's turn, and its {@code OpponentActive} must still
 *                   mean the attacker.
 */
public record TriggerSource(ITrigger trigger, Optional<PokemonInPlay> holder, Side controller) {

    public TriggerSource {
        Objects.requireNonNull(trigger, "trigger");
        Objects.requireNonNull(holder, "holder");
        Objects.requireNonNull(controller, "controller");
    }

    public static TriggerSource heldBy(ITrigger trigger, PokemonInPlay holder) {
        return new TriggerSource(trigger, Optional.of(holder), holder.owner());
    }

    public static TriggerSource unheld(ITrigger trigger, Side controller) {
        return new TriggerSource(trigger, Optional.empty(), controller);
    }

    /**
     * The context this trigger fires in.
     *
     * <p>A fresh {@link ResolutionScope} every time, so a coin flipped by one
     * trigger is invisible to the next — Burn and Sleep both flip at the end of
     * the same turn, and neither may read the other's result.
     */
    public ResolutionContext contextFor(Battle battle, GameEvent event) {
        return new ResolutionContext(
                battle, controller, holder, Optional.of(event), new ResolutionScope());
    }
}
