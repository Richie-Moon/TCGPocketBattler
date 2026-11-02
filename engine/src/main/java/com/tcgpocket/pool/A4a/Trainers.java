package com.tcgpocket.pool.A4a;

import com.tcgpocket.action.PlainAction;
import com.tcgpocket.action.PlayedOnto;
import com.tcgpocket.action.WithPrecondition;
import com.tcgpocket.card.ITrainerCard;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PreviousAttacks;
import com.tcgpocket.card.RetreatCostReduction;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.Any;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.condition.HasType;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.condition.IsAsleep;
import com.tcgpocket.condition.IsConfused;
import com.tcgpocket.condition.IsDamaged;
import com.tcgpocket.condition.IsParalyzed;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.condition.IsTool;
import com.tcgpocket.condition.IsType;
import com.tcgpocket.effect.ArrangeTopCards;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.effect.RemoveStatus;
import com.tcgpocket.effect.TakeFromTopOfDeck;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.INumber;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Stage;
import com.tcgpocket.number.Sum;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.AttackerAll;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.Matching;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.PlayTarget;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Secluded Springs (A4a): the Trainer cards, in printed set order.
 */
public final class Trainers {

    private static final ICondition<CardInstance> PSYCHIC = new IsType(Type.PSYCHIC);

    private static final INumber PSYCHIC_IN_PLAY = new Sum(
            new CountCards(new AttackerSide(), Zone.ACTIVE, Optional.of(PSYCHIC)),
            new CountCards(new AttackerSide(), Zone.BENCH, Optional.of(PSYCHIC)));

    private static final ICondition<PokemonInPlay> NEEDS_WHITNEY = new Any<>(List.of(
            new IsDamaged(), new IsAsleep(), new IsParalyzed(), new IsConfused()));

    /**
     * A4a-067 · Inflatable Boat — attachable to anything, but only a Water holder pays less. Only the Active
     * retreats, so "the holder" needs no condition of its own.
     */
    public static final ToolCard INFLATABLE_BOAT = new ToolCard(
            "A4a-067", "Inflatable Boat", "The Retreat Cost of the Water Pokémon this card is attached to is 1 less.",
            Set.of(), List.of(), List.of(), List.of(new RetreatCostReduction(1, new HasType(Type.WATER), new Always<>())));

    /**
     * A4a-068 · Memory Light — only what is still in the stack, so not a Stage 1 skipped by Rare Candy.
     */
    public static final ToolCard MEMORY_LIGHT = new ToolCard(
            "A4a-068", "Memory Light", "The Pokémon this card is attached to can use any attack from its previous Evolutions. (You still need the necessary Energy to use each attack.)",
            Set.of(), List.of(), List.of(), List.of(new PreviousAttacks()));

    /**
     * A4a-069 · Whitney — only playable on a Miltank it would help. Poison and Burn stay.
     */
    public static final SupporterCard WHITNEY = SupporterCard.of(
            "A4a-069", "Whitney",
            new PlayedOnto(
                    new Matching(new Matching(new AttackerAll(), new IsSpecies("Miltank")), NEEDS_WHITNEY),
                    "Heal 60 damage from 1 of your Miltank, and it recovers from being Asleep, Paralyzed, and Confused.",
                    new Attempt(List.of(
                            new HealDamage(new Literal(60), new PlayTarget()),
                            new RemoveStatus(new PlayTarget(), new SleepStatus()),
                            new RemoveStatus(new PlayTarget(), new ParalysisStatus()),
                            new RemoveStatus(new PlayTarget(), new ConfusionStatus())))));

    /**
     * A4a-070 · Traveling Merchant — only playable with a deck to look at.
     */
    public static final SupporterCard TRAVELING_MERCHANT = SupporterCard.of(
            "A4a-070", "Traveling Merchant",
            new WithPrecondition(
                    new GreaterThan(new CountCards(new AttackerSide(), Zone.DECK), new Literal(0)),
                    new PlainAction(
                            "Look at the top 4 cards of your deck. Put all Pokémon Tool cards you find there into your hand. Shuffle the other cards back into your deck.",
                            new Attempt(List.of(new TakeFromTopOfDeck(new Literal(4), new AttackerSide(), new IsTool()))))));

    /**
     * A4a-071 · Morty — only playable with a Psychic Pokémon in play and a card to look at. Hiker, on the opponent's deck.
     */
    public static final SupporterCard MORTY = SupporterCard.of(
            "A4a-071", "Morty",
            new WithPrecondition(
                    new And<>(new GreaterThan(PSYCHIC_IN_PLAY, new Literal(0)),
                            new GreaterThan(new CountCards(new OpponentSide(), Zone.DECK), new Literal(0))),
                    new PlainAction(
                            "For each of your Psychic Pokémon in play, look at that many cards from the top of your opponent's deck and put them back in any order.",
                            new Attempt(List.of(new ArrangeTopCards(PSYCHIC_IN_PLAY, new OpponentSide(), new AttackerSide()))))));

    /** A4a-083 · Whitney (alternate art of A4a-069) */
    public static final SupporterCard WHITNEY_UR = WHITNEY.withId("A4a-083", CardRarity.ULTRA_RARE);

    /** A4a-084 · Traveling Merchant (alternate art of A4a-070) */
    public static final SupporterCard TRAVELING_MERCHANT_UR = TRAVELING_MERCHANT.withId("A4a-084", CardRarity.ULTRA_RARE);

    /** A4a-085 · Morty (alternate art of A4a-071) */
    public static final SupporterCard MORTY_UR = MORTY.withId("A4a-085", CardRarity.ULTRA_RARE);

    static final List<ITrainerCard> CARDS = List.of(
            INFLATABLE_BOAT, MEMORY_LIGHT, WHITNEY, TRAVELING_MERCHANT, MORTY, WHITNEY_UR, TRAVELING_MERCHANT_UR, MORTY_UR);

    private Trainers() {
    }
}
