package com.tcgpocket.pool.A1;

import com.tcgpocket.action.PlainAction;
import com.tcgpocket.action.PlayedOnto;
import com.tcgpocket.action.WithPrecondition;
import com.tcgpocket.card.ITrainerCard;
import com.tcgpocket.card.PlayableItemCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.target.*;

import java.util.Arrays;
import java.util.List;

/**
 * Genetic Apex — Trainers.
 *
 * <p>Fossils and Supporters so far. A Fossil is played to the Bench like a
 * Basic; a Supporter resolves and is discarded, once a turn.
 *
 * <p>Supporters are only ever played on your own turn, so "your" is
 * {@code Attacker*} throughout.
 */
public final class Trainers {

    /**
     * A1-216 · Helix Fossil
     */
    public static final PlayableItemCard HELIX_FOSSIL = PlayableItemCard.fossil(
            "A1-216", "Helix Fossil", 40, discardFromPlay());
    /**
     * A1-217 · Dome Fossil
     */
    public static final PlayableItemCard DOME_FOSSIL = PlayableItemCard.fossil(
            "A1-217", "Dome Fossil", 40, discardFromPlay());
    /**
     * A1-218 · Old Amber
     */
    public static final PlayableItemCard OLD_AMBER = PlayableItemCard.fossil(
            "A1-218", "Old Amber", 40, discardFromPlay());
    /**
     * A1-219 · Erika — dragged onto the Grass Pokemon to heal.
     */
    public static final SupporterCard ERIKA = SupporterCard.of(
            "A1-219", "Erika",
            new PlayedOnto(
                    new Matching(new AttackerAll(), new HasType(Type.GRASS)),
                    "Heal 50 damage from 1 of your Grass Pokémon.",
                    new Attempt(List.of(
                            new HealDamage(new Literal(50), new PlayTarget())))));
    /**
     * A1-220 · Misty — dragged onto the Water Pokemon first, so the choice is
     * made before the coins are flipped, as printed.
     */
    public static final SupporterCard MISTY = SupporterCard.of(
            "A1-220", "Misty",
            new PlayedOnto(
                    new Matching(new AttackerAll(), new HasType(Type.WATER)),
                    "Choose 1 of your Water Pokémon, and flip a coin until you get tails. For each heads, take a Water Energy from your Energy Zone and attach it to that Pokémon.",
                    new Attempt(List.of(
                            new FlipUntilTails(),
                            new AttachEnergy(Type.WATER, new NumberHeads(), new PlayTarget())))));
    /**
     * A1-221 · Blaine
     */
    public static final SupporterCard BLAINE = SupporterCard.of(
            "A1-221", "Blaine",
            new PlainAction(
                    "During this turn, attacks used by your Ninetales, Rapidash, or Magmar do +30 damage to your opponent's Active Pokémon.",
                    new Attempt(List.of(
                            new IncreaseSideDamage(
                                    new Literal(30), species("Ninetales", "Rapidash", "Magmar"), new Literal(0))))));
    /**
     * A1-222 · Koga
     */
    public static final SupporterCard KOGA = SupporterCard.of(
            "A1-222", "Koga",
            new WithPrecondition(
                    new For(species("Muk", "Weezing"), new AttackerActive()),
                    new PlainAction(
                            "Put your Muk or Weezing in the Active Spot into your hand.",
                            new Attempt(List.of(
                                    new ReturnToHand(new AttackerActive()))))));
    /**
     * A1-223 · Giovanni
     */
    public static final SupporterCard GIOVANNI = SupporterCard.of(
            "A1-223", "Giovanni",
            new PlainAction(
                    "During this turn, attacks used by your Pokémon do +10 damage to your opponent's Active Pokémon.",
                    new Attempt(List.of(
                            new IncreaseSideDamage(new Literal(10), new Literal(0))))));
    /**
     * A1-224 · Brock — dragged onto the Golem or Onix.
     */
    public static final SupporterCard BROCK = SupporterCard.of(
            "A1-224", "Brock",
            new PlayedOnto(
                    new Matching(new AttackerAll(), species("Golem", "Onix")),
                    "Take 1 Fighting Energy from your Energy Zone and attach it to your Golem or Onix.",
                    new Attempt(List.of(
                            new AttachEnergy(Type.FIGHTING, new PlayTarget())))));
    /**
     * A1-225 · Sabrina — the opponent picks the replacement, which is SwitchActive's default.
     */
    public static final SupporterCard SABRINA = SupporterCard.of(
            "A1-225", "Sabrina",
            new PlainAction(
                    "Switch out your opponent's Active Pokémon to the Bench. (Your opponent chooses the new Active Pokémon.)",
                    new Attempt(List.of(
                            new SwitchActive(new OpponentSide())))));
    /**
     * A1-226 · Lt. Surge
     */
    public static final SupporterCard LT_SURGE = SupporterCard.of(
            "A1-226", "Lt. Surge",
            new WithPrecondition(
                    new For(species("Raichu", "Electrode", "Electabuzz"), new AttackerActive()),
                    new PlainAction(
                            "Move all Lightning Energy from your Benched Pokémon to your Raichu, Electrode, or Electabuzz in the Active Spot.",
                            new Attempt(List.of(
                                    new MoveTypeEnergy(Type.LIGHTNING, new AttackerBench(), new AttackerActive()))))));
    static final List<ITrainerCard> CARDS = List.of(
            HELIX_FOSSIL, DOME_FOSSIL, OLD_AMBER,
            ERIKA, MISTY, BLAINE, KOGA, GIOVANNI, BROCK, SABRINA, LT_SURGE);

    private Trainers() {
    }

    /**
     * "Your Muk or Weezing": any of the named species.
     */
    private static ICondition<CardInstance> species(String... names) {
        return new Any<>(Arrays.stream(names).<ICondition<CardInstance>>map(IsSpecies::new).toList());
    }

    /**
     * The printed text shared by every Fossil. "Can't retreat" and "as a Basic
     * Colorless Pokemon" are rules for {@code PlayCardAction} and
     * {@code RetreatAction} to learn, not effects, so only the discard is a node.
     */
    private static PlainAction discardFromPlay() {
        return new PlainAction(
                "Play this card as if it were a 40-HP Basic Colorless Pokémon. At any time during your turn, you can discard this card from play. This card can't retreat.",
                new Attempt(List.of(
                        new DiscardFromPlay(new Self()))));
    }
}
