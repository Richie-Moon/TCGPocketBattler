package com.tcgpocket.pool.A4a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.Devolve;
import com.tcgpocket.effect.DiscardTopOfDeck;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.SwitchActive;
import com.tcgpocket.effect.YouMay;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Stage;
import com.tcgpocket.number.Sum;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.SelfSide;

import java.util.List;

/**
 * Secluded Springs (A4a): the Grass cards, in printed set order.
 */
public final class Grass {

    /**
     * A4a-001 - Hoppip
     */
    public static final PokemonCard HOPPIP = PokemonCard.basic(
            "A4a-001", "Hoppip", "Outside of cold regions, Hoppip can be found practically everywhere. This is because the wind carries this species all over the world.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Splash", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4a-002 - Skiploom
     */
    public static final PokemonCard SKIPLOOM = PokemonCard.evolution(
            "A4a-002", "Skiploom", "Skiploom enthusiasts can apparently tell where a Skiploom was born by the scent drifting from the flower on the Pokémon's head.",
            1, "Hoppip", 60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bounce", "Switch this Pokémon with 1 of your Benched Pokémon.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new SwitchActive(new SelfSide())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4a-003 - Jumpluff ex
     */
    public static final PokemonCard JUMPLUFF_EX = PokemonCard.evolution(
            "A4a-003", "Jumpluff ex", "",
            2, "Skiploom", 160, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Breeze-By Attack", "You may switch this Pokémon with 1 of your Benched Pokémon.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new ConditionalEffect(new GreaterThan(new CountCards(new AttackerSide(), Zone.BENCH), new Literal(0)),
                            new YouMay("Breeze-By Attack: switch this Pokémon with 1 of your Benched Pokémon?", new SwitchActive(new SelfSide())))))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A4a-004 - Sunkern
     */
    public static final PokemonCard SUNKERN = PokemonCard.basic(
            "A4a-004", "Sunkern", "It suddenly falls out of the sky in the morning. Knowing it's weak, it simply feeds until it evolves.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4a-005 - Sunflora
     */
    public static final PokemonCard SUNFLORA = PokemonCard.evolution(
            "A4a-005", "Sunflora", "In the daytime, it rushes about in a hectic manner, but it comes to a complete stop when the sun sets.",
            1, "Sunkern", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Trip Over", "Flip a coin. If heads, this attack does 30 more damage.", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(new Literal(50), new Product(new NumberHeads(), new Literal(30))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A4a-006 - Celebi — devolving a Pokémon the 40 damage knocked out does nothing; see {@code Devolve}.
     */
    public static final PokemonCard CELEBI = PokemonCard.basic(
            "A4a-006", "Celebi", "This Pokémon wanders across time. Grass and trees flourish in the forests in which it has appeared.",
            70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Temporal Leaves", "If your opponent's Active Pokémon is an evolved Pokémon, devolve it by putting the highest Stage Evolution card on it into your opponent's hand.", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new Devolve(new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A4a-007 - Durant
     */
    public static final PokemonCard DURANT = PokemonCard.basic(
            "A4a-007", "Durant", "They attack in groups, covering themselves in steel armor to protect themselves from Heatmor.",
            70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Mountain Munch", "Discard the top card of your opponent's deck.", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new DiscardTopOfDeck(new Literal(1), new OpponentSide())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /** A4a-078 · Jumpluff ex (alternate art of A4a-003) */
    public static final PokemonCard JUMPLUFF_EX_UR = JUMPLUFF_EX.withId("A4a-078", CardRarity.ULTRA_RARE);

    /** A4a-086 · Jumpluff ex (alternate art of A4a-003) */
    public static final PokemonCard JUMPLUFF_EX_SIR = JUMPLUFF_EX.withId("A4a-086", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            HOPPIP, SKIPLOOM, JUMPLUFF_EX, SUNKERN, SUNFLORA, CELEBI,
            DURANT, JUMPLUFF_EX_UR, JUMPLUFF_EX_SIR);

    private Grass() {
    }
}
