package com.tcgpocket.trigger;

/**
 * Something that happened on the board, which triggers can listen for.
 *
 * <p>Events carry their payload, so a trigger's attempt can read what actually
 * occurred — "when this Pokemon is damaged, deal that much back" needs the
 * amount, which an empty marker type could not supply.
 */
public sealed interface GameEvent permits DamageDealt {

    // TODO: TurnStart, TurnEnd, AttackDeclared, Knockout, StatusApplied,
    //       CardPlayed, Evolved, ToolAttached, Retreated, CoinFlipped, ...
    //       Only DamageDealt exists so far because EventDamage reads it.
}
