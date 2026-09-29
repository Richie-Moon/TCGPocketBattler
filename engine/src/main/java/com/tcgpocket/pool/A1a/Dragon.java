package com.tcgpocket.pool.A1a;

import com.tcgpocket.action.Action;
import com.tcgpocket.card.CardRarity;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.condition.And;
import com.tcgpocket.condition.EventConcerns;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsActive;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.DealDamage;
import com.tcgpocket.effect.PlaceDamage;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.target.EventSource;
import com.tcgpocket.target.OpponentActive;
import com.tcgpocket.target.Self;
import com.tcgpocket.trigger.DamageDealt;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * Mythical Island — Dragon.
 */
public final class Dragon {
    /**
     * A1a-056 - Druddigon
     */
    public static final PokemonCard DRUDDIGON = PokemonCard.basic(
            "A1a-056", "Druddigon", "Druddigon lives in caves, but it never skips sunbathing—it won't be able to move if its body gets too cold.",
            100, Type.DRAGON, EnergyCost.of(Type.COLORLESS, 2), List.of(new Action(
                    "Dragon Claw", "", EnergyCost.of(Type.FIRE, 1, Type.WATER, 1, Type.COLORLESS, 1), new Attempt(List.of(
                    new DealDamage(new Literal(90), new OpponentActive())))
            )), CardRarity.UNCOMMON
    ).withAbility(new PassiveAbility("Rough Skin",
            "If this Pokémon is in the Active Spot and is damaged by an attack from your opponent's Pokémon, do 20 damage to the Attacking Pokémon.",
            List.of(new Trigger(DamageDealt.class,
                    new And<>(new EventConcerns(new Self()), new For(new IsActive(), new Self())),
                    new Attempt(List.of(new PlaceDamage(new Literal(20), new EventSource())))))));

    static final List<PokemonCard> CARDS = List.of(DRUDDIGON);

    private Dragon() {
    }
}
