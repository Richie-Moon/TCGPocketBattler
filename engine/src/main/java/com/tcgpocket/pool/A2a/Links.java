package com.tcgpocket.pool.A2a;

import com.tcgpocket.card.NoRetreatCost;
import com.tcgpocket.card.PassiveAbility;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.IncreaseSideDamage;
import com.tcgpocket.effect.ReduceDamageTaken;
import com.tcgpocket.number.Literal;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.target.Self;
import com.tcgpocket.target.SelfSide;
import com.tcgpocket.trigger.AttackDeclared;
import com.tcgpocket.trigger.Trigger;

import java.util.List;

/**
 * The Link abilities, each printed on more than one card in the set. Each
 * holds only "if you have Arceus or Arceus ex in play", asked of the holder
 * when it matters, so benching Arceus mid-turn switches it on.
 *
 * <p>Not in {@link TriumphantLight}: that class reads every type class's
 * {@code CARDS} as it initialises, so a type class reaching back into it would
 * see a half-built set.
 */
final class Links {

    /** "If you have Arceus or Arceus ex in play", asked of the holder. */
    static final ICondition<PokemonInPlay> ARCEUS_IN_PLAY =
            new OwnerHasInPlay(new Or<>(new IsSpecies("Arceus"), new IsSpecies("Arceus ex")));

    private Links() {
    }

    /** Carnivine, Tyranitar. Stamped as the holder attacks, like Fighting Coach. */
    static PassiveAbility powerLink() {
        return new PassiveAbility("Power Link",
                "If you have Arceus or Arceus ex in play, attacks used by this Pokémon do +30 damage to your opponent's Active Pokémon.",
                List.of(new Trigger(AttackDeclared.class,
                        new And<>(new EventConcerns(new Self()), new For(ARCEUS_IN_PLAY, new Self())),
                        new Attempt(List.of(new IncreaseSideDamage(new Literal(30), new Literal(0)))))));
    }

    /** Heatran, Rotom. */
    static PassiveAbility speedLink() {
        return new PassiveAbility("Speed Link",
                "If you have Arceus or Arceus ex in play, this Pokémon has no Retreat Cost.",
                new NoRetreatCost(ARCEUS_IN_PLAY));
    }

    /** Raichu, Magnezone. Stamped as the opponent attacks, like Thick Fat. */
    static PassiveAbility resilienceLink() {
        return new PassiveAbility("Resilience Link",
                "If you have Arceus or Arceus ex in play, this Pokémon takes −30 damage from attacks.",
                List.of(new Trigger(AttackDeclared.class,
                        new And<>(new Not<>(new EventSideIs(new SelfSide())), new For(ARCEUS_IN_PLAY, new Self())),
                        new Attempt(List.of(new ReduceDamageTaken(new Literal(30), new Self(), new Literal(0)))))));
    }
}
