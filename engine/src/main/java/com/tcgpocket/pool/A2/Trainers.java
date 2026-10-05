package com.tcgpocket.pool.A2;

import com.tcgpocket.action.PlainAction;
import com.tcgpocket.action.PlayedOnto;
import com.tcgpocket.action.WithPrecondition;
import com.tcgpocket.card.HpBonus;
import com.tcgpocket.card.ITrainerCard;
import com.tcgpocket.card.ItemCard;
import com.tcgpocket.card.PlayableItemCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Difference;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Points;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.BurnStatus;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.*;
import com.tcgpocket.trigger.DamageDealt;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnEnd;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Space-Time Smackdown — Trainers.
 *
 * <p>Trainers are only ever played on your own turn, so "your" is
 * {@code Attacker*} throughout. A Tool's triggers fire with its holder as the
 * source, so there {@code Self} is the Pokemon wearing it.
 */
public final class Trainers {

    /**
     * A2-144 · Skull Fossil
     */
    public static final PlayableItemCard SKULL_FOSSIL = PlayableItemCard.fossil(
            "A2-144", "Skull Fossil", 40, com.tcgpocket.pool.A1.Trainers.discardFromPlay());
    /**
     * A2-145 · Armor Fossil
     */
    public static final PlayableItemCard ARMOR_FOSSIL = PlayableItemCard.fossil(
            "A2-145", "Armor Fossil", 40, com.tcgpocket.pool.A1.Trainers.discardFromPlay());
    /**
     * A2-146 · Pokémon Communication — only playable with a Pokémon both in
     * hand and in the deck, since otherwise there is nothing to swap.
     */
    public static final ItemCard POKEMON_COMMUNICATION = ItemCard.of(
            "A2-146", "Pokémon Communication",
            new WithPrecondition(
                    new And<>(
                            new GreaterThan(new CountCards(new AttackerSide(), Zone.HAND, Optional.of(new IsPokemon())), new Literal(0)),
                            new GreaterThan(new CountCards(new AttackerSide(), Zone.DECK, Optional.of(new IsPokemon())), new Literal(0))),
                    new PlainAction(
                            "Choose a Pokémon in your hand and switch it with a random Pokémon in your deck.",
                            new Attempt(List.of(
                                    new SwapFromDeck(new AttackerSide(), new IsPokemon()))))));
    /**
     * A2-147 · Giant Cape
     */
    public static final ToolCard GIANT_CAPE = new ToolCard(
            "A2-147", "Giant Cape", "The Pokémon this card is attached to gets +20 HP.",
            Set.of(), List.of(), List.of(), List.of(new HpBonus(20)));
    /**
     * A2-148 · Rocky Helmet — the same trigger as A1a-056 Druddigon's Rough Skin.
     */
    public static final ToolCard ROCKY_HELMET = new ToolCard(
            "A2-148", "Rocky Helmet",
            "If the Pokémon this card is attached to is in the Active Spot and is damaged by an attack from your opponent's Pokémon, do 20 damage to the Attacking Pokémon.",
            Set.of(), List.of(), List.of(new Trigger(DamageDealt.class,
                    new And<>(new EventConcerns(new Self()), new For(new IsActive(), new Self())),
                    new Attempt(List.of(new PlaceDamage(new Literal(20), new EventSource()))))),
            List.of());
    /**
     * A2-149 · Lum Berry — every status counts, Poisoned and Burned included,
     * as Pocket calls all five Special Conditions.
     */
    public static final ToolCard LUM_BERRY = new ToolCard(
            "A2-149", "Lum Berry",
            "At the end of each turn, if the Pokémon this card is attached to is affected by any Special Conditions, it recovers from all of them, and discard this card.",
            Set.of(), List.of(), List.of(new Trigger(TurnEnd.class,
                    new For(new Any<PokemonInPlay>(List.of(
                            new IsAsleep(), new IsBurned(), new IsConfused(), new IsParalyzed(), new IsPoisoned())), new Self()),
                    new Attempt(List.of(
                            new RemoveStatus(new Self(), new SleepStatus()),
                            new RemoveStatus(new Self(), new BurnStatus()),
                            new RemoveStatus(new Self(), new ConfusionStatus()),
                            new RemoveStatus(new Self(), new ParalysisStatus()),
                            new RemoveStatus(new Self(), new PoisonStatus()),
                            new DiscardTool(new Self()))))),
            List.of());
    /**
     * A2-150 · Cyrus
     */
    public static final SupporterCard CYRUS = SupporterCard.of(
            "A2-150", "Cyrus",
            new WithPrecondition(
                    new ForAny(new IsDamaged(), new OpponentBench()),
                    new PlainAction(
                            "Switch in 1 of your opponent's Benched Pokémon that has damage on it to the Active Spot.",
                            new Attempt(List.of(
                                    new SwitchActive(new OpponentSide(), new ChosenFrom(
                                            new OpponentBench(), new AttackerSide(), new IsDamaged(),
                                            "Choose a damaged Benched Pokémon to switch in.")))))));
    /**
     * A2-151 · Team Galactic Grunt
     */
    public static final SupporterCard TEAM_GALACTIC_GRUNT = SupporterCard.of(
            "A2-151", "Team Galactic Grunt",
            new PlainAction(
                    "Put 1 random Glameow, Stunky, or Croagunk from your deck into your hand.",
                    new Attempt(List.of(
                            new SearchDeck(new AttackerSide(), new Literal(1), species("Glameow", "Stunky", "Croagunk"))))));
    /**
     * A2-152 · Cynthia
     */
    public static final SupporterCard CYNTHIA = SupporterCard.of(
            "A2-152", "Cynthia",
            new PlainAction(
                    "During this turn, attacks used by your Garchomp or Togekiss do +50 damage to your opponent's Active Pokémon.",
                    new Attempt(List.of(
                            new IncreaseSideDamage(new Literal(50), species("Garchomp", "Togekiss"), new Literal(0))))));
    /**
     * A2-153 · Volkner — dragged onto the Electivire or Luxray. Playable with
     * no Lightning Energy in the discard pile, when it attaches nothing.
     * TODO Check that this is the correct behaviour in the game
     */
    public static final SupporterCard VOLKNER = SupporterCard.of(
            "A2-153", "Volkner",
            new PlayedOnto(
                    new Matching(new AttackerAll(), species("Electivire", "Luxray")),
                    "Choose 1 of your Electivire or Luxray. Attach 2 Lightning Energy from your discard pile to that Pokémon.",
                    new Attempt(List.of(
                            new AttachFromDiscard(Type.LIGHTNING, new Literal(2), new PlayTarget())))));
    /**
     * A2-154 · Dawn
     */
    public static final SupporterCard DAWN = SupporterCard.of(
            "A2-154", "Dawn",
            new WithPrecondition(
                    new ForAny(new HasEnergy(EnergyCost.of(Type.COLORLESS, 1)), new AttackerBench()),
                    new PlainAction(
                            "Move an Energy from 1 of your Benched Pokémon to your Active Pokémon.",
                            new Attempt(List.of(
                                    new MoveEnergy(
                                            new ChosenFrom(new AttackerBench(), new AttackerSide(),
                                                    new HasEnergy(EnergyCost.of(Type.COLORLESS, 1)),
                                                    "Choose a Benched Pokémon to move an Energy from."),
                                            new AttackerActive(), new Literal(1)))))));
    /**
     * A2-155 · Mars — three points win, so "remaining points needed" is 3
     * less the points they have.
     */
    public static final SupporterCard MARS = SupporterCard.of(
            "A2-155", "Mars",
            new PlainAction(
                    "Your opponent shuffles their hand into their deck and draws a card for each of their remaining points needed to win.",
                    new Attempt(List.of(
                            new ShuffleHandIntoDeck(new OpponentSide()),
                            new DrawCard(new Difference(new Literal(3), new Points(new OpponentSide())), new OpponentSide())))));

    static final List<ITrainerCard> CARDS = List.of(
            SKULL_FOSSIL, ARMOR_FOSSIL, POKEMON_COMMUNICATION, GIANT_CAPE, ROCKY_HELMET, LUM_BERRY,
            CYRUS, TEAM_GALACTIC_GRUNT, CYNTHIA, VOLKNER, DAWN, MARS);

    private Trainers() {
    }

    /**
     * "Your Garchomp or Togekiss": any of the named species.
     */
    private static ICondition<CardInstance> species(String... names) {
        return new Any<>(Arrays.stream(names).<ICondition<CardInstance>>map(IsSpecies::new).toList());
    }
}
