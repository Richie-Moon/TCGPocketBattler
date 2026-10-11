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
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.target.*;
import com.tcgpocket.trigger.AttackDeclared;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnStart;

import java.util.List;

/**
 * Space-Time Smackdown — Water.
 */
public final class Water {

    /**
     * Thick Fat: stamped when the opponent declares an attack, so it reads the
     * attacker that is actually swinging. {@code AttackerActive} is that attacker
     * because targets are turn-relative. Lasts the rest of that turn, which holds
     * nothing but that one attack.
     */
    private static PassiveAbility thickFat(int reduction) {
        return new PassiveAbility("Thick Fat",
                "This Pokémon takes -" + reduction + " damage from attacks from Fire or Water Pokémon.",
                List.of(new Trigger(AttackDeclared.class,
                        new And<>(new Not<>(new EventSideIs(new SelfSide())),
                                new For(new Or<>(new HasType(Type.FIRE), new HasType(Type.WATER)), new AttackerActive())),
                        new Attempt(List.of(new ReduceDamageTaken(new Literal(reduction), new Self(), new Literal(0)))))));
    }

    /**
     * A2-031 - Swinub
     */
    public static final PokemonCard SWINUB = PokemonCard.basic(
            "A2-031", "Swinub", "It rubs its snout on the ground to find and dig up food. It sometimes discovers hot springs.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Headbutt", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A2-032 - Piloswine
     */
    public static final PokemonCard PILOSWINE = PokemonCard.evolution(
            "A2-032", "Piloswine", "If it charges at an enemy, the hairs on its back stand up straight. It is very sensitive to sound.",
            1, "Swinub", 110, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Hammer In", "", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withAbility(thickFat(20)).withWeakness(Type.METAL);

    /**
     * A2-033 - Mamoswine
     */
    public static final PokemonCard MAMOSWINE = PokemonCard.evolution(
            "A2-033", "Mamoswine", "This Pokémon can be spotted in wall paintings from as far back as 10,000 years ago. For a while, it was thought to have gone extinct.",
            2, "Piloswine", 160, Type.WATER, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Frosty Flattening", "", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive())))
            )), CardRarity.RARE
    ).withAbility(thickFat(30)).withWeakness(Type.METAL);

