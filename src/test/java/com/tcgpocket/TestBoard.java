package com.tcgpocket;

import com.tcgpocket.card.ICard;
import com.tcgpocket.card.PokemonCard;
import com.tcgpocket.energy.Type;
import com.tcgpocket.resolve.ResolutionContext;
import com.tcgpocket.resolve.SeededRandom;
import com.tcgpocket.state.Battle;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.Zone;

/**
 * A two-sided board, assembled in a line or two, so tests read as rules rather
 * than as setup.
 *
 * <p>{@code you} is the side taking its turn, so it is the battle's attacker;
 * {@code them} is the defender. The RNG is always seeded, so any test touching
 * chance is reproducible.
 */
public final class TestBoard {

    /** Arbitrary but fixed, so a run is repeatable. */
    public static final long DEFAULT_SEED = 1L;

    public final Side you = new Side("you");
    public final Side them = new Side("them");
    public final Battle battle;

    private int nextInstanceId = 1;

    public TestBoard() {
        this(DEFAULT_SEED);
    }

    public TestBoard(long seed) {
        this.battle = new Battle(you, them, new SeededRandom(seed));
    }

    public static PokemonCard card(String name, int maxHp, Type type) {
        return PokemonCard.basic(name.toLowerCase(), name, maxHp, type, 1);
    }

    public PokemonInPlay active(Side side, PokemonCard definition) {
        PokemonInPlay pokemon = new PokemonInPlay(nextInstanceId++, definition, side, Zone.ACTIVE, 0);
        side.setActive(pokemon);
        return pokemon;
    }

    public PokemonInPlay bench(Side side, PokemonCard definition) {
        PokemonInPlay pokemon = new PokemonInPlay(nextInstanceId++, definition, side, Zone.BENCH, 0);
        side.addToBench(pokemon);
        return pokemon;
    }

    public CardInstance inHand(Side side, ICard definition) {
        CardInstance instance = new CardInstance(nextInstanceId++, definition, side, Zone.HAND);
        side.addToHand(instance);
        return instance;
    }

    public CardInstance inDeck(Side side, ICard definition) {
        CardInstance instance = new CardInstance(nextInstanceId++, definition, side, Zone.DECK);
        side.addToDeck(instance);
        return instance;
    }

    public CardInstance inDiscard(Side side, ICard definition) {
        CardInstance instance = new CardInstance(nextInstanceId++, definition, side, Zone.DISCARD);
        side.addToDiscard(instance);
        return instance;
    }

    /** A loose card belonging to nobody's pile, for attaching as a Tool. */
    public CardInstance loose(Side side, ICard definition) {
        return new CardInstance(nextInstanceId++, definition, side, Zone.ATTACHED);
    }

    /** A context controlled by {@code you}, with the given Pokemon as its source. */
    public ResolutionContext contextFor(PokemonInPlay source) {
        return ResolutionContext.of(battle, you, source);
    }

    /** A context controlled by {@code you} with no source Pokemon, as a Trainer card has. */
    public ResolutionContext contextWithoutSource() {
        return ResolutionContext.of(battle, you);
    }
}
