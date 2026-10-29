package com.tcgpocket.pool.A2b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.*;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Shining Revelry — Grass.
 */
public final class Grass {

    /**
     * A2b-001 - Weedle
     */
    public static final PokemonCard WEEDLE = PokemonCard.basic(
            "A2b-001", "Weedle", "Often found in forests and grasslands. It has a sharp, toxic barb of around two inches on top of its head.",
            50, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Multiply", "Put 1 random Weedle from your deck onto your Bench.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new BenchFromDeck(new AttackerSide(), new IsSpecies("Weedle"))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2b-002 - Kakuna
     */
    public static final PokemonCard KAKUNA = PokemonCard.evolution(
            "A2b-002", "Kakuna", "Almost incapable of moving, this Pokémon can only harden its shell to protect itself when it is in danger.",
            1, "Weedle", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "String Shot", "Flip a coin. If heads, your opponent's Active Pokémon is now Paralyzed.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new ConditionalEffect(new LastCoinTossHeads(), new AddStatus(new ParalysisStatus(), new OpponentActive()))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2b-003 - Beedrill ex
     */
    public static final PokemonCard BEEDRILL_EX = PokemonCard.evolution(
            "A2b-003", "Beedrill ex", "",
            2, "Kakuna", 170, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Crushing Spear", "Discard a random Energy from your opponent's Active Pokémon.", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new DiscardRandomEnergy(new Literal(1), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX);

    /**
     * A2b-004 - Pinsir
     */
    public static final PokemonCard PINSIR = PokemonCard.basic(
            "A2b-004", "Pinsir", "These Pokémon judge one another based on pincers. Thicker, more impressive pincers make for more popularity with the opposite gender.",
            90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Guillotine Rush", "Flip a coin until you get tails. This attack does 40 more damage for each heads.",
                    EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new FlipUntilTails(),
                    new DealDamage(new Sum(new Literal(50), new Product(new NumberHeads(), new Literal(40))), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2b-005 - Sprigatito
     */
    public static final PokemonCard SPRIGATITO = PokemonCard.basic(
            "A2b-005", "Sprigatito", "Its fluffy fur is similar in composition to plants. This Pokémon frequently washes its face to keep it from drying out.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Cry for Help", "Put 1 random Grass Pokémon from your deck into your hand.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new SearchDeck(new AttackerSide(), new Literal(1), new IsType(Type.GRASS)),
                    new ShuffleDeck(new AttackerSide())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2b-006 - Floragato
     */
    public static final PokemonCard FLORAGATO = PokemonCard.evolution(
            "A2b-006", "Floragato", "Floragato deftly wields the vine hidden beneath its long fur, slamming the hard flower bud against its opponents.",
            1, "Sprigatito", 90, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Slash", "", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A2b-007 - Meowscarada
     */
    public static final PokemonCard MEOWSCARADA = PokemonCard.evolution(
            "A2b-007", "Meowscarada", "This Pokémon uses the reflective fur lining its cape to camouflage the stem of its flower, creating the illusion that the flower is floating.",
            2, "Floragato", 140, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Fighting Claws", "If your opponent's Active Pokémon is a Pokémon ex, this attack does 70 more damage.",
                    EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new For(new HasTag(CardTag.EX), new OpponentActive()), new Literal(130))),
                            new Literal(60)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /** A2b-073 · Meowscarada (alternate art of A2b-007) */
    public static final PokemonCard MEOWSCARADA_IR = MEOWSCARADA.withId("A2b-073", CardRarity.ILLUSTRATION_RARE);
    /** A2b-079 · Beedrill ex (alternate art of A2b-003) */
    public static final PokemonCard BEEDRILL_EX_UR = BEEDRILL_EX.withId("A2b-079", CardRarity.ULTRA_RARE);
    /** A2b-097 · Weedle (alternate art of A2b-001) */
    public static final PokemonCard WEEDLE_SR = WEEDLE.withId("A2b-097", CardRarity.SHINY_RARE);
    /** A2b-098 · Kakuna (alternate art of A2b-002) */
    public static final PokemonCard KAKUNA_SR = KAKUNA.withId("A2b-098", CardRarity.SHINY_RARE);
    /** A2b-107 · Beedrill ex (alternate art of A2b-003) */
    public static final PokemonCard BEEDRILL_EX_SUR = BEEDRILL_EX.withId("A2b-107", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            WEEDLE, KAKUNA, BEEDRILL_EX, PINSIR, SPRIGATITO, FLORAGATO, MEOWSCARADA,
            MEOWSCARADA_IR, BEEDRILL_EX_UR, WEEDLE_SR, KAKUNA_SR, BEEDRILL_EX_SUR);

    private Grass() {
    }
}
