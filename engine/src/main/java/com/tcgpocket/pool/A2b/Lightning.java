package com.tcgpocket.pool.A2b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.*;
import com.tcgpocket.target.*;
import com.tcgpocket.trigger.DamageDealt;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * Shining Revelry — Lightning.
 */
public final class Lightning {

    /**
     * A2b-022 - Pikachu ex
     */
    public static final PokemonCard PIKACHU_EX = PokemonCard.basic(
            "A2b-022", "Pikachu ex", "",
            120, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Thunderbolt", "Discard all Energy from this Pokémon.", EnergyCost.of(Type.LIGHTNING, 3), new Attempt(List.of(
                    new DealDamage(new Literal(150), new OpponentActive()),
                    new DiscardAllEnergy(new Self())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX);

    /**
     * A2b-023 - Voltorb
     */
    public static final PokemonCard VOLTORB = PokemonCard.basic(
            "A2b-023", "Voltorb", "It rolls to move. If the ground is uneven, a sudden jolt from hitting a bump can cause it to explode.",
            70, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Lightning Ball", "", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-024 - Electrode
     */
    public static final PokemonCard ELECTRODE = PokemonCard.evolution(
            "A2b-024", "Electrode", "The more energy it charges up, the faster it gets. But this also makes it more likely to explode.",
            1, "Voltorb", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tumbling Attack", "Flip a coin. If heads, this attack does 30 more damage.", EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(new Literal(50), new Product(new NumberHeads(), new Literal(30))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-025 - Pachirisu
     */
    public static final PokemonCard PACHIRISU = PokemonCard.basic(
            "A2b-025", "Pachirisu", "It's one of the kinds of Pokémon with electric cheek pouches. It shoots charges from its tail.",
            70, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Plasma", "Take a Lightning Energy from your Energy Zone and attach it to 1 of your Benched Pokémon.",
                    EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new AttachEnergy(Type.LIGHTNING, new ChosenFrom(
                            new AttackerBench(), new AttackerSide(), "Choose a Benched Pokémon to attach a Lightning Energy to"))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-026 - Pawmi
     */
    public static final PokemonCard PAWMI = PokemonCard.basic(
            "A2b-026", "Pawmi", "It has underdeveloped electric sacs on its cheeks. These sacs can produce electricity only if Pawmi rubs them furiously with the pads on its forepaws.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Punch", "", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-027 - Pawmo
     */
    public static final PokemonCard PAWMO = PokemonCard.evolution(
            "A2b-027", "Pawmo", "When its group is attacked, Pawmo is the first to leap into battle, defeating enemies with a fighting technique that utilizes electric shocks.",
            1, "Pawmi", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Punch", "", EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-028 - Pawmot — Counterattack is the same trigger as A1a-056
     * Druddigon's Rough Skin.
     */
    public static final PokemonCard PAWMOT = PokemonCard.evolution(
            "A2b-028", "Pawmot", "This Pokémon normally is slow to react, but once it enters battle, it will strike down its enemies with lightning-fast movements.",
            2, "Pawmo", 140, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                    "Electric Punch", "", EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING).withAbility(new PassiveAbility("Counterattack",
            "If this Pokémon is in the Active Spot and is damaged by an attack from your opponent's Pokémon, do 20 damage to the Attacking Pokémon.",
            List.of(new Trigger(DamageDealt.class,
                    new And<>(new EventConcerns(new Self()), new For(new IsActive(), new Self())),
                    new Attempt(List.of(new PlaceDamage(new Literal(20), new EventSource())))))));

    /** A2b-082 · Pikachu ex (alternate art of A2b-022) */
    public static final PokemonCard PIKACHU_EX_UR = PIKACHU_EX.withId("A2b-082", CardRarity.ULTRA_RARE);
    /** A2b-092 · Pikachu ex (alternate art of A2b-022) */
    public static final PokemonCard PIKACHU_EX_SIR = PIKACHU_EX.withId("A2b-092", CardRarity.SPECIAL_ILLUSTRATION_RARE);
    /** A2b-103 · Pachirisu (alternate art of A2b-025) */
    public static final PokemonCard PACHIRISU_SR = PACHIRISU.withId("A2b-103", CardRarity.SHINY_RARE);

    static final List<PokemonCard> CARDS = List.of(
            PIKACHU_EX, VOLTORB, ELECTRODE, PACHIRISU, PAWMI, PAWMO, PAWMOT,
            PIKACHU_EX_UR, PIKACHU_EX_SIR, PACHIRISU_SR);

    private Lightning() {
    }
}
