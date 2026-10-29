package com.tcgpocket.pool.A3;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.BurnStatus;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.*;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnStart;

import java.util.List;
import java.util.Optional;

/**
 * Celestial Guardians — Darkness.
 */
public final class Darkness {

    /**
     * A3-106 - Alolan Rattata
     */
    public static final PokemonCard ALOLAN_RATTATA = PokemonCard.basic(
            "A3-106", "Alolan Rattata", "It shows no interest in anything that isn't fresh. If you take it shopping with you, it will help you pick out ingredients.",
            50, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3-107 - Alolan Raticate
     */
    public static final PokemonCard ALOLAN_RATICATE = PokemonCard.evolution(
            "A3-107", "Alolan Raticate", "It has an incredibly greedy personality. Its nest is filled with so much food gathered by Rattata at its direction, it can't possibly eat it all.",
            1, "Alolan Rattata", 110, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Scrounge-and-Scarf", "Discard a random Item card from your opponent's hand.", EnergyCost.of(Type.DARKNESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new DiscardRandomFromHand(new OpponentSide(), new Literal(1), Optional.of(new IsItem()))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3-108 - Alolan Meowth
     */
    public static final PokemonCard ALOLAN_MEOWTH = PokemonCard.basic(
            "A3-108", "Alolan Meowth", "It's accustomed to luxury because it used to live with Alolan royalty. As a result, it's very picky about food.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Scratch", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3-109 - Alolan Persian
     */
    public static final PokemonCard ALOLAN_PERSIAN = PokemonCard.evolution(
            "A3-109", "Alolan Persian", "The round face of Alolan Persian is considered to be a symbol of prosperity in the Alola region, so these Pokémon are very well cared for.",
            1, "Alolan Meowth", 100, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Claw Slash", "", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.RARE
    ).withAbility(new PassiveAbility("Fur Coat", "This Pokémon takes -20 damage from attacks.", List.of(
            new Trigger(TurnStart.class, new Not<>(new EventSideIs(new SelfSide())),
                    new Attempt(List.of(new ReduceDamageTaken(new Literal(20), new Self(), new Literal(0))))))))
            .withWeakness(Type.GRASS);

    /**
     * A3-110 - Alolan Grimer
     */
    public static final PokemonCard ALOLAN_GRIMER = PokemonCard.basic(
            "A3-110", "Alolan Grimer", "There are a hundred or so of them living in Alola's waste-disposal site. They're all hard workers who eat a lot of trash.",
            70, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Sludge Toss", "", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-111 - Alolan Muk ex
     */
    public static final PokemonCard ALOLAN_MUK_EX = PokemonCard.evolution(
            "A3-111", "Alolan Muk ex", "",
            1, "Alolan Grimer", 160, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Chemical Panic", "1 Special Condition from among Asleep, Burned, Confused, Paralyzed, and Poisoned is chosen at random, and your opponent's Active Pokémon is now affected by that Special Condition. Any Special Conditions already affecting that Pokémon will not be chosen.",
                    EnergyCost.of(Type.DARKNESS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new AddRandomStatus(List.of(new SleepStatus(), new BurnStatus(), new ConfusionStatus(),
                            new ParalysisStatus(), new PoisonStatus()), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX);

    /**
     * A3-112 - Absol
     */
    public static final PokemonCard ABSOL = PokemonCard.basic(
            "A3-112", "Absol", "Swift as the wind, Absol races through fields and mountains. Its curved, bow-like horn is acutely sensitive to the warning signs of natural disasters.",
            80, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Unseen Claw", "If your opponent's Active Pokémon is affected by a Special Condition, this attack does 60 more damage.",
                    EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(
                            new Branch.Case(new For(new Any<PokemonInPlay>(List.of(
                                    new IsAsleep(), new IsBurned(), new IsConfused(), new IsParalyzed(), new IsPoisoned())),
                                    new OpponentActive()), new Literal(80))
                    ), new Literal(20)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.GRASS);

    /**
     * A3-113 - Trubbish
     */
    public static final PokemonCard TRUBBISH = PokemonCard.basic(
            "A3-113", "Trubbish", "Its favorite places are unsanitary ones. If you leave trash lying around, you could even find one of these Pokémon living in your room.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Pound", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-114 - Garbodor
     */
    public static final PokemonCard GARBODOR = PokemonCard.evolution(
            "A3-114", "Garbodor", "This Pokémon eats trash, which turns into poison inside its body. The main component of the poison depends on what sort of trash was eaten.",
            1, "Trubbish", 120, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Super Poison Breath", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.DARKNESS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-115 - Mareanie
     */
    public static final PokemonCard MAREANIE = PokemonCard.basic(
            "A3-115", "Mareanie", "This Pokémon wanders the seaside looking for food. It often gets electric shocks from broken Pincurchin spines that it tries to eat.",
            70, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Pierce", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-116 - Toxapex
     */
    public static final PokemonCard TOXAPEX = PokemonCard.evolution(
            "A3-116", "Toxapex", "This Pokémon lives within a dome made by its own legs. Toxapex monitors its surroundings by sensing the flow of the tide through its spikes.",
            1, "Mareanie", 110, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Spike Cannon", "Flip 4 coins. This attack does 20 damage for each heads.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(4)),
                    new DealDamage(new Product(new Literal(20), new NumberHeads()), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /** A3-188 · Alolan Muk ex (alternate art of A3-111) */
    public static final PokemonCard ALOLAN_MUK_EX_UR = ALOLAN_MUK_EX.withId("A3-188", CardRarity.ULTRA_RARE);
    /** A3-206 · Alolan Muk ex (alternate art of A3-111) */
    public static final PokemonCard ALOLAN_MUK_EX_SIR = ALOLAN_MUK_EX.withId("A3-206", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            ALOLAN_RATTATA, ALOLAN_RATICATE, ALOLAN_MEOWTH, ALOLAN_PERSIAN, ALOLAN_GRIMER, ALOLAN_MUK_EX,
            ABSOL, TRUBBISH, GARBODOR, MAREANIE, TOXAPEX,
            ALOLAN_MUK_EX_UR, ALOLAN_MUK_EX_SIR);

    private Darkness() {
    }
}
