package com.tcgpocket.pool.a1;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.LastCoinTossHeads;
import com.tcgpocket.effect.AddStatus;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.ConditionalEffect;
import com.tcgpocket.effect.FlipN;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.status.PoisonStatus;
import com.tcgpocket.target.OpponentActive;

import java.util.List;

/** Genetic Apex — Darkness. */
public final class Darkness {

    private Darkness() {
    }

    /**
     * A1-176 · Koffing — Gas: flip a coin, and on heads the Defending Pokemon
     * is Poisoned.
     *
     * <p>The "if heads" shape. {@link ConditionalEffect} rather than a failing
     * effect: a tails result is a legal nothing-happened, and must not abort
     * whatever the card says next.
     *
     * <p>Poison then does its own work — 10 damage at every turn end — through
     * the triggers on {@link PoisonStatus}. Nothing here has to say so.
     */
    public static final PokemonCard KOFFING = PokemonCard.basic(
                    "A1-176", "Koffing", "", 70, Type.DARKNESS,
                    EnergyCost.of(Type.COLORLESS, 2),
                    List.of(new Action(
                            "Gas",
                            "Flip a coin. If heads, your opponent's Active Pokemon is now Poisoned.",
                            EnergyCost.of(Type.DARKNESS, 1),
                            new Attempt(List.of(
                                    new FlipN(new Literal(1)),
                                    new ConditionalEffect(
                                            new LastCoinTossHeads(),
                                            new AddStatus(
                                                    new OpponentActive(), new PoisonStatus())))))),
                    CardRarity.COMMON)
            .withWeakness(Type.FIGHTING);

    static final List<PokemonCard> CARDS = List.of(KOFFING);
}
