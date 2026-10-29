package com.tcgpocket.pool.A2b;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.*;

import java.util.List;

/**
 * Shining Revelry — Colorless.
 */
public final class Colorless {

    /**
     * A2b-058 - Rattata
     */
    public static final PokemonCard RATTATA = PokemonCard.basic(
            "A2b-058", "Rattata", "Its incisors grow continuously throughout its life. If its incisors get too long, this Pokémon becomes unable to eat, and it starves to death.",
            50, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(10), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-059 - Raticate
     */
    public static final PokemonCard RATICATE = PokemonCard.evolution(
            "A2b-059", "Raticate", "People say that it fled from its enemies by using its small webbed hind feet to swim from island to island in Alola.",
            1, "Rattata", 90, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Tackle", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-060 - Jigglypuff
     */
    public static final PokemonCard JIGGLYPUFF = PokemonCard.basic(
            "A2b-060", "Jigglypuff", "It hugely inflates its stomach and sings a mysterious melody. If you hear this melody, you'll become sleepy right away.",
            60, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Rollout", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-061 - Wigglytuff — Comforting Song as A3's Wigglytuff has it.
     */
    public static final PokemonCard WIGGLYTUFF = PokemonCard.evolution(
            "A2b-061", "Wigglytuff", "It has a very fine fur. Take care not to make it angry, or it may inflate steadily and hit with a body slam.",
            1, "Jigglypuff", 100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Gentle Slap", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(50), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.FIGHTING).withAbility(new ActivatedAbility(
            "Comforting Song",
            "Once during your turn, you may heal 20 damage from your Active Pokémon.",
            new PlainAction("", new Attempt(List.of(
                    new HealDamage(new Literal(20), new AttackerActive())))),
            true,
            new For(new IsDamaged(), new AttackerActive())));

    /**
     * A2b-062 - Lickitung
     */
    public static final PokemonCard LICKITUNG = PokemonCard.basic(
            "A2b-062", "Lickitung", "If this Pokémon's sticky saliva gets on you and you don't clean it off, an intense itch will set in. The itch won't go away, either.",
            80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Rollout", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-063 - Lickilicky
     */
    public static final PokemonCard LICKILICKY = PokemonCard.evolution(
            "A2b-063", "Lickilicky", "Lickilicky's strange tongue can stretch to many times the length of its body. No one has figured out how Lickilicky's tongue can stretch so far.",
            1, "Lickitung", 130, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Rollout", "", EnergyCost.of(Type.COLORLESS, 3), new Attempt(List.of(
                    new DealDamage(new Literal(70), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-064 - Bidoof
     */
    public static final PokemonCard BIDOOF = PokemonCard.basic(
            "A2b-064", "Bidoof", "With nerves of steel, nothing can perturb it. It is more agile and active than it appears.",
            70, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Headbutt", "", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-065 - Bibarel ex
     */
    public static final PokemonCard BIBAREL_EX = PokemonCard.evolution(
            "A2b-065", "Bibarel ex", "",
            1, "Bidoof", 160, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Carefree Press", "Heal 30 damage from this Pokémon.", EnergyCost.of(Type.COLORLESS, 4), new Attempt(List.of(
                    new DealDamage(new Literal(100), new OpponentActive()),
                    new HealDamage(new Literal(30), new Self())))
            )), CardRarity.DOUBLE_RARE
    ).withWeakness(Type.FIGHTING).withTags(CardTag.EX);

    /**
     * A2b-066 - Buneary
     */
    public static final PokemonCard BUNEARY = PokemonCard.basic(
            "A2b-066", "Buneary", "If both of Buneary's ears are rolled up, something is wrong with its body or mind. It's a sure sign the Pokémon is in need of care.",
            50, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Pound", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-067 - Lopunny
     */
    public static final PokemonCard LOPUNNY = PokemonCard.evolution(
            "A2b-067", "Lopunny", "Lopunny is constantly monitoring its surroundings. If danger approaches, this Pokémon responds with superdestructive kicks.",
            1, "Buneary", 100, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Hopping Shot", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(30), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.FIGHTING);

    /**
     * A2b-068 - Cyclizar. Overacceleration is its only attack, so boosting all
     * of its attacks is exact.
     */
    public static final PokemonCard CYCLIZAR = PokemonCard.basic(
            "A2b-068", "Cyclizar", "Apparently Cyclizar has been allowing people to ride on its back since ancient times. Depictions of this have been found in 10,000-year-old murals.",
            80, Type.COLORLESS, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Overacceleration", "During your next turn, this Pokémon's Overacceleration attack does +20 damage.",
                    EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive()),
                    new IncreaseDamage(new Literal(20), new Self(), new Literal(2))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.FIGHTING);

    /** A2b-078 · Wigglytuff (alternate art of A2b-061) */
    public static final PokemonCard WIGGLYTUFF_IR = WIGGLYTUFF.withId("A2b-078", CardRarity.ILLUSTRATION_RARE);
    /** A2b-087 · Bibarel ex (alternate art of A2b-065) */
    public static final PokemonCard BIBAREL_EX_UR = BIBAREL_EX.withId("A2b-087", CardRarity.ULTRA_RARE);
    /** A2b-095 · Bibarel ex (alternate art of A2b-065) */
    public static final PokemonCard BIBAREL_EX_SIR = BIBAREL_EX.withId("A2b-095", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            RATTATA, RATICATE, JIGGLYPUFF, WIGGLYTUFF, LICKITUNG, LICKILICKY, BIDOOF, BIBAREL_EX,
            BUNEARY, LOPUNNY, CYCLIZAR,
            WIGGLYTUFF_IR, BIBAREL_EX_UR, BIBAREL_EX_SIR);

    private Colorless() {
    }
}
