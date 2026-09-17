package com.tcgpocket.server;

import com.tcgpocket.energy.Type;
import com.tcgpocket.engine.TurnEngine;
import com.tcgpocket.pool.CardPool;
import com.tcgpocket.resolve.RandomSource;
import com.tcgpocket.state.Battle;
import com.tcgpocket.state.CardInstance;
import com.tcgpocket.state.Side;
import com.tcgpocket.state.Zone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

/**
 * One game between two browsers.
 *
 * <p>Before every question it sends each player their own
 * {@link BoardView}, so the player who is waiting still sees the board change.
 */
final class Game {

    private static final Logger LOG = LoggerFactory.getLogger(Game.class);

    // ponytail: fixed decks, so nothing a browser sends needs validating yet; accept deck lists once there is a deck-list validator.
    static final List<String> LIGHTNING_DECK = List.of(
            "A1-094", "A1-094", "A1-095", "A1-095", "A1-096", "A1-096", "A1-097", "A1-097", "A1-098", "A1-098",
            "A1-103", "A1-103", "A1-226", "A1-226", "P-A-001", "P-A-001", "P-A-005", "P-A-005", "P-A-007", "P-A-007");
    static final List<String> FIRE_DECK = List.of(
            "A1-033", "A1-033", "A1-034", "A1-034", "A1-036", "A1-036", "A1-039", "A1-039", "A1-040", "A1-040",
            "A1-046", "A1-046", "A1-221", "A1-221", "P-A-001", "P-A-001", "P-A-005", "P-A-005", "P-A-007", "P-A-007");

    record StateMessage(String type, BoardView board) {
        StateMessage(BoardView board) {
            this("state", board);
        }
    }

    record OverMessage(String type, BoardView board, String result) {
        OverMessage(BoardView board, String result) {
            this("over", board, result);
        }
    }

    private record Seat(RemotePlayer player, Side side, Consumer<Object> send) {
    }

    private final List<Seat> seats;
    private final Battle battle;
    private int nextInstanceId;

    Game(Consumer<Object> sendFirst, Consumer<Object> sendSecond, RandomSource rng) {
        RemotePlayer first = new RemotePlayer("Player 1", sendFirst, this::broadcast);
        RemotePlayer second = new RemotePlayer("Player 2", sendSecond, this::broadcast);
        Side firstSide = deal(new Side(first.name(), first), LIGHTNING_DECK, Type.LIGHTNING);
        Side secondSide = deal(new Side(second.name(), second), FIRE_DECK, Type.FIRE);

        this.seats = List.of(new Seat(first, firstSide, sendFirst), new Seat(second, secondSide, sendSecond));
        this.battle = rng.nextBoolean()
                ? new Battle(firstSide, secondSide, rng)
                : new Battle(secondSide, firstSide, rng);
    }

    List<RemotePlayer> players() {
        return seats.stream().map(Seat::player).toList();
    }

    /** Plays to the end, then tells both players the result. Never throws. */
    void run() {
        String result;
        try {
            result = new TurnEngine(battle).playGame()
                    .map(winner -> winner.name() + " wins")
                    .orElse("Tie");
        } catch (RemotePlayer.Left e) {
            result = "A player left";
        } catch (RuntimeException e) {
            LOG.error("game crashed", e);
            result = "The game crashed";
        }
        for (Seat seat : seats) {
            seat.send().accept(new OverMessage(BoardView.of(battle, seat.side()), result));
        }
    }

    /** Ends the game at the next question, whoever it is for. */
    void abandon() {
        seats.forEach(seat -> seat.player().leave());
    }

    private void broadcast() {
        for (Seat seat : seats) {
            seat.send().accept(new StateMessage(BoardView.of(battle, seat.side())));
        }
    }

    private Side deal(Side side, List<String> deck, Type energy) {
        for (String id : deck) {
            side.addToDeck(new CardInstance(nextInstanceId++, CardPool.get(id), side, Zone.DECK));
        }
        side.registerTypes(energy);
        return side;
    }
}
