package com.tcgpocket.pool.A2a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.AttackCostReduction;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.*;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnEnd;

import java.util.List;

/**
 * Triumphant Light — Water.
 */
public final class Water {

    /**
     * A2a-014 - Marill
     */
    public static final PokemonCard MARILL = PokemonCard.basic(
            "A2a-014", "Marill", "The fur on its body naturally repels water. It can stay dry even when it plays in the water.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Water Gun", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2a-015 - Azumarill
     */
    public static final PokemonCard AZUMARILL = PokemonCard.evolution(
            "A2a-015", "Azumarill", "Its long ears are superb sensors. It can distinguish the movements of things in water and tell what they are.",
            1, "Marill", 100, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Waterfall", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2a-016 - Barboach
     */
    public static final PokemonCard BARBOACH = PokemonCard.basic(
            "A2a-016", "Barboach", "Its two whiskers provide a sensitive radar. Even in muddy waters, it can detect its prey's location.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Mud-Slap", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2a-017 - Whiscash
     */
    public static final PokemonCard WHISCASH = PokemonCard.evolution(
            "A2a-017", "Whiscash", "It is extremely protective of its territory. If any foe approaches, it attacks using vicious tremors.",
            1, "Barboach", 120, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Thrash", "Flip a coin. If heads, this attack does 60 more damage. If tails, this Pokémon also does 20 damage to itself.",
                    EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 2), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(
                            new Literal(80),
                            new Product(new Literal(60), new NumberHeads())
                    ), new OpponentActive()),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new DealDamage(new Literal(20), new Self()))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2a-018 - Snorunt
     */
    public static final PokemonCard SNORUNT = PokemonCard.basic(
            "A2a-018", "Snorunt", "It is said that several Snorunt gather under giant leaves and live together in harmony.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Icicle", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A2a-019 - Froslass
     */
    public static final PokemonCard FROSLASS = PokemonCard.evolution(
            "A2a-019", "Froslass", "When it finds humans or Pokémon it likes, it freezes them and takes them to its chilly den, where they become decorations.",
            1, "Snorunt", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Blizzard", "This attack also does 10 damage to each of your opponent's Benched Pokémon.",
                    EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new DamageEach(new Literal(10), new OpponentBench())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /**
     * A2a-020 - Snover
     */
    public static final PokemonCard SNOVER = PokemonCard.basic(
            "A2a-020", "Snover", "During cold seasons, it migrates to the mountain's lower reaches. It returns to the snow-covered summit in the spring.",
            70, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Corkscrew Punch", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A2a-021 - Abomasnow
     */
    public static final PokemonCard ABOMASNOW = PokemonCard.evolution(
            "A2a-021", "Abomasnow", "It lives a quiet life on mountains that are perpetually covered in snow. It hides itself by whipping up blizzards.",
            1, "Snover", 120, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Mega Punch", "", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.METAL).withAbility(new PassiveAbility("Vigor Link",
            "If you have Arceus or Arceus ex in play, attacks used by this Pokémon cost 1 less Colorless Energy.",
            new AttackCostReduction(1, Links.ARCEUS_IN_PLAY)));

    /**
     * A2a-022 - Glaceon ex. Pokémon Checkup follows every turn, and both
     * triggers hit the Active of Glaceon's opponent: that is
     * {@code OpponentActive} after Glaceon's own turn and
     * {@code AttackerActive} after the opponent's turn, because targets are
     * turn-relative.
     */
    public static final PokemonCard GLACEON_EX = PokemonCard.evolution(
            "A2a-022", "Glaceon ex", "",
            1, "Eevee", 140, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Freezing Wind", "", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.METAL).withTags(CardTag.EX).withAbility(new PassiveAbility("Snowy Terrain",
            "During Pokémon Checkup, if this Pokémon is in the Active Spot, do 10 damage to your opponent's Active Pokémon.",
            List.of(
                    new Trigger(TurnEnd.class,
                            new And<>(new EventSideIs(new SelfSide()), new For(new IsActive(), new Self())),
                            new Attempt(List.of(new PlaceDamage(new Literal(10), new OpponentActive())))),
                    new Trigger(TurnEnd.class,
                            new And<>(new Not<>(new EventSideIs(new SelfSide())), new For(new IsActive(), new Self())),
                            new Attempt(List.of(new PlaceDamage(new Literal(10), new AttackerActive())))))));

    /**
     * A2a-023 - Origin Forme Palkia
     */
    public static final PokemonCard ORIGIN_FORME_PALKIA = PokemonCard.basic(
            "A2a-023", "Origin Forme Palkia", "It soars across the sky in a form that greatly resembles the creator of all things. Perhaps this imitation of appearance is Palkia's strategy for gaining Arceus's powers.",
            120, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Zone Smash", "Flip a coin. If heads, this attack does 60 more damage.", EnergyCost.of(Type.WATER, 3), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(
                            new Literal(60),
                            new Product(new Literal(60), new NumberHeads())
                    ), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2a-024 - Phione
     */
    public static final PokemonCard PHIONE = PokemonCard.basic(
            "A2a-024", "Phione", "When the water warms, they inflate the flotation sac on their heads and drift languidly on the sea in packs.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Water Pulse", "Your opponent's Active Pokémon is now Asleep.", EnergyCost.of(Type.WATER, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new AddStatus(new SleepStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /** A2a-077 · Marill (alternate art of A2a-014) */
    public static final PokemonCard MARILL_IR = MARILL.withId("A2a-077", CardRarity.ILLUSTRATION_RARE);
    /** A2a-083 · Glaceon ex (alternate art of A2a-022) */
    public static final PokemonCard GLACEON_EX_UR = GLACEON_EX.withId("A2a-083", CardRarity.ULTRA_RARE);
    /** A2a-092 · Glaceon ex (alternate art of A2a-022) */
    public static final PokemonCard GLACEON_EX_SIR = GLACEON_EX.withId("A2a-092", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            MARILL, AZUMARILL, BARBOACH, WHISCASH, SNORUNT, FROSLASS, SNOVER, ABOMASNOW, GLACEON_EX,
            ORIGIN_FORME_PALKIA, PHIONE,
            MARILL_IR, GLACEON_EX_UR, GLACEON_EX_SIR);

    private Water() {
    }
}
