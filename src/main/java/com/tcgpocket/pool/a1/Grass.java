package com.tcgpocket.pool.a1;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.IsType;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.AttackerAll;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;
import java.util.Map;

/**
 * Genetic Apex — Grass.
 */
public final class Grass {

    /**
     * A1-001 · Bulbasaur — Vine Whip: 40 damage.
     */
    public static final PokemonCard BULBASAUR = PokemonCard.basic(
                    "A1-001", "Bulbasaur", "There is a plant seed on its back right from the day this Pokémon is born. The seed slowly grows larger.", 
                    70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1),
                    List.of(new Action(
                            "Vine Whip",
                            "",
                            EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive()))))), CardRarity.COMMON)
            .withWeakness(Type.FIRE);
    /**
     * A1-002: Ivysaur — Razor Leaf: 60 damage.
     */
    public static final PokemonCard IVYSAUR = PokemonCard.evolution(
                    "A1-002", "Ivysaur", "When the bulb on its back grows large, it appears to lose the ability to stand on its hind legs.",
                    1, "Bulbasaur", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2),
                    List.of(new Action(
                            "Razor Leaf",
                            "",
                            EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(60), new OpponentActive()))))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIRE);
    /**
     * A1-003: Venusaur
     */
    public static final PokemonCard VENUSAUR = PokemonCard.evolution(
                    "A1-003", "Venusaur", "The plant blooms when it is absorbing solar energy. It stays on the move to seek sunlight.",
                    2, "Ivysaur", 160, Type.GRASS, EnergyCost.of(Type.COLORLESS, 3),
                    List.of(new Action(
                            "Mega Drain",
                            "Heal 30 damage from this Pokémon.",
                            EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(80), new OpponentActive()),
                                    new HealDamage(new Literal(30), new Self()))))), CardRarity.RARE)
            .withWeakness(Type.FIRE);
    /**
     * A1-004 · Venusaur ex — Giant Bloom: 100 damage, and heal 30 from this
     * Pokemon.
     *
     * <p>Two effects in one attempt, in printed order. Healing a Venusaur at
     * full HP is a {@code NO_OP}, not a failure, so nothing swallows the rest
     * of the attack — which is why the order does not have to be defended.
     */
    public static final PokemonCard VENUSAUR_EX = PokemonCard.evolution(
                    "A1-004", "Venusaur ex", "", 2, "Ivysaur", 190, Type.GRASS, EnergyCost.of(Type.COLORLESS, 3),
                    List.of(new Action(
                            "Razor Leaf", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(new DealDamage(new Literal(60), new OpponentActive())))
                    ), new Action(
                            "Giant Bloom",
                            "This attack does 100 damage. Heal 30 damage from this Pokemon.",
                            EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(100), new OpponentActive()),
                                    new HealDamage(new Literal(30), new Self()))))), CardRarity.DOUBLE_RARE)
            .withWeakness(Type.FIRE)
            .withTags(CardTag.EX);

    /**
     * A1-005- Caterpie
     */
    public static final PokemonCard CATERPIE = PokemonCard.basic(
            "A1-005", "Caterpie", "For protection, it releases a horrible stench from the antenna on its head to drive away enemies.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Find a Friend", "Put 1 random Grass Pokémon from your deck into your hand.", EnergyCost.of(Type.COLORLESS, 1),
                    new Attempt(List.of(
                            new SearchDeck(
                                    new AttackerSide(),
                                    new Literal(1),
                                    new IsType(Type.GRASS)),
                            new ShuffleDeck(new AttackerSide())
                    ))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-006 - Metapod
     */
    public static final PokemonCard METAPOD = PokemonCard.evolution(
            "A1-006", "Metapod", "It is waiting for the moment to evolve. At this stage, it can  only harden, so it remains motionless to avoid attack.",
            1, "Caterpie", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Bug Bite", "", EnergyCost.of(Type.COLORLESS, 2),
                    new Attempt(List.of(
                            new DealDamage(new Literal(30), new OpponentActive())
                    ))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-007 - Butterfree
     */
    public static final PokemonCard BUTTERFREE = PokemonCard.evolution(
            "A1-007", "Butterfree", "In battle, it flaps its wings at great speed to release highly toxic dust into the air.",
            2, "Metapod", 120, Type.GRASS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Gust", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 2),
                    new Attempt(List.of(
                            new DealDamage(new Literal(60), new OpponentActive())
                    ))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE).withAbility(new ActivatedAbility(
            "Powder Heal",
            "Once during your turn, you may heal 20 damage from each of your Pokémon.",
            new PlainAction(
                    "Heal 20 damage from each of your Pokémon.",
                    new Attempt(List.of(
                            new HealEach(new Literal(20), new AttackerAll())
                    ))
            ),
            true,
            new Always<>()
    ));

    /**
     * A1-008 - Weedle
     */
    public static final PokemonCard WEEDLE = PokemonCard.basic(
            "A1-007", "Weedle", "Often found in forests and grasslands. It has a sharp, toxic barb of around two inches on top of its head.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sting", "", EnergyCost.of(Type.GRASS, 1),
                    new Attempt(List.of(
                            new DealDamage(new Literal(30), new OpponentActive())
                    ))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-009 - Kakuna
     */
    public static final PokemonCard KAKUNA = PokemonCard.evolution(
            "A1-009", "Kakuna", "Almost incapable of moving, this Pokémon can only harden its shell to protect itself when it is in danger.",
            1, "Weedle", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(
                    new Action("Bug Bite", "", EnergyCost.of(Type.GRASS, 1), new Attempt(
                            new DealDamage(new Literal(30), new OpponentActive())
                            ))
                    ), CardRarity.COMMON
            ).withWeakness(Type.FIRE);

    /**
     * A1-010 - Beedrill
     */
    public static final PokemonCard BEEDRILL = PokemonCard.evolution(
            "A1-010", "Beedrill", "It has three poisonous stingers on its forelegs and its tail. They are used to jab its enemies repeatedly.",
            2, "Kakuna", 120, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sharp Sting", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                            new DealDamage(new Literal(70), new OpponentActive())
                    ))
    )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A1-011 - Oddish
     */
    public static final PokemonCard ODDISH = PokemonCard.basic(
            "A1-011", "Oddish", "If exposed to moonlight, it starts to move. It roams far and wide at night to scatter its seeds.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                            new DealDamage(new Literal(20), new OpponentActive())
                    ))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-012 - Gloom
     */
    public static final PokemonCard GLOOM = PokemonCard.evolution(
            "A1-012", "Gloom", "Its pistils exude an incredibly foul odor. The horrid stench can cause fainting at a distance of 1.25 miles.",
            1, "Oddish", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Drool", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                            new DealDamage(new Literal(40), new OpponentActive())
                    ))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-013 - Vileplume
     */
    public static final PokemonCard VILEPLUME = PokemonCard.evolution(
            "A1-013", "Vileplume", "It has one of the world's largest petals. With every step, the petals shake out heavy clouds of toxic pollen",
            2, "Gloom", 140, Type.GRASS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Soothing Scent", "Your opponent's active Pokémon is now Asleep.", EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 1),
                    new Attempt(List.of(
                            new DealDamage(new Literal(80), new OpponentActive()),
                            new AddStatus(new OpponentActive(), new SleepStatus())
                    ))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    static final List<PokemonCard> CARDS =
            List.of(BULBASAUR, IVYSAUR, VENUSAUR, VENUSAUR_EX, CATERPIE, METAPOD, BUTTERFREE);

    private Grass() {
    }
}
