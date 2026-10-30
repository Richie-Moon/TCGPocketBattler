package com.tcgpocket.pool.A3a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.CardTag;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

import static com.tcgpocket.pool.A1.Fire.GROWLITHE;
import static com.tcgpocket.pool.A1.Fire.ARCANINE;
import static com.tcgpocket.pool.A1.Fire.ARCANINE_EX;

/**
 * Extradimensional Crisis — Fire.
 */
public final class Fire {

    /**
     * A3a-009 - Blacephalon
     */
    public static final PokemonCard BLACEPHALON = PokemonCard.basic(
            "A3a-009", "Blacephalon", "It slithers toward people. Then, without warning, it triggers the explosion of its own head. It's apparently one kind of Ultra Beast.",
            100, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Beat Punk", "This Pokémon also does 70 damage to itself.", EnergyCost.of(Type.FIRE, 2, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(130), new OpponentActive()),
                    new DealDamage(new Literal(70), new Self())))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER).withTags(CardTag.ULTRA_BEAST);

    /** A3a-072 · Blacephalon (alternate art of A3a-009) */
    public static final PokemonCard BLACEPHALON_IR = BLACEPHALON.withId("A3a-072", CardRarity.ILLUSTRATION_RARE);

    /** A3a-089 · Growlithe (alternate art of A1-039) */
    public static final PokemonCard GROWLITHE_SR = GROWLITHE.withId("A3a-089", CardRarity.SHINY_RARE);
    /** A3a-090 · Arcanine (alternate art of A1-040) */
    public static final PokemonCard ARCANINE_SR = ARCANINE.withId("A3a-090", CardRarity.SHINY_RARE);
    /** A3a-100 · Arcanine ex (alternate art of A1-041) */
    public static final PokemonCard ARCANINE_EX_SUR = ARCANINE_EX.withId("A3a-100", CardRarity.SHINY_ULTRA_RARE);

    static final List<PokemonCard> CARDS = List.of(
            BLACEPHALON,
            BLACEPHALON_IR,
            GROWLITHE_SR, ARCANINE_SR, ARCANINE_EX_SUR);

    private Fire() {
    }
}
