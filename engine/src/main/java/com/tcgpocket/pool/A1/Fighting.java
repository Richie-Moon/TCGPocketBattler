package com.tcgpocket.pool.A1;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsDamaged;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.*;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Genetic Apex — Fighting.
 */
public final class Fighting {

    /**
     * A1-137 - Sandshrew
     */
    public static final PokemonCard SANDSHREW = PokemonCard.basic(
                    "A1-137", "Sandshrew", "It loves to bathe in the grit of dry, sandy areas. By sand bathing, the Pokémon rids itself of dirt and moisture clinging to its body.",
                    70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Scratch", "", EnergyCost.of(Type.FIGHTING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(10), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.GRASS);

    /**
     * A1-138 - Sandslash
     */
    public static final PokemonCard SANDSLASH = PokemonCard.evolution(
                    "A1-138", "Sandslash", "The drier the area Sandslash lives in, the harder and smoother the Pokémon's spikes will feel when touched.",
                    1, "Sandshrew", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Slash", "", EnergyCost.of(Type.FIGHTING, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.GRASS);

    /**
     * A1-139 - Diglett
     */
    public static final PokemonCard DIGLETT = PokemonCard.basic(
                    "A1-139", "Diglett", "It lives about one yard underground, where it feeds on plant roots. It sometimes appears aboveground.",
                    50, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Mud-Slap", "", EnergyCost.of(Type.FIGHTING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.GRASS);

    /**
     * A1-140 - Dugtrio
     *
     * <p><b>Narrowed.</b> Dig prevents the damage but not the effects of
     * attacks: {@link PreventDamage} only installs {@code PREVENT_DAMAGE}, and
     * nothing yet blocks an attack's effects (statuses, energy discards) on a
     * Pokemon. A {@code PREVENT_EFFECTS} modifier kind, checked wherever an
     * attack's effect lands on the Defending Pokemon, would fix it.
     */
    public static final PokemonCard DUGTRIO = PokemonCard.evolution(
                    "A1-140", "Dugtrio", "Its three heads bob separately up and down to loosen the soil nearby, making it easier for it to burrow.",
                    1, "Diglett", 70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Dig", "Flip a coin. If heads, during your opponent's next turn, prevent all damage from—and effects of—attacks done to this Pokémon.", EnergyCost.of(Type.FIGHTING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive()),
                                    new FlipN(new Literal(1)),
                                    new ConditionalEffect(new LastCoinTossHeads(), new PreventDamage(new Self(), new Literal(1)))
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.GRASS);

    /**
     * A1-141 - Mankey
     */
    public static final PokemonCard MANKEY = PokemonCard.basic(
                    "A1-141", "Mankey", "It lives in groups in the treetops. If it loses sight of its group, it becomes infuriated by its loneliness.",
                    60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Low Kick", "", EnergyCost.of(Type.FIGHTING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.PSYCHIC);

    /**
     * A1-142 - Primeape
     */
    public static final PokemonCard PRIMEAPE = PokemonCard.evolution(
                    "A1-142", "Primeape", "It becomes wildly furious if it even senses someone looking at it. It chases anyone that meets its glare.",
                    1, "Mankey", 90, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Fight Back", "If this Pokémon has damage on it, this attack does 60 more damage.", EnergyCost.of(Type.FIGHTING, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Branch(List.of(
                                            new Branch.Case(new For(new IsDamaged(), new Self()), new Literal(100))
                                    ), new Literal(40)), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.PSYCHIC);

    /**
     * A1-143 - Machop
     */
    public static final PokemonCard MACHOP = PokemonCard.basic(
                    "A1-143", "Machop", "Its whole body is composed of muscles. Even though it's the size of a human child, it can hurl 100 grown-ups.",
                    70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Knuckle Punch", "", EnergyCost.of(Type.FIGHTING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.PSYCHIC);

    /**
     * A1-144 - Machoke
     */
    public static final PokemonCard MACHOKE = PokemonCard.evolution(
                    "A1-144", "Machoke", "Its muscular body is so powerful, it must wear a power-save belt to be able to regulate its motions.",
                    1, "Machop", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Strength", "", EnergyCost.of(Type.FIGHTING, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(50), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.PSYCHIC);

    /**
     * A1-145 - Machamp
     */
    public static final PokemonCard MACHAMP = PokemonCard.evolution(
                    "A1-145", "Machamp", "It quickly swings its four arms to rock its opponents with ceaseless punches and chops from all angles.",
                    2, "Machoke", 150, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Seismic Toss", "", EnergyCost.of(Type.FIGHTING, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(100), new OpponentActive())
                            )))), CardRarity.RARE)
            .withWeakness(Type.PSYCHIC);

