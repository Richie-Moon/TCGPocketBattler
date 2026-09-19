package com.tcgpocket.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("GameSocket")
class GameSocketTest {

    @LocalServerPort
    int port;

    @Autowired
    JsonMapper json;

    @Test
    @DisplayName("two browsers play a game to the end; bad answers are rejected and the opponent's hand is never sent")
    void twoClientsPlayAGame() throws Exception {
        Client cheater = connect(new Client(new Random(1), true));
        Client honest = connect(new Client(new Random(2), false));

        String result = cheater.over.get(60, TimeUnit.SECONDS);
        assertEquals(result, honest.over.get(60, TimeUnit.SECONDS));
        assertTrue(result.matches("Player [12] wins|Tie"), result);

        assertEquals(List.of("is not open", "is out of range"), cheater.errors);
        assertEquals(List.of(), honest.errors);
        assertEquals(List.of(), cheater.badIds);
        assertEquals(List.of(), honest.badIds);
        for (Client client : List.of(cheater, honest)) {
            assertFalse(client.boards.isEmpty());
            for (JsonNode board : client.boards) {
                JsonNode opponent = board.get("opponent");
                assertTrue(opponent.get("hand").isEmpty(), "opponent's hand was sent");
            }
        }
    }

    private Client connect(Client client) throws Exception {
        new StandardWebSocketClient().execute(client, "ws://localhost:" + port + "/play").get(10, TimeUnit.SECONDS);
        return client;
    }

    /** Answers every decision at random. A cheater first tries a closed decision and an out-of-range option, once. */
    private final class Client extends TextWebSocketHandler {
        final CompletableFuture<String> over = new CompletableFuture<>();
        final List<JsonNode> boards = new CopyOnWriteArrayList<>();
        final List<String> errors = new CopyOnWriteArrayList<>();
        /** Options whose ids point at nothing on the board sent just before them. */
        final List<String> badIds = new CopyOnWriteArrayList<>();
        private final Random random;
        private boolean cheat;

        Client(Random random, boolean cheat) {
            this.random = random;
            this.cheat = cheat;
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            JsonNode node = json.readTree(message.getPayload());
            switch (node.get("type").asString()) {
                case "state" -> boards.add(node.get("board"));
                case "decision" -> {
                    int id = node.get("id").asInt();
                    int size = node.get("options").size();
                    checkIds(node.get("options"));
                    if (cheat) {
                        cheat = false;
                        answer(session, id + 1000, 0);
                        answer(session, id, size);
                    }
                    answer(session, id, random.nextInt(size));
                }
                case "over" -> over.complete(node.get("result").asString());
                case "error" -> errors.add(node.get("message").asString().replaceAll("^\\w+ -?\\d+ ", ""));
                default -> { }
            }
        }

        /** A drag-and-drop client finds options by instance id, so every id must be one it can see. */
        private void checkIds(JsonNode options) {
            JsonNode board = boards.getLast();
            Set<Integer> hand = new HashSet<>();
            board.get("you").get("hand").forEach(card -> hand.add(card.get("id").asInt()));
            Set<Integer> inPlay = new HashSet<>();
            for (String side : List.of("you", "opponent")) {
                JsonNode active = board.get(side).get("active");
                if (!active.isNull()) {
                    inPlay.add(active.get("id").asInt());
                }
                board.get(side).get("bench").forEach(pokemon -> inPlay.add(pokemon.get("id").asInt()));
            }
            for (JsonNode option : options) {
                JsonNode card = option.get("card");
                JsonNode target = option.get("target");
                boolean ok = switch (option.get("kind").asString()) {
                    case "play", "evolve" -> hand.contains(card.asInt());
                    case "attack", "ability" -> inPlay.contains(card.asInt());
                    default -> true;
                } && (target.isNull() || inPlay.contains(target.asInt()));
                if (!ok) {
                    badIds.add(option.toString());
                }
            }
        }

        private void answer(WebSocketSession session, int decision, int option) throws Exception {
            session.sendMessage(new TextMessage("{\"decision\": " + decision + ", \"option\": " + option + "}"));
        }
    }
}
