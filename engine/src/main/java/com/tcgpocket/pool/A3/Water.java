package com.tcgpocket.pool.A3;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.*;
import com.tcgpocket.trigger.Knockout;
import com.tcgpocket.trigger.Trigger;

import java.util.List;
import java.util.Optional;

/**
 * Celestial Guardians — Water.
 */
public final class Water {

    /** "Wishiwashi or Wishiwashi ex", for the two cards that gather a school. */
    private static final ICondition<CardInstance> SCHOOL =
            new Or<>(new IsSpecies("Wishiwashi"), new IsSpecies("Wishiwashi ex"));

    /**
     * A3-038 - Alolan Sandshrew
     */
    public static final PokemonCard ALOLAN_SANDSHREW = PokemonCard.basic(
            "A3-038", "Alolan Sandshrew", "Life on mountains covered with deep snow has granted this Pokémon a body of ice that's as hard as steel.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Scratch", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A3-039 - Alolan Sandslash
     */
    public static final PokemonCard ALOLAN_SANDSLASH = PokemonCard.evolution(
            "A3-039", "Alolan Sandslash", "It uses large, hooked claws to cut a path through deep snow as it runs. On snowy mountains, this Sandslash is faster than any other Pokémon.",
            1, "Alolan Sandshrew", 100, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Spike Armor", "During your opponent's next turn, if this Pokémon is damaged by an attack, do 40 damage to the Attacking Pokémon.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new CounterDamage(new Literal(40), new Self(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /**
     * A3-040 - Alolan Vulpix
     */
    public static final PokemonCard ALOLAN_VULPIX = PokemonCard.basic(
            "A3-040", "Alolan Vulpix", "After long years in the ever-snowcapped mountains of Alola, this Vulpix has gained power over ice.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Call Forth Cold", "Take a Water Energy from your Energy Zone and attach it to this Pokémon.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new AttachEnergy(Type.WATER, new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A3-041 - Alolan Ninetales
     */
    public static final PokemonCard ALOLAN_NINETALES = PokemonCard.evolution(
            "A3-041", "Alolan Ninetales", "A deity resides in the snowy mountains where this Pokémon lives. In ancient times, it was worshiped as that deity's incarnation.",
            1, "Alolan Vulpix", 110, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Blizzard", "This attack also does 20 damage to each of your opponent's Benched Pokémon.", EnergyCost.of(Type.WATER, 3), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new DamageEach(new Literal(20), new OpponentBench())))
            )), CardRarity.RARE
    ).withWeakness(Type.METAL);

    /**
     * A3-042 - Shellder
     */
    public static final PokemonCard SHELLDER = PokemonCard.basic(
            "A3-042", "Shellder", "It is encased in a shell that is harder than diamond. Inside, however, it is surprisingly tender.",
            70, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-043 - Cloyster
     */
    public static final PokemonCard CLOYSTER = PokemonCard.evolution(
            "A3-043", "Cloyster", "Cloyster that live in seas with harsh tidal currents grow large, sharp spikes on their shells.",
            1, "Shellder", 110, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Guard Press", "During your opponent's next turn, this Pokémon takes -20 damage from attacks.", EnergyCost.of(Type.WATER, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new ReduceDamageTaken(new Literal(20), new Self(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-044 - Lapras
     */
    public static final PokemonCard LAPRAS = PokemonCard.basic(
            "A3-044", "Lapras", "A smart and kindhearted Pokémon, it glides across the surface of the sea while its beautiful song echoes around it.",
            110, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Surf", "", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-045 - Popplio
     */
    public static final PokemonCard POPPLIO = PokemonCard.basic(
            "A3-045", "Popplio", "The balloons it inflates with its nose grow larger and larger as it practices day by day.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sing", "Your opponent's Active Pokémon is now Asleep.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new AddStatus(new SleepStatus(), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-046 - Popplio
     */
    public static final PokemonCard POPPLIO_WATER_GUN = PokemonCard.basic(
            "A3-046", "Popplio", "The balloons it inflates with its nose grow larger and larger as it practices day by day.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Water Gun", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-047 - Brionne
     */
    public static final PokemonCard BRIONNE = PokemonCard.evolution(
            "A3-047", "Brionne", "It gets excited when it sees a dance it doesn't know. This hard worker practices diligently until it can learn that dance.",
            1, "Popplio", 80, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Wave Splash", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-048 - Primarina
     */
    public static final PokemonCard PRIMARINA = PokemonCard.evolution(
            "A3-048", "Primarina", "To Primarina, every battle is a stage. It takes down its prey with beautiful singing and dancing.",
            2, "Brionne", 140, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Surf", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING).withAbility(new ActivatedAbility(
            "Melodious Healing",
            "Once during your turn, you may heal 30 damage from each of your Water Pokémon.",
            new PlainAction("", new Attempt(List.of(
                    new HealEach(new Literal(30), new Matching(new AttackerAll(), new HasType(Type.WATER)))))),
            true,
            new ForAny(new And<>(new HasType(Type.WATER), new IsDamaged()), new AttackerAll())
    ));

    /**
     * A3-049 - Crabominable ex — the bonus covers all of its attacks, but
     * Insatiable Striking is its only one, so that is the printed restriction.
     */
    public static final PokemonCard CRABOMINABLE_EX = PokemonCard.evolution(
            "A3-049", "Crabominable ex", "",
            1, "Crabrawler", 160, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Insatiable Striking", "During your next turn, this Pokémon's Insatiable Striking attack does +40 damage.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new IncreaseDamage(new Literal(40), new Self(), new Literal(2))))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.METAL).withTags(CardTag.EX);

    /**
     * A3-050 - Wishiwashi
     */
    public static final PokemonCard WISHIWASHI = PokemonCard.basic(
            "A3-050", "Wishiwashi", "Individually, they're incredibly weak. It's by gathering up into schools that they're able to confront opponents.",
            30, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Call for Family", "Put 1 random Wishiwashi or Wishiwashi ex from your deck onto your Bench.", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new BenchFromDeck(new AttackerSide(), SCHOOL)))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-051 - Wishiwashi ex
     */
    public static final PokemonCard WISHIWASHI_EX = PokemonCard.basic(
            "A3-051", "Wishiwashi ex", "",
            170, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "School Storm", "This attack does 40 more damage for each of your Benched Wishiwashi and Wishiwashi ex.", EnergyCost.of(Type.WATER, 3), new Attempt(List.of(
                    new DealDamage(new Sum(new Literal(30), new Product(
                            new Literal(40), new CountCards(new AttackerSide(), Zone.BENCH, Optional.of(SCHOOL))
                    )), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A3-052 - Dewpider
     */
    public static final PokemonCard DEWPIDER = PokemonCard.basic(
            "A3-052", "Dewpider", "It forms a water bubble at the rear of its body and then covers its head with it. Meeting another Dewpider means comparing water-bubble sizes.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Hook", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-053 - Araquanid
     */
    public static final PokemonCard ARAQUANID = PokemonCard.evolution(
            "A3-053", "Araquanid", "It launches water bubbles with its legs, drowning prey within the bubbles. This Pokémon can then take its time to savor its meal.",
            1, "Dewpider", 110, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Dangerous Claws", "If your opponent's Active Pokémon is a Basic Pokémon, this attack does 60 more damage.", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Sum(
                            new Literal(60),
                            new Branch(List.of(new Branch.Case(
                                    new For(new IsBasic(), new OpponentActive()), new Literal(60))),
                                    new Literal(0))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-054 - Pyukumuku — {@code Knockout} is dispatched while it is still in
     * the Active Spot, and its {@code by} is empty unless an attack did it, so
     * {@code EventSource} resolves nothing for a poison or burn knockout.
     */
    public static final PokemonCard PYUKUMUKU = PokemonCard.basic(
            "A3-054", "Pyukumuku", "It lives in warm, shallow waters. If it encounters a foe, it will spit out its internal organs as a means to punch them.",
            60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sprinkle Water", "", EnergyCost.of(Type.WATER, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING).withAbility(new PassiveAbility("Innards Out",
            "If this Pokémon is in the Active Spot and is Knocked Out by damage from an attack from your opponent's Pokémon, do 50 damage to the Attacking Pokémon.",
            List.of(new Trigger(Knockout.class,
                    new And<>(new EventConcerns(new Self()), new For(new IsActive(), new Self())),
                    new Attempt(List.of(new PlaceDamage(new Literal(50), new EventSource())))))));

    /**
     * A3-055 - Bruxish
     */
    public static final PokemonCard BRUXISH = PokemonCard.basic(
            "A3-055", "Bruxish", "It grinds its teeth with great force to stimulate its brain. It fires the psychic energy created by this process from the protuberance on its head.",
            80, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Wave Splash", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-056 - Tapu Fini
     */
    public static final PokemonCard TAPU_FINI = PokemonCard.basic(
            "A3-056", "Tapu Fini", "This guardian deity of Poni Island manipulates water. Because it lives deep within a thick fog, it came to be both feared and revered.",
            100, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Spiral Drain", "Heal 20 damage from this Pokémon.", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new HealDamage(new Literal(20), new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING);

    /** A3-162 · Alolan Vulpix (alternate art of A3-040) */
    public static final PokemonCard ALOLAN_VULPIX_IR = ALOLAN_VULPIX.withId("A3-162", CardRarity.ILLUSTRATION_RARE);
    /** A3-163 · Pyukumuku (alternate art of A3-054) */
    public static final PokemonCard PYUKUMUKU_IR = PYUKUMUKU.withId("A3-163", CardRarity.ILLUSTRATION_RARE);
    /** A3-164 · Tapu Fini (alternate art of A3-056) */
    public static final PokemonCard TAPU_FINI_IR = TAPU_FINI.withId("A3-164", CardRarity.ILLUSTRATION_RARE);
    /** A3-183 · Crabominable ex (alternate art of A3-049) */
    public static final PokemonCard CRABOMINABLE_EX_UR = CRABOMINABLE_EX.withId("A3-183", CardRarity.ULTRA_RARE);
    /** A3-184 · Wishiwashi ex (alternate art of A3-051) */
    public static final PokemonCard WISHIWASHI_EX_UR = WISHIWASHI_EX.withId("A3-184", CardRarity.ULTRA_RARE);
    /** A3-201 · Crabominable ex (alternate art of A3-049) */
    public static final PokemonCard CRABOMINABLE_EX_SIR = CRABOMINABLE_EX.withId("A3-201", CardRarity.SPECIAL_ILLUSTRATION_RARE);
    /** A3-202 · Wishiwashi ex (alternate art of A3-051) */
    public static final PokemonCard WISHIWASHI_EX_SIR = WISHIWASHI_EX.withId("A3-202", CardRarity.SPECIAL_ILLUSTRATION_RARE);
    /** A3-215 · Squirtle (shiny of A1-053) */
    public static final PokemonCard SQUIRTLE_SR = com.tcgpocket.pool.A1.Water.SQUIRTLE.withId("A3-215", CardRarity.SHINY_RARE);
    /** A3-216 · Wartortle (shiny of A1-054) */
    public static final PokemonCard WARTORTLE_SR = com.tcgpocket.pool.A1.Water.WARTORTLE.withId("A3-216", CardRarity.SHINY_RARE);
    /** A3-217 · Blastoise (shiny of A1-055) */
    public static final PokemonCard BLASTOISE_SR = com.tcgpocket.pool.A1.Water.BLASTOISE.withId("A3-217", CardRarity.SHINY_RARE);
    /** A3-218 · Staryu (shiny of A1-074) */
    public static final PokemonCard STARYU_SR = com.tcgpocket.pool.A1.Water.STARYU.withId("A3-218", CardRarity.SHINY_RARE);
    /** A3-219 · Starmie (shiny of A1-075) */
    public static final PokemonCard STARMIE_SR = com.tcgpocket.pool.A1.Water.STARMIE.withId("A3-219", CardRarity.SHINY_RARE);
    /** A3-232 · Blastoise ex (shiny of A1-056) */
    public static final PokemonCard BLASTOISE_EX_SUR = com.tcgpocket.pool.A1.Water.BLASTOISE_EX.withId("A3-232", CardRarity.SHINY_ULTRA_RARE);
    /** A3-233 · Starmie ex (shiny of A1-076) */
    public static final PokemonCard STARMIE_EX_SUR = com.tcgpocket.pool.A1.Water.STARMIE_EX.withId("A3-233", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            ALOLAN_SANDSHREW, ALOLAN_SANDSLASH, ALOLAN_VULPIX, ALOLAN_NINETALES, SHELLDER, CLOYSTER, LAPRAS,
            POPPLIO, POPPLIO_WATER_GUN, BRIONNE, PRIMARINA, CRABOMINABLE_EX, WISHIWASHI, WISHIWASHI_EX,
            DEWPIDER, ARAQUANID, PYUKUMUKU, BRUXISH, TAPU_FINI,
            ALOLAN_VULPIX_IR, PYUKUMUKU_IR, TAPU_FINI_IR, CRABOMINABLE_EX_UR, WISHIWASHI_EX_UR,
            CRABOMINABLE_EX_SIR, WISHIWASHI_EX_SIR,
            SQUIRTLE_SR, WARTORTLE_SR, BLASTOISE_SR, STARYU_SR, STARMIE_SR, BLASTOISE_EX_SUR, STARMIE_EX_SUR);

    private Water() {
    }
}
