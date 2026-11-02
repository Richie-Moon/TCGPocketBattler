package com.tcgpocket.card;

/**
 * The Tool's holder can also use the attacks of the cards beneath it — Memory
 * Light. Read by {@code PokemonInPlay.attacks()}. A Stage 1 that Rare Candy
 * skipped is not in the stack, so its attacks are not offered.
 */
public record PreviousAttacks() implements IRule {
}
