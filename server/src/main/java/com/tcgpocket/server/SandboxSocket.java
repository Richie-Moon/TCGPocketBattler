package com.tcgpocket.server;

import com.tcgpocket.resolve.SeededRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@code /sandbox}: the owner's {@link SandboxGame}, for testing cards. Every endpoint is otherwise open, so this
 * is where the owner is checked. Refusing closes the socket before anything is sent; with no owner configured, or
 * without the db profile (so no sign-in), everyone is refused.
 *
 * <p>Takes what {@code /play} takes, {@code {"decision": id, "option": index}}, and also a {@link SandboxCommand}.
 * It has no {@code Ratings} and no {@code Decks}, so nothing here can touch the database.
 */
final class SandboxSocket extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SandboxSocket.class);

    private final JsonMapper json;
    private final String owner;
    private final SecureRandom seeds = new SecureRandom();
    private final Map<String, SandboxGame> games = new ConcurrentHashMap<>();

    /** @param owner the Google subject allowed in; blank allows nobody */
    SandboxSocket(JsonMapper json, String owner) {
        this.json = json;
        this.owner = owner;
    }

    static boolean isOwner(String owner, String subject) {
        return !owner.isBlank() && owner.equals(subject);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        Principal principal = session.getPrincipal();
        if (!isOwner(owner, principal == null ? null : principal.getName())) {
            session.close(CloseStatus.POLICY_VIOLATION);
            return;
        }
        SandboxGame game = new SandboxGame(message -> send(session, message), new SeededRandom(seeds.nextLong()));
        games.put(session.getId(), game);
        Thread.ofVirtual().name("sandbox-" + session.getId()).start(() -> {
            game.run();
            try {
                session.close();
            } catch (IOException e) {
                LOG.debug("could not close {}", session.getId(), e);
            }
        });
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        SandboxGame game = games.get(session.getId());
        if (game == null) {
            return;
        }
        try {
            JsonNode node = json.readTree(message.getPayload());
            game.submit(node.has("command")
                    ? json.treeToValue(node, SandboxCommand.class)
                    : json.treeToValue(node, GameSocket.Answer.class));
        } catch (JacksonException e) {
            send(session, new GameSocket.Error("could not read that: " + e.getOriginalMessage()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        SandboxGame game = games.remove(session.getId());
        if (game != null) {
            game.abandon();
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
}
