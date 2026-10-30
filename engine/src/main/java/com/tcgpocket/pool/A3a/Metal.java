package com.tcgpocket.pool.A3a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.Fail;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.effect.PreventAttack;
import com.tcgpocket.effect.ReduceDamageTaken;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.target.EventSource;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.DamageDealt;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * Extradimensional Crisis — Metal.
 */
public final class Metal {

    /**
     * A3a-046 - Alolan Diglett
     */
    public static final PokemonCard ALOLAN_DIGLETT = PokemonCard.basic(
            "A3a-046", "Alolan Diglett", "The metal-rich geology of this Pokémon's habitat caused it to develop steel whiskers on its head.",
            60, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Happened to Headbutt", "Flip a coin. If tails, this attack does nothing.", EnergyCost.of(Type.METAL, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new Fail()),
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3a-047 - Alolan Dugtrio ex
     */
    public static final PokemonCard ALOLAN_DUGTRIO_EX = PokemonCard.evolution(
            "A3a-047", "Alolan Dugtrio ex", "",
            1, "Alolan Diglett", 130, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Triplet Headbutt", "Flip 3 coins. This attack does 60 damage for each heads.", EnergyCost.of(Type.METAL, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(3)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(60)), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX);

    /**
     * A3a-048 - Aron
     */
    public static final PokemonCard ARON = PokemonCard.basic(
            "A3a-048", "Aron", "It eats iron ore—and sometimes railroad tracks— to build up the steel armor that protects its body.",
            70, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Headbutt", "", EnergyCost.of(Type.METAL, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3a-049 - Lairon
     */
    public static final PokemonCard LAIRON = PokemonCard.evolution(
            "A3a-049", "Lairon", "It loves iron ore. Groups of them fight for territory by bashing one another with their steel bodies.",
            1, "Aron", 100, Type.METAL, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Lunge Out", "", EnergyCost.of(Type.METAL, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3a-050 - Aggron
     */
    public static final PokemonCard AGGRON = PokemonCard.evolution(
            "A3a-050", "Aggron", "Aggron has a horn sharp enough to perforate thick iron sheets. It brings down its opponents by ramming into them horn first.",
            2, "Lairon", 160, Type.METAL, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Giga Impact", "During your next turn, this Pokémon can't attack.", EnergyCost.of(Type.METAL, 2, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(150), new OpponentActive()),
                    new PreventAttack(new Self(), new Literal(2))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3a-051 - Ferroseed
     */
    public static final PokemonCard FERROSEED = PokemonCard.basic(
            "A3a-051", "Ferroseed", "It absorbs the iron it finds in the rock while clinging to the ceiling. It shoots spikes when in danger.",
            60, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Spike Sting", "", EnergyCost.of(Type.METAL, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3a-052 - Ferrothorn
     */
    public static final PokemonCard FERROTHORN = PokemonCard.evolution(
            "A3a-052", "Ferrothorn", "This Pokémon scrapes its spikes across rocks, and then uses the tips of its feelers to absorb the nutrients it finds within the stone.",
            1, "Ferroseed", 110, Type.METAL, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Spinning Attack", "", EnergyCost.of(Type.METAL, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE).withAbility(new PassiveAbility("Steel Spikes",
            "If this Pokémon is in the Active Spot and is damaged by an attack from your opponent's Pokémon, do 20 damage to the Attacking Pokémon.",
            List.of(new Trigger(DamageDealt.class,
                    new And<>(new EventConcerns(new Self()), new For(new IsActive(), new Self())),
                    new Attempt(List.of(new PlaceDamage(new Literal(20), new EventSource())))))));

    /**
     * A3a-053 - Stakataka
     */
    public static final PokemonCard STAKATAKA = PokemonCard.basic(
            "A3a-053", "Stakataka", "It appeared from an Ultra Wormhole. Each one appears to be made up of many life-forms stacked one on top of each other.",
            110, Type.METAL, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Brass Rock", "During your opponent's next turn, this Pokémon takes −20 damage from attacks.", EnergyCost.of(Type.METAL, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new ReduceDamageTaken(new Literal(20), new Self(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE).withTags(CardTag.ULTRA_BEAST);

    /** A3a-080 · Alolan Dugtrio ex (alternate art of A3a-047) */
    public static final PokemonCard ALOLAN_DUGTRIO_EX_UR = ALOLAN_DUGTRIO_EX.withId("A3a-080", CardRarity.ULTRA_RARE);

    /** A3a-087 · Alolan Dugtrio ex (alternate art of A3a-047) */
    public static final PokemonCard ALOLAN_DUGTRIO_EX_SIR = ALOLAN_DUGTRIO_EX.withId("A3a-087", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            ALOLAN_DIGLETT, ALOLAN_DUGTRIO_EX, ARON, LAIRON, AGGRON, FERROSEED,
            FERROTHORN, STAKATAKA,
            ALOLAN_DUGTRIO_EX_UR,
            ALOLAN_DUGTRIO_EX_SIR);

    private Metal() {
    }
}
