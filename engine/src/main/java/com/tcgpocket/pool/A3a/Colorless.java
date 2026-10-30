package com.tcgpocket.pool.A3a;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.AttackCostIncrease;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.AllFlipsHeads;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.ForAny;
import com.tcgpocket.condition.HasTag;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.condition.SupporterPlayedThisTurn;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.Fail;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.effect.SwitchActive;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.CurrentHP;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.OpponentActive;

import java.util.List;

import static com.tcgpocket.pool.A1.Colorless.PIDGEY;
import static com.tcgpocket.pool.A1.Colorless.PIDGEOTTO;
import static com.tcgpocket.pool.A1.Colorless.PIDGEOT;
import static com.tcgpocket.pool.A1.Colorless.AERODACTYL;
import static com.tcgpocket.pool.A1a.Colorless.PIDGEOT_EX;

/**
 * Extradimensional Crisis — Colorless.
 */
public final class Colorless {

    /**
     * A3a-054 - Lillipup
     */
    public static final PokemonCard LILLIPUP = PokemonCard.basic(
            "A3a-054", "Lillipup", "This Pokémon is courageous but also cautious. It uses the soft fur covering its face to collect information about its surroundings.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3a-055 - Herdier
     */
    public static final PokemonCard HERDIER = PokemonCard.evolution(
            "A3a-055", "Herdier", "Herdier is a very smart and friendly Pokémon. So much so that there's a theory that Herdier was the first Pokémon to partner with people.",
            1, "Lillipup", 80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3a-056 - Stoutland
     */
    public static final PokemonCard STOUTLAND = PokemonCard.evolution(
            "A3a-056", "Stoutland", "These Pokémon seem to enjoy living with humans. Even a Stoutland caught in the wild will warm up to people in about three days.",
            2, "Herdier", 140, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Sharp Fang", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING).withAbility(new PassiveAbility("Guard Dog Visage",
            "As long as this Pokémon is in the Active Spot, attacks used by your opponent's Active Pokémon cost 1 Colorless more.",
            new AttackCostIncrease(1, new IsActive())));

    /**
     * A3a-057 - Stufful
     */
    public static final PokemonCard STUFFUL = PokemonCard.basic(
            "A3a-057", "Stufful", "Its fluffy fur is a delight to pet, but carelessly reaching out to touch this Pokémon could result in painful retaliation.",
            70, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3a-058 - Bewear — knocked out by damage counters rather than attack damage,
     * so weakness and damage modifiers cannot save or change it.
     */
    public static final PokemonCard BEWEAR = PokemonCard.evolution(
            "A3a-058", "Bewear", "Once it accepts you as a friend, it tries to show its affection with a hug. Letting it do that is dangerous—it could easily shatter your bones.",
            1, "Stufful", 120, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Superpowered Hug", "Flip 2 coins. If both of them are heads, your opponent's Active Pokémon is Knocked Out.", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new ConditionalEffect(new AllFlipsHeads(), new PlaceDamage(new CurrentHP(new OpponentActive()), new OpponentActive()))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3a-059 - Oranguru
     */
    public static final PokemonCard ORANGURU = PokemonCard.basic(
            "A3a-059", "Oranguru", "It normally spends its time meditating in the treetops. It throws Poké Balls and gives other Pokémon orders as it pleases.",
            100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Hammer In", "", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3a-060 - Type: Null
     */
    public static final PokemonCard TYPE_NULL = PokemonCard.basic(
            "A3a-060", "Type: Null", "A Pokémon weapon developed for a specific mission, it went berserk during an experiment, so it was cryogenically frozen.",
            80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Quick Blow", "Flip a coin. If heads, this attack does 20 more damage.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(
                            new Literal(20),
                            new Product(new NumberHeads(), new Literal(20))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3a-061 - Silvally
     */
    public static final PokemonCard SILVALLY = PokemonCard.evolution(
            "A3a-061", "Silvally", "This is its form once it has awakened and evolved. Freed from its heavy mask, its speed is greatly increased.",
            1, "Type: Null", 110, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Brave Buddies", "If you played a Supporter card from your hand during this turn, this attack does 50 more damage.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Sum(
                            new Literal(50),
                            new Branch(List.of(new Branch.Case(
                                    new SupporterPlayedThisTurn(), new Literal(50))),
                                    new Literal(0))), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A3a-062 - Celesteela
     */
    public static final PokemonCard CELESTEELA = PokemonCard.basic(
            "A3a-062", "Celesteela", "One of the dangerous UBs, high energy readings can be detected coming from both of its huge arms.",
            120, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Moombahton", "Flip a coin. If tails, this attack does nothing.", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new Fail()),
                    new DealDamage(new Literal(100), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.LIGHTNING).withTags(CardTag.ULTRA_BEAST).withAbility(new ActivatedAbility("Ultra Thrusters",
            "Once during your turn, you may switch your Active Ultra Beast with 1 of your Benched Ultra Beasts.",
            new PlainAction("", new Attempt(List.of(
                    new SwitchActive(new AttackerSide(), new ChosenFrom(
                            new AttackerBench(), new AttackerSide(), new HasTag(CardTag.ULTRA_BEAST), "Choose a Benched Ultra Beast"))))),
            true, new And<>(new For(new HasTag(CardTag.ULTRA_BEAST), new AttackerActive()), new ForAny(new HasTag(CardTag.ULTRA_BEAST), new AttackerBench()))));

    /** A3a-074 · Silvally (alternate art of A3a-061) */
    public static final PokemonCard SILVALLY_IR = SILVALLY.withId("A3a-074", CardRarity.ILLUSTRATION_RARE);
    /** A3a-075 · Celesteela (alternate art of A3a-062) */
    public static final PokemonCard CELESTEELA_IR = CELESTEELA.withId("A3a-075", CardRarity.ILLUSTRATION_RARE);

    /** A3a-095 · Pidgey (alternate art of A1-186) */
    public static final PokemonCard PIDGEY_SR = PIDGEY.withId("A3a-095", CardRarity.SHINY_RARE);
    /** A3a-096 · Pidgeotto (alternate art of A1-187) */
    public static final PokemonCard PIDGEOTTO_SR = PIDGEOTTO.withId("A3a-096", CardRarity.SHINY_RARE);
    /** A3a-097 · Pidgeot (alternate art of A1-188) */
    public static final PokemonCard PIDGEOT_SR = PIDGEOT.withId("A3a-097", CardRarity.SHINY_RARE);
    /** A3a-098 · Aerodactyl (alternate art of A1-210) */
    public static final PokemonCard AERODACTYL_SR = AERODACTYL.withId("A3a-098", CardRarity.SHINY_RARE);
    /** A3a-102 · Pidgeot ex (alternate art of A1a-059) */
    public static final PokemonCard PIDGEOT_EX_SUR = PIDGEOT_EX.withId("A3a-102", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            LILLIPUP, HERDIER, STOUTLAND, STUFFUL, BEWEAR, ORANGURU,
            TYPE_NULL, SILVALLY, CELESTEELA,
            SILVALLY_IR, CELESTEELA_IR,
            PIDGEY_SR, PIDGEOTTO_SR, PIDGEOT_SR, AERODACTYL_SR, PIDGEOT_EX_SUR);

    private Colorless() {
    }
}
