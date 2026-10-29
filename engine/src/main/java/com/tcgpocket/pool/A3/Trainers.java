package com.tcgpocket.pool.A3;

import com.tcgpocket.action.ChoiceAction;
import com.tcgpocket.action.MultiAttempt;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.action.PlayedOnto;
import com.tcgpocket.action.WithPrecondition;
import com.tcgpocket.card.HpBonus;
import com.tcgpocket.card.ITrainerCard;
import com.tcgpocket.card.ItemCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.card.ToolCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.DamageOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.*;
import com.tcgpocket.trigger.DamageDealt;
import com.tcgpocket.trigger.Trigger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Celestial Guardians — Trainers.
 *
 * <p>Trainers are only ever played on your own turn, so "your" is
 * {@code Attacker*} throughout. A Tool's triggers fire with its holder as the
 * source, so there {@code Self} is the Pokemon wearing it.
 */
public final class Trainers {

    /**
     * A3-142 · Big Malasada — only playable with something to fix.
     * TODO Check that the game refuses it on an undamaged, unaffected Active.
     */
    public static final ItemCard BIG_MALASADA = ItemCard.of(
            "A3-142", "Big Malasada",
            new WithPrecondition(
                    new For(new Any<PokemonInPlay>(List.of(
                            new IsDamaged(), new IsAsleep(), new IsBurned(), new IsConfused(), new IsParalyzed(), new IsPoisoned())),
                            new AttackerActive()),
                    new PlainAction(
                            "Heal 10 damage and remove a random Special Condition from your Active Pokémon.",
                            new Attempt(List.of(
                                    new HealDamage(new Literal(10), new AttackerActive()),
                                    new RemoveRandomStatus(new AttackerActive()))))));
    /**
     * A3-143 · Fishing Net — only playable with a Basic Water Pokémon in the
     * discard pile.
     */
    public static final ItemCard FISHING_NET = ItemCard.of(
            "A3-143", "Fishing Net",
            new WithPrecondition(
                    new GreaterThan(new CountCards(new AttackerSide(), Zone.DISCARD, Optional.of(basicWater())), new Literal(0)),
                    new PlainAction(
                            "Put a random Basic Water Pokémon from your discard pile into your hand.",
                            new Attempt(List.of(
                                    new RecoverFromDiscard(new AttackerSide(), basicWater()))))));
    /**
     * A3-144 · Rare Candy — dragged onto the Basic. The turn rules and the
     * Stage 2 in hand are {@link CanSkipStage1}'s, so it is only offered where
     * it would work.
     * TODO Check whether this approach is the most effective way of implementing this
     *  Also check interaction with fossils in game
     */
    public static final ItemCard RARE_CANDY = ItemCard.of(
            "A3-144", "Rare Candy",
            new WithPrecondition(
                    new CanSkipStage1(new PlayTarget()),
                    new PlayedOnto(
                            new Matching(new AttackerAll(), new IsBasic()),
                            "Choose 1 of your Basic Pokémon in play. If you have a Stage 2 card in your hand that evolves from that Pokémon, put that card onto the Basic Pokémon to evolve it, skipping the Stage 1. You can't use this card during your first turn or on a Basic Pokémon that was put into play this turn.",
                            new Attempt(List.of(
                                    new EvolveSkippingStage1(new PlayTarget()))))));
    /**
     * A3-145 · Rotom Dex — the look, then the "you may" as a
     * {@link ChoiceAction} between shuffling and not.
     */
    public static final ItemCard ROTOM_DEX = new ItemCard(
            "A3-145", "Rotom Dex", "", Set.of(), List.of(
                    new PlainAction(
                            "Look at the top card of your deck.",
                            new Attempt(List.of(
                                    new LookAtTopCards(new Literal(1), new AttackerSide())))),
                    new ChoiceAction(List.of(
                            new PlainAction("Shuffle your deck.", new Attempt(List.of(new ShuffleDeck(new AttackerSide())))),
                            new PlainAction("Don't shuffle.", new Attempt(List.of(new NoEffect())))),
                            new AttackerSide(), "Then, you may shuffle your deck.")));
    /**
     * A3-146 · Poison Barb — Rocky Helmet's trigger, poisoning instead.
     */
    public static final ToolCard POISON_BARB = new ToolCard(
            "A3-146", "Poison Barb",
            "If the Pokémon this card is attached to is your Active Pokémon and is damaged by an attack from your opponent's Pokémon, the Attacking Pokémon is now Poisoned.",
            Set.of(), List.of(), List.of(new Trigger(DamageDealt.class,
                    new And<>(new EventConcerns(new Self()), new For(new IsActive(), new Self())),
                    new Attempt(List.of(new AddStatus(new PoisonStatus(), new EventSource()))))),
            List.of());
    /**
     * A3-147 · Leaf Cape — attachable to anything, but only a Grass holder gets the HP.
     */
    public static final ToolCard LEAF_CAPE = new ToolCard(
            "A3-147", "Leaf Cape", "The Grass Pokémon this card is attached to gets +30 HP.",
            Set.of(), List.of(), List.of(), List.of(new HpBonus(30, new HasType(Type.GRASS))));
    /**
     * A3-148 · Acerola — dragged onto the damaged Palossand or Mimikyu.
     */
    public static final SupporterCard ACEROLA = SupporterCard.of(
            "A3-148", "Acerola",
            new PlayedOnto(
                    new Matching(new Matching(new AttackerAll(), species("Palossand", "Mimikyu")), new IsDamaged()),
                    "Choose 1 of your Palossand or Mimikyu that has damage on it, and move 40 of its damage to your opponent's Active Pokémon.",
                    new Attempt(List.of(
                            new MoveDamage(new PlayTarget(), new OpponentActive(), new Literal(40))))));
    /**
     * A3-149 · Ilima — dragged onto the damaged Colorless Pokémon.
     */
    public static final SupporterCard ILIMA = SupporterCard.of(
            "A3-149", "Ilima",
            new PlayedOnto(
                    new Matching(new AttackerAll(), new And<>(new HasType(Type.COLORLESS), new IsDamaged())),
                    "Put 1 of your Colorless Pokémon that has damage on it into your hand.",
                    new Attempt(List.of(
                            new ReturnToHand(new PlayTarget())))));
    /**
     * A3-150 · Kiawe — dragged onto the Alolan Marowak or Turtonator.
     */
    public static final SupporterCard KIAWE = SupporterCard.of(
            "A3-150", "Kiawe",
            new PlayedOnto(
                    new Matching(new AttackerAll(), species("Alolan Marowak", "Turtonator")),
                    "Choose 1 of your Alolan Marowak or Turtonator. Take 2 Fire Energy from your Energy Zone and attach it to that Pokémon. Your turn ends.",
                    new Attempt(List.of(
                            new AttachEnergy(Type.FIRE, new Literal(2), new PlayTarget()),
                            new EndTurn()))));
    /**
     * A3-151 · Guzma — "Discard all Pokémon Tool cards attached to each of
     * your opponent's Pokémon." One independent attempt per opponent's Pokémon, so an
     * empty Bench slot does not stop the rest. Only playable with a Tool to discard.
     */
    public static final SupporterCard GUZMA = SupporterCard.of(
            "A3-151", "Guzma",
            new WithPrecondition(
                    new ForAny(new HasTool(), new OpponentAll()),
                    new MultiAttempt(discardOpponentTools())));
    /**
     * A3-152 · Lana
     */
    public static final SupporterCard LANA = SupporterCard.of(
            "A3-152", "Lana",
            new WithPrecondition(
                    new And<>(
                            new ForAny(new IsSpecies("Araquanid"), new AttackerAll()),
                            new ForAny(new Always<>(), new OpponentBench())),
                    new PlainAction(
                            "You can use this card only if you have Araquanid in play. Switch in 1 of your opponent's Benched Pokémon to the Active Spot.",
                            new Attempt(List.of(
                                    new SwitchActive(new OpponentSide(), new ChosenFrom(
                                            new OpponentBench(), new AttackerSide(),
                                            "Choose a Benched Pokémon to switch in.")))))));
    /**
     * A3-153 · Sophocles
     */
    public static final SupporterCard SOPHOCLES = SupporterCard.of(
            "A3-153", "Sophocles",
            new PlainAction(
                    "During this turn, attacks used by your Alolan Golem, Vikavolt, or Togedemaru do +30 damage to your opponent's Active Pokémon.",
                    new Attempt(List.of(
                            new IncreaseSideDamage(
                                    new Literal(30), species("Alolan Golem", "Vikavolt", "Togedemaru"), new Literal(0))))));
    /**
     * A3-154 · Mallow — dragged onto the damaged Shiinotic or Tsareena. The
     * heal is never a failure there, so the Energy always goes.
     */
    public static final SupporterCard MALLOW = SupporterCard.of(
            "A3-154", "Mallow",
            new PlayedOnto(
                    new Matching(new Matching(new AttackerAll(), species("Shiinotic", "Tsareena")), new IsDamaged()),
                    "Heal all damage from 1 of your Shiinotic or Tsareena. If you do, discard all Energy from that Pokémon.",
                    new Attempt(List.of(
                            new HealDamage(new DamageOn(new PlayTarget()), new PlayTarget()),
                            new DiscardAllEnergy(new PlayTarget())))));
    /**
     * A3-155 · Lillie — dragged onto the damaged Stage 2.
     */
    public static final SupporterCard LILLIE = SupporterCard.of(
            "A3-155", "Lillie",
            new PlayedOnto(
                    new Matching(new Matching(new AttackerAll(), new IsStage(2)), new IsDamaged()),
                    "Heal 60 damage from 1 of your Stage 2 Pokémon.",
                    new Attempt(List.of(
                            new HealDamage(new Literal(60), new PlayTarget())))));

