package com.tcgpocket.pool.A3b;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.condition.Not;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.Fail;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.effect.PreventAttack;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.status.ParalysisStatus;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Eevee Grove — Dragon.
 */
public final class Dragon {

    /**
     * A3b-051 - Dratini
     */
    public static final PokemonCard DRATINI = PokemonCard.basic(
            "A3b-051", "Dratini", "It sheds many layers of skin as it grows larger. During this process, it is protected by a rapid waterfall.",
            60, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Beat", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    );

    /**
     * A3b-052 - Dragonair
     */
    public static final PokemonCard DRAGONAIR = PokemonCard.evolution(
            "A3b-052", "Dragonair", "They say that if it emits an aura from its whole body, the weather will begin to change instantly.",
            1, "Dratini", 90, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Waterfall", "", EnergyCost.of(Type.WATER, 1, Type.LIGHTNING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.COMMON
    );

    /**
     * A3b-053 - Dragonite ex
     */
    public static final PokemonCard DRAGONITE_EX = PokemonCard.evolution(
            "A3b-053", "Dragonite ex", "",
            2, "Dragonair", 180, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Giga Impact", "During your next turn, this Pokémon can't attack.", EnergyCost.of(Type.WATER, 1, Type.LIGHTNING, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(180), new OpponentActive()),
                    new PreventAttack(new Self(), new Literal(2))))
            )), CardRarity.DOUBLE_RARE
    ).withTags(CardTag.EX);

    /**
     * A3b-054 - Drampa
     */
    public static final PokemonCard DRAMPA = PokemonCard.basic(
            "A3b-054", "Drampa", "The mountains it calls home are nearly two miles in height. On rare occasions, it descends to play with the children living in the towns below.",
            100, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Dragon Breath", "Flip a coin. If tails, this attack does nothing. If heads, your opponent's Active Pokémon is now Paralyzed.", EnergyCost.of(Type.COLORLESS, 2), new Attempt(List.of(
                    new FlipN(new Literal(1)),
                    new ConditionalEffect(new Not<>(new LastCoinTossHeads()), new Fail()),
                    new DealDamage(new Literal(70), new OpponentActive()),
                    new AddStatus(new ParalysisStatus(), new OpponentActive())))
            )), CardRarity.UNCOMMON
    );

    /** A3b-082 · Dragonite ex (alternate art of A3b-053) */
    public static final PokemonCard DRAGONITE_EX_UR = DRAGONITE_EX.withId("A3b-082", CardRarity.ULTRA_RARE);

    /** A3b-090 · Dragonite ex (alternate art of A3b-053) */
    public static final PokemonCard DRAGONITE_EX_SIR = DRAGONITE_EX.withId("A3b-090", CardRarity.SPECIAL_ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(
            DRATINI, DRAGONAIR, DRAGONITE_EX, DRAMPA, DRAGONITE_EX_UR,
            DRAGONITE_EX_SIR);

    private Dragon() {
    }
}
