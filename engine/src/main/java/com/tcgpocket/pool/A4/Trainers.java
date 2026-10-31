package com.tcgpocket.pool.A4;

import com.tcgpocket.action.PlainAction;
import com.tcgpocket.action.WithPrecondition;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.ITrainerCard;
import com.tcgpocket.card.ItemCard;
import com.tcgpocket.card.ReturnToHandWhenKnockedOut;
import com.tcgpocket.card.StatusImmunity;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.condition.All;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.Any;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.condition.EventSideIs;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.ForAny;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.condition.HasEnergy;
import com.tcgpocket.condition.HasType;
import com.tcgpocket.condition.ICondition;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.IsDamaged;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.condition.IsSupporter;
import com.tcgpocket.condition.IsType;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.ArrangeTopCards;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DiscardTypeEnergy;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.GuaranteeNextHeads;
import com.tcgpocket.effect.MoveEnergyOfTypes;
import com.tcgpocket.effect.RecoverFromDiscard;
import com.tcgpocket.effect.ReduceDamageTaken;
import com.tcgpocket.effect.ReduceSideDamageTaken;
import com.tcgpocket.effect.RepeatEffect;
import com.tcgpocket.effect.RevealHand;
import com.tcgpocket.effect.ShuffleFromHand;
import com.tcgpocket.effect.ShuffleRandomFromHand;
import com.tcgpocket.effect.SwitchActive;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.EventDamage;
import com.tcgpocket.number.INumber;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Sum;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.EventSource;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.AttackDeclared;
import com.tcgpocket.trigger.DamageDealt;
import com.tcgpocket.trigger.Trigger;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Wisdom of Sea and Sky — Trainers.
 *
 * <p>Trainers are only ever played on your own turn, so "your" is
 * {@code Attacker*} throughout. A Tool's triggers fire with its holder as the
 * source, so there {@code Self} is the Pokemon wearing it, and during the
 * opponent's attack the opponent is {@code Attacker*}.
 */
public final class Trainers {

    private static final ICondition<PokemonInPlay> HAS_FIRE_WATER_OR_LIGHTNING = new Any<>(List.of(
            new HasEnergy(EnergyCost.of(Type.FIRE, 1)),
            new HasEnergy(EnergyCost.of(Type.WATER, 1)),
            new HasEnergy(EnergyCost.of(Type.LIGHTNING, 1))));

    private static final ICondition<CardInstance> FIGHTING = new IsType(Type.FIGHTING);

    private static final INumber FIGHTING_IN_PLAY = new Sum(
            new CountCards(new AttackerSide(), Zone.ACTIVE, Optional.of(FIGHTING)),
            new CountCards(new AttackerSide(), Zone.BENCH, Optional.of(FIGHTING)));

