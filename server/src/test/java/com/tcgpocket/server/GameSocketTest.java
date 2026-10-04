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
    @DisplayName("two browsers play a game to the end; bad answers are rejected, the opponent's hand is never sent, and each sees the other's moves")
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
            assertTrue(client.logs.contains("End turn"), "the opponent's moves are logged");
            assertTrue(client.logs.stream().noneMatch(text -> text.startsWith("Active ")), "the opponent's setup was logged");
            assertEquals("setup", client.firstDecisionKind, "setup is the first question");
            assertFalse(client.opponentPlacedBeforeSetup, "the opponent's opening was shown before this player chose");
            assertFalse(client.boards.isEmpty());
            for (JsonNode board : client.boards) {
                JsonNode opponent = board.get("opponent");
                assertTrue(opponent.get("hand").isEmpty(), "opponent's hand was sent");
            }
        }
    }

    @Test
    @DisplayName("/play?bot starts a game at once against a bot that is never sent anything, and each bot move gets its own board")
    void oneClientPlaysTheBot() throws Exception {
        Client client = connect(new Client(new Random(3), false), "/play?bot");

        String result = client.over.get(60, TimeUnit.SECONDS);
        assertTrue(result.matches("Player 1 wins|Bot wins|Tie"), result);
        assertEquals(List.of(), client.errors);
        assertEquals(List.of(), client.badIds);
        assertEquals("setup", client.firstDecisionKind);
        assertTrue(client.logs.contains("End turn"), "the bot's moves are logged");
        assertEquals(0, client.movesWithoutBoard, "two bot moves arrived with no board between them");
        assertTrue(Set.of("A1-221", "P-A-001", "P-A-005", "P-A-007").containsAll(client.shown),
                "only the Fire deck's Trainers flash, never a Pokemon: " + client.shown);
    }

    private Client connect(Client client) throws Exception {
        return connect(client, "/play");
    }

    private Client connect(Client client, String path) throws Exception {
        new StandardWebSocketClient().execute(client, "ws://localhost:" + port + path).get(10, TimeUnit.SECONDS);
        return client;
    }

    /** Answers every decision at random. A cheater first tries a closed decision and an out-of-range option, once. */
    private final class Client extends TextWebSocketHandler {
        final CompletableFuture<String> over = new CompletableFuture<>();
        final List<JsonNode> boards = new CopyOnWriteArrayList<>();
        final List<String> errors = new CopyOnWriteArrayList<>();
        final List<String> logs = new CopyOnWriteArrayList<>();
        /** The printed ids of the opponent's played cards flashed before their effect. */
        final List<String> shown = new CopyOnWriteArrayList<>();
        /** Options whose ids point at nothing on the board sent just before them. */
        final List<String> badIds = new CopyOnWriteArrayList<>();
        volatile String firstDecisionKind;
        /** Logged moves that came straight after another, so the browser could not show the first one land. */
        volatile int movesWithoutBoard;
        private boolean lastWasLog;
        volatile boolean opponentPlacedBeforeSetup;
        private final Random random;
        private boolean cheat;

        Client(Random random, boolean cheat) {
            this.random = random;
            this.cheat = cheat;
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            JsonNode node = json.readTree(message.getPayload());
            String type = node.get("type").asString();
            if (type.equals("log") && lastWasLog) {
                movesWithoutBoard++;
            }
            lastWasLog = type.equals("log");
            switch (type) {
                case "state" -> boards.add(node.get("board"));
                case "decision" -> {
                    int id = node.get("id").asInt();
                    int size = node.get("options").size();
                    checkIds(node.get("options"));
                    if (firstDecisionKind == null) {
                        firstDecisionKind = node.get("options").get(0).get("kind").asString();
                        opponentPlacedBeforeSetup = !boards.getLast().get("opponent").get("active").isNull();
                    }
                    if (cheat) {
                        cheat = false;
                        answer(session, id + 1000, 0);
                        answer(session, id, size);
                    }
                    answer(session, id, random.nextInt(size));
                }
                case "log" -> {
                    logs.add(node.get("text").asString());
                    if (!node.get("shown").isNull()) {
                        shown.add(node.get("shown").asString());
                    }
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
            Set<String> handCards = new HashSet<>();
            board.get("you").get("hand").forEach(card -> {
                hand.add(card.get("id").asInt());
                handCards.add(card.get("id").asInt() + " " + card.get("card").asString());
            });
            Set<Integer> inPlay = new HashSet<>();
            for (String side : List.of("you", "opponent")) {
                JsonNode active = board.get(side).get("active");
                if (!active.isNull()) {
                    inPlay.add(active.get("id").asInt());
                }
                board.get(side).get("bench").valueStream().filter(pokemon -> !pokemon.isNull())
                        .forEach(pokemon -> inPlay.add(pokemon.get("id").asInt()));
            }
            for (JsonNode option : options) {
                JsonNode card = option.get("card");
                JsonNode target = option.get("target");
                boolean ok = switch (option.get("kind").asString()) {
                    case "play", "evolve" -> hand.contains(card.asInt());
                    case "setup" -> hand.contains(card.asInt())
                            && option.get("bench").valueStream().allMatch(id -> id.isNull() || hand.contains(id.asInt()));
                    case "attack", "ability" -> inPlay.contains(card.asInt());
                    default -> true;
                } && (target.isNull() || inPlay.contains(target.asInt()))
                        // A flashed card is the one played, as printed.
                        && (option.get("shown").isNull() || handCards.contains(card.asInt() + " " + option.get("shown").asString()));
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
