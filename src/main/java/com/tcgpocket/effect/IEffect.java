package com.tcgpocket.effect;

import com.tcgpocket.resolve.ResolutionContext;

/**
 * The only thing in the model permitted to change the board.
 *
 * <p>{@code INumber}, {@code ICondition} and {@code ITarget} all read; an
 * effect writes. Keeping that boundary sharp is what lets legal-move
 * generation evaluate an attack's numbers to preview it without playing it.
 *
 * <h2>Contract</h2>
 * <ul>
 *   <li><b>Report honestly.</b> Return {@link EffectOutcome#FAILED} only when
 *       the effect genuinely could not happen — an unresolved target, an
 *       unpayable cost. Return {@link EffectOutcome#NO_OP} when there was
 *       simply nothing to do. An attempt aborts on the first, not the second.
 *   <li><b>All or nothing.</b> An effect that fails must leave the board
 *       untouched. A cost that partially deducts and then reports failure
 *       would let an attempt abort with the payment already made.
 * </ul>
 */
public sealed interface IEffect permits AddStatus, AttachEnergy, AttachFromEnergyZone, AttachTool, ConditionalEffect, DamageEach, DealDamage, DiscardFromHand, DiscardRandomEnergy, DiscardTypeEnergy, DrawCard, Fail, HealDamage, HealEach, IDurationEffect, IFlipStrategy, MoveEnergy, MultiHit, NoEffect, PlaceDamage, RemoveStatus, RepeatEffect, SearchDeck, ShuffleDeck, ShuffleHandIntoDeck, SpreadDamage, SwitchActive {

    EffectOutcome apply(ResolutionContext context);
}
