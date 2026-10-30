package com.tcgpocket.pool.A3a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.EventSideIs;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.Not;
import com.tcgpocket.condition.OwnersFirstTurn;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.AttachEnergy;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.ReduceDamageDealt;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.AttackDeclared;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnEnd;

import java.util.List;

/**
 * Extradimensional Crisis — Lightning.
 */
public final class Lightning {

    /**
     * A3a-013 - Shinx
     */
    public static final PokemonCard SHINX = PokemonCard.basic(
            "A3a-013", "Shinx", "The extension and contraction of its muscles generates electricity. It glows when in trouble.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3a-014 - Luxio
     */
    public static final PokemonCard LUXIO = PokemonCard.evolution(
            "A3a-014", "Luxio", "Strong electricity courses through the tips of its sharp claws. A light scratch causes fainting in foes.",
            1, "Shinx", 80, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Head Bolt", "", EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3a-015 - Luxray — softens the attacker as it declares, for that turn only.
     */
    public static final PokemonCard LUXRAY = PokemonCard.evolution(
            "A3a-015", "Luxray", "It can see clearly through walls to track down its prey and seek its lost young.",
            2, "Luxio", 130, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Electric Ball", "", EnergyCost.of(Type.LIGHTNING, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING).withAbility(new PassiveAbility("Intimidating Fang",
            "As long as this Pokémon is in the Active Spot, attacks used by your opponent's Active Pokémon do −20 damage.",
            List.of(new Trigger(AttackDeclared.class,
                    new And<>(new Not<>(new EventSideIs(new SelfSide())), new For(new IsActive(), new Self())),
                    new Attempt(List.of(new ReduceDamageDealt(new Literal(20), new AttackerActive(), new Literal(0))))))));

    /**
     * A3a-016 - Blitzle
     */
    public static final PokemonCard BLITZLE = PokemonCard.basic(
            "A3a-016", "Blitzle", "When thunderclouds cover the sky, it will appear. It can catch lightning with its mane and store the electricity.",
            60, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Rear Kick", "", EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3a-017 - Zebstrika
     */
    public static final PokemonCard ZEBSTRIKA = PokemonCard.evolution(
            "A3a-017", "Zebstrika", "When this ill-tempered Pokémon runs wild, it shoots lightning from its mane in all directions.",
            1, "Blitzle", 90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Head Bolt", "", EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3a-018 - Emolga
     */
    public static final PokemonCard EMOLGA = PokemonCard.basic(
            "A3a-018", "Emolga", "As Emolga flutters through the air, it crackles with electricity. This Pokémon is cute, but it can cause a lot of trouble.",
            70, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Static Shock", "", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3a-019 - Tapu Koko ex
     */
    public static final PokemonCard TAPU_KOKO_EX = PokemonCard.basic(
            "A3a-019", "Tapu Koko ex", "",
            130, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Plasma Hurricane", "Take a Lightning Energy from your Energy Zone and attach it to this Pokémon.", EnergyCost.of(Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new AttachEnergy(Type.LIGHTNING, new Literal(1), new Self())))
            ), new Action(
                    "Mach Bolt", "", EnergyCost.of(Type.LIGHTNING, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX);

    /**
     * A3a-020 - Xurkitree
     */
    public static final PokemonCard XURKITREE = PokemonCard.basic(
            "A3a-020", "Xurkitree", "Although it's alien to this world and a danger here, it's apparently a common organism in the world where it normally lives.",
            100, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Electronica", "Your opponent's Active Pokémon is now Confused.", EnergyCost.of(Type.LIGHTNING, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new AddStatus(new ConfusionStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING).withTags(CardTag.ULTRA_BEAST);

    /**
     * A3a-021 - Zeraora — {@code TurnEnd} fires before the turn is marked taken,
     * so {@code OwnersFirstTurn} still holds at the end of that first turn.
     */
    public static final PokemonCard ZERAORA = PokemonCard.basic(
            "A3a-021", "Zeraora", "It electrifies its claws and tears its opponents apart with them. Even if they dodge its attack, they'll be electrocuted by the flying sparks.",
            90, Type.LIGHTNING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Lightning Claw", "", EnergyCost.of(Type.LIGHTNING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING).withAbility(new PassiveAbility("Thunderclap Flash",
            "At the end of your first turn, take a Lightning Energy from your Energy Zone and attach it to this Pokémon.",
            List.of(new Trigger(TurnEnd.class,
                    new And<>(new EventSideIs(new SelfSide()), new For(new OwnersFirstTurn(), new Self())),
                    new Attempt(List.of(new AttachEnergy(Type.LIGHTNING, new Literal(1), new Self())))))));

    /** A3a-077 · Tapu Koko ex (alternate art of A3a-019) */
    public static final PokemonCard TAPU_KOKO_EX_UR = TAPU_KOKO_EX.withId("A3a-077", CardRarity.ULTRA_RARE);

    /** A3a-084 · Tapu Koko ex (alternate art of A3a-019) */
    public static final PokemonCard TAPU_KOKO_EX_SIR = TAPU_KOKO_EX.withId("A3a-084", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            SHINX, LUXIO, LUXRAY, BLITZLE, ZEBSTRIKA, EMOLGA,
            TAPU_KOKO_EX, XURKITREE, ZERAORA,
            TAPU_KOKO_EX_UR,
            TAPU_KOKO_EX_SIR);

    private Lightning() {
    }
}
