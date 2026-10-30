package com.tcgpocket.pool.A3a;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.condition.IsPokemon;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.SearchDeck;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;

import java.util.List;

import static com.tcgpocket.pool.A1.Psychic.JYNX;

/**
 * Extradimensional Crisis — Psychic.
 */
public final class Psychic {

    /**
     * A3a-022 - Clefairy
     */
    public static final PokemonCard CLEFAIRY = PokemonCard.basic(
            "A3a-022", "Clefairy", "It is said that happiness will come to those who see a gathering of Clefairy dancing under a full moon.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Pound", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A3a-023 - Clefable
     */
    public static final PokemonCard CLEFABLE = PokemonCard.evolution(
            "A3a-023", "Clefable", "A timid fairy Pokémon that is rarely seen, it will run and hide the moment it senses people.",
            1, "Clefairy", 100, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Moon Kick", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /**
     * A3a-024 - Phantump
     */
    public static final PokemonCard PHANTUMP = PokemonCard.basic(
            "A3a-024", "Phantump", "With a voice like a human child's, it cries out to lure adults deep into the forest, getting them lost among the trees.",
            70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Spooky Shot", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3a-025 - Trevenant
     */
    public static final PokemonCard TREVENANT = PokemonCard.evolution(
            "A3a-025", "Trevenant", "People fear it due to a belief that it devours any who try to cut down trees in its forest, but to the Pokémon it shares its woods with, it's kind.",
            1, "Phantump", 120, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Wrack Down", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3a-026 - Morelull
     */
    public static final PokemonCard MORELULL = PokemonCard.basic(
            "A3a-026", "Morelull", "It scatters spores that flicker and glow. Anyone seeing these lights falls into a deep slumber.",
            50, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Hook", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A3a-027 - Shiinotic
     */
    public static final PokemonCard SHIINOTIC = PokemonCard.evolution(
            "A3a-027", "Shiinotic", "Its flickering spores lure in prey and put them to sleep. Once this Pokémon has its prey snoozing, it drains their vitality with its fingertips.",
            1, "Morelull", 90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Gentle Slap", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.METAL).withAbility(new ActivatedAbility("Illuminate",
            "Once during your turn, you may put a random Pokémon from your deck into your hand.",
            new PlainAction("", new Attempt(List.of(
                    new SearchDeck(new AttackerSide(), new Literal(1), new IsPokemon())))),
            true, new GreaterThan(new CountCards(new AttackerSide(), Zone.DECK), new Literal(0))));

    /** A3a-094 · Jynx (alternate art of A1-127) */
    public static final PokemonCard JYNX_SR = JYNX.withId("A3a-094", CardRarity.SHINY_RARE);

    static final List<PokemonCard> CARDS = List.of(
            CLEFAIRY, CLEFABLE, PHANTUMP, TREVENANT, MORELULL, SHIINOTIC,
            JYNX_SR);

    private Psychic() {
    }
}
