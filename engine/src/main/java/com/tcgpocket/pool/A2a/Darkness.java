package com.tcgpocket.pool.A2a;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.For;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Triumphant Light — Darkness.
 */
public final class Darkness {

    /**
     * A2a-048 - Zubat
     */
    public static final PokemonCard ZUBAT = PokemonCard.basic(
            "A2a-048", "Zubat", "It emits ultrasonic waves from its mouth to check its surroundings. Even in tight caves, Zubat flies around with skill.",
            50, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2a-049 - Golbat
     */
    public static final PokemonCard GOLBAT = PokemonCard.evolution(
            "A2a-049", "Golbat", "It loves to drink other creatures' blood. It's said that if it finds others of its kind going hungry, it sometimes shares the blood it's gathered.",
            1, "Zubat", 80, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Bite", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2a-050 - Crobat
     */
    public static final PokemonCard CROBAT = PokemonCard.evolution(
            "A2a-050", "Crobat", "Both of its legs have turned into wings. Without a sound, Crobat flies swiftly toward its prey and sinks its fangs into the nape of its target's neck.",
            2, "Golbat", 110, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 0), List.of(new Action(
                    "Darkness Fang", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING).withAbility(new ActivatedAbility("Cunning Link",
            "Once during your turn, if you have Arceus or Arceus ex in play, you may do 30 damage to your opponent's Active Pokémon.",
            new PlainAction("", new Attempt(List.of(new PlaceDamage(new Literal(30), new OpponentActive())))),
            true, new For(Links.ARCEUS_IN_PLAY, new Self())));

    /**
     * A2a-051 - Croagunk
     */
    public static final PokemonCard CROAGUNK = PokemonCard.basic(
            "A2a-051", "Croagunk", "Inflating its poison sacs, it fills the area with an odd sound and hits flinching opponents with a poison jab.",
            60, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Beat", "", EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2a-052 - Toxicroak
     */
    public static final PokemonCard TOXICROAK = PokemonCard.evolution(
            "A2a-052", "Toxicroak", "Swaying and dodging the attacks of its foes, it weaves its flexible body in close, then lunges out with its poisonous claws.",
            1, "Croagunk", 100, Type.DARKNESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Toxic", "Your opponent's Active Pokémon is now Poisoned. Do 20 damage to this Pokémon instead of the usual amount for this Special Condition.",
                    EnergyCost.of(Type.DARKNESS, 1), new Attempt(List.of(
                    new AddStatus(new PoisonStatus(20), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    static final List<PokemonCard> CARDS = List.of(ZUBAT, GOLBAT, CROBAT, CROAGUNK, TOXICROAK);

    private Darkness() {
    }
}
