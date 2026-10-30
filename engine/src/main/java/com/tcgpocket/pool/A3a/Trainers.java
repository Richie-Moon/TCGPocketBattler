package com.tcgpocket.pool.A3a;

import com.tcgpocket.action.PlainAction;
import com.tcgpocket.action.PlayedOnto;
import com.tcgpocket.action.WithPrecondition;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.ITrainerCard;
import com.tcgpocket.card.ItemCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.condition.All;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.Any;
import com.tcgpocket.condition.EqualTo;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.ForAny;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.condition.HasTag;
import com.tcgpocket.condition.HasType;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.IsBasic;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.AttachRandomFromDiscard;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ChooseThen;
import com.tcgpocket.effect.IncreaseSideDamage;
import com.tcgpocket.effect.MoveEnergy;
import com.tcgpocket.effect.NoEffect;
import com.tcgpocket.effect.ReduceSideDamageTaken;
import com.tcgpocket.effect.SearchDeck;
import com.tcgpocket.effect.SwitchActive;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Points;
import com.tcgpocket.number.Product;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.target.AttackerAll;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.EventSource;
import com.tcgpocket.target.Except;
import com.tcgpocket.target.Matching;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentBench;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.PlayTarget;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.AttackDeclared;
import com.tcgpocket.trigger.Knockout;
import com.tcgpocket.trigger.Trigger;

import java.util.List;
import java.util.Set;

/**
 * Extradimensional Crisis — Trainers.
 *
 * <p>Trainers are only ever played on your own turn, so "your" is
 * {@code Attacker*} throughout. A Tool's triggers fire with its holder as the
 * source, so there {@code Self} is the Pokemon wearing it.
 */
public final class Trainers {

