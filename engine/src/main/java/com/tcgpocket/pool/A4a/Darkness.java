package com.tcgpocket.pool.A4a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.EventSideIs;
import com.tcgpocket.condition.EventSourceIs;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DamageEach;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.PreventDamage;
import com.tcgpocket.effect.PreventEffects;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentAll;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.Knockout;
import com.tcgpocket.trigger.Trigger;

import java.util.List;
import java.util.Optional;

/**
 * Secluded Springs (A4a): the Darkness cards, in printed set order.
 */
public final class Darkness {

    /**
     * A4a-048 - Seviper
     */
    public static final PokemonCard SEVIPER = PokemonCard.basic(
            "A4a-048", "Seviper", "Constant polishing makes the edge of the blade on its tail extremely sharp. It's Zangoose's archrival.",
            80, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Fateful Fang", "If your opponent's Active Pokémon is Zangoose, this attack does 40 more damage.", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(new GreaterThan(new CountCards(new OpponentSide(), Zone.ACTIVE, Optional.of(new IsSpecies("Zangoose"))), new Literal(0)), new Literal(80))), new Literal(40)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4a-049 - Zorua
     */
    public static final PokemonCard ZORUA = PokemonCard.basic(
            "A4a-049", "Zorua", "It changes into the forms of others to surprise them. Apparently, it often transforms into a silent child.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4a-050 - Zoroark
     */
    public static final PokemonCard ZOROARK = PokemonCard.evolution(
            "A4a-050", "Zoroark", "Stories say those who tried to catch Zoroark were trapped in an illusion and punished.",
            1, "Zorua", 100, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Night Daze", "", EnergyCost.of(Type.DARKNESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.GRASS).withAbility(new PassiveAbility("Illusive Trickery",
            "If your opponent's Pokémon is Knocked Out by damage from this Pokémon's attacks, during your opponent's next turn, prevent all damage from—and effects of—attacks done to this Pokémon.",
            List.of(new Trigger(Knockout.class,
                    new And<>(new EventSourceIs(new Self()), new Not<>(new EventSideIs(new SelfSide()))),
                    new Attempt(List.of(
                            new PreventDamage(new Self(), new Literal(1)),
                            new PreventEffects(new Self(), new Literal(1))))))));

    /**
     * A4a-051 - Inkay
     */
    public static final PokemonCard INKAY = PokemonCard.basic(
            "A4a-051", "Inkay", "It spins while making its luminescent spots flash. These spots allow it to communicate with others by using different patterns of light.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4a-052 - Malamar
     */
    public static final PokemonCard MALAMAR = PokemonCard.evolution(
            "A4a-052", "Malamar", "Gazing at its luminescent spots will quickly induce a hypnotic state, putting the observer under Malamar's control.",
            1, "Inkay", 90, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Mental Surge", "This attack does 20 damage to each of your opponent's Pokémon.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DamageEach(new Literal(20), new OpponentAll())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A4a-053 - Skrelp
     */
    public static final PokemonCard SKRELP = PokemonCard.basic(
            "A4a-053", "Skrelp", "Skrelp evades its enemies by hiding amid drifting seaweed. It eats rotten seaweed to create its poison.",
            50, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Melt", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4a-054 - Dragalge
     */
    public static final PokemonCard DRAGALGE = PokemonCard.evolution(
            "A4a-054", "Dragalge", "Using a liquid poison, Dragalge indiscriminately attacks anything that wanders into its territory. This poison can corrode the undersides of boats.",
            1, "Skrelp", 110, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sludge Bomb", "", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /** A4a-100 · Paldean Wooper (shiny of A2b-047) */
    public static final PokemonCard PALDEAN_WOOPER_SR = com.tcgpocket.pool.A2b.Darkness.PALDEAN_WOOPER.withId("A4a-100", CardRarity.SHINY_RARE);

    /** A4a-104 · Paldean Clodsire ex (shiny of A2b-048) */
    public static final PokemonCard PALDEAN_CLODSIRE_EX_SUR = com.tcgpocket.pool.A2b.Darkness.PALDEAN_CLODSIRE_EX.withId("A4a-104", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            SEVIPER, ZORUA, ZOROARK, INKAY, MALAMAR, SKRELP,
            DRAGALGE, PALDEAN_WOOPER_SR, PALDEAN_CLODSIRE_EX_SUR);

    private Darkness() {
    }
}
