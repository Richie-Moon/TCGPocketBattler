package com.tcgpocket.pool.A2;

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
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.INumber;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.*;
import com.tcgpocket.trigger.EnergyAttached;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * Space-Time Smackdown — Darkness.
 */
public final class Darkness {

    /** "Each Pokémon you have in play": your Active plus your Bench. */
    private static final INumber YOUR_POKEMON_IN_PLAY = new Sum(
            new CountCards(new AttackerSide(), Zone.ACTIVE),
            new CountCards(new AttackerSide(), Zone.BENCH));

    /**
     * A2-096 - Murkrow
     */
    public static final PokemonCard MURKROW = PokemonCard.basic(
            "A2-096", "Murkrow", "Feared and loathed by many, it is believed to bring misfortune to all those who see it at night.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Peck", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-097 - Honchkrow
     */
    public static final PokemonCard HONCHKROW = PokemonCard.evolution(
            "A2-097", "Honchkrow", "It is merciless by nature. It is said that it never forgives the mistakes of its Murkrow followers.",
            1, "Murkrow", 100, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Skill Dive", "This attack does 50 damage to 1 of your opponent's Pokémon.", EnergyCost.of(Type.DARKNESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-098 - Sneasel
     */
    public static final PokemonCard SNEASEL = PokemonCard.basic(
            "A2-098", "Sneasel", "This cunning Pokémon hides under the cover of darkness, waiting to attack its prey.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Double Scratch", "Flip 2 coins. This attack does 20 damage for each heads.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(20)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2-099 - Weavile ex
     */
    public static final PokemonCard WEAVILE_EX = PokemonCard.evolution(
            "A2-099", "Weavile ex", "",
            1, "Sneasel", 140, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Scratching Nails", "If your opponent's Active Pokémon has damage on it, this attack does 40 more damage.",
                    EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(
                            new Branch.Case(new For(new IsDamaged(), new OpponentActive()), new Literal(70))
                    ), new Literal(30)), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.GRASS).withTags(CardTag.EX);

    /**
     * A2-100 - Poochyena
     */
    public static final PokemonCard POOCHYENA = PokemonCard.basic(
            "A2-100", "Poochyena", "A Pokémon with a persistent nature, it chases its chosen prey until the prey becomes exhausted.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2-101 - Mightyena
     */
    public static final PokemonCard MIGHTYENA = PokemonCard.evolution(
            "A2-101", "Mightyena", "It will always obey the commands of a skilled Trainer. Its behavior arises from its living in packs in ancient times.",
            1, "Poochyena", 90, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Darkness Fang", "", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2-102 - Stunky
     */
    public static final PokemonCard STUNKY = PokemonCard.basic(
            "A2-102", "Stunky", "It sprays a foul fluid from its rear. Its stench spreads over a mile radius, driving Pokémon away.",
            70, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Scratch", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-103 - Skuntank
     */
    public static final PokemonCard SKUNTANK = PokemonCard.evolution(
            "A2-103", "Skuntank", "It attacks by spraying a horribly smelly fluid from the tip of its tail. Attacks from above confound it.",
            1, "Stunky", 100, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Poison Gas", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-104 - Spiritomb
     */
    public static final PokemonCard SPIRITOMB = PokemonCard.basic(
            "A2-104", "Spiritomb", "Its constant mischief and misdeeds resulted in it being bound to an Odd Keystone by a mysterious spell.",
            80, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Swirling Disaster", "This attack does 10 damage to each of your opponent's Pokémon.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DamageEach(new Literal(10), new OpponentAll())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2-105 - Skorupi
     */
    public static final PokemonCard SKORUPI = PokemonCard.basic(
            "A2-105", "Skorupi", "After burrowing into the sand, it waits patiently for prey to come near. This Pokémon and Sizzlipede share common descent.",
            70, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Pierce", "", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-106 - Drapion
     */
    public static final PokemonCard DRAPION = PokemonCard.evolution(
            "A2-106", "Drapion", "Its poison is potent, but it rarely sees use. This Pokémon prefers to use physical force instead, going on rampages with its car-crushing strength.",
            1, "Skorupi", 120, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Cross Poison", "Flip 4 coins. This attack does 40 damage for each heads. If at least 2 of them are heads, your opponent's Active Pokémon is now Poisoned.",
                    EnergyCost.of(Type.DARKNESS, 3), new Attempt(List.of(
                    new FlipN(new Literal(4)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(40)), new OpponentActive()),
                    new ConditionalEffect(new GreaterThan(new NumberHeads(), new Literal(1)),
                            new AddStatus(new PoisonStatus(), new OpponentActive()))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-107 - Croagunk
     */
    public static final PokemonCard CROAGUNK = PokemonCard.basic(
            "A2-107", "Croagunk", "Inflating its poison sacs, it fills the area with an odd sound and hits flinching opponents with a poison jab.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Group Beatdown", "Flip a coin for each Pokémon you have in play. This attack does 20 damage for each heads.",
                    EnergyCost.of(Type.DARKNESS, 2), new Attempt(List.of(
                    new FlipN(YOUR_POKEMON_IN_PLAY),
                    new DealDamage(new Product(new NumberHeads(), new Literal(20)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-108 - Toxicroak
     */
    public static final PokemonCard TOXICROAK = PokemonCard.evolution(
            "A2-108", "Toxicroak", "Swaying and dodging the attacks of its foes, it weaves its flexible body in close, then lunges out with its poisonous claws.",
            1, "Croagunk", 90, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Group Beatdown", "Flip a coin for each Pokémon you have in play. This attack does 40 damage for each heads.",
                    EnergyCost.of(Type.DARKNESS, 2), new Attempt(List.of(
                    new FlipN(YOUR_POKEMON_IN_PLAY),
                    new DealDamage(new Product(new NumberHeads(), new Literal(40)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-109 - Darkrai
     */
    public static final PokemonCard DARKRAI = PokemonCard.basic(
            "A2-109", "Darkrai", "It chases people and Pokémon from its territory by causing them to experience deep, nightmarish slumbers.",
            110, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Dark Void", "Your opponent's Active Pokémon is now Asleep.", EnergyCost.of(Type.DARKNESS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new AddStatus(new SleepStatus(), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.GRASS);

    /**
     * A2-110 - Darkrai ex — Nightmare Aura: Energy is only ever attached on its
     * owner's turn, so the turn-relative {@code OpponentActive} is the owner's
     * opponent. Not attack damage, like A1a-056 Druddigon's Rough Skin.
     */
    public static final PokemonCard DARKRAI_EX = PokemonCard.basic(
            "A2-110", "Darkrai ex", "",
            140, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Dark Prism", "", EnergyCost.of(Type.DARKNESS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withAbility(new PassiveAbility("Nightmare Aura",
            "Whenever you attach a Darkness Energy from your Energy Zone to this Pokémon, do 20 damage to your opponent's Active Pokémon.",
            List.of(new Trigger(EnergyAttached.class,
                    new And<>(new EventConcerns(new Self()), new EnergyFromZone(Type.DARKNESS)),
                    new Attempt(List.of(new PlaceDamage(new Literal(20), new OpponentActive())))))))
            .withWeakness(Type.GRASS).withTags(CardTag.EX);

    static final List<PokemonCard> CARDS = List.of(
            MURKROW, HONCHKROW, SNEASEL, WEAVILE_EX, POOCHYENA, MIGHTYENA, STUNKY, SKUNTANK,
            SPIRITOMB, SKORUPI, DRAPION, CROAGUNK, TOXICROAK, DARKRAI, DARKRAI_EX);

    private Darkness() {
    }
}
