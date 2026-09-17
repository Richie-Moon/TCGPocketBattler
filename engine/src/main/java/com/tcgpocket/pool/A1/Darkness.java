package com.tcgpocket.pool.A1;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsPoisoned;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.BenchFromDeck;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.PreventRetreat;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.state.Zone;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;

import java.util.List;
import java.util.Optional;

/** Genetic Apex — Darkness. */
public final class Darkness {

    private Darkness() {
    }

    /**
     * A1-164 - Ekans
     */
    public static final PokemonCard EKANS = PokemonCard.basic(
                    "A1-164", "Ekans", "By dislocating its jaw, it can swallow prey larger than itself. After a meal, it curls up and rests.",
                    60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Bite", "", EnergyCost.of(Type.DARKNESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-165 - Arbok
     */
    public static final PokemonCard ARBOK = PokemonCard.evolution(
                    "A1-165", "Arbok", "The latest research has determined that there are over 20 possible arrangements of the patterns on its stomach.",
                    1, "Ekans", 100, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Corner", "During your opponent's next turn, the Defending Pokémon can't retreat.", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(60), new OpponentActive()),
                                    new PreventRetreat(new OpponentActive(), new Literal(1))
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-166 - Nidoran♀
     */
    public static final PokemonCard NIDORAN_F = PokemonCard.basic(
                    "A1-166", "Nidoran♀", "Females are more sensitive to smells than males. While foraging, they'll use their whiskers to check wind direction and stay downwind of predators.",
                    60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Call for Family", "Put 1 random Nidoran♂ from your deck onto your Bench.", EnergyCost.of(Type.DARKNESS, 1),
                            new Attempt(List.of(
                                    new BenchFromDeck(new AttackerSide(), new IsSpecies("Nidoran♂"))
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-167 - Nidorina
     */
    public static final PokemonCard NIDORINA = PokemonCard.evolution(
                    "A1-167", "Nidorina", "The horn on its head has atrophied. It's thought that this happens so Nidorina's children won't get poked while their mother is feeding them.",
                    1, "Nidoran♀", 80, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Bite", "", EnergyCost.of(Type.DARKNESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-168 - Nidoqueen
     */
    public static final PokemonCard NIDOQUEEN = PokemonCard.evolution(
                    "A1-168", "Nidoqueen", "Nidoqueen is better at defense than offense. With scales like armor, this Pokémon will shield its children from any kind of attack.",
                    2, "Nidorina", 140, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Lovestrike", "This attack does 50 more damage for each of your Benched Nidoking.", EnergyCost.of(Type.DARKNESS, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Sum(new Literal(80), new Product(
                                            new Literal(50), new CountCards(new AttackerSide(), Zone.BENCH, Optional.of(new IsSpecies("Nidoking")))
                                    )), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-169 - Nidoran♂
     */
    public static final PokemonCard NIDORAN_M = PokemonCard.basic(
                    "A1-169", "Nidoran♂", "The horn on a male Nidoran's forehead contains a powerful poison. This is a very cautious Pokémon, always straining its large ears.",
                    60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Peck", "", EnergyCost.of(Type.DARKNESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-170 - Nidorino
     */
    public static final PokemonCard NIDORINO = PokemonCard.evolution(
                    "A1-170", "Nidorino", "With a horn that's harder than diamond, this Pokémon goes around shattering boulders as it searches for a moon stone.",
                    1, "Nidoran♂", 90, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Horn Attack", "", EnergyCost.of(Type.DARKNESS, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-171 - Nidoking
     */
    public static final PokemonCard NIDOKING = PokemonCard.evolution(
                    "A1-171", "Nidoking", "When it goes on a rampage, it's impossible to control. But in the presence of a Nidoqueen it's lived with for a long time, Nidoking calms down.",
                    2, "Nidorino", 150, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Poison Horn", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.DARKNESS, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(90), new OpponentActive()),
                                    new AddStatus(new PoisonStatus(), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-172 - Zubat
     */
    public static final PokemonCard ZUBAT = PokemonCard.basic(
                    "A1-172", "Zubat", "It emits ultrasonic waves from its mouth to check its surroundings. Even in tight caves, Zubat flies around with skill.",
                    50, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Glide", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-173 - Golbat
     */
    public static final PokemonCard GOLBAT = PokemonCard.evolution(
                    "A1-173", "Golbat", "It loves to drink other creatures' blood. It's said that if it finds others of its kind going hungry, it sometimes shares the blood it's gathered.",
                    1, "Zubat", 70, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Wing Attack", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-174 - Grimer
     */
    public static final PokemonCard GRIMER = PokemonCard.basic(
                    "A1-174", "Grimer", "Born from sludge, these Pokémon now gather in polluted places and increase the bacteria in their bodies.",
                    70, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Poison Gas", "Your opponent's Active Pokémon is now Poisoned.", EnergyCost.of(Type.DARKNESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive()),
                                    new AddStatus(new PoisonStatus(), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-175 - Muk
     */
    public static final PokemonCard MUK = PokemonCard.evolution(
                    "A1-175", "Muk", "It's thickly covered with a filthy, vile sludge. It is so toxic, even its footprints contain poison.",
                    1, "Grimer", 130, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Venoshock", "If your opponent's Active Pokémon is Poisoned, this attack does 50 more damage.", EnergyCost.of(Type.DARKNESS, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Branch(List.of(
                                            new Branch.Case(new For(new IsPoisoned(), new OpponentActive()), new Literal(120))
                                    ), new Literal(70)), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-176 - Koffing
     */
    public static final PokemonCard KOFFING = PokemonCard.basic(
                    "A1-176", "Koffing", "Its body is full of poisonous gas. It floats into garbage dumps, seeking out the fumes of raw, rotting trash.",
                    70, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Suffocating Gas", "", EnergyCost.of(Type.DARKNESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    /**
     * A1-177 - Weezing
     */
    public static final PokemonCard WEEZING = PokemonCard.evolution(
                    "A1-177", "Weezing", "If one of the twin Koffing inflates, the other one deflates. It constantly mixes its poisonous gases.",
                    1, "Koffing", 110, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Tackle", "", EnergyCost.of(Type.DARKNESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.FIGHTING);

    static final List<PokemonCard> CARDS = List.of(
            EKANS, ARBOK, NIDORAN_F, NIDORINA, NIDOQUEEN, NIDORAN_M, NIDORINO, NIDOKING,
            ZUBAT, GOLBAT, GRIMER, MUK, KOFFING, WEEZING);
}
