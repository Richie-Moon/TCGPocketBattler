package com.tcgpocket.pool.PA;

import com.tcgpocket.action.PlainAction;
import com.tcgpocket.action.PlayedOnto;
import com.tcgpocket.card.ICard;
import com.tcgpocket.card.ItemCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.condition.IsBasic;
import com.tcgpocket.condition.IsDamaged;
import com.tcgpocket.effect.*;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerAll;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.Matching;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.PlayTarget;

import java.util.List;

/**
 * Promo-A — Trainers.
 *
 * <p>Trainers are only ever played on your own turn, so "your" is
 * {@code Attacker*} throughout.
 */
public final class Trainers {

    /**
     * P-A-001 · Potion — dragged onto the Pokemon to heal. An undamaged
     * Pokemon is no drop target, so with nothing hurt it cannot be played.
     */
    public static final ItemCard POTION = ItemCard.of(
            "P-A-001", "Potion",
            new PlayedOnto(
                    new Matching(new AttackerAll(), new IsDamaged()),
                    "Heal 20 damage from 1 of your Pokémon.",
                    new Attempt(List.of(
                            new HealDamage(new Literal(20), new PlayTarget())))));
    /**
     * P-A-002 · X Speed
     */
    public static final ItemCard X_SPEED = ItemCard.of(
            "P-A-002", "X Speed",
            new PlainAction(
                    "During this turn, the Retreat Cost of your Active Pokémon is 1 less.",
                    new Attempt(List.of(
                            new ReduceRetreatCost(new Literal(1), new AttackerActive(), new Literal(0))))));
    /**
     * P-A-003 · Hand Scope
     *
     * <p><b>Narrowed.</b> A {@link NoEffect}, as for Mew's Psy Report: the engine
     * has no per-player visibility, so revealing changes nothing. A
     * hidden-information model would give a {@code RevealHand} node meaning.
     */
    public static final ItemCard HAND_SCOPE = ItemCard.of(
            "P-A-003", "Hand Scope",
            new PlainAction(
                    "Your opponent reveals their hand.",
                    new Attempt(List.of(
                            new NoEffect()))));
    /**
     * P-A-004 · Pokédex
     */
    public static final ItemCard POKEDEX = pokedex("P-A-004");
    /**
     * P-A-005 · Poké Ball
     */
    public static final ItemCard POKE_BALL = ItemCard.of(
            "P-A-005", "Poké Ball",
            new PlainAction(
                    "Put a random Basic Pokémon from your deck into your hand.",
                    new Attempt(List.of(
                            new SearchDeck(new AttackerSide(), new Literal(1), new IsBasic())))));
    /**
     * P-A-006 · Red Card
     */
    public static final ItemCard RED_CARD = ItemCard.of(
            "P-A-006", "Red Card",
            new PlainAction(
                    "Your opponent shuffles their hand into their deck and draws 3 cards.",
                    new Attempt(List.of(
                            new ShuffleHandIntoDeck(new OpponentSide()),
                            new DrawCard(new Literal(3), new OpponentSide())))));
    /**
     * P-A-007 · Professor's Research
     */
    public static final SupporterCard PROFESSORS_RESEARCH = SupporterCard.of(
            "P-A-007", "Professor's Research",
            new PlainAction(
                    "Draw 2 cards.",
                    new Attempt(List.of(
                            new DrawCard(new Literal(2), new AttackerSide())))));
    /**
     * P-A-008 · Pokédex — a second printing of P-A-004.
     */
    public static final ItemCard POKEDEX_2 = pokedex("P-A-008");

    /** Every card in the set so far, in printed order. */
    public static final List<ICard> CARDS = List.of(
            POTION, X_SPEED, HAND_SCOPE, POKEDEX, POKE_BALL, RED_CARD, PROFESSORS_RESEARCH, POKEDEX_2);

    private Trainers() {
    }

    /**
     * <b>Narrowed.</b> A {@link NoEffect}: looking changes nothing without
     * per-player visibility, and the deck order is untouched. A hidden-information
     * model would give a {@code LookAtTopOfDeck} node meaning.
     */
    private static ItemCard pokedex(String id) {
        return ItemCard.of(
                id, "Pokédex",
                new PlainAction(
                        "Look at the top 3 cards of your deck.",
                        new Attempt(List.of(
                                new NoEffect()))));
    }
}
