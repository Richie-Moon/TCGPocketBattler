package com.tcgpocket.pool.A2;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.status.BurnStatus;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Space-Time Smackdown — Fire.
 */
public final class Fire {

    /**
     * A2-023 - Magmar
     */
    public static final PokemonCard MAGMAR = PokemonCard.basic(
            "A2-023", "Magmar", "Magmar dispatches its prey with fire. But it regrets this habit once it realizes that it has burned its intended prey to a charred crisp.",
            80, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Stoke", "Take a Fire Energy from your Energy Zone and attach it to this Pokémon.", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new AttachEnergy(Type.FIRE, new Literal(1), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A2-024 - Magmortar
     */
    public static final PokemonCard MAGMORTAR = PokemonCard.evolution(
            "A2-024", "Magmortar", "When Magmortar inhales deeply, the fire burning in its belly intensifies, rising in temperature to over 3,600 degrees Fahrenheit.",
            1, "Magmar", 130, Type.FIRE, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Bursting Inferno", "Your opponent's Active Pokémon is now Burned.", EnergyCost.of(Type.FIRE, 4), new Attempt(List.of(
                    new DealDamage(new Literal(100), new OpponentActive()),
                    new AddStatus(new BurnStatus(), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.WATER);

    /**
     * A2-025 - Slugma
     */
    public static final PokemonCard SLUGMA = PokemonCard.basic(
            "A2-025", "Slugma", "A common sight in volcanic areas, it slowly slithers around in a constant search for warm places.",
            70, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Flare", "", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A2-026 - Magcargo
     */
    public static final PokemonCard MAGCARGO = PokemonCard.evolution(
            "A2-026", "Magcargo", "Its brittle shell occasionally spouts intense flames that circulate throughout its body.",
            1, "Slugma", 120, Type.FIRE, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Searing Flame", "Your opponent's Active Pokémon is now Burned.", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new AddStatus(new BurnStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A2-027 - Chimchar
     */
    public static final PokemonCard CHIMCHAR = PokemonCard.basic(
            "A2-027", "Chimchar", "Its fiery rear end is fueled by gas made in its belly. Even rain can't extinguish the fire.",
            60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Scratch", "", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A2-028 - Monferno
     */
    public static final PokemonCard MONFERNO = PokemonCard.evolution(
            "A2-028", "Monferno", "It skillfully controls the intensity of the fire on its tail to keep its foes at an ideal distance.",
            1, "Chimchar", 80, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Fiery Punch", "", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A2-029 - Infernape ex — "all" is however many Fire Energy are attached
     * when the attack resolves.
     */
    public static final PokemonCard INFERNAPE_EX = PokemonCard.evolution(
            "A2-029", "Infernape ex", "",
            2, "Monferno", 170, Type.FIRE, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                    "Flare Blitz", "Discard all Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Literal(140), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIRE, new EnergyOn(new Self(), Type.FIRE), new Self())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.WATER).withTags(CardTag.EX);

    /**
     * A2-030 - Heat Rotom
     */
    public static final PokemonCard HEAT_ROTOM = PokemonCard.basic(
            "A2-030", "Heat Rotom", "If the convection microwave oven is not working properly, then the Rotom inhabiting it will become lethargic.",
            80, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Heat Breath", "Flip a coin. If heads, this attack does 30 more damage.", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(
                            new Literal(30),
                            new Product(new NumberHeads(), new Literal(30))), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    static final List<PokemonCard> CARDS = List.of(
            MAGMAR, MAGMORTAR, SLUGMA, MAGCARGO, CHIMCHAR, MONFERNO, INFERNAPE_EX, HEAT_ROTOM);

    private Fire() {
    }
}