    /**
     * A1-146 - Machamp ex
     */
    public static final PokemonCard MACHAMP_EX = PokemonCard.evolution(
                    "A1-146", "Machamp ex", "",
                    2, "Machoke", 180, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Mega Punch", "", EnergyCost.of(Type.FIGHTING, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(120), new OpponentActive())
                            )))), CardRarity.DOUBLE_RARE)
            .withWeakness(Type.PSYCHIC)
            .withTags(CardTag.EX);

    /**
     * A1-147 - Geodude
     */
    public static final PokemonCard GEODUDE = PokemonCard.basic(
                    "A1-147", "Geodude", "Geodude that have lived a long life have had all their edges smoothed out until they're totally round. They also have a calm, quiet disposition.",
                    70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Tackle", "", EnergyCost.of(Type.FIGHTING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.GRASS);

    /**
     * A1-148 - Graveler
     */
    public static final PokemonCard GRAVELER = PokemonCard.evolution(
                    "A1-148", "Graveler", "It climbs up cliffs as it heads toward the peak of a mountain. As soon as it reaches the summit, it rolls back down the way it came.",
                    1, "Geodude", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Rollout", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.GRASS);

    /**
     * A1-149 - Golem
     */
    public static final PokemonCard GOLEM = PokemonCard.evolution(
                    "A1-149", "Golem", "When Golem grow old, they stop shedding their shells. Those that have lived a long, long time have shells green with moss.",
                    2, "Graveler", 160, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                            "Double-Edge", "This Pokémon also does 50 damage to itself.", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(150), new OpponentActive()),
                                    new DealDamage(new Literal(50), new Self())
                            )))), CardRarity.RARE)
            .withWeakness(Type.GRASS);

    /**
     * A1-150 - Onix
     */
    public static final PokemonCard ONIX = PokemonCard.basic(
                    "A1-150", "Onix", "As it digs through the ground, it absorbs many hard objects. This is what makes its body so solid.",
                    110, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                            "Land Crush", "", EnergyCost.of(Type.FIGHTING, 3),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.GRASS);

    /**
     * A1-151 - Cubone
     */
    public static final PokemonCard CUBONE = PokemonCard.basic(
                    "A1-151", "Cubone", "When the memory of its departed mother brings it to tears, its cries echo mournfully within the skull it wears on its head.",
                    60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Growl", "During your opponent's next turn, attacks used by the Defending Pokémon do -20 damage.", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new ReduceDamageDealt(new Literal(20), new OpponentActive(), new Literal(1))
                            )))), CardRarity.COMMON)
            .withWeakness(Type.GRASS);

    /**
     * A1-152 - Marowak
     */
    public static final PokemonCard MAROWAK = PokemonCard.evolution(
                    "A1-152", "Marowak", "This Pokémon overcame its sorrow to evolve a sturdy new body. Marowak faces its opponents bravely, using a bone as a weapon.",
                    1, "Cubone", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Bone Beatdown", "", EnergyCost.of(Type.FIGHTING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.GRASS);

    /**
     * A1-153 - Marowak ex
     */
    public static final PokemonCard MAROWAK_EX = PokemonCard.evolution(
                    "A1-153", "Marowak ex", "",
                    1, "Cubone", 140, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Bonemerang", "Flip 2 coins. This attack does 80 damage for each heads.", EnergyCost.of(Type.FIGHTING, 2),
                            new Attempt(List.of(
                                    new FlipN(new Literal(2)),
                                    new DealDamage(new Product(new Literal(80), new NumberHeads()), new OpponentActive())
                            )))), CardRarity.DOUBLE_RARE)
            .withWeakness(Type.GRASS)
            .withTags(CardTag.EX);

    /**
     * A1-154 - Hitmonlee
     */
    public static final PokemonCard HITMONLEE = PokemonCard.basic(
                    "A1-154", "Hitmonlee", "This amazing Pokémon has an awesome sense of balance. It can kick in succession from any position.",
                    80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Stretch Kick", "This attack does 30 damage to 1 of your opponent's Benched Pokémon.", EnergyCost.of(Type.FIGHTING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new ChosenFrom(new OpponentBench(), new AttackerSide(), "Select a Benched Pokémon"))
                            )))), CardRarity.COMMON)
            .withWeakness(Type.PSYCHIC);

    /**
     * A1-155 - Hitmonchan
     */
    public static final PokemonCard HITMONCHAN = PokemonCard.basic(
                    "A1-155", "Hitmonchan", "Its punches slice the air. They are launched at such high speed, even a slight graze could cause a burn.",
                    80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Jab", "", EnergyCost.of(Type.FIGHTING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.PSYCHIC);

    /**
     * A1-156 - Rhyhorn
     */
    public static final PokemonCard RHYHORN = PokemonCard.basic(
                    "A1-156", "Rhyhorn", "Strong, but not too bright, this Pokémon can shatter even a skyscraper with its charging tackles.",
                    80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Tackle", "", EnergyCost.of(Type.FIGHTING, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(60), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.GRASS);

    /**
     * A1-157 - Rhydon
     */
    public static final PokemonCard RHYDON = PokemonCard.evolution(
                    "A1-157", "Rhydon", "It begins walking on its hind legs after evolution. It can punch holes through boulders with its horn.",
                    1, "Rhyhorn", 120, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                            "Horn Drill", "", EnergyCost.of(Type.FIGHTING, 3, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(100), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.GRASS);

    /**
     * A1-158 - Kabuto
     */
    public static final PokemonCard KABUTO = PokemonCard.evolution(
                    "A1-158", "Kabuto", "This species is almost entirely extinct. Kabuto molt every three days, making their shells harder and harder.",
                    1, "Dome Fossil", 90, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Shell Attack", "", EnergyCost.of(Type.FIGHTING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.GRASS);

    /**
     * A1-159 - Kabutops
     */
    public static final PokemonCard KABUTOPS = PokemonCard.evolution(
                    "A1-159", "Kabutops", "Kabutops slices its prey apart and sucks out the fluids. The discarded body parts become food for other Pokémon.",
                    2, "Kabuto", 140, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Leech Life", "Heal from this Pokémon the same amount of damage you did to your opponent's Active Pokémon.", EnergyCost.of(Type.FIGHTING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(50), new OpponentActive()),
                                    new HealDamage(new DamageDone(), new Self())
                            )))), CardRarity.RARE)
            .withWeakness(Type.GRASS);

    /**
     * A1-160 - Mienfoo
     */
    public static final PokemonCard MIENFOO = PokemonCard.basic(
                    "A1-160", "Mienfoo", "In one minute, a well-trained Mienfoo can chop with its arms more than 100 times.",
                    60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Pound", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(20), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.PSYCHIC);

    /**
     * A1-161 - Mienshao
     */
    public static final PokemonCard MIENSHAO = PokemonCard.evolution(
                    "A1-161", "Mienshao", "When Mienshao comes across a truly challenging opponent, it will lighten itself by biting off the fur on its arms.",
                    1, "Mienfoo", 80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Spiral Kick", "", EnergyCost.of(Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.PSYCHIC);

    /**
     * A1-162 - Clobbopus
     */
    public static final PokemonCard CLOBBOPUS = PokemonCard.basic(
                    "A1-162", "Clobbopus", "It's very curious, but its means of investigating things is to try to punch them with its tentacles. The search for food is what brings it onto land.",
                    80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                            "Knuckle Punch", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(30), new OpponentActive())
                            )))), CardRarity.COMMON)
            .withWeakness(Type.PSYCHIC);

    /**
     * A1-163 - Grapploct
     */
    public static final PokemonCard GRAPPLOCT = PokemonCard.evolution(
                    "A1-163", "Grapploct", "A body made up of nothing but muscle makes the grappling moves this Pokémon performs with its tentacles tremendously powerful.",
                    1, "Clobbopus", 130, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Knock Back", "Switch out your opponent's Active Pokémon to the Bench. (Your opponent chooses the new Active Pokémon.)", EnergyCost.of(Type.FIGHTING, 2, Type.COLORLESS, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(70), new OpponentActive()),
                                    new SwitchActive(new OpponentSide())
                            )))), CardRarity.UNCOMMON)
            .withWeakness(Type.PSYCHIC);

    static final List<PokemonCard> CARDS = List.of(
            SANDSHREW, SANDSLASH, DIGLETT, DUGTRIO, MANKEY, PRIMEAPE, MACHOP, MACHOKE, MACHAMP, MACHAMP_EX,
            GEODUDE, GRAVELER, GOLEM, ONIX, CUBONE, MAROWAK, MAROWAK_EX, HITMONLEE, HITMONCHAN, RHYHORN, RHYDON, 
            KABUTO, KABUTOPS, MIENFOO, MIENSHAO, CLOBBOPUS, GRAPPLOCT);

    private Fighting() {
    }
}
