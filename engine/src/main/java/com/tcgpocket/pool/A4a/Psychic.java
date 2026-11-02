package com.tcgpocket.pool.A4a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.NoRetreatCost;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.All;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.condition.OwnerHasInPlay;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DelayedDamage;
import com.tcgpocket.effect.DiscardAllEnergy;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.effect.RevealHand;
import com.tcgpocket.effect.YouMay;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.CurrentHP;
import com.tcgpocket.number.Literal;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.EventSource;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentSide;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.Knockout;
import com.tcgpocket.trigger.PokemonBenched;
import com.tcgpocket.trigger.Trigger;

import java.util.List;
import java.util.Optional;

/**
 * Secluded Springs (A4a): the Psychic cards, in printed set order.
 */
public final class Psychic {

    /**
     * A4a-032 - Misdreavus
     */
    public static final PokemonCard MISDREAVUS = PokemonCard.basic(
            "A4a-032", "Misdreavus", "This Pokémon startles people in the middle of the night. It gathers fear as its energy.",
            50, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Will-O-Wisp", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS).withAbility(new PassiveAbility("Infiltrating Inspection",
            "Once during your turn, when you put this Pokémon from your hand onto your Bench, you may have your opponent reveal their hand.",
            List.of(new Trigger(PokemonBenched.class,
                    new EventConcerns(new Self()),
                    new Attempt(List.of(new YouMay("Infiltrating Inspection: have your opponent reveal their hand?",
                            new RevealHand(new OpponentSide()))))))));

    /**
     * A4a-033 - Mismagius — the curse ends if the Pokémon leaves the Active Spot, and survives Mismagius leaving play.
     */
    public static final PokemonCard MISMAGIUS = PokemonCard.evolution(
            "A4a-033", "Mismagius", "Its cry sounds like an incantation. It is said the cry may rarely be imbued with happiness-giving power.",
            1, "Misdreavus", 100, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Cursed Prose", "At the end of your opponent's next turn, do 90 damage to the Defending Pokémon.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DelayedDamage(new Literal(90), new OpponentActive(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A4a-034 - Galarian Corsola
     */
    public static final PokemonCard GALARIAN_CORSOLA = PokemonCard.basic(
            "A4a-034", "Galarian Corsola", "Watch your step when wandering areas oceans once covered. What looks like a stone could be this Pokémon, and it will curse you if you kick it.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A4a-035 - Galarian Cursola
     */
    public static final PokemonCard GALARIAN_CURSOLA = PokemonCard.evolution(
            "A4a-035", "Galarian Cursola", "Its shell is overflowing with its heightened otherworldly energy. The ectoplasm serves as protection for this Pokémon's core spirit.",
            1, "Galarian Corsola", 80, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Spooky Shot", "", EnergyCost.of(Type.PSYCHIC, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS).withAbility(new PassiveAbility("Perish Body",
            "If this Pokémon is in the Active Spot and is Knocked Out by damage from an attack from your opponent's Pokémon, flip a coin. If heads, the Attacking Pokémon is Knocked Out.",
            List.of(new Trigger(Knockout.class,
                    new All<>(List.of(
                            new EventConcerns(new Self()),
                            new For(new IsActive(), new Self()),
                            new For(new Always<>(), new EventSource()),
                            new Not<>(new EventConcerns(new EventSource())))),
                    new Attempt(List.of(
                            new FlipN(new Literal(1)),
                            new ConditionalEffect(new LastCoinTossHeads(),
                                    new PlaceDamage(new CurrentHP(new EventSource()), new EventSource()))))))));

    /**
     * A4a-036 - Latias
     */
    public static final PokemonCard LATIAS = PokemonCard.basic(
            "A4a-036", "Latias", "It communicates using telepathy. Its body is covered in down that refracts light to make it invisible.",
            90, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Crossing Flights", "If Latios is on your Bench, this attack does 20 more damage.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Branch(List.of(new Branch.Case(new GreaterThan(new CountCards(new AttackerSide(), Zone.BENCH, Optional.of(new IsSpecies("Latios"))), new Literal(0)), new Literal(40))), new Literal(20)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS);

    /**
     * A4a-037 - Latios
     */
    public static final PokemonCard LATIOS = PokemonCard.basic(
            "A4a-037", "Latios", "It understands human speech and is highly intelligent. It is a tender Pokémon that dislikes fighting.",
            110, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Luster Purge", "Discard all Energy from this Pokémon.", EnergyCost.of(Type.PSYCHIC, 3), new Attempt(List.of(
                    new DealDamage(new Literal(120), new OpponentActive()),
                    new DiscardAllEnergy(new Self())))
            )), CardRarity.RARE
    ).withWeakness(Type.DARKNESS).withAbility(new PassiveAbility("Fantastical Floating",
            "If you have Latias in play, this Pokémon has no Retreat Cost.",
            new NoRetreatCost(new OwnerHasInPlay(new IsSpecies("Latias")))));

    /**
     * A4a-038 - Frillish
     */
    public static final PokemonCard FRILLISH = PokemonCard.basic(
            "A4a-038", "Frillish", "It envelops its prey in its veillike arms and draws it down to the deeps, five miles below the ocean's surface.",
            60, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Absorb", "Heal 10 damage from this Pokémon.", EnergyCost.of(Type.PSYCHIC, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new HealDamage(new Literal(10), new Self())))
            )), CardRarity.COMMON
    ).withWeakness(Type.DARKNESS);

    /**
     * A4a-039 - Jellicent
     */
    public static final PokemonCard JELLICENT = PokemonCard.evolution(
            "A4a-039", "Jellicent", "Most of this Pokémon's body composition is identical to sea water. It makes sunken ships its lair.",
            1, "Frillish", 110, Type.PSYCHIC, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Devour Soul", "", EnergyCost.of(Type.PSYCHIC, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(80), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.DARKNESS);

    /** A4a-075 · Latios (alternate art of A4a-037) */
    public static final PokemonCard LATIOS_IR = LATIOS.withId("A4a-075", CardRarity.ILLUSTRATION_RARE);

    /** A4a-102 · Mew ex (shiny of A1a-032) */
    public static final PokemonCard MEW_EX_SUR = com.tcgpocket.pool.A1a.Psychic.MEW_EX.withId("A4a-102", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            MISDREAVUS, MISMAGIUS, GALARIAN_CORSOLA, GALARIAN_CURSOLA, LATIAS, LATIOS,
            FRILLISH, JELLICENT, LATIOS_IR, MEW_EX_SUR);

    private Psychic() {
    }
}
