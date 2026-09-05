package com.tcgpocket.action;

import com.tcgpocket.effect.AttachFromEnergyZone;
import com.tcgpocket.effect.AttemptResult;
import com.tcgpocket.effect.EffectOutcome;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.Side;
import com.tcgpocket.target.ITarget;

import java.util.List;
import java.util.Objects;

/**
 * The once-a-turn energy attachment.
 *
 * <p>The limit lives here rather than in {@code AttachFromEnergyZone}, because
 * it is a rule about the turn action: card text that grants an extra
 * attachment is supposed to bypass it, and does, by using the effect directly.
 */
public record AttachEnergyAction(ITarget target) implements IAction {

    public AttachEnergyAction {
        Objects.requireNonNull(target, "target");
    }

    @Override
    public boolean isLegal(ResolutionContext context) {
        Side side = context.controller();
        return side.currentEnergy().isPresent()
                && !side.energyAttachedThisTurn()
                && target.resolve(context).filter(side.inPlay()::contains).isPresent();
    }

    @Override
    public AttemptResult execute(ResolutionContext context) {
        if (!isLegal(context)) {
            return AttemptResult.failure("cannot attach energy", List.of());
        }

        EffectOutcome outcome = new AttachFromEnergyZone(target).apply(context);
        if (outcome.isFailure()) {
            return AttemptResult.failure("energy zone was empty", List.of(outcome));
        }

        context.controller().markEnergyAttached();
        return AttemptResult.success(List.of(outcome));
    }
}
