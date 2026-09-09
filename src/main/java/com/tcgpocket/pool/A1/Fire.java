package com.tcgpocket.pool.A1;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.HasType;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.Matching;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

public final class Fire {
    /**
     * A1-033 - Charmander
     */
    public static final PokemonCard CHARMANDER = PokemonCard.basic(
            "A1-033", "Charmander", "It has a preference for hot things. When it rains, steam is said to spout from the tip of its tail.",
            60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ember", "Discard a Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIRE, new Literal(1), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A1-034 - Charmeleon
     */
    public static final PokemonCard CHARMELEON = PokemonCard.evolution(
            "A1-034", "Charmeleon", "It has a barbaric nature. In the battle, it whips its fiery tail around and slashes away with sharp claws.",
            1, "Charmeleon", 90, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Fire Claws", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A1-035 - Charizard
     */
    public static final PokemonCard CHARIZARD = PokemonCard.evolution(
            "A1-035", "Charizard", "It spits fire that is hot enough to melt boulders. It may cause forest fires by blowing flames.",
            2, "Charmeleon", 150, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Fire Spin", "Discard 2 Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(150), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIRE, new Literal(2), new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.WATER);

    /**
     * A1-036 - Charizard ex
     */
    public static final PokemonCard CHARIZARD_EX = PokemonCard.evolution(
            "A1-036", "Charizard ex", "",
            2, "Charmeleon", 180, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Slash", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 2), new Attempt(List.of(
                            new DealDamage(new Literal(60), new OpponentActive())))
                    ), new Action(
                            "Crimson Storm", "Discard 2 Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 2), new Attempt(List.of(
                            new DealDamage(new Literal(200), new OpponentActive()),
                            new DiscardTypeEnergy(Type.FIRE, new Literal(2), new Self()))))
            ), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.WATER).withTags(CardTag.EX);

    /**
     * A1-037 - Vulpix
     */
    public static final PokemonCard VULPIX = PokemonCard.basic(
            "A1-037", "Vulpix", "While young, it has six gorgeous tails. When it grows, several new tails are sprouted.",
            50, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tail Whip", "Flip a coin. If heads, the Defending Pokémon can't attack during your opponent's next turn.", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(
                            new LastCoinTossHeads(),
                            new PreventAttack(new OpponentActive(), new Literal(1))))))
            ), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A1-038 - Ninetales
     */
    public static final PokemonCard NINETALES = PokemonCard.evolution(
            "A1-038", "Ninetales", "It is said to live for 1,000 years, and each of its tails is loaded with supernatural powers.",
            1, "Vulpix", 90, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flamethrower", "Discard a Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIRE, new Literal(1), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A1-039 - Growlithe
     */
    public static final PokemonCard GROWLITHE = PokemonCard.basic(
                    "A1-039", "Growlithe", "It has a brave and trustworthy nature. It fearlessly stands up to bigger and stronger foes.",
                    70, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Bite", "", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.WATER);


    /**
     * A1-040 - Arcanine
     */
    public static final PokemonCard ARCANINE = PokemonCard.evolution(
                    "A1-040", "Arcanine", "An ancient picture scroll shows that people were captivated by its movement as it ran through prairies.",
                    1, "Growlithe", 130, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Heat Tackle", "This Pokémon also does 20 damage to itself.", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(90), new OpponentActive()),
                                    new DealDamage(new Literal(20), new Self())
                            )))), CardRarity.RARE)
            .withWeakness(Type.WATER);

    /**
     * A1-041 - Arcanine ex
     */
    public static final PokemonCard ARCANINE_EX = PokemonCard.evolution(
                    "A1-041", "Arcanine ex", "",
                    1, "Growlithe", 150, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Inferno Onrush", "", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(120), new OpponentActive()),
                                    new DealDamage(new Literal(20), new Self())
                            )))), CardRarity.DOUBLE_RARE)
            .withWeakness(Type.WATER);

    /**
     * A1-042 - Ponyta
     */
    public static final PokemonCard PONYTA = PokemonCard.basic(
                    "A1-042", "Ponyta", "It can't run properly when it's newly born. As it races around with others of its kind, its legs grow stronger.",
                    60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Flare", "", EnergyCost.of(Type.FIRE, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.WATER);

    /**
     * A1-043 - Rapidash
     */
    public static final PokemonCard RAPIDASH = PokemonCard.evolution(
                    "A1-043", "Rapidash", "This Pokémon can be seen galloping through fields at speeds of up to 150 mph, its fiery mane fluttering in the wind.",
                    1, "Ponyta", 100, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Fire Mane", "", EnergyCost.of(Type.FIRE, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.WATER);

    /**
     * A1-044 - Magmar
     */
    public static final PokemonCard MAGMAR = PokemonCard.basic(
                    "A1-044", "Magmar", "Magmar dispatches its prey with fire. But it regrets this habit once it realizes that it has burned its intended prey to a charred crisp.",
                    80, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Magma Punch", "", EnergyCost.of(Type.FIRE, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(50), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.WATER);

    /**
     * A1-045 - Flareon
     */
    public static final PokemonCard FLAREON = PokemonCard.evolution(
                    "A1-045", "Flareon", "Inhaled air is carried to its flame sac, heated, and exhaled as fire that reaches over 3,000 degrees Fahrenheit.",
                    1, "Eevee", 120, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Flamethrower", "Discard 1 Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(110), new OpponentActive()),
                                    new DiscardTypeEnergy(Type.FIRE, new Literal(1), new Self())
                            )))), CardRarity.RARE)
            .withWeakness(Type.WATER);

    /**
     * A1-046 - Moltres
     */
    public static final PokemonCard MOLTRES = PokemonCard.basic(
                    "A1-046", "Moltres", "It's one of the legendary bird Pokémon. When Moltres flaps its flaming wings, they glimmer with a dazzling red glow.",
                    100, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Sky Attack", "Flip a coin. If tails, this attack does nothing.", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new FlipN(new Literal(1)),
                                    new ConditionalEffect(
                                            new Not<>(new LastCoinTossHeads()),
                                            new Fail()
                                    ),
                                    new DealDamage(new Literal(130), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-047 - Moltres ex - Inferno Dance: energy from the zone onto the bench.
     *
     * <p>"In any way you like" is {@link DistributeEnergy}: one decision over
     * every placement of the Energy, not one question per Energy, so all three
     * can land on one Pokemon or spread across the bench and the player sees
     * the whole placement before committing to any of it. {@code NumberHeads}
     * reads the flip the preceding {@code FlipN} recorded in the scope.
     */
    public static final PokemonCard MOLTRES_EX = PokemonCard.basic(
                    "A1-047", "Moltres ex", "",
                    140, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(
                            new Action(
                                    "Inferno Dance", "Flip 3 coins. Take an amount of Fire Energy from your Energy Zone equal to the number of heads and attach it to your Benched Fire Pokémon in any way you like.", EnergyCost.of(Type.FIRE, 1),
                                    new Attempt(List.of(
                                            new FlipN(new Literal(3)),
                                            new DistributeEnergy(
                                                    Type.FIRE, new NumberHeads(),
                                                    new Matching(new AttackerBench(), new HasType(Type.FIRE)),
                                                    "Please place the Fire Energy on your Benched Fire-type Pokémon.")))),
                            new Action(
                                    "Heat Blast", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 2),
                                    new Attempt(List.of(
                                            new DealDamage(new Literal(70), new OpponentActive()))))),
                    CardRarity.DOUBLE_RARE)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-048 - Heatmor
     */
    public static final PokemonCard HEATMOR = PokemonCard.basic(
                    "A1-048", "Heatmor", "There's a hole in its tail that allows it to draw in the air it needs to keep its fire burning. If the hole gets blocked, this Pokémon will fall ill.",
                    80, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Combustion", "", EnergyCost.of(Type.FIRE, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.WATER);

    /**
     * A1-049 - Salandit
     */
    public static final PokemonCard SALANDIT = PokemonCard.basic(
                    "A1-049", "Salandit", "It taunts its prey and lures them into narrow, rocky areas where it then sprays them with toxic gas to make them dizzy and take them down.",
                    60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Scratch", "", EnergyCost.of(Type.FIRE, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.WATER);

    /**
     * A1-050 - Salazzle
     */
    public static final PokemonCard SALAZZLE = PokemonCard.evolution(
                    "A1-050", "Salazzle", "Salazzle makes its opponents light-headed with poisonous gas, then captivates them with alluring movements to turn them into loyal servants.",
                    1, "Salandit", 90, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Fire Claws", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(60), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.WATER);

    /**
     * A1-051 - Sizzlipede
     */
    public static final PokemonCard SIZZLIPEDE = PokemonCard.basic(
                    "A1-051", "Sizzlipede", "It stores flammable gas in its body and uses it to generate heat. The yellow sections on its belly get particularly hot.",
                    60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Gnaw", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.WATER);

    /**
     * A1-052 - Centiskorch
     */
    public static final PokemonCard CENTISKORCH = PokemonCard.evolution(
                    "A1-052", "Centiskorch", "When it heats up, its body temperature reaches about 1,500 degrees Fahrenheit. It lashes its body like a whip and launches itself at enemies.",
                    1, "Sizzlipede", 130, Type.FIRE, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Fire Blast", "Discard a Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(130), new OpponentActive()),
                                    new DiscardTypeEnergy(Type.FIRE, new Literal(1), new Self())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.WATER);
    
    static final List<PokemonCard> CARDS = List.of(
            CHARMANDER, CHARMELEON, CHARIZARD, CHARIZARD_EX, VULPIX, NINETALES, GROWLITHE,
            ARCANINE, ARCANINE_EX, PONYTA, RAPIDASH, MAGMAR, FLAREON, MOLTRES, MOLTRES_EX, HEATMOR, SALANDIT, SALAZZLE, 
            SIZZLIPEDE, CENTISKORCH
    );

    private Fire() {
    }
}
