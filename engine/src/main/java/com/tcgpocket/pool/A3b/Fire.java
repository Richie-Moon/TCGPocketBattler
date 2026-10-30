package com.tcgpocket.pool.A3b;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.effect.AttachEnergy;
import com.tcgpocket.effect.AttachFromDiscard;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardTypeEnergy;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.EnergyInDiscard;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;

import java.util.List;

/**
 * Eevee Grove — Fire.
 */
public final class Fire {

    /**
     * A3b-008 - Flareon
     */
    public static final PokemonCard FLAREON = PokemonCard.evolution(
            "A3b-008", "Flareon", "Inhaled air is carried to its flame sac, heated, and exhaled as fire that reaches over 3,000 degrees Fahrenheit.",
            1, "Eevee", 110, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Assisting Heater", "Take a Fire Energy from your Energy Zone and attach it to 1 of your Benched Pokémon.", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new AttachEnergy(Type.FIRE, new Literal(1), new ChosenFrom(new AttackerBench(), new SelfSide(), "Choose a Benched Pokémon to attach Fire Energy to"))))
            )), CardRarity.RARE
    ).withWeakness(Type.WATER);

    /**
     * A3b-009 - Flareon ex
     */
    public static final PokemonCard FLAREON_EX = PokemonCard.evolution(
            "A3b-009", "Flareon ex", "",
            1, "Eevee", 150, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Fire Spin", "Discard 2 Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(130), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIRE, new Literal(2), new Self())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.WATER).withTags(CardTag.EX).withAbility(new ActivatedAbility(
            "Combust",
            "Once during your turn, you may attach a Fire Energy from your discard pile to this Pokémon. If you do, do 20 damage to this Pokémon.",
            new PlainAction("", new Attempt(List.of(
                    new AttachFromDiscard(Type.FIRE, new Literal(1), new Self()),
                    new PlaceDamage(new Literal(20), new Self())))),
            true, new GreaterThan(new EnergyInDiscard(new SelfSide(), Type.FIRE), new Literal(0))));

    /**
     * A3b-010 - Torkoal
     */
    public static final PokemonCard TORKOAL = PokemonCard.basic(
            "A3b-010", "Torkoal", "It burns coal inside its shell for energy. It blows out black soot if it is endangered.",
            100, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Flamethrower", "Discard a Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIRE, new Literal(1), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A3b-011 - Litten
     */
    public static final PokemonCard LITTEN = PokemonCard.basic(
            "A3b-011", "Litten", "If you try too hard to get close to it, it won't open up to you. Even if you do grow close, giving it too much affection is still a no-no.",
            70, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flare", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A3b-012 - Torracat
     */
    public static final PokemonCard TORRACAT = PokemonCard.evolution(
            "A3b-012", "Torracat", "Torracat will let its Trainer coddle it once they've grown close, but it's a powerful, sharp-clawed Pokémon, so its Trainer gets covered in scratches.",
            1, "Litten", 80, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Claw Slash", "", EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A3b-013 - Incineroar
     */
    public static final PokemonCard INCINEROAR = PokemonCard.evolution(
            "A3b-013", "Incineroar", "Although it's rough mannered and egotistical, it finds beating down unworthy opponents boring. It gets motivated for stronger opponents.",
            2, "Torracat", 150, Type.FIRE, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Darkest Lariat", "Flip 2 coins. This attack does 100 damage for each heads.", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Product(new NumberHeads(), new Literal(100)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A3b-014 - Salandit
     */
    public static final PokemonCard SALANDIT = PokemonCard.basic(
            "A3b-014", "Salandit", "It taunts its prey and lures them into narrow, rocky areas where it then sprays them with toxic gas to make them dizzy and take them down.",
            60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Ember", "Discard a Fire Energy from this Pokémon.", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new DiscardTypeEnergy(Type.FIRE, new Literal(1), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A3b-015 - Salazzle
     */
    public static final PokemonCard SALAZZLE = PokemonCard.evolution(
            "A3b-015", "Salazzle", "Salazzle makes its opponents light-headed with poisonous gas, then captivates them with alluring movements to turn them into loyal servants.",
            1, "Salandit", 90, Type.FIRE, EnergyCost.free(), List.of(new Action(
                    "Combustion", "", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /** A3b-071 · Flareon (alternate art of A3b-008) */
    public static final PokemonCard FLAREON_IR = FLAREON.withId("A3b-071", CardRarity.ILLUSTRATION_RARE);

    /** A3b-079 · Flareon ex (alternate art of A3b-009) */
    public static final PokemonCard FLAREON_EX_UR = FLAREON_EX.withId("A3b-079", CardRarity.ULTRA_RARE);

    /** A3b-087 · Flareon ex (alternate art of A3b-009) */
    public static final PokemonCard FLAREON_EX_SIR = FLAREON_EX.withId("A3b-087", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    /** A3b-103 · Moltres ex (shiny of A1-047) */
    public static final PokemonCard MOLTRES_EX_SUR = com.tcgpocket.pool.A1.Fire.MOLTRES_EX.withId("A3b-103", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            FLAREON, FLAREON_EX, TORKOAL, LITTEN, TORRACAT, INCINEROAR,
            SALANDIT, SALAZZLE, FLAREON_IR, FLAREON_EX_UR,
            FLAREON_EX_SIR, MOLTRES_EX_SUR);

    private Fire() {
    }
}
