package com.tcgpocket.pool.A2a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsDamaged;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Branch;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;

import java.util.List;

/**
 * Triumphant Light — Fire.
 */
public final class Fire {

    /**
     * A2a-011 - Houndour
     */
    public static final PokemonCard HOUNDOUR = PokemonCard.basic(
            "A2a-011", "Houndour", "It is smart enough to hunt in packs. It uses a variety of cries for communicating with others.",
            60, Type.FIRE, EnergyCost.of(Type.COLORLESS, 1), List.of(new Action(
                    "Flare", "", EnergyCost.of(Type.FIRE, 1), new Attempt(List.of(
                    new DealDamage(new Literal(20), new OpponentActive())))
            )), CardRarity.COMMON
    ).withWeakness(Type.WATER);

    /**
     * A2a-012 - Houndoom
     */
    public static final PokemonCard HOUNDOOM = PokemonCard.evolution(
            "A2a-012", "Houndoom", "If you are burned by the flames it shoots from its mouth, the pain will never go away.",
            1, "Houndour", 100, Type.FIRE, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Corner", "During your opponent's next turn, the Defending Pokémon can't retreat.",
                    EnergyCost.of(Type.FIRE, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(60), new OpponentActive()),
                    new PreventRetreat(new OpponentActive(), new Literal(1))))
            )), CardRarity.UNCOMMON
    ).withWeakness(Type.WATER);

    /**
     * A2a-013 - Heatran
     */
    public static final PokemonCard HEATRAN = PokemonCard.basic(
            "A2a-013", "Heatran", "It dwells in volcanic caves. It digs in with its cross-shaped feet to crawl on ceilings and walls.",
            110, Type.FIRE, EnergyCost.of(Type.COLORLESS, 3), List.of(new Action(
                    "Ragin' Mad Strike", "If this Pokémon has damage on it, this attack does 40 more damage.",
                    EnergyCost.of(Type.FIRE, 2), new Attempt(List.of(
                    new DealDamage(new Branch(
                            List.of(new Branch.Case(new For(new IsDamaged(), new Self()), new Literal(80))),
                            new Literal(40)), new OpponentActive())))
            )), CardRarity.RARE
    ).withWeakness(Type.WATER).withAbility(Links.speedLink());

    /** A2a-076 · Houndoom (alternate art of A2a-012) */
    public static final PokemonCard HOUNDOOM_IR = HOUNDOOM.withId("A2a-076", CardRarity.ILLUSTRATION_RARE);

    static final List<PokemonCard> CARDS = List.of(HOUNDOUR, HOUNDOOM, HEATRAN,
            HOUNDOOM_IR);

    private Fire() {
    }
}
