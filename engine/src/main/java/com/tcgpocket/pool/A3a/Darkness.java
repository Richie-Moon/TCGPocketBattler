package com.tcgpocket.pool.A3a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.EventSideIs;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsPoisoned;
import com.tcgpocket.condition.IsTool;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardRandomEnergy;
import com.tcgpocket.effect.DiscardRandomFromHand;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.FlipUntilTails;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.effect.RepeatEffect;
import com.tcgpocket.effect.ShuffleRandomFromHand;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnEnd;

import java.util.List;
import java.util.Optional;

/**
 * Extradimensional Crisis — Darkness.
 */
public final class Darkness {

    /**
     * A3a-037 - Alolan Meowth
     */
    public static final PokemonCard ALOLAN_MEOWTH = PokemonCard.basic(
            "A3a-037", "Alolan Meowth", "It's accustomed to luxury because it used to live with Alolan royalty. As a result, it's very picky about food.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Meddle", "Discard a random Pokémon Tool card from your opponent's hand.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DiscardRandomFromHand(new OpponentSide(), new Literal(1), Optional.of(new IsTool()))))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3a-038 - Alolan Persian
     */
    public static final PokemonCard ALOLAN_PERSIAN = PokemonCard.evolution(
            "A3a-038", "Alolan Persian", "The round face of Alolan Persian is considered to be a symbol of prosperity in the Alola region, so these Pokémon are very well cared for.",
            1, "Alolan Meowth", 100, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Fake Out", "Flip a coin. If heads, your opponent's Active Pokémon is now Paralyzed.", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new AddStatus(new ParalysisStatus(), new OpponentActive()))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3a-039 - Sandile
     */
    public static final PokemonCard SANDILE = PokemonCard.basic(
            "A3a-039", "Sandile", "It submerges itself in sand and moves as if swimming. This wise behavior keeps its enemies from finding it and maintains its temperature.",
            70, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3a-040 - Krokorok
     */
    public static final PokemonCard KROKOROK = PokemonCard.evolution(
            "A3a-040", "Krokorok", "Protected by thin membranes, their eyes can see even in the dead of night. They live in groups of a few individuals.",
            1, "Sandile", 90, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Light Punch", "", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3a-041 - Krookodile
     */
    public static final PokemonCard KROOKODILE = PokemonCard.evolution(
            "A3a-041", "Krookodile", "After clamping down with its powerful jaws, it twists its body around to rip its prey in half.",
            2, "Krokorok", 140, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Poaching Fangs", "Flip 3 coins. For each heads, a card is chosen at random from your opponent's hand. Your opponent reveals that card and shuffles it into their deck.", EnergyCost.of(Type.DARKNESS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive()),
                    new FlipN(new Literal(3)),
                    new RepeatEffect(new NumberHeads(), new ShuffleRandomFromHand(new OpponentSide()))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3a-042 - Nihilego — a second tick beside Poison's own. Two triggers because
     * {@code TurnEnd} fires on either player's turn and the targets are turn-relative:
     * "your opponent's Active" is the Opponent on your turn, the Attacker on theirs.
     */
    public static final PokemonCard NIHILEGO = PokemonCard.basic(
            "A3a-042", "Nihilego", "A life-form from another world, it was dubbed a UB and is thought to produce a strong neurotoxin.",
            70, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "New Wave", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.ULTRA_BEAST).withAbility(new PassiveAbility("More Poison",
            "Your opponent's Active Pokémon takes +10 damage from being Poisoned.",
            List.of(new Trigger(TurnEnd.class,
                            new And<>(new EventSideIs(new SelfSide()), new For(new IsPoisoned(), new OpponentActive())),
                            new Attempt(List.of(new PlaceDamage(new Literal(10), new OpponentActive())))),
                    new Trigger(TurnEnd.class,
                            new And<>(new Not<>(new EventSideIs(new SelfSide())), new For(new IsPoisoned(), new AttackerActive())),
                            new Attempt(List.of(new PlaceDamage(new Literal(10), new AttackerActive())))))));

    /**
     * A3a-043 - Guzzlord ex
     */
    public static final PokemonCard GUZZLORD_EX = PokemonCard.basic(
            "A3a-043", "Guzzlord ex", "",
            170, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Grindcore", "Flip a coin until you get tails. For each heads, discard a random Energy from your opponent's Active Pokémon.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new FlipUntilTails(),
                    new RepeatEffect(new NumberHeads(), new DiscardRandomEnergy(new Literal(1), new OpponentActive()))))
            ), new Action(
                    "Tyrannical Hole", "", EnergyCost.of(Type.DARKNESS, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.GRASS).withTags(CardTag.EX, CardTag.ULTRA_BEAST);

    /**
     * A3a-044 - Poipole
     */
    public static final PokemonCard POIPOLE = PokemonCard.basic(
            "A3a-044", "Poipole", "This Ultra Beast is well enough liked to be chosen as a first partner in its own world.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "2-Step", "Flip 2 coins. This attack does 20 damage for each heads.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(20)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING).withTags(CardTag.ULTRA_BEAST);

    /**
     * A3a-045 - Naganadel
     */
    public static final PokemonCard NAGANADEL = PokemonCard.evolution(
            "A3a-045", "Naganadel", "It stores hundreds of liters of poisonous liquid inside its body. It is one of the organisms known as UBs.",
            1, "Poipole", 100, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Electro House", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new AddStatus(new PoisonStatus(), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.ULTRA_BEAST);

    /** A3a-073 · Alolan Meowth (alternate art of A3a-037) */
    public static final PokemonCard ALOLAN_MEOWTH_IR = ALOLAN_MEOWTH.withId("A3a-073", CardRarity.ILLUSTRATION_RARE);
    /** A3a-079 · Guzzlord ex (alternate art of A3a-043) */
    public static final PokemonCard GUZZLORD_EX_UR = GUZZLORD_EX.withId("A3a-079", CardRarity.ULTRA_RARE);

    /** A3a-086 · Guzzlord ex (alternate art of A3a-043) */
    public static final PokemonCard GUZZLORD_EX_SIR = GUZZLORD_EX.withId("A3a-086", CardRarity.SPECIAL_ILLUSTRATION_RARE);
    /** A3a-103 · Nihilego (alternate art of A3a-042) */
    public static final PokemonCard NIHILEGO_HR = NIHILEGO.withId("A3a-103", CardRarity.HYPER_RARE);

    static final List<PokemonCard> CARDS = List.of(
            ALOLAN_MEOWTH, ALOLAN_PERSIAN, SANDILE, KROKOROK, KROOKODILE, NIHILEGO,
            GUZZLORD_EX, POIPOLE, NAGANADEL,
            ALOLAN_MEOWTH_IR, GUZZLORD_EX_UR,
            GUZZLORD_EX_SIR, NIHILEGO_HR);

    private Darkness() {
    }
}
