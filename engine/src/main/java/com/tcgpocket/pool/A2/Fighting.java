package com.tcgpocket.pool.A2;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CurrentHP;
import com.tcgpocket.number.EnergyOn;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.target.*;
import com.tcgpocket.trigger.AttackDeclared;
import com.tcgpocket.trigger.Trigger;
import com.tcgpocket.trigger.TurnStart;

import java.util.List;

/**
 * Space-Time Smackdown — Fighting.
 */
public final class Fighting {

    /**
     * A2-080 - Rhyhorn
     */
    public static final PokemonCard RHYHORN = PokemonCard.basic(
            "A2-080", "Rhyhorn", "Strong, but not too bright, this Pokémon can shatter even a skyscraper with its charging tackles.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Horn Attack", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2-081 - Rhydon
     */
    public static final PokemonCard RHYDON = PokemonCard.evolution(
            "A2-081", "Rhydon", "It begins walking on its hind legs after evolution. It can punch holes through boulders with its horn.",
            1, "Rhyhorn", 110, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Wrack Down", "", EnergyCost.of(Type.FIGHTING, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2-082 - Rhyperior
     */
    public static final PokemonCard RHYPERIOR = PokemonCard.evolution(
            "A2-082", "Rhyperior", "It can load up to three projectiles per arm into the holes in its hands. What launches out of those holes could be either rocks or Roggenrola.",
            2, "Rhydon", 160, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Mountain Swing", "Discard the top 3 cards of your deck.", EnergyCost.of(Type.FIGHTING, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(150), new OpponentActive()),
                    new DiscardTopOfDeck(new Literal(3), new AttackerSide())))
            )), CardRarity.RARE
    ).withWeakness(Type.GRASS);

    /**
     * A2-083 - Gligar
     */
    public static final PokemonCard GLIGAR = PokemonCard.basic(
            "A2-083", "Gligar", "It usually clings to cliffs. When it spots its prey, it spreads its wings and glides down to attack.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Pierce", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2-084 - Gliscor
     */
    public static final PokemonCard GLISCOR = PokemonCard.evolution(
            "A2-084", "Gliscor", "It observes prey while hanging inverted from branches. When the chance presents itself, it swoops!",
            1, "Gligar", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Acrobatics", "Flip 2 coins. This attack does 20 more damage for each heads.", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new FlipN(new Literal(2)),
                    new DealDamage(new Sum(
                            new Literal(20),
                            new Product(new NumberHeads(), new Literal(20))), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2-085 - Hitmontop
     */
    public static final PokemonCard HITMONTOP = PokemonCard.basic(
            "A2-085", "Hitmontop", "It launches kicks while spinning. If it spins at high speed, it may bore its way into the ground.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Spinning Attack", "", EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A2-086 - Nosepass
     */
    public static final PokemonCard NOSEPASS = PokemonCard.basic(
            "A2-086", "Nosepass", "It moves less than an inch a year, but when it's in a jam, it will spin and drill down into the ground in a split second.",
            70, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Ram", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2-087 - Regirock — Exoskeleton: renewed each opponent turn like A1-182
     * Melmetal's Hard Coat. Exact, since a Basic only enters play on its
     * owner's turn.
     */
    public static final PokemonCard REGIROCK = PokemonCard.basic(
            "A2-087", "Regirock", "Every bit of Regirock's body is made of stone. As parts of its body erode, this Pokémon sticks rocks to itself to repair what's been lost.",
            120, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Boulder Crush", "", EnergyCost.of(Type.FIGHTING, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(100), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withAbility(new PassiveAbility("Exoskeleton", "This Pokémon takes -20 damage from attacks.", List.of(
            new Trigger(TurnStart.class, new Not<>(new EventSideIs(new SelfSide())),
                    new Attempt(List.of(new ReduceDamageTaken(new Literal(20), new Self(), new Literal(0))))))))
            .withWeakness(Type.GRASS);

    /**
     * A2-088 - Cranidos
     */
    public static final PokemonCard CRANIDOS = PokemonCard.evolution(
            "A2-088", "Cranidos", "A primeval Pokémon, it possesses a hard and sturdy skull, lacking any intelligence within.",
            1, "Skull Fossil", 90, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Headbutt", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2-089 - Rampardos — Head Smash: checked straight after the damage,
     * before the engine removes the Knocked Out Pokémon, so 0 HP left is
     * "Knocked Out by damage from this attack".
     */
    public static final PokemonCard RAMPARDOS = PokemonCard.evolution(
            "A2-089", "Rampardos", "In ancient times, people would dig up fossils of this Pokémon and use its skull, which is harder than steel, to make helmets.",
            2, "Cranidos", 150, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Head Smash", "If your opponent's Pokémon is Knocked Out by damage from this attack, this Pokémon also does 50 damage to itself.",
                    EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(130), new OpponentActive()),
                    new ConditionalEffect(new LessThan(new CurrentHP(new OpponentActive()), new Literal(1)),
                            new DealDamage(new Literal(50), new Self()))))
            )), CardRarity.RARE
    ).withWeakness(Type.GRASS);

    /**
     * A2-090 - Wormadam
     */
    public static final PokemonCard WORMADAM = PokemonCard.evolution(
            "A2-090", "Wormadam", "Its appearance changes depending on where it evolved. The materials on hand become a part of its body.",
            1, "Burmy", 120, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Land Crush", "", EnergyCost.of(Type.FIGHTING, 1, Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2-091 - Riolu
     */
    public static final PokemonCard RIOLU = PokemonCard.basic(
            "A2-091", "Riolu", "They communicate with one another using their auras. They are able to run all through the night.",
            60, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Jab", "", EnergyCost.of(Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.PSYCHIC);

    /**
     * A2-092 - Lucario — Fighting Coach: a side bonus stamped as your Fighting
     * Pokémon declares an attack, so it covers whichever of them attacks, and
     * {@code DamageCalculator} confines it to the opponent's Active. Each
     * Lucario stamps its own, so two of them give +40.
     */
    public static final PokemonCard LUCARIO = PokemonCard.evolution(
            "A2-092", "Lucario", "It's said that no foe can remain invisible to Lucario, since it can detect auras—even those of foes it could not otherwise see.",
            1, "Riolu", 100, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Submarine Blow", "", EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.RARE
    ).withAbility(new PassiveAbility("Fighting Coach",
            "Attacks used by your Fighting Pokémon do +20 damage to your opponent's Active Pokémon.", List.of(
            new Trigger(AttackDeclared.class,
                    new And<>(new EventSideIs(new SelfSide()), new For(new HasType(Type.FIGHTING), new AttackerActive())),
                    new Attempt(List.of(new IncreaseSideDamage(new Literal(20), new Literal(0))))))))
            .withWeakness(Type.PSYCHIC);

    /**
     * A2-093 - Hippopotas
     */
    public static final PokemonCard HIPPOPOTAS = PokemonCard.basic(
            "A2-093", "Hippopotas", "It shuts its nostrils tight, then travels through sand as if walking. They form colonies of around 10.",
            80, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Rolling Tackle", "", EnergyCost.of(Type.FIGHTING, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2-094 - Hippowdon
     */
    public static final PokemonCard HIPPOWDON = PokemonCard.evolution(
            "A2-094", "Hippowdon", "It is surprisingly quick to anger. It holds its mouth agape as a display of its strength.",
            1, "Hippopotas", 140, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 4), List.of(new Action(
                    "Earthen Press", "", EnergyCost.of(Type.FIGHTING, 3, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.GRASS);

    /**
     * A2-095 - Gallade ex
     */
    public static final PokemonCard GALLADE_EX = PokemonCard.evolution(
            "A2-095", "Gallade ex", "",
            2, "Kirlia", 170, Type.FIGHTING, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Energized Blade", "This attack does 20 more damage for each Energy attached to your opponent's Active Pokémon.",
                    EnergyCost.of(Type.FIGHTING, 2), new Attempt(List.of(
                    new DealDamage(new Sum(
                            new Literal(70),
                            new Product(new EnergyOn(new OpponentActive()), new Literal(20))), new OpponentActive())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.PSYCHIC).withTags(CardTag.EX);

    static final List<PokemonCard> CARDS = List.of(
            RHYHORN, RHYDON, RHYPERIOR, GLIGAR, GLISCOR, HITMONTOP, NOSEPASS, REGIROCK, CRANIDOS,
            RAMPARDOS, WORMADAM, RIOLU, LUCARIO, HIPPOPOTAS, HIPPOWDON, GALLADE_EX);

    private Fighting() {
    }
}
