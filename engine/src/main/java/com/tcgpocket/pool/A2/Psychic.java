package com.tcgpocket.pool.A2;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.action.WithPrecondition;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.NoRetreatCost;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Space-Time Smackdown — Psychic.
 */
public final class Psychic {

    /**
     * A2-063 - Togepi
     */
    public static final PokemonCard TOGEPI = PokemonCard.basic(
            "A2-063", "Togepi", "The shell seems to be filled with joy. It is said that it will share good luck when treated kindly.",
            50, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Pound", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A2-064 - Togetic
     */
    public static final PokemonCard TOGETIC = PokemonCard.evolution(
            "A2-064", "Togetic", "They say that it will appear before kindhearted, caring people and shower them with happiness.",
            1, "Togepi", 80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Fairy Wind", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /**
     * A2-065 - Togekiss — Overdrive Smash: a boost to all of Togekiss's damage
     * through your next turn, which is the same thing since Overdrive Smash is
     * its only attack. Applied after this attack's damage, so it misses it.
     */
    public static final PokemonCard TOGEKISS = PokemonCard.evolution(
            "A2-065", "Togekiss", "These Pokémon are never seen anywhere near conflict or turmoil. In recent times, they've hardly been seen at all.",
            2, "Togetic", 140, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Overdrive Smash", "During your next turn, this Pokémon's Overdrive Smash attack does +60 damage.",
                    EnergyCost.of(Type.PSYCHIC, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new IncreaseDamage(new Literal(60), new Self(), new Literal(2))))
            )), CardRarity.RARE
    ).withWeakness(Type.METAL);

    /**
     * A2-066 - Misdreavus
     */
    public static final PokemonCard MISDREAVUS = PokemonCard.basic(
            "A2-066", "Misdreavus", "This Pokémon startles people in the middle of the night. It gathers fear as its energy.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Mumble", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2-067 - Mismagius ex
     */
    public static final PokemonCard MISMAGIUS_EX = PokemonCard.evolution(
            "A2-067", "Mismagius ex", "",
            1, "Misdreavus", 140, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Magical Delusion", "Your opponent's Active Pokémon is now Confused.", EnergyCost.of(Type.PSYCHIC, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new AddStatus(new ConfusionStatus(), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.DARKNESS).withTags(CardTag.EX);

    /**
     * A2-068 - Ralts
     */
    public static final PokemonCard RALTS = PokemonCard.basic(
            "A2-068", "Ralts", "The horns on its head provide a strong power that enables it to sense people's emotions.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Teleport", "Switch this Pokémon with 1 of your Benched Pokémon.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new SwitchActive(new SelfSide())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2-069 - Kirlia
     */
    public static final PokemonCard KIRLIA = PokemonCard.evolution(
            "A2-069", "Kirlia", "It has a psychic power that enables it to distort the space around it and see into the future.",
            1, "Ralts", 80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Slap", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2-070 - Duskull
     */
    public static final PokemonCard DUSKULL = PokemonCard.basic(
            "A2-070", "Duskull", "If it finds bad children who won't listen to their parents, it will spirit them away—or so it's said.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Will-O-Wisp", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2-071 - Dusclops
     */
    public static final PokemonCard DUSCLOPS = PokemonCard.evolution(
            "A2-071", "Dusclops", "It seeks drifting will-o'-the-wisps and sucks them into its empty body. What happens inside is a mystery.",
            1, "Duskull", 90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Psypunch", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /** Shadow Void's sources: your damaged Pokémon other than Dusknoir itself. */
    private static final IMultiTarget YOUR_OTHER_POKEMON = new Except(new AttackerAll(), new Self());

    /**
     * A2-072 - Dusknoir
     */
    public static final PokemonCard DUSKNOIR = PokemonCard.evolution(
            "A2-072", "Dusknoir", "At the bidding of transmissions from the spirit world, it steals people and Pokémon away. No one knows whether it has a will of its own.",
            2, "Dusclops", 130, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Devour Soul", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.RARE)
            .withAbility(new ActivatedAbility("Shadow Void",
                    "As often as you like during your turn, you may choose 1 of your Pokémon that has damage on it, and move all of its damage to this Pokémon.",
                    new PlainAction("", new Attempt(List.of(
                            new MoveDamage(new ChosenFrom(YOUR_OTHER_POKEMON, new AttackerSide(), new IsDamaged(),
                                    "Choose a Pokémon to move all of its damage to Dusknoir"), new Self())))),
                    false,
                    new ForAny(new IsDamaged(), YOUR_OTHER_POKEMON)))
            .withWeakness(Type.DARKNESS);

    /**
     * A2-073 - Drifloon
     */
    public static final PokemonCard DRIFLOON = PokemonCard.basic(
            "A2-073", "Drifloon", "It is whispered that any child who mistakes Drifloon for a balloon and holds on to it could wind up missing.",
            50, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Expand", "During your opponent's next turn, this Pokémon takes -20 damage from attacks.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new ReduceDamageTaken(new Literal(20), new Self(), new Literal(1))))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-074 - Drifblim
     */
    public static final PokemonCard DRIFBLIM = PokemonCard.evolution(
            "A2-074", "Drifblim", "Some say this Pokémon is a collection of souls burdened with regrets, silently drifting through the dusk.",
            1, "Drifloon", 100, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Balloon Strike", "", EnergyCost.of(Type.PSYCHIC, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2-075 - Uxie
     */
    public static final PokemonCard UXIE = PokemonCard.basic(
            "A2-075", "Uxie", "Known as \"The Being of Knowledge.\" It is said that it can wipe out the memory of those who see its eyes.",
            70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Mind Boost", "Take a Psychic Energy from your Energy Zone and attach it to Mesprit or Azelf.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new AttachEnergy(Type.PSYCHIC, new Literal(1), new ChosenFrom(
                            new AttackerAll(), new AttackerSide(), new Or<>(new IsSpecies("Mesprit"), new IsSpecies("Azelf")),
                            "Choose Mesprit or Azelf to attach a Psychic Energy to."))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2-076 - Mesprit
     */
    public static final PokemonCard MESPRIT = PokemonCard.basic(
            "A2-076", "Mesprit", "Known as \"The Being of Emotion.\" It taught humans the nobility of sorrow, pain, and joy.",
            70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new WithPrecondition(
                    new And<>(new ForAny(new IsSpecies("Uxie"), new AttackerBench()),
                            new ForAny(new IsSpecies("Azelf"), new AttackerBench())),
                    new Action(
                            "Supreme Blast", "You can use this attack only if you have Uxie and Azelf on your Bench. Discard all Energy from this Pokémon.",
                            EnergyCost.of(Type.PSYCHIC, 3), new Attempt(List.of(
                            new DealDamage(new Literal(160), new OpponentActive()),
                            new DiscardAllEnergy(new Self()))))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS);

    /**
     * A2-077 - Azelf
     */
    public static final PokemonCard AZELF = PokemonCard.basic(
            "A2-077", "Azelf", "Known as \"The Being of Willpower.\" It sleeps at the bottom of a lake to keep the world in balance.",
            70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Psychic Arrow", "This attack does 20 damage to 1 of your opponent's Pokémon.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2-078 - Giratina
     */
    public static final PokemonCard GIRATINA = PokemonCard.basic(
            "A2-078", "Giratina", "This Pokémon is said to live in a world on the reverse side of ours, where common knowledge is distorted and strange.",
            120, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Spooky Shot", "", EnergyCost.of(Type.PSYCHIC, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.RARE)
            .withAbility(new PassiveAbility("Levitate", "If this Pokémon has any Energy attached, it has no Retreat Cost.",
                    new NoRetreatCost(new HasEnergy(EnergyCost.of(Type.COLORLESS, 1)))))
            .withWeakness(Type.DARKNESS);

    /**
     * A2-079 - Cresselia
     */
    public static final PokemonCard CRESSELIA = PokemonCard.basic(
            "A2-079", "Cresselia", "Shiny particles are released from its wings like a veil. It is said to represent the crescent moon.",
            110, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Moonlight Gain", "Heal 20 damage from this Pokémon.", EnergyCost.of(Type.PSYCHIC, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new HealDamage(new Literal(20), new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS);

    static final List<PokemonCard> CARDS = List.of(
            TOGEPI, TOGETIC, TOGEKISS, MISDREAVUS, MISMAGIUS_EX, RALTS, KIRLIA, DUSKULL, DUSCLOPS,
            DUSKNOIR, DRIFLOON, DRIFBLIM, UXIE, MESPRIT, AZELF, GIRATINA, CRESSELIA);

    private Psychic() {
    }
}
