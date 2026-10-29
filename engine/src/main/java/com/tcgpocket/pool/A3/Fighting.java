package com.tcgpocket.pool.A3;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.CurrentHP;
import com.tcgpocket.number.Difference;
import com.tcgpocket.number.EventDamage;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.*;
import com.tcgpocket.trigger.DamageIncoming;
import com.tcgpocket.trigger.Knockout;
import com.tcgpocket.trigger.Trigger;

import com.tcgpocket.number.EnergyOn;
import java.util.List;

/**
 * Celestial Guardians — Fighting.
 */
public final class Fighting {

    /**
     * A3-089 - Cubone
     */
    public static final PokemonCard CUBONE = PokemonCard.basic(
            "A3-089", "Cubone", "When the memory of its departed mother brings it to tears, its cries echo mournfully within the skull it wears on its head.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Beat", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3-090 - Makuhita
     */
    public static final PokemonCard MAKUHITA = PokemonCard.basic(
            "A3-090", "Makuhita", "It grows stronger by enduring harsh training. It is a gutsy Pokémon that can withstand any attack.",
            70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Slap Push", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A3-091 - Hariyama
     */
    public static final PokemonCard HARIYAMA = PokemonCard.evolution(
            "A3-091", "Hariyama", "It loves challenging others to tests of strength. It has the power to stop a train with a slap.",
            1, "Makuhita", 120, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Push Out", "Switch out your opponent's Active Pokémon to the Bench. (Your opponent chooses the new Active Pokémon.)", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive()),
                    new SwitchActive(new OpponentSide())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A3-092 - Solrock
     */
    public static final PokemonCard SOLROCK = PokemonCard.basic(
            "A3-092", "Solrock", "When it rotates itself, it gives off light similar to the sun, thus blinding its foes.",
            90, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Solar Beam", "", EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3-093 - Drilbur
     */
    public static final PokemonCard DRILBUR = PokemonCard.basic(
            "A3-093", "Drilbur", "It brings its claws together and whirls around at high speed before rushing toward its prey.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Scratch", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3-094 - Timburr
     */
    public static final PokemonCard TIMBURR = PokemonCard.basic(
            "A3-094", "Timburr", "It loves helping out with construction projects. It loves it so much that if rain causes work to halt, it swings its log around and throws a tantrum.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Corkscrew Punch", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A3-095 - Gurdurr
     */
    public static final PokemonCard GURDURR = PokemonCard.evolution(
            "A3-095", "Gurdurr", "It shows off its muscles to Machoke and other Gurdurr. If it fails to measure up to the other Pokémon, it lies low for a little while.",
            1, "Timburr", 90, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Strength", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A3-096 - Conkeldurr — Guts answers {@link DamageIncoming} when the hit
     * would take at least its remaining HP, and on heads cuts the hit to leave
     * exactly 10. Like Bastiodon's Guarded Grill, the reduction lasts the rest
     * of the turn, which no attack in Pocket hits twice within.
     */
    public static final PokemonCard CONKELDURR = PokemonCard.evolution(
            "A3-096", "Conkeldurr", "Concrete mixed by Conkeldurr is much more durable than normal concrete, even when the compositions of the two materials are the same.",
            2, "Gurdurr", 150, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Mega Punch", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.PSYCHIC).withAbility(new PassiveAbility("Guts",
            "If this Pokémon would be Knocked Out by damage from an attack, flip a coin. If heads, this Pokémon is not Knocked Out, and its remaining HP becomes 10.",
            List.of(new Trigger(DamageIncoming.class,
                    new And<>(new EventConcerns(new Self()),
                            new Not<>(new LessThan(new EventDamage(), new CurrentHP(new Self())))),
                    new Attempt(List.of(
                            new FlipN(new Literal(1)),
                            new ConditionalEffect(new LastCoinTossHeads(),
                                    new ReduceDamageTaken(
                                            new Difference(new EventDamage(), new Difference(new CurrentHP(new Self()), new Literal(10))),
                                            new Self(), new Literal(0)))))))));

    /**
     * A3-097 - Crabrawler
     */
    public static final PokemonCard CRABRAWLER = PokemonCard.basic(
            "A3-097", "Crabrawler", "This Pokémon punches trees and eats the berries that drop down, training itself and getting food at the same time.",
            70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Punch", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A3-098 - Rockruff
     */
    public static final PokemonCard ROCKRUFF = PokemonCard.basic(
            "A3-098", "Rockruff", "This Pokémon is very friendly when it's young. Its disposition becomes vicious once it matures, but it never forgets the kindness of its master.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Signs of Evolution", "Put a random card that evolves from Rockruff from your deck into your hand.", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new SearchDeck(new AttackerSide(), new Literal(1), new EvolvesFrom("Rockruff"))))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3-099 - Rockruff
     */
    public static final PokemonCard ROCKRUFF_TACKLE = PokemonCard.basic(
            "A3-099", "Rockruff", "This Pokémon is very friendly when it's young. Its disposition becomes vicious once it matures, but it never forgets the kindness of its master.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3-100 - Lycanroc
     */
    public static final PokemonCard LYCANROC = PokemonCard.evolution(
            "A3-100", "Lycanroc", "Lycanroc attacks its prey with its sharp claws and fangs. It loyally obeys the instructions of a Trainer it trusts.",
            1, "Rockruff", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Crunch", "Flip a coin. If heads, discard a random Energy from your opponent's Active Pokémon.", EnergyCost.of(Type.FIGHTING, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(),
                            new DiscardRandomEnergy(new Literal(1), new OpponentActive()))))
            )), CardRarity.RARE
    ).withWeakness(Type.GRASS);

    /**
     * A3-101 - Lycanroc
     */
    public static final PokemonCard LYCANROC_BLOOD_FANG = PokemonCard.evolution(
            "A3-101", "Lycanroc", "This Pokémon uses its rocky mane to slash any who approach. It will even disobey its Trainer if it dislikes the orders it was given.",
            1, "Rockruff", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Blood Fang", "If your opponent's Active Pokémon has more remaining HP than this Pokémon, this attack does 50 more damage.", EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new GreaterThan(new CurrentHP(new OpponentActive()), new CurrentHP(new Self())), new Literal(100))),
                            new Literal(50)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.GRASS);

    /**
     * A3-102 - Mudbray
     */
    public static final PokemonCard MUDBRAY = PokemonCard.basic(
            "A3-102", "Mudbray", "This Pokémon covers itself in mud that it has regurgitated. The mud won't dry out even if it's exposed to the sun for a long time.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Rear Kick", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3-103 - Mudsdale
     */
    public static final PokemonCard MUDSDALE = PokemonCard.evolution(
            "A3-103", "Mudsdale", "This Pokémon has been treasured not just for its physical labor but also because it produces high-quality mud used for making pottery.",
            1, "Mudbray", 140, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "High Horsepower", "This Pokémon also does 40 damage to itself.", EnergyCost.of(Type.FIGHTING, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(140), new OpponentActive()),
                    new DealDamage(new Literal(40), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A3-104 - Passimian ex — Offload Pass, like Pyukumuku's Innards Out, runs
     * on {@code Knockout} while it is still in the Active Spot with its Energy
     * attached. Gated on an attacker other than itself, so the knockout came
     * on the opponent's turn: that makes it the defender, and "your Benched
     * Pokémon" the turn-relative {@link OpponentBench}.
     */
    public static final PokemonCard PASSIMIAN_EX = PokemonCard.basic(
            "A3-104", "Passimian ex", "",
            130, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Seismic Toss", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.PSYCHIC).withTags(CardTag.EX).withAbility(new PassiveAbility("Offload Pass",
            "If this Pokémon is in the Active Spot and is Knocked Out by damage from an attack from your opponent's Pokémon, move all Fighting Energy from this Pokémon to 1 of your Benched Pokémon.",
            List.of(new Trigger(Knockout.class,
                    new All<>(List.of(
                            new EventConcerns(new Self()),
                            new For(new IsActive(), new Self()),
                            new For(new Always<>(), new EventSource()),
                            new Not<>(new EventConcerns(new EventSource())))),
                    new Attempt(List.of(new MoveTypeEnergy(Type.FIGHTING,
                            new Matching(new OpponentAll(), new IsActive()),
                            new ChosenFrom(new OpponentBench(), new SelfSide(), "Choose a Benched Pokémon to move Fighting Energy to"))))))));

    /**
     * A3-105 - Minior
     */
    public static final PokemonCard MINIOR = PokemonCard.basic(
            "A3-105", "Minior", "Places where Minior fall from the night sky are few and far between, with Alola being one of the precious few.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Star Drop", "This attack does 40 damage to 1 of your opponent's Pokémon.", EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target"))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /** A3-172 · Rockruff (alternate art of A3-098) */
    public static final PokemonCard ROCKRUFF_IR = ROCKRUFF.withId("A3-172", CardRarity.ILLUSTRATION_RARE);
    /** A3-173 · Mudsdale (alternate art of A3-103) */
    public static final PokemonCard MUDSDALE_IR = MUDSDALE.withId("A3-173", CardRarity.ILLUSTRATION_RARE);
    /** A3-174 · Minior (alternate art of A3-105) */
    public static final PokemonCard MINIOR_IR = MINIOR.withId("A3-174", CardRarity.ILLUSTRATION_RARE);
    /** A3-187 · Passimian ex (alternate art of A3-104) */
    public static final PokemonCard PASSIMIAN_EX_UR = PASSIMIAN_EX.withId("A3-187", CardRarity.ULTRA_RARE);
    /** A3-205 · Passimian ex (alternate art of A3-104) */
    public static final PokemonCard PASSIMIAN_EX_SIR = PASSIMIAN_EX.withId("A3-205", CardRarity.SPECIAL_ILLUSTRATION_RARE);
    /** A3-223 · Machop (shiny of A1-143) */
    public static final PokemonCard MACHOP_SR = com.tcgpocket.pool.A1.Fighting.MACHOP.withId("A3-223", CardRarity.SHINY_RARE);
    /** A3-224 · Machoke (shiny of A1-144) */
    public static final PokemonCard MACHOKE_SR = com.tcgpocket.pool.A1.Fighting.MACHOKE.withId("A3-224", CardRarity.SHINY_RARE);
    /**
     * A3-225 · Machamp (only printed as a shiny so far). The base card will be added
     * later; when it is, refactor this into a {@code withId(...)} of it.
     */
    public static final PokemonCard MACHAMP = PokemonCard.evolution(
            "A3-225", "Machamp", "It quickly swings its four arms to rock its opponents with ceaseless punches and chops from all angles.",
            2, "Machoke", 150, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Power Press", "If this Pokémon has at least 2 extra Fighting Energy attached, this attack does 50 more damage.",
                    EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                            new DealDamage(new Branch(
                                    List.of(new Branch.Case(
                                            new GreaterThan(new EnergyOn(new Self(), Type.FIGHTING), new Literal(3)),
                                            new Literal(120))),
                                    new Literal(70)), new OpponentActive()))))
            ), CardRarity.SHINY_RARE
    ).withWeakness(Type.PSYCHIC);
    /** A3-226 · Cubone (shiny of A1-151) */
    public static final PokemonCard CUBONE_GROWL_SR = com.tcgpocket.pool.A1.Fighting.CUBONE.withId("A3-226", CardRarity.SHINY_RARE);
    /** A3-227 · Marowak (shiny of A1-152) */
    public static final PokemonCard MAROWAK_SR = com.tcgpocket.pool.A1.Fighting.MAROWAK.withId("A3-227", CardRarity.SHINY_RARE);
    /** A3-235 · Machamp ex (shiny of A1-146) */
    public static final PokemonCard MACHAMP_EX_SUR = com.tcgpocket.pool.A1.Fighting.MACHAMP_EX.withId("A3-235", CardRarity.SHINY_ULTRA_RARE);
    /** A3-236 · Marowak ex (shiny of A1-153) */
    public static final PokemonCard MAROWAK_EX_SUR = com.tcgpocket.pool.A1.Fighting.MAROWAK_EX.withId("A3-236", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            CUBONE, MAKUHITA, HARIYAMA, SOLROCK, DRILBUR, TIMBURR, GURDURR, CONKELDURR, CRABRAWLER,
            ROCKRUFF, ROCKRUFF_TACKLE, LYCANROC, LYCANROC_BLOOD_FANG, MUDBRAY, MUDSDALE, PASSIMIAN_EX, MINIOR,
            ROCKRUFF_IR, MUDSDALE_IR, MINIOR_IR, PASSIMIAN_EX_UR, PASSIMIAN_EX_SIR,
            MACHOP_SR, MACHOKE_SR, MACHAMP, CUBONE_GROWL_SR, MAROWAK_SR, MACHAMP_EX_SUR, MAROWAK_EX_SUR);

    private Fighting() {
    }
}
