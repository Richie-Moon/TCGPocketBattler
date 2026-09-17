package com.tcgpocket.pool.A1;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.RepeatEffect;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.OpponentAll;
import com.tcgpocket.target.RandomFrom;

import java.util.List;

/** Genetic Apex — Dragon. */
public final class Dragon {

    private Dragon() {
    }

    /**
     * A1-183 - Dratini
     */
    public static final PokemonCard DRATINI = PokemonCard.basic(
                    "A1-183", "Dratini", "It sheds many layers of skin as it grows larger. During this process, it is protected by a rapid waterfall.",
                    70, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Ram", "", EnergyCost.of(Type.WATER, 1, Type.LIGHTNING, 1),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(40), new OpponentActive())
                            )))), CardRarity.COMMON);

    /**
     * A1-184 - Dragonair
     */
    public static final PokemonCard DRAGONAIR = PokemonCard.evolution(
                    "A1-184", "Dragonair", "They say that if it emits an aura from its whole body, the weather will begin to change instantly.",
                    1, "Dratini", 100, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                            "Tail Smack", "", EnergyCost.of(Type.WATER, 1, Type.LIGHTNING, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new DealDamage(new Literal(80), new OpponentActive())
                            )))), CardRarity.UNCOMMON);

    /**
     * A1-185 - Dragonite
     */
    public static final PokemonCard DRAGONITE = PokemonCard.evolution(
                    "A1-185", "Dragonite", "It is said that somewhere in the ocean lies an island where these gather. Only they live there.",
                    2, "Dragonair", 160, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                            "Draco Meteor", "1 of your opponent's Pokémon is chosen at random 4 times. For each time a Pokémon was chosen, do 50 damage to it.", EnergyCost.of(Type.WATER, 1, Type.LIGHTNING, 1, Type.COLORLESS, 2),
                            new Attempt(List.of(
                                    new RepeatEffect(new Literal(4), new DealDamage(new Literal(50), new RandomFrom(new OpponentAll())))
                            )))), CardRarity.RARE);

    static final List<PokemonCard> CARDS = List.of(DRATINI, DRAGONAIR, DRAGONITE);
}
