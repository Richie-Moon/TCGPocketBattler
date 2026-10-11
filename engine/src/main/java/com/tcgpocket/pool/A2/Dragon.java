package com.tcgpocket.pool.A2;

import com.tcgpocket.action.Action;
import com.tcgpocket.action.PlainAction;
import com.tcgpocket.card.ActivatedAbility;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.GreaterThan;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardFromHand;
import com.tcgpocket.effect.DrawCard;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;

import java.util.List;

/**
 * Space-Time Smackdown — Dragon.
 */
public final class Dragon {

    /**
     * A2-121 - Gible
     */
    public static final PokemonCard GIBLE = PokemonCard.basic(
            "A2-121", "Gible", "It skulks in caves, and when prey or an enemy passes by, it leaps out and chomps them. The force of its attack sometimes chips its teeth.",
            60, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Gnaw", "", EnergyCost.of(Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    );

    /**
     * A2-122 - Gabite
     */
    public static final PokemonCard GABITE = PokemonCard.evolution(
            "A2-122", "Gabite", "In rare cases, it molts and sheds its scales. Medicine containing its scales as an ingredient will make a weary body feel invigorated.",
            1, "Gible", 90, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Slash", "", EnergyCost.of(Type.WATER, 1, Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive())))
            )), CardRarity.UNCOMMON
    );

    /**
     * A2-123 - Garchomp — Reckless Shearing: the discard is a cost, so it comes
     * first and a failed discard stops the draw.
     */
    public static final PokemonCard GARCHOMP = PokemonCard.evolution(
            "A2-123", "Garchomp", "It is said that when one runs at high speed, its wings create blades of wind that can fell nearby trees.",
            2, "Gabite", 140, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Dragon Claw", "", EnergyCost.of(Type.WATER, 1, Type.FIGHTING, 1), new Attempt(List.of(
                    new DealDamage(new Literal(100), new OpponentActive())))
            )), CardRarity.RARE
    ).withAbility(new ActivatedAbility(
            "Reckless Shearing",
            "You must discard a card from your hand in order to use this Ability. Once during your turn, you may draw a card.",
            new PlainAction("", new Attempt(List.of(
                    new DiscardFromHand(new AttackerSide(), new Literal(1)),
                    new DrawCard(new Literal(1), new AttackerSide())))),
            true,
            new GreaterThan(new CountCards(new AttackerSide(), Zone.HAND), new Literal(0))
    ));

    static final List<PokemonCard> CARDS = List.of(GIBLE, GABITE, GARCHOMP);

    private Dragon() {
    }
}
