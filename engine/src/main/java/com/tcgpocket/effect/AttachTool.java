package com.tcgpocket.effect;

import com.tcgpocket.card.ToolCard;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.ITarget;
import com.tcgpocket.trigger.ToolAttached;
import com.tcgpocket.trigger.TriggerDispatcher;

import java.util.Objects;
import java.util.Optional;

/**
 * Attaches a Tool from the controller's hand to a Pokemon.
 *
 * <p>Fails when the hand holds no copy of that Tool, or when the target
 * already holds one — a Pokemon may carry only a single Tool.
 */
public record AttachTool(ITarget target, ToolCard tool) implements IEffect {

    public AttachTool {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(tool, "tool");
    }

    @Override
    public EffectOutcome apply(ResolutionContext context) {
        Optional<PokemonInPlay> resolved = target.resolve(context);
        if (resolved.isEmpty() || resolved.get().hasTool()) {
            return EffectOutcome.FAILED;
        }

        Side controller = context.controller();
        Optional<CardInstance> inHand = controller.hand().stream()
                .filter(card -> card.definition().equals(tool))
                .findFirst();

        if (inHand.isEmpty()) {
            return EffectOutcome.FAILED;
        }

        PokemonInPlay holder = resolved.get();
        CardInstance card = inHand.get();
        controller.removeFromHand(card);
        holder.attachTool(card);

        TriggerDispatcher.dispatch(context.battle(), new ToolAttached(holder, card));
        return EffectOutcome.APPLIED;
    }
}