    /**
     * A4-151 · Elemental Switch
     */
    public static final ItemCard ELEMENTAL_SWITCH = ItemCard.of(
            "A4-151", "Elemental Switch",
            new WithPrecondition(
                    new ForAny(HAS_FIRE_WATER_OR_LIGHTNING, new AttackerBench()),
                    new PlainAction(
                            "Move a Fire, Water, or Lightning Energy from 1 of your Benched Pokémon to your Active Pokémon.",
                            new Attempt(List.of(
                                    new MoveEnergyOfTypes(List.of(Type.FIRE, Type.WATER, Type.LIGHTNING),
                                            new ChosenFrom(new AttackerBench(), new AttackerSide(), HAS_FIRE_WATER_OR_LIGHTNING,
                                                    "Choose a Benched Pokémon to move an Energy from"),
                                            new AttackerActive()))))));
    /**
     * A4-152 · Squirt Bottle
     */
    public static final ItemCard SQUIRT_BOTTLE = ItemCard.of(
            "A4-152", "Squirt Bottle",
            new WithPrecondition(
                    new For(new HasEnergy(EnergyCost.of(Type.FIRE, 1)), new OpponentActive()),
                    new PlainAction(
                            "Discard a Fire Energy from your opponent's Active Pokémon.",
                            new Attempt(List.of(
                                    new DiscardTypeEnergy(Type.FIRE, new Literal(1), new OpponentActive()))))));
    /**
     * A4-153 · Steel Apron — the reduction answers the opponent's
     * {@link AttackDeclared} and lasts that turn, so it never softens the
     * holder's own recoil.
     */
    public static final ToolCard STEEL_APRON = new ToolCard(
            "A4-153", "Steel Apron",
            "The Metal Pokémon this card is attached to takes −10 damage from attacks from your opponent's Pokémon, recovers from all Special Conditions, and can't be affected by any Special Conditions.",
            Set.of(), List.of(), List.of(new Trigger(AttackDeclared.class,
                    new And<>(new Not<>(new EventSideIs(new SelfSide())), new For(new HasType(Type.METAL), new Self())),
                    new Attempt(List.of(new ReduceDamageTaken(new Literal(10), new Self(), new Literal(0)))))),
            List.of(new StatusImmunity(new HasType(Type.METAL))));
    /**
     * A4-154 · Dark Pendant — asks that the hit landed on the defending side
     * and had a source, so neither the holder's own recoil nor poison sets it off.
     */
    public static final ToolCard DARK_PENDANT = new ToolCard(
            "A4-154", "Dark Pendant",
            "If the Darkness Pokémon this card is attached to is in the Active Spot and is damaged by an attack from your opponent's Pokémon, your opponent reveals a random card from their hand and shuffles it into their deck.",
            Set.of(), List.of(), List.of(new Trigger(DamageDealt.class,
                    new All<>(List.<ICondition<ResolutionContext>>of(
                            new EventConcerns(new Self()),
                            new EventSideIs(new OpponentSide()),
                            new For(new Always<>(), new EventSource()),
                            new For(new And<PokemonInPlay>(new IsActive(), new HasType(Type.DARKNESS)), new Self()),
                            new GreaterThan(new EventDamage(), new Literal(0)))),
                    new Attempt(List.of(new ShuffleRandomFromHand(new AttackerSide()))))),
            List.of());
    /**
     * A4-155 · Rescue Scarf
     */
    public static final ToolCard RESCUE_SCARF = new ToolCard(
            "A4-155", "Rescue Scarf",
            "If the Pokémon this card is attached to is Knocked Out by damage from an attack from your opponent's Pokémon, put it into your hand instead of the discard pile.",
            Set.of(), List.of(), List.of(), List.of(new ReturnToHandWhenKnockedOut()));
    /**
     * A4-156 · Will
     */
    public static final SupporterCard WILL = SupporterCard.of(
            "A4-156", "Will",
            new PlainAction(
                    "The next time you flip any number of coins for the effect of an attack, Ability, or Trainer card after using this card on this turn, the first coin flip will definitely be heads.",
                    new Attempt(List.of(new GuaranteeNextHeads(new AttackerSide())))));
    /**
     * A4-157 · Lyra
     */
    public static final SupporterCard LYRA = SupporterCard.of(
            "A4-157", "Lyra",
            new WithPrecondition(
                    new And<>(new For(new IsDamaged(), new AttackerActive()),
                            new GreaterThan(new CountCards(new AttackerSide(), Zone.BENCH), new Literal(0))),
                    new PlainAction(
                            "Switch your Active Pokémon that has damage on it with 1 of your Benched Pokémon.",
                            new Attempt(List.of(new SwitchActive(new AttackerSide()))))));
    /**
     * A4-158 · Silver — playable with any cards in the opponent's hand, since
     * asking for a Supporter would tell you what is there before the reveal.
     */
    public static final SupporterCard SILVER = SupporterCard.of(
            "A4-158", "Silver",
            new WithPrecondition(
                    new GreaterThan(new CountCards(new OpponentSide(), Zone.HAND), new Literal(0)),
                    new PlainAction(
                            "Your opponent reveals their hand. Choose a Supporter card you find there and shuffle it into your opponent's deck.",
                            new Attempt(List.of(
                                    new RevealHand(new OpponentSide()),
                                    new ShuffleFromHand(new OpponentSide(), new IsSupporter()))))));
    /**
     * A4-159 · Fisher — only playable with a Water Pokémon in the discard pile.
     */
    public static final SupporterCard FISHER = SupporterCard.of(
            "A4-159", "Fisher",
            new WithPrecondition(
                    new GreaterThan(new CountCards(new AttackerSide(), Zone.DISCARD, Optional.of(new IsType(Type.WATER))), new Literal(0)),
                    new PlainAction(
                            "Flip 3 coins. For each heads, a Water Pokémon is chosen at random from your discard pile and put into your hand.",
                            new Attempt(List.of(
                                    new FlipN(new Literal(3)),
                                    new RepeatEffect(new NumberHeads(), new RecoverFromDiscard(new AttackerSide(), new IsType(Type.WATER))))))));
    /**
     * A4-160 · Jasmine
     */
    public static final SupporterCard JASMINE = SupporterCard.of(
            "A4-160", "Jasmine",
            new PlainAction(
                    "During your opponent's next turn, all of your Steelix and Skarmory ex take −50 damage from attacks from your opponent's Pokémon.",
                    new Attempt(List.of(new ReduceSideDamageTaken(new Literal(50),
                            new Any<CardInstance>(List.of(new IsSpecies("Steelix"), new IsSpecies("Skarmory ex"))), new Literal(1))))));
    /**
     * A4-161 · Hiker — only playable with a Fighting Pokémon in play and a card to look at.
     */
    public static final SupporterCard HIKER = SupporterCard.of(
            "A4-161", "Hiker",
            new WithPrecondition(
                    new And<>(new GreaterThan(FIGHTING_IN_PLAY, new Literal(0)),
                            new GreaterThan(new CountCards(new AttackerSide(), Zone.DECK), new Literal(0))),
                    new PlainAction(
                            "For each of your Fighting Pokémon in play, look at that many cards from the top of your deck and put them back in any order.",
                            new Attempt(List.of(new ArrangeTopCards(FIGHTING_IN_PLAY, new AttackerSide()))))));

