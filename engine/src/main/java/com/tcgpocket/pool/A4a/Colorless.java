package com.tcgpocket.pool.A4a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.condition.IsDamaged;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.Fail;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.HealDamage;
import com.tcgpocket.effect.IncreaseDamage;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.NumberHeads;
import com.tcgpocket.number.Product;
import com.tcgpocket.number.Sum;
import com.tcgpocket.status.SleepStatus;
import com.tcgpocket.target.AttackerBench;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.ChosenFrom;
import com.tcgpocket.target.EventSource;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentAll;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.DamageDealt;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * Secluded Springs (A4a): the Colorless cards, in printed set order.
 */
public final class Colorless {

    /** Longer than any game; leaving the Active Spot is what actually ends it. */
    private static final int UNTIL_IT_LEAVES_THE_ACTIVE_SPOT = 1000;

    /**
     * A4a-056 - Farfetch'd
     */
    public static final PokemonCard FARFETCHD = PokemonCard.basic(
            "A4a-056", "Farfetch'd", "The stalk this Pokémon carries in its wings serves as a sword to cut down opponents. In a dire situation, the stalk can also serve as food.",
            70, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Leek Slam", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4a-057 - Lickitung
     */
    public static final PokemonCard LICKITUNG = PokemonCard.basic(
            "A4a-057", "Lickitung", "If this Pokémon's sticky saliva gets on you and you don't clean it off, an intense itch will set in. The itch won't go away, either.",
            80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Stretch Tongue", "This attack does 30 damage to 1 of your opponent's Pokémon.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"))))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4a-058 - Lickilicky
     */
    public static final PokemonCard LICKILICKY = PokemonCard.evolution(
            "A4a-058", "Lickilicky", "Lickilicky's strange tongue can stretch to many times the length of its body. No one has figured out how Lickilicky's tongue can stretch so far.",
            1, "Lickitung", 130, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Stretch Tongue", "This attack does 60 damage to 1 of your opponent's Pokémon.", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(60), new ChosenFrom(new OpponentAll(), new AttackerSide(), "Select a target:"))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4a-059 - Igglybuff
     */
    public static final PokemonCard IGGLYBUFF = PokemonCard.basic(
            "A4a-059", "Igglybuff", "Its body has a faintly sweet scent and is bouncy and soft. If it bounces even once, it cannot stop.",
            30, Type.COLORLESS, EnergyCost.free(), List.of(new Action(
                    "Sleepy Lullaby", "Your opponent's Active Pokémon is now Asleep.", EnergyCost.free(), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new AddStatus(new SleepStatus(), new OpponentActive())))
            )), CardRarity.RARE
    );

    /**
     * A4a-060 - Teddiursa
     */
    public static final PokemonCard TEDDIURSA = PokemonCard.basic(
            "A4a-060", "Teddiursa", "Its paws are soaked in oodles of honey. When nervous, Teddiursa will lick its paws and soon have a smile back on its face.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Surprise Attack", "Flip a coin. If tails, this attack does nothing.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new Fail()),
                    new DealDamage(new Literal(40), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4a-061 - Ursaring
     */
    public static final PokemonCard URSARING = PokemonCard.evolution(
            "A4a-061", "Ursaring", "It usually wears a hardened expression, but when it's licking up honey—which it loves—the joy it feels will cause it to break into a wide grin.",
            1, "Teddiursa", 130, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Claw Slash", "", EnergyCost.of(Type.COLORLESS, 4), new Attempt(List.of(
                    new DealDamage(new Literal(110), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4a-062 - Miltank — an attack that stacks: each use leaves a modifier that goes when it leaves the Active Spot.
     *
     * <p><b>Narrowed.</b> The boost is {@code IncreaseDamage}, on every attack the Pokémon uses, not only Rolling
     * Frenzy. Miltank has no other, so this only differs for one that copies it; an attack-named damage modifier
     * would fix that.
     */
    public static final PokemonCard MILTANK = PokemonCard.basic(
            "A4a-062", "Miltank", "Miltank produces highly nutritious milk, so it's been supporting the lives of people and other Pokémon since ancient times.",
            110, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Rolling Frenzy", "Until this Pokémon leaves the Active Spot, this Pokémon's Rolling Frenzy attack does +30 damage. This effect stacks.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive()),
                    new IncreaseDamage(new Literal(30), new Self(), new Literal(UNTIL_IT_LEAVES_THE_ACTIVE_SPOT))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A4a-063 - Azurill
     */
    public static final PokemonCard AZURILL = PokemonCard.basic(
            "A4a-063", "Azurill", "Its tail bounces like a rubber ball. It flings that tail around to fight opponents bigger than itself.",
            30, Type.COLORLESS, EnergyCost.free(), List.of(new Action(
                    "Squishy Healing", "Heal 50 damage from 1 of your Benched Pokémon.", EnergyCost.free(), new Attempt(List.of(
                    new HealDamage(new Literal(50), new ChosenFrom(new AttackerBench(), new AttackerSide(), new IsDamaged(), "Choose a Benched Pokémon to heal."))))
            )), CardRarity.RARE
    );

    /**
     * A4a-064 - Swablu
     */
    public static final PokemonCard SWABLU = PokemonCard.basic(
            "A4a-064", "Swablu", "It constantly grooms its cotton-like wings. It takes a shower to clean itself if it becomes dirty.",
            50, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Peck", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /**
     * A4a-065 - Zangoose
     */
    public static final PokemonCard ZANGOOSE = PokemonCard.basic(
            "A4a-065", "Zangoose", "It's Seviper's archrival. To threaten those it encounters, it fans out the claws on its front paws.",
            80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Slash", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING).withAbility(new PassiveAbility("Counterattack",
            "If this Pokémon is in the Active Spot and is damaged by an attack from your opponent's Pokémon, do 20 damage to the Attacking Pokémon.",
            List.of(new Trigger(DamageDealt.class,
                    new And<>(new EventConcerns(new Self()), new For(new IsActive(), new Self())),
                    new Attempt(List.of(new PlaceDamage(new Literal(20), new EventSource())))))));

    /**
     * A4a-066 - Fletchling
     */
    public static final PokemonCard FLETCHLING = PokemonCard.basic(
            "A4a-066", "Fletchling", "This Pokémon is normally calm, but once it enters battle, its hormonal balance changes and it becomes aggressive.",
            50, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Quick Attack", "Flip a coin. If heads, this attack does 30 more damage.", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new DealDamage(new Sum(new Literal(10), new Product(new NumberHeads(), new Literal(30))), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.LIGHTNING);

    /** A4a-077 · Azurill (alternate art of A4a-063) */
    public static final PokemonCard AZURILL_IR = AZURILL.withId("A4a-077", CardRarity.ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            FARFETCHD, LICKITUNG, LICKILICKY, IGGLYBUFF, TEDDIURSA, URSARING,
            MILTANK, AZURILL, SWABLU, ZANGOOSE, FLETCHLING, AZURILL_IR);

    private Colorless() {
    }
}
