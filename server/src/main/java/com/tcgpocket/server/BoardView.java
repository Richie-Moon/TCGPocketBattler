package com.tcgpocket.server;

import com.tcgpocket.energy.Type;
import com.tcgpocket.state.Battle;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.PokemonInPlay;
import com.tcgpocket.state.Side;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * The board as one player is allowed to see it.
 *
 * <p>This is the only board state that leaves the server; {@code Battle} is
 * never serialized. Hidden information is left out here: the opponent's hand is
 * just a count unless it was revealed, and both decks are just counts, since their order is secret too.
 * The RNG isn't included either.
 */
record BoardView(int turn, boolean yourTurn, CardView stadium, SideView you, SideView opponent) {

    /** {@code id} is this copy's instance id; {@code card} is the printed card, such as "A1-094". */
    record CardView(int id, String card, String name) {
    }

    /** {@code id} stays the same from hand to play and through evolution. */
    record PokemonView(int id, String card, String name, int hp, int maxHp,
                       Map<Type, Integer> energy, List<String> statuses, CardView tool) {
    }

    /** Object-storage names from the side's deck ({@code "Coin_Pokéball.png"}); public, so both players see both. */
    record Cosmetics(String coin, String sleeve, String playmat) {
        static final Cosmetics DEFAULT = new Cosmetics("Coin_Pokéball.png", "Sleeve_Default.png", "Playmat_Default.png");
    }

    /**
     * For the opponent, {@code hand} is only what was revealed; {@code handSize} is always the real count.
     * {@code topCards} are the deck's top cards, top first, once its owner has looked at them (Porygon), and only for them.
     */
    record SideView(String name, int points, List<CardView> hand, int handSize, int deckSize, List<CardView> topCards,
                    List<CardView> discard, PokemonView active, List<PokemonView> bench,
                    Type energy, Type nextEnergy, Cosmetics cosmetics) {
    }

    static BoardView of(Battle battle, Side viewer, Function<Side, Cosmetics> cosmetics) {
        return new BoardView(
                battle.turn(),
                battle.attacker() == viewer,
                battle.stadium().map(BoardView::card).orElse(null),
                side(viewer, true, cosmetics),
                side(battle.opponentOf(viewer), false, cosmetics));
    }

    private static SideView side(Side side, boolean own, Function<Side, Cosmetics> cosmetics) {
        return new SideView(
                side.name(),
                side.points(),
                (own ? side.hand() : side.revealedHand()).stream().map(BoardView::card).toList(),
                side.hand().size(),
                side.deck().size(),
                (own ? side.seenTopCards() : List.<CardInstance>of()).stream().map(BoardView::card).toList(),
                side.discardPile().stream().map(BoardView::card).toList(),
                side.active().map(BoardView::pokemon).orElse(null),
                side.bench().stream().map(BoardView::pokemon).toList(),
                side.currentEnergy().orElse(null),
                side.nextEnergy().orElse(null),
                cosmetics.apply(side));
    }

    private static CardView card(CardInstance card) {
        return new CardView(card.instanceId(), card.definition().id(), card.definition().name());
    }

    private static PokemonView pokemon(PokemonInPlay pokemon) {
        return new PokemonView(
                pokemon.instanceId(),
                pokemon.definition().id(),
                pokemon.definition().name(),
                pokemon.currentHp(),
                pokemon.maxHp(),
                pokemon.attachedEnergy(),
                pokemon.statuses().stream().map(status -> status.getClass().getSimpleName().replace("Status", "")).toList(),
                pokemon.tool().map(BoardView::card).orElse(null));
    }
}
