package com.tcgpocket.pool.A3;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.card.StatusImmunity;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Celestial Guardians — Psychic.
 */
public final class Psychic {

    /** "has any Psychic Energy attached" — Flower Shield and Psychic Connect. */
    private static final ICondition<PokemonInPlay> HAS_PSYCHIC_ENERGY = new HasEnergy(EnergyCost.of(Type.PSYCHIC, 1));

    /**
     * A3-069 - Mr. Mime
     */
    public static final PokemonCard MR_MIME = PokemonCard.basic(
            "A3-069", "Mr. Mime", "The broadness of its hands may be no coincidence—many scientists believe its palms became enlarged specifically for pantomiming.",
            80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Barrier Shove", "Flip a coin. If heads, during your opponent's next turn, prevent all damage from—and effects of—attacks done to this Pokémon.", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventDamage(new Self(), new Literal(1))),
                    new ConditionalEffect(new LastCoinTossHeads(), new PreventEffects(new Self(), new Literal(1)))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-070 - Sableye
     */
    public static final PokemonCard SABLEYE = PokemonCard.basic(
            "A3-070", "Sableye", "It dwells in the darkness of caves. It uses its sharp claws to dig up gems to nourish itself.",
            80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Corner", "During your opponent's next turn, the Defending Pokémon can't retreat.", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new PreventRetreat(new OpponentActive(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-071 - Spoink
     */
    public static final PokemonCard SPOINK = PokemonCard.basic(
            "A3-071", "Spoink", "Spoink will die if it stops bouncing. The pearl on its head amplifies its psychic powers.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Psycharge", "Take a Psychic Energy from your Energy Zone and attach it to this Pokémon.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new AttachEnergy(Type.PSYCHIC, new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-072 - Grumpig
     */
    public static final PokemonCard GRUMPIG = PokemonCard.evolution(
            "A3-072", "Grumpig", "It can perform odd dance steps to influence foes. Its style of dancing became hugely popular overseas.",
            1, "Spoink", 110, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Zen Headbutt", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-073 - Lunatone
     */
    public static final PokemonCard LUNATONE = PokemonCard.basic(
            "A3-073", "Lunatone", "The phase of the moon apparently has some effect on its power. It's active on the night of a full moon.",
            90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Moon Press", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-074 - Shuppet
     */
    public static final PokemonCard SHUPPET = PokemonCard.basic(
            "A3-074", "Shuppet", "It loves to feed on feelings like envy and malice. Its upright horn catches the emotions of people.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Will-O-Wisp", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-075 - Banette
     */
    public static final PokemonCard BANETTE = PokemonCard.evolution(
            "A3-075", "Banette", "This Pokémon developed from an abandoned doll that amassed a grudge. It is seen in dark alleys.",
            1, "Shuppet", 90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Night Bind", "During your opponent's next turn, they can't take any Energy from their Energy Zone to attach to their Active Pokémon.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new PreventActiveEnergy(new Literal(1))))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-076 - Oricorio
     */
    public static final PokemonCard ORICORIO = PokemonCard.basic(
            "A3-076", "Oricorio", "This form of Oricorio has sipped pink nectar. It elevates its mind with the gentle steps of its dance, then unleashes its psychic energy.",
            70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Dazzle Dance", "Your opponent's Active Pokémon is now Confused.", EnergyCost.of(Type.PSYCHIC, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new AddStatus(new ConfusionStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-077 - Oricorio
     */
    public static final PokemonCard ORICORIO_SPITEFUL_DANCE = PokemonCard.basic(
            "A3-077", "Oricorio", "This form of Oricorio has sipped purple nectar. It uses ethereal dance steps to call forth the spirits of the dead.",
            70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Spiteful Dance", "If any of your Pokémon were Knocked Out by damage from an attack during your opponent's last turn, this attack does 60 more damage.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Sum(
                            new Literal(20),
                            new Branch(List.of(new Branch.Case(new KnockedOutLastTurn(), new Literal(60))),
                                    new Literal(0))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-078 - Cutiefly
     */
    public static final PokemonCard CUTIEFLY = PokemonCard.basic(
            "A3-078", "Cutiefly", "Nectar and pollen are its favorite fare. In fields of flowers, it gets into skirmishes with Butterfree over food.",
            30, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                    "Flap", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A3-079 - Ribombee
     */
    public static final PokemonCard RIBOMBEE = PokemonCard.evolution(
            "A3-079", "Ribombee", "It makes pollen puffs from pollen and nectar. The puffs' effects depend on the type of ingredients and how much of each one is used.",
            1, "Cutiefly", 80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                    "Fairy Wind", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.METAL);

    /**
     * A3-080 - Comfey
     */
    public static final PokemonCard COMFEY = PokemonCard.basic(
            "A3-080", "Comfey", "Comfey picks flowers with its vine and decorates itself with them. For some reason, flowers won't wither once they're attached to a Comfey.",
            70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Spinning Attack", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.METAL).withAbility(new PassiveAbility("Flower Shield",
            "Each of your Pokémon that has any Psychic Energy attached recovers from all Special Conditions and can't be affected by any Special Conditions.",
            new StatusImmunity(HAS_PSYCHIC_ENERGY)));

    /**
     * A3-081 - Sandygast
     */
    public static final PokemonCard SANDYGAST = PokemonCard.basic(
            "A3-081", "Sandygast", "If it loses its shovel, it will stick something else—like a branch—in its head to make do until it finds another shovel.",
            80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Vibration", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-082 - Palossand
     */
    public static final PokemonCard PALOSSAND = PokemonCard.evolution(
            "A3-082", "Palossand", "The terrifying Palossand drags smaller Pokémon into its sandy body. Once its victims are trapped, it drains them of their vitality whenever it pleases.",
            1, "Sandygast", 130, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Spooky Shot", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-083 - Mimikyu
     */
    public static final PokemonCard MIMIKYU = PokemonCard.basic(
            "A3-083", "Mimikyu", "There was a scientist who peeked under Mimikyu's old rag in the name of research. The scientist died of a mysterious disease.",
            70, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Shadow Hit", "This attack also does 20 damage to 1 of your Pokémon.", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new DealDamage(new Literal(20), new ChosenFrom(new AttackerAll(), new AttackerSide(), "Choose 1 of your Pokémon to take 20 damage"))))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-084 - Tapu Lele — the target is chosen once, then both hit and
     * counted, so the damage is for that Pokémon's Energy and no other's.
     */
    public static final PokemonCard TAPU_LELE = PokemonCard.basic(
            "A3-084", "Tapu Lele", "It heals the wounds of people and Pokémon by sprinkling them with its sparkling scales. This guardian deity is worshiped on Akala.",
            90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Energy Arrow", "This attack does 20 damage to 1 of your opponent's Pokémon for each Energy attached to that Pokémon.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new ChooseThen(
                            new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"),
                            new DealDamage(new Product(new Literal(20), new EnergyOn(new PlayTarget())), new PlayTarget()))))
            )), CardRarity.RARE
    ).withWeakness(Type.METAL);

    /**
     * A3-085 - Cosmog
     */
    public static final PokemonCard COSMOG = PokemonCard.basic(
            "A3-085", "Cosmog", "Even though its helpless, gaseous body can be blown away by the slightest breeze, it doesn't seem to care.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Teleport", "Switch this Pokémon with 1 of your Benched Pokémon.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new SwitchActive(new SelfSide())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-086 - Cosmoem
     */
    public static final PokemonCard COSMOEM = PokemonCard.evolution(
            "A3-086", "Cosmoem", "The king who ruled Alola in times of antiquity called it the “cocoon of the stars” and built an altar to worship it.",
            1, "Cosmog", 100, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Stiffen", "During your opponent's next turn, this Pokémon takes -50 damage from attacks.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new ReduceDamageTaken(new Literal(50), new Self(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A3-087 - Lunala ex — Psychic Connect chooses the Benched Pokémon once,
     * then moves as many Psychic Energy as it has, so "all" is counted on the
     * one chosen.
     */
    public static final PokemonCard LUNALA_EX = PokemonCard.evolution(
            "A3-087", "Lunala ex", "",
            2, "Cosmoem", 180, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Lunar Blast", "", EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(100), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.DARKNESS).withTags(CardTag.EX).withAbility(new ActivatedAbility(
            "Psychic Connect",
            "Once during your turn, you may move all Psychic Energy from 1 of your Benched Psychic Pokémon to your Active Pokémon.",
            new PlainAction("", new Attempt(List.of(
                    new ChooseThen(
                            new ChosenFrom(new AttackerBench(), new AttackerSide(),
                                    new And<>(new HasType(Type.PSYCHIC), HAS_PSYCHIC_ENERGY),
                                    "Choose a Benched Psychic Pokémon to move its Psychic Energy from"),
                            new MoveEnergy(new PlayTarget(), new AttackerActive(),
                                    new EnergyOn(new PlayTarget(), Type.PSYCHIC), Type.PSYCHIC))))),
            true,
            new ForAny(new And<>(new HasType(Type.PSYCHIC), HAS_PSYCHIC_ENERGY), new AttackerBench())
    ));

    /**
     * A3-088 - Necrozma
     */
    public static final PokemonCard NECROZMA = PokemonCard.basic(
            "A3-088", "Necrozma", "It looks somehow pained as it rages around in search of light, which serves as its energy. It's apparently from another world.",
            120, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Prismatic Laser", "During your next turn, this Pokémon can't attack.", EnergyCost.of(Type.PSYCHIC, 2, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive()),
                    new PreventAttack(new Self(), new Literal(2))))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS);

    /** A3-167 · Cutiefly (alternate art of A3-078) */
    public static final PokemonCard CUTIEFLY_IR = CUTIEFLY.withId("A3-167", CardRarity.ILLUSTRATION_RARE);
    /** A3-168 · Comfey (alternate art of A3-080) */
    public static final PokemonCard COMFEY_IR = COMFEY.withId("A3-168", CardRarity.ILLUSTRATION_RARE);
    /** A3-169 · Sandygast (alternate art of A3-081) */
    public static final PokemonCard SANDYGAST_IR = SANDYGAST.withId("A3-169", CardRarity.ILLUSTRATION_RARE);
    /** A3-170 · Tapu Lele (alternate art of A3-084) */
    public static final PokemonCard TAPU_LELE_IR = TAPU_LELE.withId("A3-170", CardRarity.ILLUSTRATION_RARE);
    /** A3-171 · Cosmog (alternate art of A3-085) */
    public static final PokemonCard COSMOG_IR = COSMOG.withId("A3-171", CardRarity.ILLUSTRATION_RARE);
    /** A3-186 · Lunala ex (alternate art of A3-087) */
    public static final PokemonCard LUNALA_EX_UR = LUNALA_EX.withId("A3-186", CardRarity.ULTRA_RARE);
    /** A3-204 · Lunala ex (alternate art of A3-087) */
    public static final PokemonCard LUNALA_EX_SIR = LUNALA_EX.withId("A3-204", CardRarity.SPECIAL_ILLUSTRATION_RARE);
    /**
     * A3-220 · Gastly (only printed as a shiny so far). The base card will be added
     * later; when it is, refactor this into a {@code withId(...)} of it.
     */
    public static final PokemonCard GASTLY = PokemonCard.basic(
            "A3-220", "Gastly", "It wraps its opponent in its gas-like body, slowly weakening its prey by poisoning it through the skin.",
            50, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Astonish", "Flip a coin. If heads, your opponent reveals a random card from their hand and shuffles it into their deck.",
                    EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                            new FlipN(new Literal(1)),
                            new ConditionalEffect(new LastCoinTossHeads(),
                                    new ShuffleRandomFromHand(new OpponentSide())))))
            ), CardRarity.SHINY_RARE
    ).withWeakness(Type.DARKNESS);
    /** A3-221 · Haunter (shiny of A1-121) */
    public static final PokemonCard HAUNTER_SR = com.tcgpocket.pool.A1.Psychic.HAUNTER.withId("A3-221", CardRarity.SHINY_RARE);
    /** A3-222 · Gengar (shiny of A1-122) */
    public static final PokemonCard GENGAR_SR = com.tcgpocket.pool.A1.Psychic.GENGAR.withId("A3-222", CardRarity.SHINY_RARE);
    /** A3-234 · Gengar ex (shiny of A1-123) */
    public static final PokemonCard GENGAR_EX_SUR = com.tcgpocket.pool.A1.Psychic.GENGAR_EX.withId("A3-234", CardRarity.SHINY_ULTRA_RARE);
    /** A3-238 · Lunala ex (alternate art of A3-087) */
    public static final PokemonCard LUNALA_EX_HR = LUNALA_EX.withId("A3-238", CardRarity.HYPER_RARE);

    static final List<PokemonCard> CARDS = List.of(
            MR_MIME, SABLEYE, SPOINK, GRUMPIG, LUNATONE, SHUPPET, BANETTE, ORICORIO, ORICORIO_SPITEFUL_DANCE,
            CUTIEFLY, RIBOMBEE, COMFEY, SANDYGAST, PALOSSAND, MIMIKYU, TAPU_LELE, COSMOG, COSMOEM, LUNALA_EX,
            NECROZMA,
            CUTIEFLY_IR, COMFEY_IR, SANDYGAST_IR, TAPU_LELE_IR, COSMOG_IR, LUNALA_EX_UR, LUNALA_EX_SIR,
            GASTLY, HAUNTER_SR, GENGAR_SR, GENGAR_EX_SUR, LUNALA_EX_HR);

    private Psychic() {
    }
}
