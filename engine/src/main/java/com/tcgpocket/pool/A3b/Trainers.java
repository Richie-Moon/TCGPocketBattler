package com.tcgpocket.pool.A3b;

import com.tcgpocket.action.ChoiceAction;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.ITrainerCard;
import com.tcgpocket.card.ItemCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.Any;
import com.tcgpocket.condition.EventSideIs;
import com.tcgpocket.condition.EvolvesFrom;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.condition.IsSupporter;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.effect.HealEach;
import com.tcgpocket.effect.IncreaseSideDamage;
import com.tcgpocket.effect.LookAtRandomCard;
import com.tcgpocket.effect.ShuffleDeck;
import com.tcgpocket.effect.UsePickedCard;
import com.tcgpocket.number.Literal;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.target.AttackerAll;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.Matching;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnEnd;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Eevee Grove — Trainers.
 *
 * <p>Trainers are only ever played on your own turn, so "your" is
 * {@code Attacker*} throughout. A Tool's triggers fire with its holder as the
 * source, so there {@code Self} is the Pokemon wearing it.
 */
public final class Trainers {

    /**
     * A3b-066 · Eevee Bag
     */
    public static final ItemCard EEVEE_BAG = ItemCard.of(
            "A3b-066", "Eevee Bag",
            new ChoiceAction(List.of(
                    new PlainAction("During this turn, attacks used by your Pokémon that evolve from Eevee do +10 damage to your opponent's Active Pokémon.",
                            new Attempt(List.of(
                                    new IncreaseSideDamage(new Literal(10), new EvolvesFrom("Eevee"), new Literal(0))))),
                    new PlainAction("Heal 20 damage from each of your Pokémon that evolves from Eevee.",
                            new Attempt(List.of(
                                    new HealEach(new Literal(20), new Matching(new AttackerAll(), new EvolvesFrom("Eevee"))))))),
                    new AttackerSide(), "Choose 1:"));
    /**
     * A3b-067 · Leftovers
     */
    public static final ToolCard LEFTOVERS = new ToolCard(
            "A3b-067", "Leftovers",
            "At the end of your turn, if the Pokémon this card is attached to is in the Active Spot, heal 10 damage from that Pokémon.",
            Set.of(), List.of(), List.of(new Trigger(TurnEnd.class,
                    new And<>(new EventSideIs(new SelfSide()), new For(new IsActive(), new Self())),
                    new Attempt(List.of(new HealDamage(new Literal(10), new Self()))))),
            List.of());
    /**
     * A3b-068 · Hau
     */
    public static final SupporterCard HAU = SupporterCard.of(
            "A3b-068", "Hau",
            new PlainAction(
                    "During this turn, attacks used by your Decidueye ex, Incineroar ex, or Primarina ex do +30 damage to your opponent's Active Pokémon.",
                    new Attempt(List.of(
                            new IncreaseSideDamage(new Literal(30), species("Decidueye ex", "Incineroar ex", "Primarina ex"), new Literal(0))))));
    /**
     * A3b-069 · Penny — copied by name, so no alternate art of Penny can be picked.
     */
    public static final SupporterCard PENNY = SupporterCard.of(
            "A3b-069", "Penny",
            new PlainAction(
                    "Look at a random Supporter card that's not Penny from your opponent's deck and shuffle it back into their deck. Use the effect of that card as the effect of this card.",
                    new Attempt(List.of(
                            new LookAtRandomCard(new OpponentSide(), new And<>(new IsSupporter(), new Not<>(new IsSpecies("Penny")))),
                            new ShuffleDeck(new OpponentSide()),
                            new UsePickedCard()))));

    /** A3b-085 · Hau (alternate art of A3b-068) */
    public static final SupporterCard HAU_UR = HAU.withId("A3b-085", CardRarity.ULTRA_RARE);
    /** A3b-086 · Penny (alternate art of A3b-069) */
    public static final SupporterCard PENNY_UR = PENNY.withId("A3b-086", CardRarity.ULTRA_RARE);

    /** A3b-107 · Eevee Bag (alternate art of A3b-066) */
    public static final ItemCard EEVEE_BAG_HR = EEVEE_BAG.withId("A3b-107", CardRarity.HYPER_RARE);

    static final List<ITrainerCard> CARDS = List.of(
            EEVEE_BAG, LEFTOVERS, HAU, PENNY,
            HAU_UR, PENNY_UR, EEVEE_BAG_HR);

    private static ICondition<CardInstance> species(String... names) {
        return new Any<>(Arrays.stream(names).<ICondition<CardInstance>>map(IsSpecies::new).toList());
    }

    private Trainers() {
    }
}