    // Ultra Rare alternate arts. Trainers carry no rarity yet, so only the id differs.
    /** A3-190 · Acerola (alternate art of A3-148) */
    public static final SupporterCard ACEROLA_UR = ACEROLA.withId("A3-190");
    /** A3-191 · Ilima (alternate art of A3-149) */
    public static final SupporterCard ILIMA_UR = ILIMA.withId("A3-191");
    /** A3-192 · Kiawe (alternate art of A3-150) */
    public static final SupporterCard KIAWE_UR = KIAWE.withId("A3-192");
    /** A3-193 · Guzma (alternate art of A3-151) */
    public static final SupporterCard GUZMA_UR = GUZMA.withId("A3-193");
    /** A3-194 · Lana (alternate art of A3-152) */
    public static final SupporterCard LANA_UR = LANA.withId("A3-194");
    /** A3-195 · Sophocles (alternate art of A3-153) */
    public static final SupporterCard SOPHOCLES_UR = SOPHOCLES.withId("A3-195");
    /** A3-196 · Mallow (alternate art of A3-154) */
    public static final SupporterCard MALLOW_UR = MALLOW.withId("A3-196");
    /** A3-197 · Lillie (alternate art of A3-155) */
    public static final SupporterCard LILLIE_UR = LILLIE.withId("A3-197");
    // Immersive alternate arts.
    /** A3-208 · Guzma (alternate art of A3-151) */
    public static final SupporterCard GUZMA_IM = GUZMA.withId("A3-208");
    /** A3-209 · Lillie (alternate art of A3-155) */
    public static final SupporterCard LILLIE_IM = LILLIE.withId("A3-209");

