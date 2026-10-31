package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.target.ISideTarget;

import java.util.Objects;

/**
 * The first coin of that side's next flip this turn is heads — Will.
 *
 * <p>Stamps the {@code Side}, which {@link IFlipStrategy#coin} reads. Flips for
 * Special Conditions neither use nor spend it, as Will names only attacks,
 * Abilities and Trainer cards.
 */
public record GuaranteeNextHeads(ISideTarget side) implements IEffect {

    public GuaranteeNextHeads {
        Objects.requireNonNull(side, "side");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        side.resolve(context).guaranteeHeadsOn(context.battle().turn());
        return EffectOutcome.APPLIED;
    }
}
