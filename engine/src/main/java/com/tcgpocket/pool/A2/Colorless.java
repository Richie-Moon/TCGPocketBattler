package com.tcgpocket.pool.A2;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CurrentHP;
import com.tcgpocket.number.DamageOn;
import com.tcgpocket.number.Difference;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Quotient;
import com.tcgpocket.number.Sum;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Space-Time Smackdown — Colorless.
 */
public final class Colorless {

    /**
     * A2-124 - Lickitung
     */
    public static final PokemonCard LICKITUNG = PokemonCard.basic(
            "A2-124", "Lickitung", "If this Pokémon's sticky saliva gets on you and you don't clean it off, an intense itch will set in. The itch won't go away, either.",
            80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Tongue Slap", "", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-125 - Lickilicky ex
     */
    public static final PokemonCard LICKILICKY_EX = PokemonCard.evolution(
            "A2-125", "Lickilicky ex", "",
            1, "Lickitung", 160, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Licking Fury", "Flip a coin until you get tails. This attack does 40 more damage for each heads.",
                    EnergyCost.of(Type.COLORLESS, 4), new Attempt(List.of(
                    new FlipUntilTails(),
                    new DealDamage(new Sum(
                            new Literal(100),
                            new Product(new NumberHeads(), new Literal(40))), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX);

    /**
     * A2-126 - Eevee
     */
    public static final PokemonCard EEVEE = PokemonCard.basic(
            "A2-126", "Eevee", "Its ability to evolve into many forms allows it to adapt smoothly and perfectly to any environment.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Quick Attack", "Flip a coin. If heads, this attack does 20 more damage.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(
                            new Literal(10),
                            new Product(new NumberHeads(), new Literal(20))), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-127 - Porygon
     */
    public static final PokemonCard PORYGON = PokemonCard.basic(
            "A2-127", "Porygon", "State-of-the-art technology was used to create Porygon. It was the first artificial Pokémon to be created via computer programming.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Beam", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-128 - Porygon2
     */
    public static final PokemonCard PORYGON2 = PokemonCard.evolution(
            "A2-128", "Porygon2", "This is a Porygon that was updated with special data. Porygon2 develops itself by learning about many different subjects all on its own.",
            1, "Porygon", 80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sharpen", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-129 - Porygon-Z
     */
    public static final PokemonCard PORYGON_Z = PokemonCard.evolution(
            "A2-129", "Porygon-Z", "Porygon-Z had a program installed to allow it to move between dimensions, but the program also caused instability in Porygon-Z's behavior.",
            2, "Porygon2", 140, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Buggy Beam", "Change the type of the next Energy that will be generated for your opponent to 1 of the following at random: Grass, Fire, Water, Lightning, Psychic, Fighting, Darkness, or Metal.",
                    EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new ChangeNextEnergy(new OpponentSide(), List.of(
                            Type.GRASS, Type.FIRE, Type.WATER, Type.LIGHTNING,
                            Type.PSYCHIC, Type.FIGHTING, Type.DARKNESS, Type.METAL))))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-130 - Aipom
     */
    public static final PokemonCard AIPOM = PokemonCard.basic(
            "A2-130", "Aipom", "As it did more and more with its tail, its hands became clumsy. It makes its nest high in the treetops.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tail Jab", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-131 - Ambipom
     */
    public static final PokemonCard AMBIPOM = PokemonCard.evolution(
            "A2-131", "Ambipom", "It uses its tails for everything. If it wraps both of its tails around you and gives you a squeeze, that's proof it really likes you.",
            1, "Aipom", 90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Double Hit", "Flip 2 coins. This attack does 40 damage for each heads.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(40)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-132 - Starly — the Tool goes before the damage, so a Tool that would
     * have reduced it no longer can.
     */
    public static final PokemonCard STARLY = PokemonCard.basic(
            "A2-132", "Starly", "They flock around mountains and fields, chasing after bug Pokémon. Their singing is noisy and annoying.",
            50, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Pluck", "Before doing damage, discard all Pokémon Tools from your opponent's Active Pokémon.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DiscardTool(new OpponentActive()),
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-133 - Staravia
     */
    public static final PokemonCard STARAVIA = PokemonCard.evolution(
            "A2-133", "Staravia", "Recognizing their own weakness, they always live in a group. When alone, a Staravia cries noisily.",
            1, "Starly", 80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Wing Attack", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-134 - Staraptor
     */
    public static final PokemonCard STARAPTOR = PokemonCard.evolution(
            "A2-134", "Staraptor", "When Staravia evolve into Staraptor, they leave the flock to live alone. They have sturdy wings.",
            2, "Staravia", 140, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Clutch", "During your opponent's next turn, the Defending Pokémon can't retreat.", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new PreventRetreat(new OpponentActive(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-135 - Bidoof — Super Fang places damage rather than dealing it, so
     * Weakness and damage modifiers don't apply. "Rounded down" applies to the
     * HP that is left, in steps of 10: 70 HP goes to 30, so 40 damage.
     */
    public static final PokemonCard BIDOOF = PokemonCard.basic(
            "A2-135", "Bidoof", "With nerves of steel, nothing can perturb it. It is more agile and active than it appears.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Super Fang", "Halve your opponent's Active Pokémon's remaining HP, rounded down.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new PlaceDamage(new Difference(
                            new CurrentHP(new OpponentActive()),
                            new Product(new Quotient(new CurrentHP(new OpponentActive()), new Literal(20)), new Literal(10))),
                            new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-136 - Bibarel
     */
    public static final PokemonCard BIBAREL = PokemonCard.evolution(
            "A2-136", "Bibarel", "It busily makes its nest with stacks of branches and roots it has cut up with its sharp incisors.",
            1, "Bidoof", 110, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Rolling Tackle", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-137 - Buneary
     */
    public static final PokemonCard BUNEARY = PokemonCard.basic(
            "A2-137", "Buneary", "If both of Buneary's ears are rolled up, something is wrong with its body or mind. It's a sure sign the Pokémon is in need of care.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Splash", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-138 - Lopunny
     */
    public static final PokemonCard LOPUNNY = PokemonCard.evolution(
            "A2-138", "Lopunny", "Lopunny is constantly monitoring its surroundings. If danger approaches, this Pokémon responds with superdestructive kicks.",
            1, "Buneary", 90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Jump Kick", "This attack also does 20 damage to 1 of your opponent's Benched Pokémon.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new DealDamage(new Literal(20), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Select a Benched Pokémon"))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-139 - Glameow
     */
    public static final PokemonCard GLAMEOW = PokemonCard.basic(
            "A2-139", "Glameow", "It claws if displeased and purrs when affectionate. Its fickleness is very popular among some.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Pose", "Flip a coin. If tails, this attack does nothing.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new Fail()),
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-140 - Purugly
     */
    public static final PokemonCard PURUGLY = PokemonCard.evolution(
            "A2-140", "Purugly", "It would claim another Pokémon's nest as its own if it finds a nest sufficiently comfortable.",
            1, "Glameow", 110, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Interrupt", "Your opponent reveals their hand. Choose a card you find there and shuffle it into your opponent's deck.",
                    EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new RevealHand(new OpponentSide()),
                    new ShuffleFromHand(new OpponentSide())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2-141 - Chatot
     */
    public static final PokemonCard CHATOT = PokemonCard.basic(
            "A2-141", "Chatot", "It mimics the cries of other Pokémon to trick them into thinking it's one of them. This way they won't attack it.",
            70, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Fury Attack", "Flip 3 coins. This attack does 20 damage for each heads.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(3)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(20)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-142 - Fan Rotom
     */
    public static final PokemonCard FAN_ROTOM = PokemonCard.basic(
            "A2-142", "Fan Rotom", "This Rotom has entered an electric fan. It smirks with satisfaction over a prank well pulled after it blows away everything around it.",
            80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Spin Storm", "Flip a coin. If heads, put your opponent's Active Pokémon into their hand.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new ReturnToHand(new OpponentActive()))))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-143 - Regigigas
     */
    public static final PokemonCard REGIGIGAS = PokemonCard.basic(
            "A2-143", "Regigigas", "It is said to have made Pokémon that look like itself from a special ice mountain, rocks, and magma.",
            140, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Raging Hammer", "This attack does more damage equal to the damage this Pokémon has on it.", EnergyCost.of(Type.COLORLESS, 4), new Attempt(List.of(
                    new DealDamage(new Sum(new Literal(50), new DamageOn(new Self())), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    static final List<PokemonCard> CARDS = List.of(
            LICKITUNG, LICKILICKY_EX, EEVEE, PORYGON, PORYGON2, PORYGON_Z, AIPOM, AMBIPOM,
            STARLY, STARAVIA, STARAPTOR, BIDOOF, BIBAREL, BUNEARY, LOPUNNY, GLAMEOW, PURUGLY,
            CHATOT, FAN_ROTOM, REGIGIGAS);

    private Colorless() {
    }
}
