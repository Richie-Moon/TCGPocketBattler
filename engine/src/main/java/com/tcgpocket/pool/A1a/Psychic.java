package com.tcgpocket.pool.A1a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.*;

import java.util.List;
import java.util.Optional;

/**
 * Mythical Island — Psychic.
 */
public final class Psychic {
    /**
     * A1a-031 - Mew
     */
    public static final PokemonCard MEW = PokemonCard.basic(
            "A1a-031", "Mew", "Because it can use all kinds of moves, many scientists believe Mew to be the ancestor of Pokémon.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Psy Report", "Your opponent reveals their hand.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new RevealHand(new OpponentSide())))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS);

    /**
     * A1a-032 - Mew ex
     */
    public static final PokemonCard MEW_EX = PokemonCard.basic(
            "A1a-032", "Mew ex", "",
            130, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Psyshot", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            ), new Action(
                    "Genome Hacking", "Choose 1 of your opponent's Active Pokémon's attacks and use it as this attack.",
                    EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new CopyAttack(new Matching(new OpponentAll(), new IsActive()), false)))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.DARKNESS).withTags(CardTag.EX);

    /**
     * A1a-033 - Sigilyph
     */
    public static final PokemonCard SIGILYPH = PokemonCard.basic(
            "A1a-033", "Sigilyph", "Psychic power allows these Pokémon to fly. Some say they were the guardians of an ancient city. Others say they were the guardians' emissaries.",
            80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Spike Draw", "Draw a card.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new DrawCard(new Literal(1), new AttackerSide())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A1a-034 - Elgyem
     */
    public static final PokemonCard ELGYEM = PokemonCard.basic(
            "A1a-034", "Elgyem", "If this Pokémon stands near a TV, strange scenery will appear on the screen. That scenery is said to be from its home.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Headbutt", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A1a-035 - Beheeyem
     */
    public static final PokemonCard BEHEEYEM = PokemonCard.evolution(
            "A1a-035", "Beheeyem", "Whenever a Beheeyem visits a farm, a Dubwool mysteriously disappears.",
            1, "Elgyem", 90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Mind Jack", "This attack does 20 more damage for each of your opponent's Benched Pokémon.",
                    EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Sum(
                            new Literal(10),
                            new Product(new Literal(20), new CountCards(new OpponentSide(), Zone.BENCH, Optional.empty()))
                    ), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A1a-036 - Flabébé
     */
    public static final PokemonCard FLABEBE = PokemonCard.basic(
            "A1a-036", "Flabébé", "This Pokémon can draw forth the power hidden within blooming wild flowers. It is particularly fond of red flowers.",
            40, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Hypnotic Gaze", "Your opponent's Active Pokémon is now Asleep.", EnergyCost.of(Type.PSYCHIC, 1),
                    new Attempt(List.of(
                            new AddStatus(new SleepStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A1a-037 - Floette
     */
    public static final PokemonCard FLOETTE = PokemonCard.evolution(
            "A1a-037", "Floette", "This Pokémon draws forth what power is left in withered flowers to make them healthy again. It holds a red flower.",
            1, "Flabébé", 70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leaf Drain", "Heal 10 damage from this Pokémon.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new HealDamage(new Literal(10), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A1a-038 - Florges
     */
    public static final PokemonCard FLORGES = PokemonCard.evolution(
            "A1a-038", "Florges", "This Pokémon creates an impressive flower garden in its territory. It draws forth the power of the red flowers around its neck.",
            2, "Floette", 120, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bloomshine", "Heal 20 damage from each of your Pokémon.", EnergyCost.of(Type.PSYCHIC, 2), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new HealEach(new Literal(20), new AttackerAll())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /**
     * A1a-039 - Swirlix
     */
    public static final PokemonCard SWIRLIX = PokemonCard.basic(
            "A1a-039", "Swirlix", "It eats its own weight in sugar every day. If it doesn't get enough sugar, it becomes incredibly grumpy.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Fairy Wind", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A1a-040 - Slurpuff
     */
    public static final PokemonCard SLURPUFF = PokemonCard.evolution(
            "A1a-040", "Slurpuff", "By taking in a person's scent, it can sniff out their mental and physical condition. It's hoped that this skill will have many medical applications.",
            1, "Swirlix", 100, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Magical Shot", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    static final List<PokemonCard> CARDS = List.of(
            MEW, MEW_EX, SIGILYPH, ELGYEM, BEHEEYEM, FLABEBE, FLOETTE, FLORGES, SWIRLIX, SLURPUFF);

    private Psychic() {
    }
}
