package com.tcgpocket.pool.A1a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.HasTag;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.*;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentSide;

import java.util.List;
import java.util.Optional;

/**
 * Mythical Island — Colorless.
 */
public final class Colorless {
    /**
     * A1a-057 - Pidgey
     */
    public static final PokemonCard PIDGEY = PokemonCard.basic(
                    "A1a-057", "Pidgey", "A common sight in forests and woods. It flaps its wings at ground level to kick up blinding sand.",
                    50, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Flap", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);
    /**
     * A1a-058 - Pidgeotto
     */
    public static final PokemonCard PIDGEOTTO = PokemonCard.evolution(
                    "A1a-058", "Pidgeotto", "The claws on its feet are well developed. It can carry prey such as an Exeggcute to its nest over 60 miles away.",
                    1, "Pidgey", 90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Wing Attack", "", EnergyCost.of(Type.COLORLESS, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(50), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);
    /**
     * A1a-059 - Pidgeot ex
     */
    public static final PokemonCard PIDGEOT_EX = PokemonCard.evolution(
                    "A1a-059", "Pidgeot ex", "",
                    2, "Pidgeotto", 170, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Scattering Cyclone", "This attack does 20 more damage for each of your opponent's Benched Pokémon.", EnergyCost.of(Type.COLORLESS, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Sum(new Literal(80), new Product(
                                            new Literal(20), new CountCards(new OpponentSide(), Zone.BENCH, Optional.empty())
                                    )), new OpponentActive())
                            )))), CardRarity.DOUBLE_RARE)
            .withWeakness(Type.LIGHTNING)
            .withTags(CardTag.EX);
    /**
     * A1a-060 - Tauros
     */
    public static final PokemonCard TAUROS = PokemonCard.basic(
                    "A1a-060", "Tauros", "When Tauros begins whipping itself with its tails, it's a warning that the Pokémon is about to charge with astounding speed.",
                    100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Fighting Tackle", "If your opponent's Active Pokémon is a Pokémon ex, this attack does 80 more damage.", EnergyCost.of(Type.COLORLESS, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Branch(List.of(
                                            new Branch.Case(new For(new HasTag(CardTag.EX), new OpponentActive()), new Literal(120))
                                    ), new Literal(40)), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.FIGHTING);
    /**
     * A1a-061 - Eevee
     */
    public static final PokemonCard EEVEE = PokemonCard.basic(
                    "A1a-061", "Eevee", "Its ability to evolve into many forms allows it to adapt smoothly and perfectly to any environment.",
                    60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Continuous Steps", "Flip a coin until you get tails. This attack does 20 damage for each heads.", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new FlipUntilTails(),
                                    new DealDamage(new Product(new Literal(20), new NumberHeads()), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);
    /**
     * A1a-062 - Chatot
     */
    public static final PokemonCard CHATOT = PokemonCard.basic(
                    "A1a-062", "Chatot", "It mimics the cries of other Pokémon to trick them into thinking it's one of them. This way they won't attack it.",
                    60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Mimic", "Shuffle your hand into your deck. Draw a card for each card in your opponent's hand.", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new ShuffleHandIntoDeck(new AttackerSide()),
                                    new DrawCard(new CountCards(new OpponentSide(), Zone.HAND, Optional.empty()), new AttackerSide())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /** A1a-079 · Pidgeot ex (alternate art of A1a-059) */
    public static final PokemonCard PIDGEOT_EX_UR = PIDGEOT_EX.withId("A1a-079", CardRarity.ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(PIDGEY, PIDGEOTTO, PIDGEOT_EX, TAUROS, EEVEE, CHATOT, PIDGEOT_EX_UR);

    private Colorless() {
    }
}
