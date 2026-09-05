package com.tcgpocket.card;

/**
 * A Trainer card: Item, Supporter, Tool, Stadium, or a Fossil.
 *
 * <p>Only {@link ToolCard} exists so far, because {@code AttachTool} needs it.
 * The rest arrive with the {@code IAction} hierarchy, which is what gives them
 * anything to do.
 */
public sealed interface ITrainerCard extends ICard permits ToolCard {
}
