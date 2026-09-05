package com.tcgpocket.card;

/**
 * A Trainer card: Item, Supporter, Tool, Stadium, or a Fossil.
 *
 * <p>The distinction between {@link ItemCard} and {@link SupporterCard} is not
 * cosmetic — only one Supporter may be played per turn, so the rules have to
 * be able to tell them apart.
 */
public sealed interface ITrainerCard extends ICard permits ItemCard, SupporterCard, ToolCard {

    // TODO: StadiumCard and the Fossil (PlayableItemCard) branch — the first
    //       needs ITrigger, the second is both a Trainer and a playable card.
}
