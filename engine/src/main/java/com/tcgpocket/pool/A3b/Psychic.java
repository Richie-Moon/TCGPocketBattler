package com.tcgpocket.pool.A3b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.AttackUsedLastTurn;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.IsBasic;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.CopyAttack;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DrawCard;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.YouMay;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.EnergyAmong;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.number.TimesAttackUsed;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.Matching;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentAll;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.Evolved;
import com.tcgpocket.trigger.Trigger;

import java.util.List;
import java.util.Optional;

/**
 * Eevee Grove — Psychic.
 */
public final class Psychic {

    /**
     * A3b-028 - Espeon
     */
    public static final PokemonCard ESPEON = PokemonCard.evolution(
            "A3b-028", "Espeon", "The tip of its forked tail quivers when it is predicting its opponent's next move.",
            1, "Eevee", 100, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Energy Crush", "This attack does 20 damage for each Energy attached to all of your opponent's Pokémon.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Product(new EnergyAmong(new OpponentAll()), new Literal(20)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS);

    /**
     * A3b-029 - Woobat
     */
    public static final PokemonCard WOOBAT = PokemonCard.basic(
            "A3b-029", "Woobat", "While inside a cave, if you look up and see lots of heart-shaped marks lining the walls, it's evidence that Woobat live there.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Gust", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3b-030 - Swoobat
     */
    public static final PokemonCard SWOOBAT = PokemonCard.evolution(
            "A3b-030", "Swoobat", "Emitting powerful sound waves tires it out. Afterward, it won't be able to fly for a little while.",
            1, "Woobat", 100, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Psyshot", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3b-031 - Swirlix
     */
    public static final PokemonCard SWIRLIX = PokemonCard.basic(
            "A3b-031", "Swirlix", "It eats its own weight in sugar every day. If it doesn't get enough sugar, it becomes incredibly grumpy.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sweets Relay", "If 1 of your Pokémon used Sweets Relay during your last turn, this attack does 20 more damage.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(
                            new AttackUsedLastTurn("Sweets Relay"),
                            new Literal(30))
                    ), new Literal(10)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A3b-032 - Slurpuff
     */
    public static final PokemonCard SLURPUFF = PokemonCard.evolution(
            "A3b-032", "Slurpuff", "By taking in a person's scent, it can sniff out their mental and physical condition. It's hoped that this skill will have many medical applications.",
            1, "Swirlix", 120, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Sweets Relay", "If 1 of your Pokémon used Sweets Relay during your last turn, this attack does 60 more damage.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(
                            new AttackUsedLastTurn("Sweets Relay"),
                            new Literal(90))
                    ), new Literal(30)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /**
     * A3b-033 - Sylveon
     */
    public static final PokemonCard SYLVEON = PokemonCard.evolution(
            "A3b-033", "Sylveon", "This Pokémon uses its ribbonlike feelers to send a soothing aura into its opponents, erasing their hostility.",
            1, "Eevee", 90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Evoharmony", "This attack does 30 more damage for each Evolution Pokémon on your Bench.", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Sum(new Literal(40), new Product(new Literal(30), new CountCards(new AttackerSide(), Zone.BENCH, Optional.of(new Not<>(new IsBasic()))))), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.METAL);

    /**
     * A3b-034 - Sylveon ex — Happy Ribbon asks only with a card left to draw.
     */
    public static final PokemonCard SYLVEON_EX = PokemonCard.evolution(
            "A3b-034", "Sylveon ex", "",
            1, "Eevee", 140, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Fairy Wind", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.METAL).withTags(CardTag.EX).withAbility(new PassiveAbility(
            "Happy Ribbon",
            "Once during your turn, when you play this Pokémon from your hand to evolve 1 of your Pokémon, you may draw 2 cards.",
            List.of(new Trigger(Evolved.class,
                    new And<>(new EventConcerns(new Self()), new GreaterThan(new CountCards(new SelfSide(), Zone.DECK), new Literal(0))),
                    new Attempt(List.of(new YouMay("Happy Ribbon: draw 2 cards?",
                            new DrawCard(new Literal(2), new SelfSide()))))))));

    /**
     * A3b-035 - Mimikyu — Try to Imitate takes no Energy check: the text asks for none, as Mew ex's Genome Hacking.
     */
    public static final PokemonCard MIMIKYU = PokemonCard.basic(
            "A3b-035", "Mimikyu", "This Pokémon lives in dark places untouched by sunlight. When it appears before humans, it hides itself under a cloth that resembles a Pikachu.",
            70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Try to Imitate", "Flip a coin. If heads, choose 1 of your opponent's Active Pokémon's attacks and use it as this attack.", EnergyCost.of(Type.PSYCHIC, 2), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new CopyAttack(new Matching(new OpponentAll(), new IsActive()), false))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /**
     * A3b-036 - Milcery
     */
    public static final PokemonCard MILCERY = PokemonCard.basic(
            "A3b-036", "Milcery", "This Pokémon was born from sweet-smelling particles in the air. Its body is made of cream.",
            50, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sweets Relay", "If 1 of your Pokémon used Sweets Relay during your last turn, this attack does 20 more damage.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(
                            new AttackUsedLastTurn("Sweets Relay"),
                            new Literal(30))
                    ), new Literal(10)), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A3b-037 - Alcremie
     */
    public static final PokemonCard ALCREMIE = PokemonCard.evolution(
            "A3b-037", "Alcremie", "When it trusts a Trainer, it will treat them to berries it's decorated with cream.",
            1, "Milcery", 80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Sweets Overload", "This attack does 40 damage for each time your Pokémon used Sweets Relay during this game.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Product(new TimesAttackUsed("Sweets Relay"), new Literal(40)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.METAL);

    /** A3b-075 · Espeon (alternate art of A3b-028) */
    public static final PokemonCard ESPEON_IR = ESPEON.withId("A3b-075", CardRarity.ILLUSTRATION_RARE);

    /** A3b-076 · Sylveon (alternate art of A3b-033) */
    public static final PokemonCard SYLVEON_IR = SYLVEON.withId("A3b-076", CardRarity.ILLUSTRATION_RARE);

    /** A3b-081 · Sylveon ex (alternate art of A3b-034) */
    public static final PokemonCard SYLVEON_EX_UR = SYLVEON_EX.withId("A3b-081", CardRarity.ULTRA_RARE);

    /** A3b-089 · Sylveon ex (alternate art of A3b-034) */
    public static final PokemonCard SYLVEON_EX_SIR = SYLVEON_EX.withId("A3b-089", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    /** A3b-097 · Ralts (shiny of A2-068) */
    public static final PokemonCard RALTS_SR = com.tcgpocket.pool.A2.Psychic.RALTS.withId("A3b-097", CardRarity.SHINY_RARE);

    /** A3b-098 · Kirlia (shiny of A2-069) */
    public static final PokemonCard KIRLIA_SR = com.tcgpocket.pool.A2.Psychic.KIRLIA.withId("A3b-098", CardRarity.SHINY_RARE);

    /** A3b-099 · Gardevoir (shiny of A1-132) */
    public static final PokemonCard GARDEVOIR_SR = com.tcgpocket.pool.A1.Psychic.GARDEVOIR.withId("A3b-099", CardRarity.SHINY_RARE);

    static final List<PokemonCard> CARDS = List.of(
            ESPEON, WOOBAT, SWOOBAT, SWIRLIX, SLURPUFF, SYLVEON,
            SYLVEON_EX, MIMIKYU, MILCERY, ALCREMIE, ESPEON_IR, SYLVEON_IR,
            SYLVEON_EX_UR,
            SYLVEON_EX_SIR, RALTS_SR, KIRLIA_SR, GARDEVOIR_SR);

    private Psychic() {
    }
}
