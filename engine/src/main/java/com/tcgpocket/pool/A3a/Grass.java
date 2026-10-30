package com.tcgpocket.pool.A3a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.PreventNamedAttack;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentAll;
import com.tcgpocket.target.OpponentBench;
import com.tcgpocket.target.Self;

import java.util.List;

import static com.tcgpocket.pool.A1a.Grass.CELEBI_EX;

/**
 * Extradimensional Crisis — Grass.
 */
public final class Grass {

    /**
     * A3a-001 - Petilil
     */
    public static final PokemonCard PETILIL = PokemonCard.basic(
            "A3a-001", "Petilil", "The leaves on its head grow right back even if they fall out. These bitter leaves refresh those who eat them.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Seed Bomb", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3a-002 - Lilligant
     */
    public static final PokemonCard LILLIGANT = PokemonCard.evolution(
            "A3a-002", "Lilligant", "The fragrance of the garland on its head has a relaxing effect, but taking care of it is very difficult.",
            1, "Petilil", 110, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Cut", "", EnergyCost.of(Type.GRASS, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3a-003 - Rowlet
     */
    public static final PokemonCard ROWLET = PokemonCard.basic(
            "A3a-003", "Rowlet", "It sends its feathers, which are as sharp as blades, flying in attack. Its legs are strong, so its kicks are also formidable.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Fury Attack", "Flip 3 coins. This attack does 10 damage for each heads.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new FlipN(new Literal(3)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(10)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3a-004 - Dartrix
     */
    public static final PokemonCard DARTRIX = PokemonCard.evolution(
            "A3a-004", "Dartrix", "This narcissistic Pokémon is a clean freak. If you don't groom it diligently, it may stop listening to you.",
            1, "Rowlet", 80, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Skill Dive", "This attack does 20 damage to 1 of your opponent's Pokémon.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE);

    /**
     * A3a-005 - Decidueye
     */
    public static final PokemonCard DECIDUEYE = PokemonCard.evolution(
            "A3a-005", "Decidueye", "It nocks its arrow quills and shoots them at opponents. When it simply can't afford to miss, it tugs the vine on its head to improve its focus.",
            2, "Dartrix", 130, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Sniping Arrow", "This attack does 70 damage to 1 of your opponent's Pokémon.", EnergyCost.of(Type.GRASS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"))))
            )), CardRarity.RARE
    ).withWeakness(Type.FIRE);

    /**
     * A3a-006 - Buzzwole ex — Big Beat is locked by name, since Punch stays usable.
     */
    public static final PokemonCard BUZZWOLE_EX = PokemonCard.basic(
            "A3a-006", "Buzzwole ex", "",
            140, Type.GRASS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Punch", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            ), new Action(
                    "Big Beat", "During your next turn, this Pokémon can't use Big Beat.", EnergyCost.of(Type.GRASS, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive()),
                    new PreventNamedAttack("Big Beat", new Self(), new Literal(2))))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIRE).withTags(CardTag.EX, CardTag.ULTRA_BEAST);

    /**
     * A3a-007 - Pheromosa
     */
    public static final PokemonCard PHEROMOSA = PokemonCard.basic(
            "A3a-007", "Pheromosa", "A life-form that lives in another world, its body is thin and supple, but it also possesses great power.",
            70, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Jump Blues", "This attack also does 20 damage to 1 of your opponent's Benched Pokémon.", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new DealDamage(new Literal(20), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Select a Benched Pokémon:"))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIRE).withTags(CardTag.ULTRA_BEAST);

    /**
     * A3a-008 - Kartana
     */
    public static final PokemonCard KARTANA = PokemonCard.basic(
            "A3a-008", "Kartana", "This Ultra Beast's body, which is as thin as paper, is like a sharpened sword.",
            60, Type.GRASS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Thrash Metal", "", EnergyCost.of(Type.GRASS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIRE).withTags(CardTag.ULTRA_BEAST);

    /** A3a-070 · Rowlet (alternate art of A3a-003) */
    public static final PokemonCard ROWLET_IR = ROWLET.withId("A3a-070", CardRarity.ILLUSTRATION_RARE);
    /** A3a-071 · Pheromosa (alternate art of A3a-007) */
    public static final PokemonCard PHEROMOSA_IR = PHEROMOSA.withId("A3a-071", CardRarity.ILLUSTRATION_RARE);
    /** A3a-076 · Buzzwole ex (alternate art of A3a-006) */
    public static final PokemonCard BUZZWOLE_EX_UR = BUZZWOLE_EX.withId("A3a-076", CardRarity.ULTRA_RARE);

    /** A3a-088 · Buzzwole ex (alternate art of A3a-006) */
    public static final PokemonCard BUZZWOLE_EX_IM = BUZZWOLE_EX.withId("A3a-088", CardRarity.IMMERSIVE);
    /** A3a-099 · Celebi ex (alternate art of A1a-003) */
    public static final PokemonCard CELEBI_EX_SUR = CELEBI_EX.withId("A3a-099", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            PETILIL, LILLIGANT, ROWLET, DARTRIX, DECIDUEYE, BUZZWOLE_EX,
            PHEROMOSA, KARTANA,
            ROWLET_IR, PHEROMOSA_IR, BUZZWOLE_EX_UR,
            BUZZWOLE_EX_IM, CELEBI_EX_SUR);

    private Grass() {
    }
}
