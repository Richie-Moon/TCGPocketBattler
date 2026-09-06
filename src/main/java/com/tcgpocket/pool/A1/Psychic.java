package com.tcgpocket.pool.A1;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.DiscardTypeEnergy;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/** Genetic Apex — Psychic. */
public final class Psychic {

    private Psychic() {
    }

    /**
     * A1-129 · Mewtwo ex — Psychic Sphere for 50, or Psydrive for 150 at the
     * cost of two Energy.
     *
     * <p>Note the order in Psydrive: the damage lands and <em>then</em> the
     * Energy goes. Reversing it would be a different card — the discard would
     * be a cost that could fail, which under an attempt's short-circuit would
     * cancel the damage.
     */
    public static final PokemonCard MEWTWO_EX = PokemonCard.basic(
                    "A1-129", "Mewtwo ex", "", 150, Type.PSYCHIC,
                    EnergyCost.of(Type.COLORLESS, 2),
                    List.of(
                            new Action(
                                    "Psychic Sphere",
                                    "This attack does 50 damage.",
                                    EnergyCost.of(Type.PSYCHIC, 1, Type.COLORLESS, 1),
                                    new Attempt(List.of(
                                            new DealDamage(new Literal(50), new OpponentActive())))),
                            new Action(
                                    "Psydrive",
                                    "This attack does 150 damage. Discard 2 Psychic Energy from this Pokemon.",
                                    EnergyCost.of(Type.PSYCHIC, 2, Type.COLORLESS, 2),
                                    new Attempt(List.of(
                                            new DealDamage(new Literal(150), new OpponentActive()),
                                            new DiscardTypeEnergy(
                                                    Type.PSYCHIC, new Literal(2), new Self()))))),
                    CardRarity.DOUBLE_RARE)
            .withWeakness(Type.DARKNESS)
            .withTags(CardTag.EX);

    static final List<PokemonCard> CARDS = List.of(MEWTWO_EX);
}
