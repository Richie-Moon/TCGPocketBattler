package com.tcgpocket.server;

import com.tcgpocket.resolve.SeededRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pairs connections into games and passes answers from the browser to the game.
 *
 * <p>Matchmaking is first come, first served: a connection waits until the
 * next one arrives, and the two play each other.
 *
 * <p>Each game runs on its own virtual thread and blocks inside
 * {@link RemotePlayer#choose} while it waits for a browser. The engine is left
 * as a plain loop instead of being rewritten to advance one step per message.
 *
 * <p>Protocol, all JSON. The server sends {@code state}, {@code decision},
 * {@code over}, {@code waiting} and {@code error} messages. The browser sends
 * only {@code {"decision": id, "option": index}}.
 */
final class GameSocket extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GameSocket.class);

    /** What the browser sends: which decision it is answering, and the index of its pick. */
    record Answer(int decision, int option) {
    }

    record Waiting(String type) {
        Waiting() {
            this("waiting");
        }
    }

    record Error(String type, String message) {
        Error(String message) {
            this("error", message);
        }
    }

    private record Seat(Game game, RemotePlayer player) {
    }

    private final JsonMapper json;
    private final SecureRandom seeds = new SecureRandom();
    private final Map<String, Seat> seats = new ConcurrentHashMap<>();
    private WebSocketSession waiting; // guarded by this

    GameSocket(JsonMapper json) {
        this.json = json;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        WebSocketSession opponent;
        synchronized (this) {
            if (waiting == null) {
                waiting = session;
                send(session, new Waiting());
                return;
            }
            opponent = waiting;
            waiting = null;
        }
        start(opponent, session);
    }

    private void start(WebSocketSession first, WebSocketSession second) {
        // The seed stays on the server: whoever knows it can predict every flip and shuffle.
        Game game = new Game(
                message -> send(first, message),
                message -> send(second, message),
                new SeededRandom(seeds.nextLong()));
        seats.put(first.getId(), new Seat(game, game.players().get(0)));
        seats.put(second.getId(), new Seat(game, game.players().get(1)));

        Thread.ofVirtual().name("game-" + first.getId()).start(() -> {
            game.run();
            close(first);
            close(second);
        });
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        Seat seat = seats.get(session.getId());
        if (seat == null) {
            send(session, new Error("no game yet"));
            return;
        }
        Answer answer;
        try {
            answer = json.readValue(message.getPayload(), Answer.class);
        } catch (JacksonException e) {
            send(session, new Error("expected {\"decision\": id, \"option\": index}"));
            return;
        }
        seat.player().answer(answer.decision(), answer.option())
                .ifPresent(problem -> send(session, new Error(problem)));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        synchronized (this) {
            if (waiting == session) {
                waiting = null;
            }
        }
        Seat seat = seats.remove(session.getId());
        if (seat != null) {
            seat.game().abandon();
        }
    }

    /** Called from the game thread and from socket threads, so one send per session at a time. */
    private void send(WebSocketSession session, Object message) {
        synchronized (session) {
            if (!session.isOpen()) {
                return;
            }
            try {
                session.sendMessage(new TextMessage(json.writeValueAsString(message)));
            } catch (IOException e) {
                LOG.debug("could not send to {}", session.getId(), e);
            }
        }
    }

    private static void close(WebSocketSession session) {
        try {
            session.close();
        } catch (IOException e) {
            LOG.debug("could not close {}", session.getId(), e);
        }
    }
}