    /** A4-196 · Will (alternate art of A4-156) */
    public static final SupporterCard WILL_UR = WILL.withId("A4-196", CardRarity.ULTRA_RARE);

    /** A4-197 · Lyra (alternate art of A4-157) */
    public static final SupporterCard LYRA_UR = LYRA.withId("A4-197", CardRarity.ULTRA_RARE);

    /** A4-198 · Silver (alternate art of A4-158) */
    public static final SupporterCard SILVER_UR = SILVER.withId("A4-198", CardRarity.ULTRA_RARE);

    /** A4-199 · Fisher (alternate art of A4-159) */
    public static final SupporterCard FISHER_UR = FISHER.withId("A4-199", CardRarity.ULTRA_RARE);

    /** A4-200 · Jasmine (alternate art of A4-160) */
    public static final SupporterCard JASMINE_UR = JASMINE.withId("A4-200", CardRarity.ULTRA_RARE);

    /** A4-201 · Hiker (alternate art of A4-161) */
    public static final SupporterCard HIKER_UR = HIKER.withId("A4-201", CardRarity.ULTRA_RARE);

    static final List<ITrainerCard> CARDS = List.of(
            ELEMENTAL_SWITCH, SQUIRT_BOTTLE, STEEL_APRON, DARK_PENDANT, RESCUE_SCARF,
            WILL, LYRA, SILVER, FISHER, JASMINE, HIKER,
            WILL_UR, LYRA_UR, SILVER_UR, FISHER_UR, JASMINE_UR, HIKER_UR);

    private Trainers() {
    }
}
