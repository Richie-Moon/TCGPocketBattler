package com.tcgpocket.pool.A1a;

import com.tcgpocket.action.PlainAction;
import com.tcgpocket.action.WithPrecondition;
import com.tcgpocket.card.ITrainerCard;
import com.tcgpocket.card.ItemCard;
import com.tcgpocket.card.PlayableItemCard;
import com.tcgpocket.card.SupporterCard;
import com.tcgpocket.condition.Always;
import com.tcgpocket.condition.For;
import com.tcgpocket.condition.IsSpecies;
import com.tcgpocket.condition.IsType;
import com.tcgpocket.effect.Attempt;
import com.tcgpocket.effect.BenchFromDiscard;
import com.tcgpocket.effect.ReduceRetreatCost;
import com.tcgpocket.effect.ReduceSideDamageTaken;
import com.tcgpocket.effect.ReturnToHand;
import com.tcgpocket.effect.TopCardToHand;
import com.tcgpocket.energy.Type;
import com.tcgpocket.number.Literal;
import com.tcgpocket.target.AttackerActive;
import com.tcgpocket.target.AttackerSide;
import com.tcgpocket.target.OpponentSide;

import java.util.List;

/**
 * Mythical Island — Trainers.
 *
 * <p>Trainers are only ever played on your own turn, so "your" is
 * {@code Attacker*} throughout.
 */
public final class Trainers {
    /**
     * A1a-063 · Old Amber (reprint of A1-218)
     */
    public static final PlayableItemCard OLD_AMBER = com.tcgpocket.pool.A1.Trainers.OLD_AMBER.withId("A1a-063");
    /**
     * A1a-064 · Pokémon Flute
     * 
     * Check: Can the card be used if the bench is full, or does it fail
     */
    public static final ItemCard POKEMON_FLUTE = ItemCard.of(
            "A1a-064", "Pokémon Flute",
            new PlainAction(
                    "Put a Basic Pokémon from your opponent's discard pile onto their Bench.",
                    new Attempt(List.of(
                            new BenchFromDiscard(new OpponentSide(), new Always<>())))));
    /**
     * A1a-065 · Mythical Slab
     */
    public static final ItemCard MYTHICAL_SLAB = ItemCard.of(
            "A1a-065", "Mythical Slab",
            new PlainAction(
                    "Look at the top card of your deck. If that card is a Psychic Pokémon, put it into your hand. If it is not a Psychic Pokémon, put it on the bottom of your deck.",
                    new Attempt(List.of(
                            new TopCardToHand(new AttackerSide(), new IsType(Type.PSYCHIC))))));
    /**
     * A1a-066 · Budding Expeditioner
     */
    public static final SupporterCard BUDDING_EXPEDITIONER = SupporterCard.of(
            "A1a-066", "Budding Expeditioner",
            new WithPrecondition(
                    new For(new IsSpecies("Mew ex"), new AttackerActive()),
                    new PlainAction(
                            "Put your Mew ex in the Active Spot into your hand.",
                            new Attempt(List.of(
                                    new ReturnToHand(new AttackerActive()))))));
    /**
     * A1a-067 · Blue
     */
    public static final SupporterCard BLUE = SupporterCard.of(
            "A1a-067", "Blue",
            new PlainAction(
                    "During your opponent's next turn, all of your Pokémon take −10 damage from attacks from your opponent's Pokémon.",
                    new Attempt(List.of(
                            new ReduceSideDamageTaken(new Literal(10), new Literal(1))))));
    /**
     * A1a-068 · Leaf
     */
    public static final SupporterCard LEAF = SupporterCard.of(
            "A1a-068", "Leaf",
            new PlainAction(
                    "During this turn, the Retreat Cost of your Active Pokémon is 2 less.",
                    new Attempt(List.of(
                            new ReduceRetreatCost(new Literal(2), new AttackerActive(), new Literal(0))))));

    // Ultra Rare alternate arts. Trainers carry no rarity yet, so only the id differs.
    /** A1a-080 · Budding Expeditioner (alternate art of A1a-066) */
    public static final SupporterCard BUDDING_EXPEDITIONER_UR = BUDDING_EXPEDITIONER.withId("A1a-080");
    /** A1a-081 · Blue (alternate art of A1a-067) */
    public static final SupporterCard BLUE_UR = BLUE.withId("A1a-081");
    /** A1a-082 · Leaf (alternate art of A1a-068) */
    public static final SupporterCard LEAF_UR = LEAF.withId("A1a-082");

    static final List<ITrainerCard> CARDS = List.of(
            OLD_AMBER, POKEMON_FLUTE, MYTHICAL_SLAB, BUDDING_EXPEDITIONER, BLUE, LEAF,
            BUDDING_EXPEDITIONER_UR, BLUE_UR, LEAF_UR);

    private Trainers() {
    }
}