    static final List<ITrainerCard> CARDS = List.of(
            BIG_MALASADA, FISHING_NET, RARE_CANDY, ROTOM_DEX, POISON_BARB, LEAF_CAPE,
            ACEROLA, ILIMA, KIAWE, GUZMA, LANA, SOPHOCLES, MALLOW, LILLIE,
            ACEROLA_UR, ILIMA_UR, KIAWE_UR, GUZMA_UR, LANA_UR, SOPHOCLES_UR, MALLOW_UR, LILLIE_UR, GUZMA_IM, LILLIE_IM);

    private Trainers() {
    }

    /**
     * "Your Palossand or Mimikyu": any of the named species.
     */
    private static ICondition<CardInstance> species(String... names) {
        return new Any<>(Arrays.stream(names).<ICondition<CardInstance>>map(IsSpecies::new).toList());
    }

    private static ICondition<CardInstance> basicWater() {
        return new And<>(new IsBasic(), new IsType(Type.WATER));
    }

    /** Guzma's "each of your opponent's Pokémon": the Active and every Bench slot. */
    private static List<IAttempt> discardOpponentTools() {
        List<IAttempt> attempts = new ArrayList<>();
        attempts.add(new Attempt(List.of(new DiscardTool(new OpponentActive()))));
        for (int slot = 0; slot < Side.BENCH_LIMIT; slot++) {
            attempts.add(new Attempt(List.of(new DiscardTool(new OpponentBenchSpecific(slot)))));
        }
        return attempts;
    }
}
