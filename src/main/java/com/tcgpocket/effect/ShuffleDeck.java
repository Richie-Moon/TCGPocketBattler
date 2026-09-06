package com.tcgpocket.effect;

import com.tcgpocket.state.Side;
import com.tcgpocket.target.ISideTarget;
import com.tcgpocket.resolve.ResolutionContext;

import java.util.Objects;

public record ShuffleDeck(ISideTarget side) implements IEffect {
    
    public ShuffleDeck {
        Objects.requireNonNull(side, "side");
    }
    
    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Side target = side.resolve(context);
        
        if (target.deck().isEmpty()) {
            return EffectOutcome.NO_OP;
        }
        
        target.shuffleDeck(context.battle().rng());
        return EffectOutcome.APPLIED;
    }
}
