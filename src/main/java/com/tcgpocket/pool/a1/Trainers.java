package com.tcgpocket.pool.a1;

import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ITrainerCard;
import com.tcgpocket.card.ItemCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DrawCard;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.DamageDealt;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * Genetic Apex — Trainers.
 *
 * <p>All three kinds, because they reach the board by three different routes:
 * an Item resolves and is discarded, a Supporter does the same but only once a
 * turn, and a Tool stays in play and does its work entirely through triggers.
 */
public final class Trainers {

    private Trainers() {
    }

    /**
     * A1-219 · Potion — heal 20 damage from 1 of your Pokemon.
     *
     * <p><b>Narrowed.</b> The printed card lets you pick any of your Pokemon;
     * nothing in {@code ITarget} can express a choice yet, so this heals the
     * Active. Correct whenever the Active is the one you wanted, wrong
     * otherwise — the first card in the pool that {@code ChosenFrom} would fix.
     */
    public static final ItemCard POTION = ItemCard.of(
            "A1-219", "Potion",
            new PlainAction(
                    "Heal 20 damage from 1 of your Pokemon.",
                    new Attempt(List.of(
                            new HealDamage(new Literal(20), new AttackerActive())))));

    /** A1-222 · Professor's Research — draw 2 cards. */
    public static final SupporterCard PROFESSORS_RESEARCH = SupporterCard.of(
            "A1-222", "Professor's Research",
            new PlainAction(
                    "Draw 2 cards.",
                    new Attempt(List.of(
                            new DrawCard(new Literal(2), new AttackerSide())))));

    /**
     * A1-225 · Rocky Helmet — when the holder takes damage from an attack, the
     * Attacking Pokemon takes 20.
     *
     * <p>Reads exactly as printed, and shows why targets are turn-relative:
     * "the Attacking Pokemon" is {@link AttackerActive} even though this card
     * belongs to the player being attacked. {@code EventConcerns(Self)} is what
     * keeps a board-wide dispatch from firing every Helmet in play.
     */
    public static final ToolCard ROCKY_HELMET = ToolCard.of(
            "A1-225", "Rocky Helmet",
            new Trigger(
                    DamageDealt.class,
                    new EventConcerns(new Self()),
                    new Attempt(List.of(
                            new PlaceDamage(new Literal(20), new AttackerActive())))));

    static final List<ITrainerCard> CARDS = List.of(POTION, PROFESSORS_RESEARCH, ROCKY_HELMET);
}
