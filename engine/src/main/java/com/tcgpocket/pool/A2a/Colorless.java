package com.tcgpocket.pool.A2a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.card.RetreatCostReduction;
import com.tcgpocket.card.StatusImmunity;
import com.tcgpocket.condition.IsBasic;
import com.tcgpocket.condition.IsBenched;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Triumphant Light — Colorless.
 */
public final class Colorless {

    /**
     * A2a-062 - Eevee
     */
    public static final PokemonCard EEVEE = PokemonCard.basic(
            "A2a-062", "Eevee", "Its ability to evolve into many forms allows it to adapt smoothly and perfectly to any environment.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tail Whap", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2a-063 - Snorlax
     */
    public static final PokemonCard SNORLAX = PokemonCard.basic(
            "A2a-063", "Snorlax", "It is not satisfied unless it eats over 880 pounds of food every day. When it is done eating, it goes promptly to sleep.",
            140, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Collapse", "This Pokémon is now Asleep.", EnergyCost.of(Type.COLORLESS, 4), new Attempt(List.of(
                    new DealDamage(new Literal(100), new OpponentActive()),
                    new AddStatus(new SleepStatus(), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2a-064 - Hoothoot
     */
    public static final PokemonCard HOOTHOOT = PokemonCard.basic(
            "A2a-064", "Hoothoot", "It always stands on one foot. It changes feet so fast, the movement can rarely be seen.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Peck", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2a-065 - Noctowl
     */
    public static final PokemonCard NOCTOWL = PokemonCard.evolution(
            "A2a-065", "Noctowl", "Its eyes are specially developed to enable it to see clearly even in murky darkness and minimal light.",
            1, "Hoothoot", 100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Silent Wing", "Your opponent reveals their hand.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new RevealHand(new OpponentSide())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2a-066 - Starly
     */
    public static final PokemonCard STARLY = PokemonCard.basic(
            "A2a-066", "Starly", "They flock around mountains and fields, chasing after bug Pokémon. Their singing is noisy and annoying.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Glide", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2a-067 - Staravia
     */
    public static final PokemonCard STARAVIA = PokemonCard.evolution(
            "A2a-067", "Staravia", "Recognizing their own weakness, they always live in a group. When alone, a Staravia cries noisily.",
            1, "Starly", 80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Wing Attack", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2a-068 - Staraptor
     */
    public static final PokemonCard STARAPTOR = PokemonCard.evolution(
            "A2a-068", "Staraptor", "When Staravia evolve into Staraptor, they leave the flock to live alone. They have sturdy wings.",
            2, "Staravia", 150, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Brave Bird", "This Pokémon also does 20 damage to itself.", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(130), new OpponentActive()),
                    new DealDamage(new Literal(20), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A2a-069 - Shaymin
     */
    public static final PokemonCard SHAYMIN = PokemonCard.basic(
            "A2a-069", "Shaymin", "It can dissolve toxins in the air to instantly transform ruined land into a lush field of flowers.",
            70, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flap", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING).withAbility(new PassiveAbility("Sky Support",
            "As long as this Pokémon is on your Bench, your Active Basic Pokémon's Retreat Cost is 1 less.",
            new RetreatCostReduction(1, new IsBasic(), new IsBenched())));

    /**
     * A2a-070 - Arceus
     */
    public static final PokemonCard ARCEUS = PokemonCard.basic(
            "A2a-070", "Arceus", "According to the legends of Sinnoh, this Pokémon emerged from an egg and shaped all there is in this world.",
            120, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Power Blast", "Discard a random Energy from this Pokémon.", EnergyCost.of(Type.COLORLESS, 4), new Attempt(List.of(
                    new DealDamage(new Literal(100), new OpponentActive()),
                    new DiscardRandomEnergy(new Literal(1), new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A2a-071 - Arceus ex. Fabled Luster protects "this Pokémon", and
     * {@link StatusImmunity} covers its holder's side: naming Arceus ex is
     * exact, since any other Arceus ex in play holds the same ability.
     */
    public static final PokemonCard ARCEUS_EX = PokemonCard.basic(
            "A2a-071", "Arceus ex", "",
            140, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Ultimate Force", "This attack does 20 more damage for each of your Benched Pokémon.",
                    EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Sum(
                            new Literal(70),
                            new Product(new Literal(20), new CountCards(new AttackerSide(), Zone.BENCH))
                    ), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX).withAbility(new PassiveAbility("Fabled Luster",
            "This Pokémon can't be affected by any Special Conditions.",
            new StatusImmunity(new IsSpecies("Arceus ex"))));

    /** A2a-081 · Shaymin (alternate art of A2a-069) */
    public static final PokemonCard SHAYMIN_IR = SHAYMIN.withId("A2a-081", CardRarity.ILLUSTRATION_RARE);
    /** A2a-086 · Arceus ex (alternate art of A2a-071) */
    public static final PokemonCard ARCEUS_EX_UR = ARCEUS_EX.withId("A2a-086", CardRarity.ULTRA_RARE);
    /** A2a-095 · Arceus ex (alternate art of A2a-071) */
    public static final PokemonCard ARCEUS_EX_IM = ARCEUS_EX.withId("A2a-095", CardRarity.IMMERSIVE);
    /** A2a-096 · Arceus ex (alternate art of A2a-071) */
    public static final PokemonCard ARCEUS_EX_HR = ARCEUS_EX.withId("A2a-096", CardRarity.HYPER_RARE);

    static final List<PokemonCard> CARDS = List.of(
            EEVEE, SNORLAX, HOOTHOOT, NOCTOWL, STARLY, STARAVIA, STARAPTOR, SHAYMIN, ARCEUS, ARCEUS_EX,
            SHAYMIN_IR, ARCEUS_EX_UR, ARCEUS_EX_IM, ARCEUS_EX_HR);

    private Colorless() {
    }
}