    /**
     * A3a-063 · Beast Wall — only playable while your opponent has no points.
     */
    public static final ItemCard BEAST_WALL = ItemCard.of(
            "A3a-063", "Beast Wall",
            new WithPrecondition(
                    new EqualTo(new Points(new OpponentSide()), new Literal(0)),
                    new PlainAction(
                            "You can use this card only if your opponent hasn't gotten any points. During your opponent's next turn, all of your Ultra Beasts take −20 damage from attacks from your opponent's Pokémon.",
                            new Attempt(List.of(
                                    new ReduceSideDamageTaken(new Literal(20), new HasTag(CardTag.ULTRA_BEAST), new Literal(1)))))));
    /**
     * A3a-064 · Repel — only playable on a Basic Active with a Bench to replace it.
     */
    public static final ItemCard REPEL = ItemCard.of(
            "A3a-064", "Repel",
            new WithPrecondition(
                    new And<>(new For(new IsBasic(), new OpponentActive()),
                            new ForAny(new Always<>(), new OpponentBench())),
                    new PlainAction(
                            "Switch out your opponent's Active Basic Pokémon to the Bench. (Your opponent chooses the new Active Pokémon.)",
                            new Attempt(List.of(
                                    new SwitchActive(new OpponentSide()))))));
    /**
     * A3a-065 · Electrical Cord — the same gate as A3-104 Passimian ex's Offload
     * Pass, so "your Benched Pokémon" is the turn-relative {@link OpponentBench}.
     * The first choice is excluded from the second, so the 2 Energy land on 2
     * Pokémon. With one Benched Pokémon only the first moves.
     */
    public static final ToolCard ELECTRICAL_CORD = new ToolCard(
            "A3a-065", "Electrical Cord",
            "If the Lightning Pokémon this card is attached to is in the Active Spot and is Knocked Out by damage from an attack from your opponent's Pokémon, move 2 Lightning Energy from that Pokémon and attach 1 Energy each to 2 of your Benched Pokémon.",
            Set.of(), List.of(), List.of(new Trigger(Knockout.class,
                    new All<>(List.of(
                            new EventConcerns(new Self()),
                            new For(new HasType(Type.LIGHTNING), new Self()),
                            new For(new IsActive(), new Self()),
                            new For(new Always<>(), new EventSource()),
                            new Not<>(new EventConcerns(new EventSource())))),
                    new Attempt(List.of(new ChooseThen(
                            new ChosenFrom(new OpponentBench(), new SelfSide(), "Choose a Benched Pokémon to move Lightning Energy to"),
                            List.of(
                                    new MoveEnergy(new Self(), new PlayTarget(), new Literal(1), Type.LIGHTNING),
                                    new MoveEnergy(new Self(), new ChosenFrom(
                                            new Except(new OpponentBench(), new PlayTarget()), new SelfSide(), "Choose another Benched Pokémon to move Lightning Energy to"),
                                            new Literal(1), Type.LIGHTNING))))))),
            List.of());
    /**
     * A3a-066 · Beastite — the same trigger as A2a Power Link.
     */
    public static final ToolCard BEASTITE = new ToolCard(
            "A3a-066", "Beastite",
            "Attacks used by the Ultra Beast this card is attached to do +10 damage to your opponent's Active Pokémon for each point you have gotten.",
            Set.of(), List.of(), List.of(new Trigger(AttackDeclared.class,
                    new And<>(new EventConcerns(new Self()), new For(new HasTag(CardTag.ULTRA_BEAST), new Self())),
                    new Attempt(List.of(new IncreaseSideDamage(
                            new Product(new Points(new SelfSide()), new Literal(10)), new Literal(0)))))),
            List.of());
    /**
     * A3a-067 · Gladion
     */
    public static final SupporterCard GLADION = SupporterCard.of(
            "A3a-067", "Gladion",
            new PlainAction(
                    "Put 1 random Type: Null or Silvally from your deck into your hand.",
                    new Attempt(List.of(
                            new SearchDeck(new AttackerSide(), new Literal(1), new Any<CardInstance>(List.of(
                                    new IsSpecies("Type: Null"), new IsSpecies("Silvally"))))))));
    /**
     * A3a-068 · Looker
     *
     * <p><b>Narrowed.</b> A {@link NoEffect}: showing cards changes nothing on the
     * board, and {@code Side} can only record a revealed hand or the top of a deck.
     * A node that records revealed deck cards, read by {@code BoardView}, would
     * give it meaning.
     */
    public static final SupporterCard LOOKER = SupporterCard.of(
            "A3a-068", "Looker",
            new PlainAction(
                    "Your opponent reveals all of the Supporter cards in their deck.",
                    new Attempt(List.of(
                            new NoEffect()))));
    /**
     * A3a-069 · Lusamine — dragged onto the Ultra Beast, once your opponent has a point.
     */
    public static final SupporterCard LUSAMINE = SupporterCard.of(
            "A3a-069", "Lusamine",
            new WithPrecondition(
                    new GreaterThan(new Points(new OpponentSide()), new Literal(0)),
                    new PlayedOnto(
                            new Matching(new AttackerAll(), new HasTag(CardTag.ULTRA_BEAST)),
                            "You can use this card only if your opponent has gotten at least 1 point. Choose 1 of your Ultra Beasts. Attach 2 random Energy from your discard pile to that Pokémon.",
                            new Attempt(List.of(
                                    new AttachRandomFromDiscard(new Literal(2), new PlayTarget()))))));

    /** A3a-081 · Gladion (alternate art of A3a-067) */
    public static final SupporterCard GLADION_UR = GLADION.withId("A3a-081", CardRarity.ULTRA_RARE);
    /** A3a-082 · Looker (alternate art of A3a-068) */
    public static final SupporterCard LOOKER_UR = LOOKER.withId("A3a-082", CardRarity.ULTRA_RARE);
    /** A3a-083 · Lusamine (alternate art of A3a-069) */
    public static final SupporterCard LUSAMINE_UR = LUSAMINE.withId("A3a-083", CardRarity.ULTRA_RARE);

    static final List<ITrainerCard> CARDS = List.of(
            BEAST_WALL, REPEL, ELECTRICAL_CORD, BEASTITE, GLADION, LOOKER,
            LUSAMINE,
            GLADION_UR, LOOKER_UR, LUSAMINE_UR);

    private Trainers() {
    }
}
