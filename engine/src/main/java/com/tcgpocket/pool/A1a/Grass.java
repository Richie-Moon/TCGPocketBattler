package com.tcgpocket.pool.A1a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.EnergyBoost;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.condition.HasType;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.*;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Mythical Island — Grass.
 */
public final class Grass {
    /**
     * A1a-001 - Exeggcute
     */
    public static final PokemonCard EXEGGCUTE = PokemonCard.basic(
            "A1a-001", "Exeggcute", "Though it may look like it's just a bunch of eggs, it's a proper Pokémon. Exeggcute communicates with others of its kind via telepathy, apparently.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Growth Spurt", "Take a Grass Energy from your Energy Zone and attach it to this Pokémon.",
                    EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new AttachEnergy(Type.GRASS, new Literal(1), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1a-002 - Exeggutor
     */
    public static final PokemonCard EXEGGUTOR = PokemonCard.evolution(
            "A1a-002", "Exeggutor", "Each of Exeggutor's three heads is thinking different thoughts. The three don't seem to be very interested in one another.",
            1, "Exeggcute", 130, Type.GRASS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Psychic", "This attack does 20 more damage for each Energy attached to your opponent's Active Pokémon.",
                    EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Sum(
                            new Literal(80),
                            new Product(new EnergyOn(new OpponentActive()), new Literal(20))),
                            new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1a-003 - Celebi ex — Powerful Bloom: one flip per attached Energy, 50
     * per heads. The flip count is read when the attack resolves, so it
     * includes every Energy, not just the two the cost asks for.
     */
    public static final PokemonCard CELEBI_EX = PokemonCard.basic(
            "A1a-003", "Celebi ex", "",
            130, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Powerful Bloom", "Flip a coin for each Energy attached to this Pokémon. This attack does 50 damage for each heads.",
                    EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new EnergyOn(new Self())),
                    new DealDamage(new Product(new NumberHeads(), new Literal(50)), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX);

    /**
     * A1a-004 - Snivy
     */
    public static final PokemonCard SNIVY = PokemonCard.basic(
            "A1a-004", "Snivy", "Being exposed to sunlight makes its movements swifter. It uses vines more adeptly than its hands.",
            70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Vine Whip", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1a-005 - Servine
     */
    public static final PokemonCard SERVINE = PokemonCard.evolution(
            "A1a-005", "Servine", "It moves along the ground as if sliding. Its swift movements befuddle its foes, and it then attacks with a vine whip.",
            1, "Snivy", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Vine Whip", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1a-006 - Serperior
     */
    public static final PokemonCard SERPERIOR = PokemonCard.evolution(
            "A1a-006", "Serperior", "It only gives its all against strong opponents who are not fazed by the glare from Serperior's noble eyes.",
            2, "Servine", 110, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Solar Beam", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.RARE)
            .withAbility(
                    new PassiveAbility("Jungle Totem", 
                            "Each Grass Energy attached to your Grass Pokémon provides 2 Grass Energy. This effect does not stack.",
                            new EnergyBoost(Type.GRASS, new HasType(Type.GRASS), 2)
                    )
            )
            .withWeakness(Type.FIRE);

    /**
     * A1a-007 - Morelull
     */
    public static final PokemonCard MORELULL = PokemonCard.basic(
            "A1a-007", "Morelull", "Pokémon living in the forest eat the delicious caps on Morelull's head. The caps regrow overnight.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1a-008 - Shiinotic
     */
    public static final PokemonCard SHIINOTIC = PokemonCard.evolution(
            "A1a-008", "Shiinotic", "Its flickering spores lure in prey and put them to sleep. Once this Pokémon has its prey snoozing, it drains their vitality with its fingertips.",
            1, "Morelull", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Flickering Spores", "Your opponent's Active Pokémon is now Asleep.", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new AddStatus(new SleepStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A1a-009 - Dhelmise — Energy Whip: "3 extra" on a one-Grass cost means
     * at least 4 Grass attached, hence {@code > 3}.
     */
    public static final PokemonCard DHELMISE = PokemonCard.basic(
            "A1a-009", "Dhelmise", "After a piece of seaweed merged with debris from a sunken ship, it was reborn as this ghost Pokémon.",
            100, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Energy Whip", "If this Pokémon has at least 3 extra Grass Energy attached, this attack does 70 more damage.",
                    EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(
                                    new GreaterThan(new EnergyOn(new Self(), Type.GRASS), new Literal(3)),
                                    new Literal(90))),
                            new Literal(20)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /** A1a-069 · Exeggutor (alternate art of A1a-002) */
    public static final PokemonCard EXEGGUTOR_IR = EXEGGUTOR.withId("A1a-069", CardRarity.ILLUSTRATION_RARE);
    /** A1a-070 · Serperior (alternate art of A1a-006) */
    public static final PokemonCard SERPERIOR_IR = SERPERIOR.withId("A1a-070", CardRarity.ILLUSTRATION_RARE);
    /** A1a-075 · Celebi ex (alternate art of A1a-003) */
    public static final PokemonCard CELEBI_EX_UR = CELEBI_EX.withId("A1a-075", CardRarity.ULTRA_RARE);
    /** A1a-085 · Celebi ex (alternate art of A1a-003) */
    public static final PokemonCard CELEBI_EX_IM = CELEBI_EX.withId("A1a-085", CardRarity.IMMERSIVE);

    static final List<PokemonCard> CARDS = List.of(
            EXEGGCUTE, EXEGGUTOR, CELEBI_EX, SNIVY, SERVINE, SERPERIOR, MORELULL, SHIINOTIC, DHELMISE,
            EXEGGUTOR_IR, SERPERIOR_IR, CELEBI_EX_UR, CELEBI_EX_IM);

    private Grass() {
    }
}
