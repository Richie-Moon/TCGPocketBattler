package com.tcgpocket.pool.A2a;

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
import com.tcgpocket.number.Sum;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Triumphant Light — Grass.
 */
public final class Grass {

    /**
     * A2a-001 - Heracross
     */
    public static final PokemonCard HERACROSS = PokemonCard.basic(
            "A2a-001", "Heracross", "It loves sweet nectar. To keep all the nectar to itself, it hurls rivals away with its prized horn.",
            100, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Single-Horn Throw", "Flip 2 coins. If both of them are heads, this attack does 70 more damage.",
                    EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Sum(
                            new Literal(50),
                            new Branch(List.of(new Branch.Case(new AllFlipsHeads(), new Literal(70))), new Literal(0))
                    ), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2a-002 - Burmy
     */
    public static final PokemonCard BURMY = PokemonCard.basic(
            "A2a-002", "Burmy", "To shelter itself from cold, wintry winds, it covers itself with a cloak made of twigs and leaves.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2a-003 - Mothim
     */
    public static final PokemonCard MOTHIM = PokemonCard.evolution(
            "A2a-003", "Mothim", "It loves the honey of flowers and steals honey collected by Combee.",
            1, "Burmy", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                    "Gust", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2a-004 - Combee
     */
    public static final PokemonCard COMBEE = PokemonCard.basic(
            "A2a-004", "Combee", "At night, Combee sleep in a group of about a hundred, packed closely together in a lump.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bug Bite", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2a-005 - Vespiquen
     */
    public static final PokemonCard VESPIQUEN = PokemonCard.evolution(
            "A2a-005", "Vespiquen", "It houses its colony in cells in its body and releases various pheromones to make those grubs do its bidding.",
            1, "Combee", 100, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Reckless Charge", "This Pokémon also does 30 damage to itself.", EnergyCost.of(Type.GRASS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive()),
                    new DealDamage(new Literal(30), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2a-006 - Cherubi
     */
    public static final PokemonCard CHERUBI = PokemonCard.basic(
            "A2a-006", "Cherubi", "It nimbly dashes about to avoid getting pecked by bird Pokémon that would love to make off with its small, nutrient-rich storage ball.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leafage", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2a-007 - Cherrim (Overcast Form)
     */
    public static final PokemonCard CHERRIM = PokemonCard.evolution(
            "A2a-007", "Cherrim", "As a bud, it barely moves. It sits still, placidly waiting for sunlight to appear.",
            1, "Cherubi", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Worry Seed", "Your opponent's Active Pokémon is now Confused.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new AddStatus(new ConfusionStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2a-008 - Cherrim (Sunshine Form)
     */
    public static final PokemonCard CHERRIM_SUNSHINE = PokemonCard.evolution(
            "A2a-008", "Cherrim", "After absorbing plenty of sunlight, Cherrim takes this form. It's full of energy while it's like this, and its liveliness will go on until sundown.",
            1, "Cherubi", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leech Seed", "Heal 20 damage from this Pokémon.", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new HealDamage(new Literal(20), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2a-009 - Carnivine
     */
    public static final PokemonCard CARNIVINE = PokemonCard.basic(
            "A2a-009", "Carnivine", "It attracts prey with its sweet-smelling saliva, then chomps down. It takes a whole day to eat prey.",
            80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Vine Whip", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE).withAbility(Links.powerLink());

    /**
     * A2a-010 - Leafeon ex
     */
    public static final PokemonCard LEAFEON_EX = PokemonCard.evolution(
            "A2a-010", "Leafeon ex", "",
            1, "Eevee", 140, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Solar Beam", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX).withAbility(new ActivatedAbility("Forest Breath",
            "Once during your turn, if this Pokémon is in the Active Spot, you may take a Grass Energy from your Energy Zone and attach it to 1 of your Grass Pokémon.",
            new PlainAction("", new Attempt(List.of(
                    new AttachEnergy(Type.GRASS, new Literal(1), new ChosenFrom(
                            new AttackerAll(), new AttackerSide(), new IsType(Type.GRASS), "Choose a Grass Pokémon to attach a Grass Energy to"))))),
            true, new For(new IsActive(), new Self())));

    static final List<PokemonCard> CARDS = List.of(
            HERACROSS, BURMY, MOTHIM, COMBEE, VESPIQUEN, CHERUBI, CHERRIM, CHERRIM_SUNSHINE, CARNIVINE, LEAFEON_EX);

    private Grass() {
    }
}
