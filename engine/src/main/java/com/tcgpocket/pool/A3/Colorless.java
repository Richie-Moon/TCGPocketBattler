package com.tcgpocket.pool.A3;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.status.ConfusionStatus;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.*;
import com.tcgpocket.trigger.EnergyAttached;
import com.tcgpocket.trigger.Trigger;

import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import java.util.List;

/**
 * Celestial Guardians — Colorless.
 */
public final class Colorless {

    /**
     * A3-128 - Tauros
     */
    public static final PokemonCard TAUROS = PokemonCard.basic(
            "A3-128", "Tauros", "When Tauros begins whipping itself with its tails, it's a warning that the Pokémon is about to charge with astounding speed.",
            100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Rising Lunge", "Flip a coin. If heads, this attack does 50 more damage.", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(new Literal(50), new Product(new NumberHeads(), new Literal(50))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-129 - Skitty
     */
    public static final PokemonCard SKITTY = PokemonCard.basic(
            "A3-129", "Skitty", "It can't stop itself from chasing moving things, and it runs in a circle, chasing its own tail.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-130 - Delcatty
     */
    public static final PokemonCard DELCATTY = PokemonCard.evolution(
            "A3-130", "Delcatty", "It dislikes dirty places. It often searches for a comfortable place in which to groom itself.",
            1, "Skitty", 90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Energy Assist", "Take a Colorless Energy from your Energy Zone and attach it to 1 of your Benched Pokémon.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive()),
                    new AttachEnergy(Type.COLORLESS, new Literal(1), new ChosenFrom(
                            new AttackerBench(), new AttackerSide(), "Please choose a Pokémon to attach an Energy to."))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-131 - Fletchling
     */
    public static final PokemonCard FLETCHLING = PokemonCard.basic(
            "A3-131", "Fletchling", "This Pokémon is normally calm, but once it enters battle, its hormonal balance changes and it becomes aggressive.",
            50, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flap", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-132 - Hawlucha
     */
    public static final PokemonCard HAWLUCHA = PokemonCard.basic(
            "A3-132", "Hawlucha", "Its elegant finishing moves—performed by nimbly leaping around using its wings—are polished in the forest where it was born and raised.",
            80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Justified Press", "If your opponent's Active Pokémon is a Darkness Pokémon, this attack does 30 more damage.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(
                            new Branch.Case(new For(new HasType(Type.DARKNESS), new OpponentActive()), new Literal(50))
                    ), new Literal(20)), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-133 - Pikipek
     */
    public static final PokemonCard PIKIPEK = PokemonCard.basic(
            "A3-133", "Pikipek", "It pecks at trees with its hard beak. You can get some idea of its mood or condition from the rhythm of its pecking.",
            50, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Peck", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-134 - Trumbeak
     */
    public static final PokemonCard TRUMBEAK = PokemonCard.evolution(
            "A3-134", "Trumbeak", "It can bend the tip of its beak to produce over a hundred different cries at will.",
            1, "Pikipek", 80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Glide", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-135 - Toucannon
     */
    public static final PokemonCard TOUCANNON = PokemonCard.evolution(
            "A3-135", "Toucannon", "They smack beaks with others of their kind to communicate. The strength and number of hits tell each other how they feel.",
            2, "Trumbeak", 140, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Drill Peck", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A3-136 - Yungoos
     */
    public static final PokemonCard YUNGOOS = PokemonCard.basic(
            "A3-136", "Yungoos", "Its stomach fills most of its torso. It wanders the same path every day, searching for fresh food.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-137 - Gumshoos
     */
    public static final PokemonCard GUMSHOOS = PokemonCard.evolution(
            "A3-137", "Gumshoos", "Once it finds signs of prey, it will patiently stake out the location, waiting until the sun goes down.",
            1, "Yungoos", 100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Headbang", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-138 - Stufful
     */
    public static final PokemonCard STUFFUL = PokemonCard.basic(
            "A3-138", "Stufful", "Its fluffy fur is a delight to pet, but carelessly reaching out to touch this Pokémon could result in painful retaliation.",
            70, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Hammer In", "", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-139 - Bewear
     */
    public static final PokemonCard BEWEAR = PokemonCard.evolution(
            "A3-139", "Bewear", "Once it accepts you as a friend, it tries to show its affection with a hug. Letting it do that is dangerous—it could easily shatter your bones.",
            1, "Stufful", 130, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Tantrum", "This Pokémon is now Confused.", EnergyCost.of(Type.COLORLESS, 4), new Attempt(List.of(
                    new DealDamage(new Literal(130), new OpponentActive()),
                    new AddStatus(new ConfusionStatus(), new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-140 - Oranguru
     */
    public static final PokemonCard ORANGURU = PokemonCard.basic(
            "A3-140", "Oranguru", "People used to mistake Oranguru for a human when they saw it issue command after command to the other Pokémon of the forest.",
            100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Primate's Trap", "During your opponent's next turn, attacks used by the Defending Pokémon cost 1 Colorless more, and its Retreat Cost is 1 Colorless more.",
                    EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive()),
                    new IncreaseAttackCost(new Literal(1), new OpponentActive(), new Literal(1)),
                    new IncreaseRetreatCost(new Literal(1), new OpponentActive(), new Literal(1))))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING);

    /**
     * A3-141 - Komala
     */
    public static final PokemonCard KOMALA = PokemonCard.basic(
            "A3-141", "Komala", "Komala spends its entire life sleeping. It feeds on leaves that contain a potent poison only Komala can break down.",
            90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Rolling Tackle", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withAbility(new PassiveAbility("Comatose",
            "As long as this Pokémon is in the Active Spot, whenever you attach an Energy from your Energy Zone to it, it is now Asleep.",
            List.of(new Trigger(EnergyAttached.class,
                    new And<>(new EventConcerns(new Self()), new And<>(new EnergyFromZone(), new For(new IsActive(), new Self()))),
                    new Attempt(List.of(new AddStatus(new SleepStatus(), new Self())))))))
            .withWeakness(Type.FIGHTING);

    /** A3-177 · Pikipek (alternate art of A3-133) */
    public static final PokemonCard PIKIPEK_IR = PIKIPEK.withId("A3-177", CardRarity.ILLUSTRATION_RARE);
    /** A3-178 · Bewear (alternate art of A3-139) */
    public static final PokemonCard BEWEAR_IR = BEWEAR.withId("A3-178", CardRarity.ILLUSTRATION_RARE);
    /** A3-179 · Komala (alternate art of A3-141) */
    public static final PokemonCard KOMALA_IR = KOMALA.withId("A3-179", CardRarity.ILLUSTRATION_RARE);
    /**
     * A3-228 · Jigglypuff (only printed as a shiny so far). The base card will be added
     * later; when it is, refactor this into a {@code withId(...)} of it.
     */
    public static final PokemonCard JIGGLYPUFF = PokemonCard.basic(
            "A3-228", "Jigglypuff", "When its huge eyes waver, it sings a mysteriously soothing melody that lulls its enemies to sleep.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Rollout", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                            new DealDamage(new Literal(20), new OpponentActive()))))
            ), CardRarity.SHINY_RARE
    ).withWeakness(Type.FIGHTING);
    /**
     * A3-229 · Wigglytuff (only printed as a shiny so far). The base card will be added
     * later; when it is, refactor this into a {@code withId(...)} of it.
     */
    public static final PokemonCard WIGGLYTUFF = PokemonCard.evolution(
            "A3-229", "Wigglytuff", "It has a very fine fur. Take care not to make it angry, or it may inflate steadily and hit with a body slam.",
            1, "Jigglypuff", 100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Gentle Slap", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                            new DealDamage(new Literal(50), new OpponentActive()))))
            ), CardRarity.SHINY_RARE
    ).withWeakness(Type.FIGHTING).withAbility(new ActivatedAbility(
            "Comforting Song",
            "Once during your turn, you may heal 20 damage from your Active Pokémon.",
            new PlainAction("", new Attempt(List.of(
                    new HealDamage(new Literal(20), new AttackerActive())))),
            true,
            new For(new IsDamaged(), new AttackerActive())
    ));
    /** A3-237 · Wigglytuff ex (shiny of A1-195) */
    public static final PokemonCard WIGGLYTUFF_EX_SUR = com.tcgpocket.pool.A1.Colorless.WIGGLYTUFF_EX.withId("A3-237", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            TAUROS, SKITTY, DELCATTY, FLETCHLING, HAWLUCHA, PIKIPEK, TRUMBEAK, TOUCANNON,
            YUNGOOS, GUMSHOOS, STUFFUL, BEWEAR, ORANGURU, KOMALA,
            PIKIPEK_IR, BEWEAR_IR, KOMALA_IR,
            JIGGLYPUFF, WIGGLYTUFF_EX_SUR);

    private Colorless() {
    }
}
