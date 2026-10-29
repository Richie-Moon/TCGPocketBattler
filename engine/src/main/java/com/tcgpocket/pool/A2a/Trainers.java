package com.tcgpocket.pool.A2a;

import com.tcgpocket.action.PlainAction;
import com.tcgpocket.action.WithPrecondition;
import com.tcgpocket.card.ITrainerCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.condition.*;
import com.tcgpocket.effect.*;
import com.tcgpocket.energy.EnergyCost;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.CountCards;
import com.tcgpocket.number.Literal;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Zone;
import com.tcgpocket.target.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Triumphant Light — Trainers.
 *
 * <p>Trainers are only ever played on your own turn, so "your" is
 * {@code Attacker*} throughout.
 */
public final class Trainers {

    private static final ICondition<PokemonInPlay> HAS_WATER_ENERGY = new HasEnergy(EnergyCost.of(Type.WATER, 1));

    /**
     * A2a-072 · Irida — only playable with a damaged Pokémon she would heal.
     */
    public static final SupporterCard IRIDA = SupporterCard.of(
            "A2a-072", "Irida",
            new WithPrecondition(
                    new ForAny(new And<>(HAS_WATER_ENERGY, new IsDamaged()), new AttackerAll()),
                    new PlainAction(
                            "Heal 40 damage from each of your Pokémon that has any Water Energy attached.",
                            new Attempt(List.of(
                                    new HealEach(new Literal(40), new Matching(new AttackerAll(), HAS_WATER_ENERGY)))))));
    /**
     * A2a-073 · Celestic Town Elder — only playable with a Basic Pokémon in
     * the discard pile.
     */
    public static final SupporterCard CELESTIC_TOWN_ELDER = SupporterCard.of(
            "A2a-073", "Celestic Town Elder",
            new WithPrecondition(
                    new GreaterThan(new CountCards(new AttackerSide(), Zone.DISCARD, Optional.of(new IsBasic())), new Literal(0)),
                    new PlainAction(
                            "Put 1 random Basic Pokémon from your discard pile into your hand.",
                            new Attempt(List.of(
                                    new RecoverFromDiscard(new AttackerSide(), new IsBasic()))))));
    /**
     * A2a-074 · Barry
     */
    public static final SupporterCard BARRY = SupporterCard.of(
            "A2a-074", "Barry",
            new PlainAction(
                    "During this turn, attacks used by your Snorlax, Heracross, and Staraptor cost 2 less Colorless Energy.",
                    new Attempt(List.of(
                            new ReduceSideAttackCost(
                                    new Literal(2), species("Snorlax", "Heracross", "Staraptor"), new Literal(0))))));
    /**
     * A2a-075 · Adaman
     */
    public static final SupporterCard ADAMAN = SupporterCard.of(
            "A2a-075", "Adaman",
            new PlainAction(
                    "During your opponent's next turn, all of your Metal Pokémon take −20 damage from attacks from your opponent's Pokémon.",
                    new Attempt(List.of(
                            new ReduceSideDamageTaken(new Literal(20), new HasType(Type.METAL), new Literal(1))))));

    private static ICondition<CardInstance> species(String... names) {
        return new Any<>(Arrays.stream(names).<ICondition<CardInstance>>map(IsSpecies::new).toList());
    }

    static final List<ITrainerCard> CARDS = List.of(IRIDA, CELESTIC_TOWN_ELDER, BARRY, ADAMAN);

    private Trainers() {
    }
}
