package com.tcgpocket.pool.a1;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.number.Product;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentActive;

import java.util.List;

/** Genetic Apex — Lightning. */
public final class Lightning {

    private Lightning() {
    }

    /**
     * A1-096 · Pikachu ex — Circle Circuit: 30 damage for each of your Benched
     * Pokemon.
     *
     * <p>The card that justifies {@code INumber} being a tree rather than an
     * int. The damage is read off the board when the attack resolves, so
     * benching a fourth Pokemon changes it without the card knowing anything
     * happened.
     */
    public static final PokemonCard PIKACHU_EX = PokemonCard.basic(
                    "A1-096", "Pikachu ex", "", 120, Type.LIGHTNING,
                    EnergyCost.of(Type.COLORLESS, 1),
                    List.of(new Action(
                            "Circle Circuit",
                            "This attack does 30 damage for each of your Benched Pokemon.",
                            EnergyCost.of(Type.LIGHTNING, 2),
                            new Attempt(List.of(
                                    new DealDamage(
                                            new Product(
                                                    new Literal(30),
                                                    new CountCards(new AttackerSide(), Zone.BENCH)),
                                            new OpponentActive()))))),
                    CardRarity.DOUBLE_RARE)
            .withWeakness(Type.FIGHTING)
            .withTags(CardTag.EX);

    static final List<PokemonCard> CARDS = List.of(PIKACHU_EX);
}
