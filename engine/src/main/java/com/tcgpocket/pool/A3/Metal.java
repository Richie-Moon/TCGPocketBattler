package com.tcgpocket.pool.A3;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Celestial Guardians — Metal.
 */
public final class Metal {

    /**
     * A3-117 - Alolan Diglett
     */
    public static final PokemonCard ALOLAN_DIGLETT = PokemonCard.basic(
            "A3-117", "Alolan Diglett", "The metal-rich geology of this Pokémon's habitat caused it to develop steel whiskers on its head.",
            50, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Headbutt", "", EnergyCost.of(Type.METAL, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-118 - Alolan Dugtrio
     */
    public static final PokemonCard ALOLAN_DUGTRIO = PokemonCard.evolution(
            "A3-118", "Alolan Dugtrio", "Their beautiful, metallic whiskers create a sort of protective helmet on their heads, and they also function as highly precise sensors.",
            1, "Alolan Diglett", 90, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Iron Head", "Flip a coin until you get tails. This attack does 70 damage for each heads.", EnergyCost.of(Type.METAL, 2), new Attempt(List.of(
                    new FlipUntilTails(),
                    new DealDamage(new Product(new Literal(70), new NumberHeads()), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-119 - Excadrill
     */
    public static final PokemonCard EXCADRILL = PokemonCard.evolution(
            "A3-119", "Excadrill", "It's not uncommon for tunnels that appear to have formed naturally to actually be a result of Excadrill's rampant digging.",
            1, "Drilbur", 110, Type.METAL, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Slash", "", EnergyCost.of(Type.METAL, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-120 - Escavalier
     */
    public static final PokemonCard ESCAVALIER = PokemonCard.evolution(
            "A3-120", "Escavalier", "Wearing the shell covering they stole from Shelmet, they defend themselves and attack with two lances.",
            1, "Karrablast", 110, Type.METAL, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Pike", "This attack also does 20 damage to 1 of your opponent's Benched Pokémon.", EnergyCost.of(Type.METAL, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new DealDamage(new Literal(20), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Select a target:"))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3-121 - Klefki
     */
    public static final PokemonCard KLEFKI = PokemonCard.basic(
            "A3-121", "Klefki", "Once it absorbs a key's metal ions, it discards the key without a second thought. However, it will hang on to keys it favors for decades.",
            70, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Unlock", "Draw a card.", EnergyCost.of(Type.METAL, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new DrawCard(new Literal(1), new AttackerSide())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A3-122 - Solgaleo ex
     */
    public static final PokemonCard SOLGALEO_EX = PokemonCard.evolution(
            "A3-122", "Solgaleo ex", "",
            2, "Cosmoem", 180, Type.METAL, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Sol Breaker", "This Pokémon also does 10 damage to itself.", EnergyCost.of(Type.METAL, 2), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive()),
                    new DealDamage(new Literal(10), new Self())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX).withAbility(new ActivatedAbility("Rising Road",
            "Once during your turn, if this Pokémon is on your Bench, you may switch it with your Active Pokémon.",
            new PlainAction("", new Attempt(List.of(
                    new SwitchActive(new AttackerSide(), new Self())))),
            true, new For(new IsBenched(), new Self())));

    /**
     * A3-123 - Magearna
     */
    public static final PokemonCard MAGEARNA = PokemonCard.basic(
            "A3-123", "Magearna", "It synchronizes its consciousness with others to understand their feelings. This faculty makes it useful for taking care of people.",
            90, Type.METAL, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Silver Cannon", "If your opponent's Active Pokémon has an Ability, this attack does 40 more damage.", EnergyCost.of(Type.METAL, 2), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(
                            new Branch.Case(new For(new HasAbility(), new OpponentActive()), new Literal(80))
                    ), new Literal(40)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /** A3-175 · Magearna (alternate art of A3-123) */
    public static final PokemonCard MAGEARNA_IR = MAGEARNA.withId("A3-175", CardRarity.ILLUSTRATION_RARE);
    /** A3-189 · Solgaleo ex (alternate art of A3-122) */
    public static final PokemonCard SOLGALEO_EX_UR = SOLGALEO_EX.withId("A3-189", CardRarity.ULTRA_RARE);
    /** A3-207 · Solgaleo ex (alternate art of A3-122) */
    public static final PokemonCard SOLGALEO_EX_SIR = SOLGALEO_EX.withId("A3-207", CardRarity.SPECIAL_ILLUSTRATION_RARE);
    /** A3-239 · Solgaleo ex (alternate art of A3-122) */
    public static final PokemonCard SOLGALEO_EX_HR = SOLGALEO_EX.withId("A3-239", CardRarity.HYPER_RARE);

    static final List<PokemonCard> CARDS = List.of(
            ALOLAN_DIGLETT, ALOLAN_DUGTRIO, EXCADRILL, ESCAVALIER, KLEFKI, SOLGALEO_EX, MAGEARNA,
            MAGEARNA_IR, SOLGALEO_EX_UR, SOLGALEO_EX_SIR,
            SOLGALEO_EX_HR);

    private Metal() {
    }
}