    /**
     * A2-034 - Regice — Crystal Body: renewed each opponent turn like A1-182
     * Melmetal's Hard Coat. Exact, since a Basic only enters play on its
     * owner's turn.
     */
    public static final PokemonCard REGICE = PokemonCard.basic(
            "A2-034", "Regice", "With cold air that can reach temperatures as low as -328 degrees Fahrenheit, Regice instantly freezes any creature that approaches it.",
            110, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Frost Smash", "", EnergyCost.of(Type.WATER, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withAbility(new PassiveAbility("Crystal Body",
            "Prevent all effects of attacks used by your opponent's Pokémon done to this Pokémon.", List.of(
            new Trigger(TurnStart.class, new Not<>(new EventSideIs(new SelfSide())),
                    new Attempt(List.of(new PreventEffects(new Self(), new Literal(0))))))))
            .withWeakness(Type.METAL);

    /**
     * A2-035 - Piplup
     */
    public static final PokemonCard PIPLUP = PokemonCard.basic(
            "A2-035", "Piplup", "It doesn't like to be taken care of. It's difficult to bond with since it won't listen to its Trainer.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Nap", "Heal 20 damage from this Pokémon.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new HealDamage(new Literal(20), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-036 - Prinplup
     */
    public static final PokemonCard PRINPLUP = PokemonCard.evolution(
            "A2-036", "Prinplup", "It lives alone, away from others. Apparently, every one of them believes it is the most important.",
            1, "Piplup", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Surf", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-037 - Empoleon
     */
    public static final PokemonCard EMPOLEON = PokemonCard.evolution(
            "A2-037", "Empoleon", "It swims as fast as a jet boat. The edges of its wings are sharp and can slice apart drifting ice.",
            2, "Prinplup", 150, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Aqua Jet", "This attack also does 30 damage to 1 of your opponent's Benched Pokémon.",
                    EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new DealDamage(new Literal(30), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Select a Benched Pokémon"))))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-038 - Buizel
     */
    public static final PokemonCard BUIZEL = PokemonCard.basic(
            "A2-038", "Buizel", "It spins its two tails like a screw to propel itself through water. The tails also slice clinging seaweed.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Water Gun", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-039 - Floatzel
     */
    public static final PokemonCard FLOATZEL = PokemonCard.evolution(
            "A2-039", "Floatzel", "With its flotation sac inflated, it can carry people on its back. It deflates the sac before it dives.",
            1, "Buizel", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Jet Screw", "Flip a coin. If heads, this attack does 30 more damage.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(
                            new Literal(30),
                            new Product(new NumberHeads(), new Literal(30))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-040 - Shellos
     */
    public static final PokemonCard SHELLOS = PokemonCard.basic(
            "A2-040", "Shellos", "It used to have a shell on its back long ago. This species is closely related to Pokémon like Shellder.",
            70, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Mud-Slap", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-041 - Gastrodon
     */
    public static final PokemonCard GASTRODON = PokemonCard.evolution(
            "A2-041", "Gastrodon", "They normally inhabit rocky seashores, but in times of continuous rain, they can sometimes be found in the mountains, far from the sea.",
            1, "Shellos", 120, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Muddy Water", "This attack also does 20 damage to 1 of your opponent's Benched Pokémon.",
                    EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new DealDamage(new Literal(20), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Select a Benched Pokémon"))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-042 - Finneon — Elegant Swim: built like A1-140 Dugtrio's Dig.
     */
    public static final PokemonCard FINNEON = PokemonCard.basic(
            "A2-042", "Finneon", "The line running down its side can store sunlight. It shines vividly at night.",
            50, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Elegant Swim", "Flip a coin. If heads, during your opponent's next turn, prevent all damage from—and effects of—attacks done to this Pokémon.",
                    EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventDamage(new Self(), new Literal(1))),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventEffects(new Self(), new Literal(1)))))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-043 - Lumineon
     */
    public static final PokemonCard LUMINEON = PokemonCard.evolution(
            "A2-043", "Lumineon", "With its shining light, it lures its prey close. However, the light also happens to attract ferocious fish Pokémon—its natural predators.",
            1, "Finneon", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Waterfall", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-044 - Snover
     */
    public static final PokemonCard SNOVER = PokemonCard.basic(
            "A2-044", "Snover", "During cold seasons, it migrates to the mountain's lower reaches. It returns to the snow-covered summit in the spring.",
            70, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Ice Shard", "If your opponent's Active Pokémon is a Fighting Pokémon, this attack does 30 more damage.",
                    EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(
                            new Branch.Case(new For(new HasType(Type.FIGHTING), new OpponentActive()), new Literal(40))
                    ), new Literal(10)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A2-045 - Abomasnow
     */
    public static final PokemonCard ABOMASNOW = PokemonCard.evolution(
            "A2-045", "Abomasnow", "It lives a quiet life on mountains that are perpetually covered in snow. It hides itself by whipping up blizzards.",
            1, "Snover", 140, Type.WATER, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Frost Breath", "", EnergyCost.of(Type.WATER, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /**
     * A2-046 - Glaceon
     */
    public static final PokemonCard GLACEON = PokemonCard.evolution(
            "A2-046", "Glaceon", "It can control its body temperature at will. This enables it to freeze the moisture in the atmosphere, creating flurries of diamond dust.",
            1, "Eevee", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ice Beam", "Flip a coin. If heads, your opponent's Active Pokémon is now Paralyzed.", EnergyCost.of(Type.WATER, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(),
                            new AddStatus(new ParalysisStatus(), new OpponentActive()))))
            )), CardRarity.RARE
    ).withWeakness(Type.METAL);

    /**
     * A2-047 - Wash Rotom
     */
    public static final PokemonCard WASH_ROTOM = PokemonCard.basic(
            "A2-047", "Wash Rotom", "This Rotom has entered a washing machine. It nods with satisfaction after it floods the surrounding area.",
            80, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Wave Splash", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-048 - Frost Rotom
     */
    public static final PokemonCard FROST_ROTOM = PokemonCard.basic(
            "A2-048", "Frost Rotom", "This Rotom has entered a refrigerator. It leaps around gleefully after it uses cold air to freeze the area around it.",
            80, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Blizzard", "This attack also does 10 damage to each of your opponent's Benched Pokémon.", EnergyCost.of(Type.WATER, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new DamageEach(new Literal(10), new OpponentBench())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A2-049 - Palkia ex
     */
    public static final PokemonCard PALKIA_EX = PokemonCard.basic(
            "A2-049", "Palkia ex", "",
            150, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Slash", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            ), new Action(
                    "Dimensional Storm", "Discard 3 Water Energy from this Pokémon. This attack also does 20 damage to each of your opponent's Benched Pokémon.",
                    EnergyCost.of(Type.WATER, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(150), new OpponentActive()),
                    new DiscardTypeEnergy(Type.WATER, new Literal(3), new Self()),
                    new DamageEach(new Literal(20), new OpponentBench())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A2-050 - Manaphy — Oceanic Gift: a distinct {@link DistributeEnergy}, so
     * 2 different Benched Pokémon, or the only one.
     */
    public static final PokemonCard MANAPHY = PokemonCard.basic(
            "A2-050", "Manaphy", "It is born with a wondrous power that lets it bond with any kind of Pokémon.",
            50, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Oceanic Gift", "Choose 2 of your Benched Pokémon. For each of those Pokémon, take a Water Energy from your Energy Zone and attach it to that Pokémon.",
                    EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DistributeEnergy(Type.WATER, new Literal(2), new AttackerBench(),
                            "Choose 2 of your Benched Pokémon to attach a Water Energy to.", true)))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    static final List<PokemonCard> CARDS = List.of(
            SWINUB, PILOSWINE, MAMOSWINE, REGICE, PIPLUP, PRINPLUP, EMPOLEON, BUIZEL, FLOATZEL,
            SHELLOS, GASTRODON, FINNEON, LUMINEON, SNOVER, ABOMASNOW, GLACEON, WASH_ROTOM,
            FROST_ROTOM, PALKIA_EX, MANAPHY);

    private Water() {
    }
}
