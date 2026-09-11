package com.tcgpocket.pool.A1;

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
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.*;

import java.util.List;

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
                    "",
                    new Attempt(List.of(
                            new HealEach(new Literal(20), new AttackerAll())
                    ))
            ),
            true,
            new ForAny(new IsDamaged(), new AttackerAll())
    ));

    /**
     * A1-008 - Weedle
     */
    public static final PokemonCard WEEDLE = PokemonCard.basic(
            "A1-008", "Weedle", "Often found in forests and grasslands. It has a sharp, toxic barb of around two inches on top of its head.",
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
                            new AddStatus(new SleepStatus(), new OpponentActive())
                    ))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A1-014 - Paras
     */
    public static final PokemonCard PARAS = PokemonCard.basic(
            "A1-014", "Paras", "The mushrooms, known as tochukaso, are controlling the bug. Even if the bug bugs the mushrooms, they tell it to bug off.",
            70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Scratch", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1),
                    new Attempt(List.of(
                            new DealDamage(new Literal(30), new OpponentActive())
                    ))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-015 - Parasect
     */
    public static final PokemonCard PARASECT = PokemonCard.evolution(
            "A1-015", "Parasect", "The bug is mostly dead, with the mushrooms on its back having become the main body. If the mushroom comes off, the bug stops moving.",
            1, "Paras", 120, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Slash", "", EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 1),
                    new Attempt(List.of(
                            new DealDamage(new Literal(80), new OpponentActive())
                    ))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-016 - Venonat
     */
    public static final PokemonCard VENONAT = PokemonCard.basic(
            "A1-016", "Venonat", "Poison oozes from all over its bosy. It catches small bug Pokémon at night that are attracted by light.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1),
                    new Attempt(List.of(
                            new DealDamage(new Literal(20), new OpponentActive())
                    ))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-017 - Venomoth
     */
    public static final PokemonCard VENOMOTH = PokemonCard.evolution(
            "A1-017", "Venomoth", "Its wings are covered with dustlike scales. Every time it flaps its wings, it looss highly toxic dust",
            1, "Venonat", 120, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Poison Powder", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 1),
                    new Attempt(List.of(
                            new DealDamage(new Literal(30), new OpponentActive()),
                            new AddStatus(new PoisonStatus(), new OpponentActive())
                    ))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-018 - Bellsprout
     */
    public static final PokemonCard BELLSPROUT = PokemonCard.basic(
            "A1-018", "Bellsprout", "Even though its body is extremely skinny, it is blindingly fast when catching its prey.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Vine Whip", "", EnergyCost.of(Type.GRASS, 1),
                    new Attempt(List.of(
                            new DealDamage(new Literal(20), new OpponentActive())
                    ))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-019 - Weepinbell
     */
    public static final PokemonCard WEEPINBELL = PokemonCard.evolution(
            "A1-019", "Weepinbell", "The leafy parts act as cutters for slashing foes. It spits a fluid that dissolves everything.",
            1, "Bellsprout", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Razor Leaf", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1),
                    new Attempt(List.of(
                            new DealDamage(new Literal(40), new OpponentActive())
                    ))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-020 - Victreebel
     */
    public static final PokemonCard VICTREEBEL = PokemonCard.evolution(
            "A1-020", "Victreebel", "Said to live in huge colonies deep in jungles, although no one has ever returned from there.",
            2, "Weepinbell", 140, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Vine Whip", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1),
                    new Attempt(List.of(
                            new DealDamage(new Literal(60), new OpponentActive())
                    ))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE).withAbility(new ActivatedAbility(
                    "Fragrance Trap",
                    "If this Pokemon is in the Active Spot, once during your turn, you may switch in "
                            + "1 of your opponent's Benched Basic Pokémon to the Active Spot.",
                    new PlainAction(
                            "",
                            new Attempt(List.of(
                                    new SwitchActive(
                                            new OpponentSide(),
                                            new ChosenFrom(
                                                    new OpponentBench(),
                                                    new AttackerSide(),
                                                    new IsBasic(),
                                                    "Choose a Benched Basic Pokemon to bring up"))))),
                    true,
                    // Not offered unless it could do something: Victreebel has to be
                    // Active, and there has to be a Basic on the other bench to drag up.
                    new And<>(
                            new For(new IsActive(), new Self()),
                            new ForAny(new IsBasic(), new OpponentBench()))
            )
    );

    /**
     * A1-021 - Exeggcute
     */
    public static final PokemonCard EXEGGCUTE = PokemonCard.basic(
            "A1-021", "Exeggcute", "Though it may look like it's just a bunch of eggs, it's actually a proper Pokémon. Exeggcute communicates with others of its kind via telepathy, apparently.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Seed Bomb", "", EnergyCost.of(Type.GRASS, 1), new Attempt(
                    List.of(new DealDamage(new Literal(20), new OpponentActive())
                    ))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-022 - Exeggutor
     */
    public static final PokemonCard EXEGGUTOR = PokemonCard.evolution(
            "A1-022", "Exeggutor", "Each of Exeggutor's three heads is thinking different thoughts. The three don't seem to be very interested in one another.",
            1, "Exeggcute", 130, Type.GRASS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Stomp", "Flip a coin. If heads, this attack does 30 more damage.",
                    EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(
                            new Sum(
                                    new Literal(30),
                                    new Product(new NumberHeads(), new Literal(30))),
                            new OpponentActive()))))),
            CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-023 - Exeggcutor ex
     */
    public static final PokemonCard EXEGGUTOR_EX = PokemonCard.evolution(
            "A1-023", "Exeggutor ex", "",
            1, "Exeggcute", 160, Type.GRASS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Tropical Swing", "Flip a coin. If heads, this attack does 40 more damage.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Sum(
                            new Literal(40),
                            new Product(new NumberHeads(), new Literal(40))),
                            new OpponentActive()
                    ))
            ))), CardRarity.RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX);

    /**
     * A1-024 - Tangela
     */
    public static final PokemonCard TANGELA = PokemonCard.basic(
            "A1-024", "Tangela", "Hidden beneath a tangle of vines that grows nonstop even if the vines are torn off, this Pokémon's true appearance remains a mystery.",
            80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Absorb", "Heal 10 damage from this Pokémon.", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new HealDamage(new Literal(10), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-025 - Scyther
     */
    public static final PokemonCard SCYTHER = PokemonCard.basic(
            "A1-025", "Scyther", "It slashes through the grass with its sharp scythes, moving too fast for the human eye to track.",
            70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sharp Scythe", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-026 - Pinsir
     */
    public static final PokemonCard PINSIR = PokemonCard.basic(
            "A1-026", "Pinsir", "These Pokémon judge one another based on pincers. Ticker, more impressive pincers make for more popularity with the opposite gender.",
            90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Double Horn", "Flip 2 coins. This attack does 50 damage for each heads.", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Product(new Literal(50), new NumberHeads()), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-027 - Cottonee
     */
    public static final PokemonCard COTTONEE = PokemonCard.basic(
            "A1-027", "Cottonee", "It shoots cotton from its body to protect itself. If it gets caught up  in hurricane-strength winds, it can get sent to the other side of the earth.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Attach", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-028 - Whimsicott
     */
    public static final PokemonCard WHIMSICOTT = PokemonCard.basic(
            "A1-028", "Whimsicott", "It scatters cotton all over the place as a prank. If it gets wet, it'll become too heavy to move and have no choice but to answer for its mischief.",
            80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Rolling Tackle", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-029 - Petilil
     */
    public static final PokemonCard PETILIL = PokemonCard.basic(
            "A1-029", "Petilil", "The leaves on its head grows right back even if they fall out. These bitter leaves refresh those who eat them.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Blot", "Heal 10 damage from this Pokémon.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new HealDamage(new Literal(10), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-030 - Lilligant
     */
    public static final PokemonCard LILLIGANT = PokemonCard.evolution(
            "A1-030", "Lilligant", "The fragrance of the garland on its head has a relaxing effect, but taking care of it is very difficult.",
            1, "Petilil", 100, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leaf Supply", "Take a Grass Energy from your Energy Zone and attach it to 1 of your Benched Grass Pokémon.", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new AttachEnergy(Type.GRASS, new Literal(1), new ChosenFrom(
                            new AttackerBench(), new AttackerSide(), new IsType(Type.GRASS), "Please choose a Grass-type Pokémon to attach an Energy to.")
                    )))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-031 - Skiddo - Surprise Attack: 30 damage, or no attack at all.
     *
     * <p>"If tails, this attack does nothing" is not 0 damage. The Fail aborts
     * the attempt, so DealDamage never runs: no damage event is dispatched, no
     * weakness is applied, and nothing that answers being hit - a Rocky Helmet,
     * a damage-triggered Tool - gets to answer. Dealing 0 would fire all of it.
     *
     * <p>The attack was still made, and the attempt failing is not the action
     * being illegal. TurnEngine must end the turn on a tails.
     */
    public static final PokemonCard SKIDDO = PokemonCard.basic(
            "A1-031", "Skiddo", "Until recently, people living in the mountains would ride on the backs of these Pokémon to traverse the mountain paths..",
            70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Surprise Attack", "Flip a coin. If tails, this attack does nothing.",
                    EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new Fail()),
                    new DealDamage(new Literal(40), new OpponentActive()))))),
            CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1-032 - Gogoat
     */
    public static final PokemonCard GOGOAT = PokemonCard.evolution(
            "A1-032", "Gogoat", "It can sense the feelings of others by touching them with its horns. This species has assisted people with their work since 5000 years ago.",
            1, "Skiddo", 120, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Razor Leaf", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    static final List<PokemonCard> CARDS = List.of(
            BULBASAUR, IVYSAUR, VENUSAUR, VENUSAUR_EX, CATERPIE, METAPOD, BUTTERFREE, WEEDLE, KAKUNA,
            BEEDRILL, ODDISH, GLOOM, VILEPLUME, PARAS, PARASECT, VENONAT, VENOMOTH, BELLSPROUT, WEEPINBELL, VICTREEBEL,
            EXEGGCUTE, EXEGGUTOR, EXEGGUTOR_EX, TANGELA, SCYTHER, PINSIR, COTTONEE, WHIMSICOTT, PETILIL, LILLIGANT,
            SKIDDO, GOGOAT);

    private Grass() {
    }
}
