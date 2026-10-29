package com.tcgpocket.pool.A2a;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Triumphant Light — Psychic.
 */
public final class Psychic {

    /**
     * A2a-029 - Clefairy
     */
    public static final PokemonCard CLEFAIRY = PokemonCard.basic(
            "A2a-029", "Clefairy", "It is said that happiness will come to those who see a gathering of Clefairy dancing under a full moon.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Smack", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A2a-030 - Clefable
     */
    public static final PokemonCard CLEFABLE = PokemonCard.evolution(
            "A2a-030", "Clefable", "A timid fairy Pokémon that is rarely seen, it will run and hide the moment it senses people.",
            1, "Clefairy", 100, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Moonblast", "During your opponent's next turn, attacks used by the Defending Pokémon do −30 damage.",
                    EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new ReduceDamageDealt(new Literal(30), new OpponentActive(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /**
     * A2a-031 - Gastly
     */
    public static final PokemonCard GASTLY = PokemonCard.basic(
            "A2a-031", "Gastly", "It wraps its opponent in its gas-like body, slowly weakening its prey by poisoning it through the skin.",
            50, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Astonish", "Flip a coin. If heads, your opponent reveals a random card from their hand and shuffles it into their deck.",
                    EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(),
                            new ShuffleRandomFromHand(new OpponentSide())))))
            ), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2a-032 - Haunter
     */
    public static final PokemonCard HAUNTER = PokemonCard.evolution(
            "A2a-032", "Haunter", "It likes to lurk in the dark and tap shoulders with a gaseous hand. Its touch causes endless shuddering.",
            1, "Gastly", 80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Mumble", "", EnergyCost.of(Type.PSYCHIC, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2a-033 - Gengar
     */
    public static final PokemonCard GENGAR = PokemonCard.evolution(
            "A2a-033", "Gengar", "To steal the life of its target, it slips into the prey's shadow and silently waits for an opportunity.",
            2, "Haunter", 140, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Hypnoblast", "Your opponent's Active Pokémon is now Asleep.", EnergyCost.of(Type.PSYCHIC, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new AddStatus(new SleepStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2a-034 - Unown (CHECK).
     *
     * <p><b>Narrowed.</b> "You may choose either player" is cut to your own
     * deck: {@link LookAtTopCards} shows a deck only to its owner, so looking
     * at the opponent's would show it to them instead. A viewer on
     * {@code LookAtTopCards} (an {@code ISideTarget} for who sees it) would
     * fix it, with a {@code ChoiceAction} over the two decks.
     */
    public static final PokemonCard UNOWN = PokemonCard.basic(
            "A2a-034", "Unown", "Its flat, thin body is always stuck on walls. Its shape appears to have some meaning.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Hidden Power", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS).withAbility(new ActivatedAbility("CHECK",
            "Once during your turn, you may choose either player. Look at the top card of that player's deck.",
            new PlainAction("", new Attempt(List.of(
                    new LookAtTopCards(new Literal(1), new AttackerSide())))),
            true, new GreaterThan(new CountCards(new AttackerSide(), Zone.DECK), new Literal(0))));

    /**
     * A2a-035 - Rotom
     */
    public static final PokemonCard ROTOM = PokemonCard.basic(
            "A2a-035", "Rotom", "Its electricity-like body can enter some kinds of machines and take control in order to make mischief.",
            70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Dash Attack", "This attack does 20 damage to 1 of your opponent's Benched Pokémon.",
                    EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Select a Benched Pokémon"))))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS).withAbility(Links.speedLink());

    static final List<PokemonCard> CARDS = List.of(CLEFAIRY, CLEFABLE, GASTLY, HAUNTER, GENGAR, UNOWN, ROTOM);

    private Psychic() {
    }
}
