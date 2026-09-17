package com.tcgpocket.pool.A1;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.CopyAttack;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardRandomEnergy;
import com.tcgpocket.effect.DiscardRandomFromHand;
import com.tcgpocket.effect.DrawCard;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.FlipUntilTails;
import com.tcgpocket.effect.ShuffleIntoDeck;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentSide;

import java.util.List;
import java.util.Optional;

/** Genetic Apex — Colorless. */
public final class Colorless {

    private Colorless() {
    }

    /**
     * A1-186 - Pidgey
     */
    public static final PokemonCard PIDGEY = PokemonCard.basic(
                    "A1-186", "Pidgey", "A common sight in forests and woods. It flaps its wings at ground level to kick up blinding sand.",
                    60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Gust", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-187 - Pidgeotto
     */
    public static final PokemonCard PIDGEOTTO = PokemonCard.evolution(
                    "A1-187", "Pidgeotto", "The claws on its feet are well developed. It can carry prey such as an Exeggcute to its nest over 60 miles away.",
                    1, "Pidgey", 80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Gust", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-188 - Pidgeot
     */
    public static final PokemonCard PIDGEOT = PokemonCard.evolution(
                    "A1-188", "Pidgeot", "When hunting, it skims the surface of water at high speed to pick off unwary prey such as Magikarp.",
                    2, "Pidgeotto", 130, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Wing Attack", "", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-189 - Rattata
     */
    public static final PokemonCard RATTATA = PokemonCard.basic(
                    "A1-189", "Rattata", "Its incisors grow continuously throughout its life. If its incisors get too long, this Pokémon becomes unable to eat, and it starves to death.",
                    40, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Gnaw", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-190 - Raticate
     */
    public static final PokemonCard RATICATE = PokemonCard.evolution(
                    "A1-190", "Raticate", "People say that it fled from its enemies by using its small webbed hind feet to swim from island to island in Alola.",
                    1, "Rattata", 80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Bite", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-191 - Spearow
     */
    public static final PokemonCard SPEAROW = PokemonCard.basic(
                    "A1-191", "Spearow", "Its reckless nature leads it to stand up to others—even large Pokémon—if it has to protect its territory.",
                    60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Peck", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-192 - Fearow
     */
    public static final PokemonCard FEAROW = PokemonCard.evolution(
                    "A1-192", "Fearow", "Carrying food through Fearow's territory is dangerous. It will snatch the food away from you in a flash!",
                    1, "Spearow", 100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Drill Run", "Flip a coin. If heads, discard a random Energy from your opponent's Active Pokémon.", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new FlipN(new Literal(1)),
                                    new DealDamage(new Literal(50), new OpponentActive()),
                                    new ConditionalEffect(
                                            new LastCoinTossHeads(),
                                            new DiscardRandomEnergy(new Literal(1), new OpponentActive()))
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-193 - Jigglypuff
     */
    public static final PokemonCard JIGGLYPUFF = PokemonCard.basic(
                    "A1-193", "Jigglypuff", "When its huge eyes waver, it sings a mysteriously soothing melody that lulls its enemies to sleep.",
                    60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Pound", "", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-194 - Wigglytuff
     */
    public static final PokemonCard WIGGLYTUFF = PokemonCard.evolution(
                    "A1-194", "Wigglytuff", "It has a very fine fur. Take care not to make it angry, or it may inflate steadily and hit with a body slam.",
                    1, "Jigglypuff", 100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Hyper Voice", "", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(60), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-195 - Wigglytuff ex
     */
    public static final PokemonCard WIGGLYTUFF_EX = PokemonCard.evolution(
                    "A1-195", "Wigglytuff ex", "",
                    1, "Jigglypuff", 140, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Sleepy Song", "Your opponent's Active Pokémon is now Asleep.", EnergyCost.of(Type.COLORLESS, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(80), new OpponentActive()),
                                    new AddStatus(new SleepStatus(), new OpponentActive())
                            )))), CardRarity.DOUBLE_RARE)
            .withWeakness(Type.FIGHTING)
            .withTags(CardTag.EX);

    /**
     * A1-196 - Meowth
     */
    public static final PokemonCard MEOWTH = PokemonCard.basic(
                    "A1-196", "Meowth", "All it does is sleep during the daytime. At night, it patrols its territory with its eyes aglow.",
                    60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Pay Day", "Draw a card.", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive()),
                                    new DrawCard(new Literal(1), new AttackerSide())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-197 - Persian
     */
    public static final PokemonCard PERSIAN = PokemonCard.evolution(
                    "A1-197", "Persian", "Although its fur has many admirers, it is tough to raise as a pet because of its fickle meanness.",
                    1, "Meowth", 90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Shadow Claw", "Flip a coin. If heads, discard a random card from your opponent's hand.", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new FlipN(new Literal(1)),
                                    new DealDamage(new Literal(40), new OpponentActive()),
                                    new ConditionalEffect(
                                            new LastCoinTossHeads(),
                                            new DiscardRandomFromHand(new OpponentSide(), new Literal(1)))
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-198 - Farfetch'd
     */
    public static final PokemonCard FARFETCHD = PokemonCard.basic(
                    "A1-198", "Farfetch'd", "The stalk this Pokémon carries in its wings serves as a sword to cut down opponents. In a dire situation, the stalk can also serve as food.",
                    60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Leek Slap", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-199 - Doduo
     */
    public static final PokemonCard DODUO = PokemonCard.basic(
                    "A1-199", "Doduo", "A two-headed Pokémon that was discovered as a sudden mutation. It runs at a pace of over 60 miles per hour.",
                    60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Peck", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-200 - Dodrio
     */
    public static final PokemonCard DODRIO = PokemonCard.evolution(
                    "A1-200", "Dodrio", "An enemy that takes its eyes off any of the three heads—even for a second—will get pecked severely.",
                    1, "Doduo", 80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                            "Drill Peck", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-201 - Lickitung
     */
    public static final PokemonCard LICKITUNG = PokemonCard.basic(
                    "A1-201", "Lickitung", "If this Pokémon's sticky saliva gets on you and you don't clean it off, an intense itch will set in. The itch won't go away, either.",
                    90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Continuous Lick", "Flip a coin until you get tails. This attack does 60 damage for each heads.", EnergyCost.of(Type.COLORLESS, 3),
                            new Attempt(List.of(
                                    new FlipUntilTails(),
                                    new DealDamage(new Product(new Literal(60), new NumberHeads()), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-202 - Chansey
     */
    public static final PokemonCard CHANSEY = PokemonCard.basic(
                    "A1-202", "Chansey", "This kindly Pokémon lays highly nutritious eggs and shares them with injured Pokémon or people.",
                    120, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Gentle Slap", "", EnergyCost.of(Type.COLORLESS, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(60), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-203 - Kangaskhan
     */
    public static final PokemonCard KANGASKHAN = PokemonCard.basic(
                    "A1-203", "Kangaskhan", "Although it's carrying its baby in a pouch on its belly, Kangaskhan is swift on its feet. It intimidates its opponents with quick jabs.",
                    100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Dizzy Punch", "Flip 2 coins. This attack does 30 damage for each heads.", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new FlipN(new Literal(2)),
                                    new DealDamage(new Product(new Literal(30), new NumberHeads()), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-204 - Tauros
     */
    public static final PokemonCard TAUROS = PokemonCard.basic(
                    "A1-204", "Tauros", "When Tauros begins whipping itself with its tails, it's a warning that the Pokémon is about to charge with astounding speed.",
                    100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Horn Attack", "", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(50), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-205 - Ditto
     */
    public static final PokemonCard DITTO = PokemonCard.basic(
                    "A1-205", "Ditto", "Its transformation ability is perfect. However, if made to laugh, it can't maintain its disguise.",
                    70, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Copy Anything", "Choose 1 of your opponent's Pokémon's attacks and use it as this attack. If this Pokémon doesn't have the necessary Energy to use that attack, this attack does nothing.", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new CopyAttack()
                            )))), CardRarity.RARE)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-206 - Eevee
     */
    public static final PokemonCard EEVEE_206 = eevee("A1-206");

    /**
     * A1-207 - Eevee
     */
    public static final PokemonCard EEVEE_207 = eevee("A1-207");

    /**
     * A1-208 - Eevee
     */
    public static final PokemonCard EEVEE_208 = eevee("A1-208");

    /** The three Eevee prints differ only in id. */
    private static PokemonCard eevee(String id) {
        return PokemonCard.basic(
                        id, "Eevee", "Its ability to evolve into many forms allows it to adapt smoothly and perfectly to any environment.",
                        60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                                "Tackle", "", EnergyCost.of(Type.COLORLESS, 1),
                                new Attempt(List.of(
                                        new DealDamage(new Literal(20), new OpponentActive())
                                )))), CardRarity.COMMON)
                .withWeakness(Type.FIGHTING);
    }

    /**
     * A1-209 - Porygon
     */
    public static final PokemonCard PORYGON = PokemonCard.basic(
                    "A1-209", "Porygon", "State-of-the-art technology was used to create Porygon. It was the first artificial Pokémon to be created via computer programming.",
                    50, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Sharpen", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-210 - Aerodactyl
     */
    public static final PokemonCard AERODACTYL = PokemonCard.evolution(
                    "A1-210", "Aerodactyl", "This is a ferocious Pokémon from ancient times. Apparently even modern technology is incapable of producing a perfectly restored specimen.",
                    1, "Old Amber", 100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Primal Wingbeat", "Flip a coin. If heads, your opponent shuffles their Active Pokémon into their deck.", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new FlipN(new Literal(1)),
                                    new ConditionalEffect(
                                            new LastCoinTossHeads(),
                                            new ShuffleIntoDeck(new OpponentActive()))
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-211 - Snorlax
     */
    public static final PokemonCard SNORLAX = PokemonCard.basic(
                    "A1-211", "Snorlax", "It is not satisfied unless it eats over 880 pounds of food every day. When it is done eating, it goes promptly to sleep.",
                    150, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                            "Rollout", "", EnergyCost.of(Type.COLORLESS, 4),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-212 - Minccino
     */
    public static final PokemonCard MINCCINO = PokemonCard.basic(
                    "A1-212", "Minccino", "The way it brushes away grime with its tail can be helpful when cleaning. But its focus on spotlessness can make cleaning more of a hassle.",
                    60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Tail Smack", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-213 - Cinccino
     */
    public static final PokemonCard CINCCINO = PokemonCard.evolution(
                    "A1-213", "Cinccino", "Its body secretes oil that this Pokémon spreads over its nest as a coating to protect it from dust. Cinccino won't tolerate even a speck of the stuff.",
                    1, "Minccino", 90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Do the Wave", "This attack does 30 damage for each of your Benched Pokémon.", EnergyCost.of(Type.COLORLESS, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Product(
                                            new Literal(30), new CountCards(new AttackerSide(), Zone.BENCH, Optional.empty())
                                    ), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-214 - Wooloo
     */
    public static final PokemonCard WOOLOO = PokemonCard.basic(
                    "A1-214", "Wooloo", "Its curly fleece is such an effective cushion that this Pokémon could fall off a cliff and stand right back up at the bottom, unharmed.",
                    70, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Tackle", "", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-215 - Dubwool
     */
    public static final PokemonCard DUBWOOL = PokemonCard.evolution(
                    "A1-215", "Dubwool", "Weave a carpet from its springy wool, and you end up with something closer to a trampoline. You'll start to bounce the moment you set foot on it.",
                    1, "Wooloo", 120, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Rolling Tackle", "", EnergyCost.of(Type.COLORLESS, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(80), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    static final List<PokemonCard> CARDS = List.of(
            PIDGEY, PIDGEOTTO, PIDGEOT, RATTATA, RATICATE, SPEAROW, FEAROW,
            JIGGLYPUFF, WIGGLYTUFF, WIGGLYTUFF_EX, MEOWTH, PERSIAN, FARFETCHD,
            DODUO, DODRIO, LICKITUNG, CHANSEY, KANGASKHAN, TAUROS, DITTO,
            EEVEE_206, EEVEE_207, EEVEE_208, PORYGON, AERODACTYL, SNORLAX,
            MINCCINO, CINCCINO, WOOLOO, DUBWOOL);
}
