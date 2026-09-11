package com.tcgpocket.pool.A1;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.EqualTo;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

public final class Water {
    /**
     * A1-053 - Squirtle
     */
    public static final PokemonCard SQUIRTLE = PokemonCard.basic(
                    "A1-053", "Squirtle", "When it retracts its long neck into its shell, it squirts out water with vigorous force.",
                    60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Water Gun", "", EnergyCost.of(Type.WATER, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-054 - Wartortle
     */
    public static final PokemonCard WARTORTLE = PokemonCard.evolution(
                    "A1-054", "Wartortle", "It is recognized as a symbol of longevity. If its shell has algae on it, that Wartortle is very old.",
                    1, "Squirtle", 80, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Wave Splash", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-055 - Blastoise
     */
    public static final PokemonCard BLASTOISE = PokemonCard.evolution(
                    "A1-055", "Blastoise", "It crushes its foe under its heavy body to cause fainting. In a pinch, it will withdraw inside its shell.",
                    2, "Wartortle", 150, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Hydro Pump", "If this Pokémon has at least 2 extra Water Energy attached, this attack does 60 more damage.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Branch(
                                            List.of(new Branch.Case(
                                                    new GreaterThan(new EnergyOn(new Self(), Type.WATER), new Literal(3)),
                                                    new Literal(140))),
                                            new Literal(80)), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-056 - Blastoise ex
     */
    public static final PokemonCard BLASTOISE_EX = PokemonCard.evolution(
                    "A1-056", "Blastoise ex", "",
                    2, "Wartortle", 180, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Surf", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            ))), new Action(
                            "Hydro Bazooka", "If this Pokémon has at least 2 extra Water Energy attached, this attack does 60 more damage.", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Branch(
                                            List.of(new Branch.Case(
                                                    new GreaterThan(new EnergyOn(new Self(), Type.WATER), new Literal(4)),
                                                    new Literal(160)
                                            )), new Literal(100)
                                    ), new OpponentActive())
                            )))), CardRarity.DOUBLE_RARE)
            .withWeakness(Type.LIGHTNING).withTags(CardTag.EX);

    /**
     * A1-057 - Psyduck
     */
    public static final PokemonCard PSYDUCK = PokemonCard.basic(
                    "A1-057", "Psyduck", "It is constantly wracked by a headache. When the headache turns intense, it begins using mysterious powers.",
                    60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Headache", "Your opponent can't use any Supporter cards from their hand during their next turn.", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive()),
                                    new PreventSupporter(new Literal(1))
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-058 - Golduck
     */
    public static final PokemonCard GOLDUCK = PokemonCard.evolution(
                    "A1-058", "Golduck", "When it swims at full speed using its long, webbed limbs, its forehead somehow begins to glow.",
                    1, "Psyduck", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Aqua Edge", "", EnergyCost.of(Type.WATER, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-059 - Poliwag
     */
    public static final PokemonCard POLIWAG = PokemonCard.basic(
                    "A1-059", "Poliwag", "For Poliwag, swimming is easier than walking. The swirl pattern on its belly is actually part of the Pokémon's innards showing through the skin.",
                    60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Razor Fin", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-060 - Poliwhirl
     */
    public static final PokemonCard POLIWHIRL = PokemonCard.evolution(
                    "A1-060", "Poliwhirl", "Staring at the swirl on its belly causes drowsiness. This trait of Poliwhirl's has been used in place of lullabies to get children to go to sleep.",
                    1, "Poliwag", 90, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Knuckle Punch", "", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-061 - Poliwrath
     */
    public static final PokemonCard POLIWRATH = PokemonCard.evolution(
                    "A1-061", "Poliwrath", "Its body is solid muscle. When swimming through cold seas, Poliwrath uses its impressive arms to smash through drift ice and plow forward.",
                    2, "Poliwhirl", 150, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Mega Punch", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(80), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.LIGHTNING);
    
    /**
     * A1-062 - Tentacool
     */
    public static final PokemonCard TENTACOOL = PokemonCard.basic(
                    "A1-062", "Tentacool", "Tentacool is not a particularly strong swimmer. It drifts across the surface of shallow seas as it searches for prey.",
                    60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Gentle Slap", "", EnergyCost.of(Type.WATER, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-063 - Tentacruel
     */
    public static final PokemonCard TENTACRUEL = PokemonCard.evolution(
                    "A1-063", "Tentacruel", "When the red orbs on Tentacruel's head glow brightly, watch out. The Pokémon is about to fire off a burst of ultrasonic waves.",
                    1, "Tentacool", 110, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Poison Tentacles", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(50), new OpponentActive()),
                                    new AddStatus(new PoisonStatus(), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-064 - Seel
     */
    public static final PokemonCard SEEL = PokemonCard.basic(
                    "A1-064", "Seel", "Thanks to its thick fat, cold seas don't bother it at all, but it gets tired pretty easily in warm waters.",
                    80, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Headbutt", "", EnergyCost.of(Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-065 - Dewgong
     */
    public static final PokemonCard DEWGONG = PokemonCard.evolution(
                    "A1-065", "Dewgong", "It sunbathes on the beach after meals. The rise in its body temperature helps its digestion.",
                    1, "Seel", 120, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Surf", "", EnergyCost.of(Type.WATER, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(90), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-066 - Shellder
     */
    public static final PokemonCard SHELLDER = PokemonCard.basic(
                    "A1-066", "Shellder", "It is encased in a shell that is harder than diamond. Inside, however, it is surprisingly tender.",
                    60, Type.WATER, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Tongue Slap", "", EnergyCost.of(Type.WATER, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-067 - Cloyster
     */
    public static final PokemonCard CLOYSTER = PokemonCard.evolution(
                    "A1-067", "Cloyster", "Cloyster that live in seas with harsh tidal currents grow large, sharp spikes on their shells.",
                    1, "Shellder", 120, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Surf", "", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-068 - Krabby
     */
    public static final PokemonCard KRABBY = PokemonCard.basic(
                    "A1-068", "Krabby", "It can be found near the sea. The large pincers grow back if they are torn out of their sockets.",
                    70, Type.WATER, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Vise Grip", "", EnergyCost.of(Type.WATER, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.LIGHTNING);

    /**
     * A1-069 - Kingler
     */
    public static final PokemonCard KINGLER = PokemonCard.evolution(
                    "A1-069", "Kingler", "Its large and hard pincer has 10,000-horsepower strength. However, being so big, it is unwieldy to move.",
                    1, "Krabby", 120, Type.WATER, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "KO Crab", "Flip 2 coins. If both of them are heads, this attack does 80 more damage.", EnergyCost.of(Type.WATER, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new FlipN(new Literal(2)),
                                    new DealDamage(new Branch(
                                            List.of(new Branch.Case(
                                                    new EqualTo(new NumberHeads(), new Literal(2)),
                                                    new Literal(160))),
                                            new Literal(80)), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.LIGHTNING);

}