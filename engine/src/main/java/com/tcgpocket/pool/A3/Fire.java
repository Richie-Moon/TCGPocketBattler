package com.tcgpocket.pool.A3;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.condition.IsDamaged;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.status.BurnStatus;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Celestial Guardians — Fire.
 */
public final class Fire {

    /**
     * A3-025 - Growlithe
     */
    public static final PokemonCard GROWLITHE = PokemonCard.basic(
            "A3-025", "Growlithe", "It has a brave and trustworthy nature. It fearlessly stands up to bigger and stronger foes.",
            70, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Combustion", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A3-026 - Arcanine
     */
    public static final PokemonCard ARCANINE = PokemonCard.evolution(
            "A3-026", "Arcanine", "An ancient picture scroll shows that people were captivated by its movement as it ran through prairies.",
            1, "Growlithe", 130, Type.FIRE, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Fire Mane", "", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A3-027 - Alolan Marowak
     */
    public static final PokemonCard ALOLAN_MAROWAK = PokemonCard.evolution(
            "A3-027", "Alolan Marowak", "This Pokémon sets the bone it holds on fire and dances through the night as a way to mourn its fallen allies.",
            1, "Cubone", 110, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Burning Bonemerang", "Flip 2 coins. This attack does 70 damage for each heads. If at least 1 of them is heads, your opponent's Active Pokémon is now Burned.", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Product(new Literal(70), new NumberHeads()), new OpponentActive()),
                    new ConditionalEffect(
                            new GreaterThan(new NumberHeads(), new Literal(0)),
                            new AddStatus(new BurnStatus(), new OpponentActive()))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A3-028 - Fletchinder
     */
    public static final PokemonCard FLETCHINDER = PokemonCard.evolution(
            "A3-028", "Fletchinder", "Fletchinder scatters embers in tall grass where bug Pokémon might be hiding and then catches them as they come leaping out.",
            1, "Fletchling", 80, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Steady Firebreathing", "", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-029 - Talonflame
     */
    public static final PokemonCard TALONFLAME = PokemonCard.evolution(
            "A3-029", "Talonflame", "It has top-notch flying capabilities. It flies around easily, even while carrying prey that weighs more than 220 lbs.",
            2, "Fletchinder", 120, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Fire Wing", "", EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-030 - Litten
     */
    public static final PokemonCard LITTEN = PokemonCard.basic(
            "A3-030", "Litten", "If you try too hard to get close to it, it won't open up to you. Even if you do grow close, giving it too much affection is still a no-no.",
            60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Heat Tackle", "This Pokémon also does 10 damage to itself.", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new DealDamage(new Literal(10), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A3-031 - Litten
     */
    public static final PokemonCard LITTEN_SCRATCH = PokemonCard.basic(
            "A3-031", "Litten", "If you try too hard to get close to it, it won't open up to you. Even if you do grow close, giving it too much affection is still a no-no.",
            60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Scratch", "", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A3-032 - Torracat
     */
    public static final PokemonCard TORRACAT = PokemonCard.evolution(
            "A3-032", "Torracat", "Torracat will let its Trainer coddle it once they've grown close, but it's a powerful, sharp-clawed Pokémon, so its Trainer gets covered in scratches.",
            1, "Litten", 80, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Heat Tackle", "This Pokémon also does 10 damage to itself.", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new DealDamage(new Literal(10), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A3-033 - Incineroar ex
     */
    public static final PokemonCard INCINEROAR_EX = PokemonCard.evolution(
            "A3-033", "Incineroar ex", "",
            2, "Torracat", 180, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(
                    new Action(
                            "Fire Fang", "Your opponent's Active Pokémon is now Burned.", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                            new DealDamage(new Literal(30), new OpponentActive()),
                            new AddStatus(new BurnStatus(), new OpponentActive())))),
                    new Action(
                            "Scar-Charged Smash", "If this Pokémon has damage on it, this attack does 60 more damage.", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 1), new Attempt(List.of(
                            new DealDamage(new Sum(
                                    new Literal(80),
                                    new Branch(List.of(new Branch.Case(
                                            new For(new IsDamaged(), new Self()), new Literal(60))),
                                            new Literal(0))), new OpponentActive()))))
            ), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.WATER).withTags(CardTag.EX);

    /**
     * A3-034 - Oricorio — its own Energy goes first: it always has some, having
     * just paid for the attack, while the opponent's Active may have none, and
     * that discard reporting FAILED must not stop Oricorio's.
     */
    public static final PokemonCard ORICORIO = PokemonCard.basic(
            "A3-034", "Oricorio", "This form of Oricorio has sipped red nectar. It whips up blazing flames as it moves to the steps of its passionate dance.",
            70, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Kindle", "Discard a random Energy from both Active Pokémon.", EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new DiscardRandomEnergy(new Literal(1), new Self()),
                    new DiscardRandomEnergy(new Literal(1), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A3-035 - Salandit
     */
    public static final PokemonCard SALANDIT = PokemonCard.basic(
            "A3-035", "Salandit", "It taunts its prey and lures them into narrow, rocky areas where it then sprays them with toxic gas to make them dizzy and take them down.",
            50, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Gnaw", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A3-036 - Salazzle
     */
    public static final PokemonCard SALAZZLE = PokemonCard.evolution(
            "A3-036", "Salazzle", "Salazzle makes its opponents light-headed with poisonous gas, then captivates them with alluring movements to turn them into loyal servants.",
            1, "Salandit", 90, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Heated Poison", "Your opponent's Active Pokémon is now Poisoned and Burned.", EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive()),
                    new AddStatus(new BurnStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A3-037 - Turtonator
     */
    public static final PokemonCard TURTONATOR = PokemonCard.basic(
            "A3-037", "Turtonator", "Explosive substances coat the shell on its back. Enemies that dare attack it will be blown away by an immense detonation.",
            120, Type.FIRE, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Fire Spin", "Discard a Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIRE, new Literal(1), new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.WATER);

    /** A3-160 · Alolan Marowak (alternate art of A3-027) */
    public static final PokemonCard ALOLAN_MAROWAK_IR = ALOLAN_MAROWAK.withId("A3-160", CardRarity.ILLUSTRATION_RARE);
    /** A3-161 · Turtonator (alternate art of A3-037) */
    public static final PokemonCard TURTONATOR_IR = TURTONATOR.withId("A3-161", CardRarity.ILLUSTRATION_RARE);
    /** A3-182 · Incineroar ex (alternate art of A3-033) */
    public static final PokemonCard INCINEROAR_EX_UR = INCINEROAR_EX.withId("A3-182", CardRarity.ULTRA_RARE);
    /** A3-200 · Incineroar ex (alternate art of A3-033) */
    public static final PokemonCard INCINEROAR_EX_SIR = INCINEROAR_EX.withId("A3-200", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            GROWLITHE, ARCANINE, ALOLAN_MAROWAK, FLETCHINDER, TALONFLAME, LITTEN, LITTEN_SCRATCH, TORRACAT,
            INCINEROAR_EX, ORICORIO, SALANDIT, SALAZZLE, TURTONATOR,
            ALOLAN_MAROWAK_IR, TURTONATOR_IR, INCINEROAR_EX_UR, INCINEROAR_EX_SIR);

    private Fire() {
    }
}
