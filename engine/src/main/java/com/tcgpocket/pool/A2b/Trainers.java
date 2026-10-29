package com.tcgpocket.pool.A2b;

import com.tcgpocket.action.PlainAction;
import com.tcgpocket.action.WithPrecondition;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.ITrainerCard;
import com.tcgpocket.card.ItemCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.*;
import com.tcgpocket.target.*;

import java.util.List;
import java.util.Optional;

/**
 * Shining Revelry — Trainers.
 *
 * <p>Trainers are only ever played on your own turn, so "your" is
 * {@code Attacker*} throughout.
 */
public final class Trainers {

    /** Damaged or affected by any Special Condition: something Pokémon Center Lady would fix. */
    private static final ICondition<PokemonInPlay> NEEDS_CARE = new Any<>(List.of(
            new IsDamaged(), new IsAsleep(), new IsBurned(), new IsConfused(), new IsParalyzed(), new IsPoisoned()));

    /**
     * A2b-069 · Iono
     */
    public static final SupporterCard IONO = SupporterCard.of(
            "A2b-069", "Iono",
            new PlainAction(
                    "Each player shuffles the cards in their hand into their deck, then draws that many cards.",
                    new Attempt(List.of(
                            new ShuffleHandIntoDeck(new AttackerSide(), true),
                            new ShuffleHandIntoDeck(new OpponentSide(), true)))));
    /**
     * A2b-070 · Pokémon Center Lady — only playable with a Pokémon she would
     * heal or cure.
     */
    public static final SupporterCard POKEMON_CENTER_LADY = SupporterCard.of(
            "A2b-070", "Pokémon Center Lady",
            new WithPrecondition(
                    new ForAny(NEEDS_CARE, new AttackerAll()),
                    new PlainAction(
                            "Heal 30 damage from 1 of your Pokémon, and it recovers from all Special Conditions.",
                            new Attempt(List.of(
                                    new ChooseThen(
                                            new ChosenFrom(new AttackerAll(), new AttackerSide(), NEEDS_CARE, "Choose a Pokémon to heal"),
                                            List.of(
                                                    new HealDamage(new Literal(30), new PlayTarget()),
                                                    new RemoveStatus(new PlayTarget(), new SleepStatus()),
                                                    new RemoveStatus(new PlayTarget(), new BurnStatus()),
                                                    new RemoveStatus(new PlayTarget(), new ConfusionStatus()),
                                                    new RemoveStatus(new PlayTarget(), new ParalysisStatus()),
                                                    new RemoveStatus(new PlayTarget(), new PoisonStatus()))))))));
    /**
     * A2b-071 · Red
     */
    public static final SupporterCard RED = SupporterCard.of(
            "A2b-071", "Red",
            new PlainAction(
                    "During this turn, attacks used by your Pokémon do +20 damage to your opponent's Active Pokémon ex.",
                    new Attempt(List.of(
                            new IncreaseSideDamage(
                                    new Literal(20), Optional.empty(), Optional.of(new HasTag(CardTag.EX)), new Literal(0))))));
    /**
     * A2b-072 · Team Rocket Grunt — one Energy per heads, each only while any
     * is left, so a run of heads longer than the Energy attached discards all
     * of it rather than failing the card.
     */
    public static final SupporterCard TEAM_ROCKET_GRUNT = SupporterCard.of(
            "A2b-072", "Team Rocket Grunt",
            new PlainAction(
                    "Flip a coin until you get tails. For each heads, discard a random Energy from your opponent's Active Pokémon.",
                    new Attempt(List.of(
                            new FlipUntilTails(),
                            new RepeatEffect(new NumberHeads(), new ConditionalEffect(
                                    new For(new HasEnergy(EnergyCost.of(Type.COLORLESS, 1)), new OpponentActive()),
                                    new DiscardRandomEnergy(new Literal(1), new OpponentActive())))))));

    /** A2b-088 · Iono (alternate art of A2b-069) */
    public static final SupporterCard IONO_UR = IONO.withId("A2b-088");
    /** A2b-089 · Pokémon Center Lady (alternate art of A2b-070) */
    public static final SupporterCard POKEMON_CENTER_LADY_UR = POKEMON_CENTER_LADY.withId("A2b-089");
    /** A2b-090 · Red (alternate art of A2b-071) */
    public static final SupporterCard RED_UR = RED.withId("A2b-090");
    /** A2b-091 · Team Rocket Grunt (alternate art of A2b-072) */
    public static final SupporterCard TEAM_ROCKET_GRUNT_UR = TEAM_ROCKET_GRUNT.withId("A2b-091");

    /** A2b-111 · Poké Ball (reprint of P-A-005) */
    public static final ItemCard POKE_BALL_HR = com.tcgpocket.pool.PA.Trainers.POKE_BALL.withId("A2b-111");

    static final List<ITrainerCard> CARDS = List.of(
            IONO, POKEMON_CENTER_LADY, RED, TEAM_ROCKET_GRUNT,
            IONO_UR, POKEMON_CENTER_LADY_UR, RED_UR, TEAM_ROCKET_GRUNT_UR, POKE_BALL_HR);

    private Trainers() {
    }
}
