package com.tcgpocket.pool.A2b;

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
import com.tcgpocket.number.*;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Shining Revelry — Psychic.
 */
public final class Psychic {

    /**
     * A2b-029 - Abra
     */
    public static final PokemonCard ABRA = PokemonCard.basic(
            "A2b-029", "Abra", "This Pokémon uses its psychic powers while it sleeps. The contents of Abra's dreams affect the powers that the Pokémon wields.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Psyshot", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2b-030 - Kadabra
     */
    public static final PokemonCard KADABRA = PokemonCard.evolution(
            "A2b-030", "Kadabra", "Using its psychic power, Kadabra levitates as it sleeps. It uses its springy tail as a pillow.",
            1, "Abra", 80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Psyshot", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2b-031 - Alakazam
     */
    public static final PokemonCard ALAKAZAM = PokemonCard.evolution(
            "A2b-031", "Alakazam", "It has an incredibly high level of intelligence. Some say that Alakazam remembers everything that ever happens to it, from birth till death.",
            2, "Kadabra", 130, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Psychic Suppression", "This attack also does 20 damage to each of your opponent's Benched Pokémon that has any Energy attached.",
                    EnergyCost.of(Type.PSYCHIC, 2), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new DamageEach(new Literal(20), new Matching(new OpponentBench(), new HasEnergy(EnergyCost.of(Type.COLORLESS, 1))))))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS);

    /**
     * A2b-032 - Mr. Mime
     */
    public static final PokemonCard MR_MIME = PokemonCard.basic(
            "A2b-032", "Mr. Mime", "The broadness of its hands may be no coincidence—many scientists believe its palms became enlarged specifically for pantomiming.",
            90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Juggling", "Flip 4 coins. This attack does 20 damage for each heads.", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(4)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(20)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2b-033 - Drifloon
     */
    public static final PokemonCard DRIFLOON = PokemonCard.basic(
            "A2b-033", "Drifloon", "It is whispered that any child who mistakes Drifloon for a balloon and holds on to it could wind up missing.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2b-034 - Drifblim
     */
    public static final PokemonCard DRIFBLIM = PokemonCard.evolution(
            "A2b-034", "Drifblim", "It can generate and release gas within its body. That's how it can control the altitude of its drift.",
            1, "Drifloon", 90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                    "Gust", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A2b-035 - Giratina ex
     */
    public static final PokemonCard GIRATINA_EX = PokemonCard.basic(
            "A2b-035", "Giratina ex", "",
            150, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Chaotic Impact", "This Pokémon also does 20 damage to itself.", EnergyCost.of(Type.PSYCHIC, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(130), new OpponentActive()),
                    new DealDamage(new Literal(20), new Self())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.DARKNESS).withTags(CardTag.EX).withAbility(new ActivatedAbility("Broken-Space Bellow",
            "Once during your turn, you may take a Psychic Energy from your Energy Zone and attach it to this Pokémon. If you use this Ability, your turn ends.",
            new PlainAction("", new Attempt(List.of(
                    new AttachEnergy(Type.PSYCHIC, new Self()),
                    new EndTurn()))),
            true, new Always<>()));

    /**
     * A2b-036 - Gimmighoul
     */
    public static final PokemonCard GIMMIGHOUL = PokemonCard.basic(
            "A2b-036", "Gimmighoul", "This Pokémon was born inside a treasure chest about 1,500 years ago. It sucks the life-force out of scoundrels who try to steal the treasure.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Chest-ouflage", "Flip a coin. If heads, during your opponent's next turn, prevent all damage from—and effects of—attacks done to this Pokémon.",
                    EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventDamage(new Self(), new Literal(1))),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventEffects(new Self(), new Literal(1)))))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /** A2b-083 · Giratina ex (alternate art of A2b-035) */
    public static final PokemonCard GIRATINA_EX_UR = GIRATINA_EX.withId("A2b-083", CardRarity.ULTRA_RARE);
    /** A2b-096 · Giratina ex (alternate art of A2b-035) */
    public static final PokemonCard GIRATINA_EX_IM = GIRATINA_EX.withId("A2b-096", CardRarity.IMMERSIVE);

    static final List<PokemonCard> CARDS = List.of(
            ABRA, KADABRA, ALAKAZAM, MR_MIME, DRIFLOON, DRIFBLIM, GIRATINA_EX, GIMMIGHOUL,
            GIRATINA_EX_UR, GIRATINA_EX_IM);

    private Psychic() {
    }
}
