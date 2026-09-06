package com.tcgpocket.card;

/**
 * A Trainer card: Item, Supporter, Tool or Stadium.
 *
 * <p>The distinction between {@link ItemCard} and {@link SupporterCard} is not
 * cosmetic — only one Supporter may be played per turn, so the rules have to
 * be able to tell them apart. {@link ToolCard} and {@link StadiumCard} differ
 * again: they stay on the board and act through triggers rather than resolving
 * once and going to the discard pile.
 */
public sealed interface ITrainerCard extends ICard
        permits ItemCard, SupporterCard, ToolCard, StadiumCard {

    // TODO: the Fossil branch (PlayableItemCard) — a card that is both a
    //       Trainer and something that sits on the bench like a Pokemon.
}
