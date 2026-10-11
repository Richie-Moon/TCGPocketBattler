package com.tcgpocket.card;

/**
 * A passive ability that is read, not fired: a standing rule that must hold at
 * every moment rather than happen at one.
 *
 * <p>A trigger stamped at turn start (Melmetal) is exact only when nothing
 * relevant can change during the turn it covers. These cover the cases where
 * something can — a Pokemon benched, evolved or switched in mid-turn — so the
 * code that needs the answer asks {@code Side.standingRules()} at the moment it
 * needs it, and gets each rule with the Pokemon holding it. That walks the
 * board on every call, like {@code TriggerDispatcher.collect()}, so a rule can
 * never outlive its holder.
 *
 * <p>Each rule is read by exactly one place in the engine, named on the rule.
 */
public sealed interface IRule permits EnergyBoost, EvolutionLock, HpBonus, NoRetreatCost, SupporterLock {
}
