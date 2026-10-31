package com.tcgpocket.server;

import com.tcgpocket.resolve.SeededRandom;
import jakarta.websocket.ContainerProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Sandbox")
class SandboxTest {

    private static final JsonMapper JSON = JsonMapper.builder().build();

    private static final String PIKACHU = "A1-094";
    private static final String FIRE_ATTACKER = "A1-033";

    @Nested
    @DisplayName("access")
    class Access {

        @Test
        @DisplayName("only the configured subject is the owner, and a blank owner lets nobody in")
        void ownerMatch() {
            assertTrue(SandboxSocket.isOwner("me", "me"));
            assertFalse(SandboxSocket.isOwner("me", "you"));
            assertFalse(SandboxSocket.isOwner("me", null));
            assertFalse(SandboxSocket.isOwner("", ""));
            assertFalse(SandboxSocket.isOwner("", null));
        }
    }

    @Nested
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "SANDBOX_OWNER_SUBJECT=the-owner")
    @DisplayName("/sandbox")
    class Socket {

        @LocalServerPort
        int port;

        @Test
        @DisplayName("closes on a signed-out connection without sending it anything")
        void refusesSignedOut() throws Exception {
            List<String> received = new CopyOnWriteArrayList<>();
            CompletableFuture<CloseStatus> closed = new CompletableFuture<>();
            new StandardWebSocketClient(ContainerProvider.getWebSocketContainer()).execute(new TextWebSocketHandler() {
                @Override
                protected void handleTextMessage(WebSocketSession session, TextMessage message) {
                    received.add(message.getPayload());
                }

                @Override
                public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
                    closed.complete(status);
                }
            }, "ws://localhost:" + port + "/sandbox").get(10, TimeUnit.SECONDS);

            assertEquals(CloseStatus.POLICY_VIOLATION.getCode(), closed.get(10, TimeUnit.SECONDS).getCode());
            assertEquals(List.of(), received);
        }
    }

    @Nested
    @DisplayName("a game")
    class AGame {

        private final BlockingQueue<JsonNode> sent = new LinkedBlockingQueue<>();
        private final SandboxGame game = new SandboxGame(this::record, new SeededRandom(1));
        private final Thread thread = Thread.ofVirtual().start(game::run);

        private void record(Object message) {
            sent.add(JSON.valueToTree(message));
        }

        @AfterEach
        void leave() throws Exception {
            game.abandon();
            thread.join(5000);
        }

        /** The next message of a type, skipping others. */
        private JsonNode next(String type) throws Exception {
            while (true) {
                JsonNode message = sent.poll(10, TimeUnit.SECONDS);
                assertNotNull(message, "nothing sent; wanted " + type);
                if (message.get("type").asString().equals(type)) {
                    return message;
                }
            }
        }

        /** Sends an edit, and returns the board and decision it refreshed to. */
        private JsonNode[] edit(String command) throws Exception {
            game.submit(JSON.readValue(command, SandboxCommand.class));
            return new JsonNode[] {next("state").get("board"), next("decision")};
        }

        private int option(JsonNode decision, String kind) {
            JsonNode options = decision.get("options");
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).get("kind").asString().equals(kind)) {
                    return i;
                }
            }
            throw new AssertionError("no " + kind + " option in " + options);
        }

        private JsonNode[] answer(JsonNode decision, int option) throws Exception {
            game.submit(new GameSocket.Answer(decision.get("id").asInt(), option));
            return new JsonNode[] {next("state").get("board"), next("decision")};
        }

        @Test
        @DisplayName("starts empty on turn 1 with both hands and decks shown, and only End turn to do")
        void startsEmpty() throws Exception {
            JsonNode board = next("state").get("board");
            JsonNode decision = next("decision");

            assertEquals(1, board.get("turn").asInt());
            assertTrue(board.get("yourTurn").asBoolean());
            assertTrue(board.get("you").get("active").isNull());
            assertTrue(board.get("opponent").get("sandbox").get("deck").isEmpty());
            assertEquals(1, decision.get("options").size());
            assertEquals("endTurn", decision.get("options").get(0).get("kind").asString());
            assertTrue(decision.get("editable").asBoolean());
        }

        @Test
        @DisplayName("an edit refreshes the legal moves: granting energy makes an attack legal")
        void energyMakesAnAttackLegal() throws Exception {
            next("decision");
            JsonNode[] placed = edit("""
                    {"command": "addCard", "opponent": false, "card": "%s", "zone": "ACTIVE"}""".formatted(PIKACHU));
            JsonNode active = placed[0].get("you").get("active");
            assertEquals(PIKACHU, active.get("card").asString());
            assertTrue(placed[1].get("options").valueStream().noneMatch(o -> o.get("kind").asString().equals("attack")));

            JsonNode[] charged = edit("""
                    {"command": "setEnergy", "pokemon": %d, "type": "LIGHTNING", "count": 5}""".formatted(active.get("id").asInt()));
            assertEquals(5, charged[0].get("you").get("active").get("energy").get("LIGHTNING").asInt());
            option(charged[1], "attack");
        }

        @Test
        @DisplayName("the opponent's turn is answered by the same browser, and its decisions say so")
        void playsBothSides() throws Exception {
            JsonNode decision = next("decision");
            assertFalse(decision.get("opponent").asBoolean());
            JsonNode[] theirs = answer(decision, option(decision, "endTurn"));

            assertTrue(theirs[1].get("opponent").asBoolean());
            assertEquals(2, theirs[0].get("turn").asInt());
            assertFalse(theirs[0].get("yourTurn").asBoolean());
        }

        @Test
        @DisplayName("lethal damage scores a real knockout, and play goes on with a side's last Pokemon gone")
        void lethalDamageKnocksOut() throws Exception {
            next("decision");
            edit("""
                    {"command": "addCard", "opponent": false, "card": "%s", "zone": "ACTIVE"}""".formatted(PIKACHU));
            JsonNode[] placed = edit("""
                    {"command": "addCard", "opponent": true, "card": "%s", "zone": "ACTIVE"}""".formatted(FIRE_ATTACKER));
            int target = placed[0].get("opponent").get("active").get("id").asInt();

            JsonNode[] after = edit("""
                    {"command": "setDamage", "pokemon": %d, "damage": 9999}""".formatted(target));
            assertEquals(1, after[0].get("you").get("points").asInt());
            assertTrue(after[0].get("opponent").get("active").isNull());

            JsonNode[] theirs = answer(after[1], option(after[1], "endTurn"));
            assertEquals(2, theirs[0].get("turn").asInt());
        }

        @Test
        @DisplayName("three points and the last turn of a real game end nothing")
        void noWinAndNoTurnLimit() throws Exception {
            next("decision");
            edit("""
                    {"command": "addCard", "opponent": true, "card": "%s", "zone": "ACTIVE"}""".formatted(PIKACHU));
            edit("""
                    {"command": "addCard", "opponent": true, "card": "%s", "zone": "BENCH"}""".formatted(PIKACHU));
            edit("""
                    {"command": "setPoints", "opponent": false, "points": 2}""");
            JsonNode[] last = edit("""
                    {"command": "setTurn", "turn": 30}""");
            int target = last[0].get("opponent").get("active").get("id").asInt();

            game.submit(JSON.readValue("""
                    {"command": "setDamage", "pokemon": %d, "damage": 9999}""".formatted(target), SandboxCommand.class));
            JsonNode board = next("state").get("board");
            JsonNode decision = next("decision");
            assertEquals(3, board.get("you").get("points").asInt());
            assertFalse(board.get("opponent").get("active").isNull());

            JsonNode[] beyond = answer(decision, option(decision, "endTurn"));
            assertEquals(31, beyond[0].get("turn").asInt());
        }

        @Test
        @DisplayName("queued coins land in order and are shown like any flip, and the rest stay queued")
        void queuedCoins() throws Exception {
            next("decision");
            JsonNode[] attacker = edit("""
                    {"command": "addCard", "opponent": false, "card": "A1-111", "zone": "ACTIVE"}""");
            edit("""
                    {"command": "setEnergy", "pokemon": %d, "type": "LIGHTNING", "count": 2}""".formatted(attacker[0].get("you").get("active").get("id").asInt()));
            edit("""
                    {"command": "addCard", "opponent": true, "card": "A1-096", "zone": "ACTIVE"}""");
            JsonNode[] queued = edit("""
                    {"command": "queueCoins", "coins": [false, true]}""");
            assertEquals(2, queued[1].get("coins").size());

            game.submit(new GameSocket.Answer(queued[1].get("id").asInt(), option(queued[1], "attack")));

            JsonNode state = next("state");
            assertEquals(1, state.get("flips").size());
            assertFalse(state.get("flips").get(0).asBoolean());
            JsonNode target = state.get("board").get("opponent").get("active");
            assertEquals(40, target.get("maxHp").asInt() - target.get("hp").asInt(), "tails adds nothing to the 40");
            JsonNode next = next("decision");
            assertEquals(1, next.get("coins").size());
            assertTrue(next.get("coins").get(0).asBoolean());

            JsonNode[] cleared = edit("""
                    {"command": "clearCoins"}""");
            assertTrue(cleared[1].get("coins").isEmpty());
        }

        @Test
        @DisplayName("a bad command is an error, and the question stays open")
        void badCommand() throws Exception {
            JsonNode decision = next("decision");
            game.submit(JSON.readValue("""
                    {"command": "addCard", "opponent": false, "card": "nope", "zone": "HAND"}""", SandboxCommand.class));
            assertTrue(next("error").get("message").asString().contains("nope"));

            game.submit(new GameSocket.Answer(decision.get("id").asInt() + 100, 0));
            assertTrue(next("error").get("message").asString().contains("not open"));

            answer(decision, 0); // still answerable
        }

        @Test
        @DisplayName("board edits wait while a question in the middle of an effect is open, but coins don't")
        void editsWaitForAMidEffectQuestion() throws Exception {
            next("decision");
            edit("""
                    {"command": "addCard", "opponent": false, "card": "%s", "zone": "ACTIVE"}""".formatted(PIKACHU));
            edit("""
                    {"command": "addCard", "opponent": false, "card": "%s", "zone": "BENCH"}""".formatted(PIKACHU));
            edit("""
                    {"command": "addCard", "opponent": false, "card": "%s", "zone": "BENCH"}""".formatted(PIKACHU));
            JsonNode[] placed = edit("""
                    {"command": "addCard", "opponent": true, "card": "%s", "zone": "ACTIVE"}""".formatted(FIRE_ATTACKER));
            int active = placed[0].get("you").get("active").get("id").asInt();

            // Knocking out your Active with two on the Bench asks which to promote.
            JsonNode[] promoting = edit("""
                    {"command": "setDamage", "pokemon": %d, "damage": 9999}""".formatted(active));
            assertFalse(promoting[1].get("editable").asBoolean());

            game.submit(JSON.readValue("""
                    {"command": "setPoints", "opponent": false, "points": 2}""", SandboxCommand.class));
            assertTrue(next("error").get("message").asString().contains("before editing"));

            JsonNode[] queued = edit("""
                    {"command": "queueCoins", "coins": [true]}""");
            assertEquals(promoting[1].get("id").asInt(), queued[1].get("id").asInt());
            assertEquals(1, queued[1].get("coins").size());
        }

        @Test
        @DisplayName("points, the turn and the Energy Zone can be set, even down, and an impossible value changes nothing")
        void setsState() throws Exception {
            next("decision");
            edit("""
                    {"command": "setPoints", "opponent": true, "points": 2}""");
            JsonNode[] lowered = edit("""
                    {"command": "setPoints", "opponent": true, "points": 1}""");
            assertEquals(1, lowered[0].get("opponent").get("points").asInt());

            assertEquals(7, edit("""
                    {"command": "setTurn", "turn": 7}""")[0].get("turn").asInt());

            JsonNode you = edit("""
                    {"command": "setEnergyZone", "opponent": false, "current": "FIRE", "next": "WATER", "types": ["GRASS"]}""")[0].get("you");
            assertEquals("FIRE", you.get("energy").asString());
            assertEquals("WATER", you.get("nextEnergy").asString());
            assertEquals("GRASS", you.get("sandbox").get("energyTypes").get(0).asString());

            game.submit(JSON.readValue("""
                    {"command": "setEnergyZone", "opponent": false, "current": null, "next": null, "types": ["COLORLESS"]}""", SandboxCommand.class));
            assertTrue(next("error").get("message").asString().contains("generated"));
            game.submit(JSON.readValue("""
                    {"command": "setTurn", "turn": 0}""", SandboxCommand.class));
            assertTrue(next("error").get("message").asString().contains("turn must be"));
            assertEquals("FIRE", edit("""
                    {"command": "resetTurn", "opponent": false}""")[0].get("you").get("energy").asString());
        }

        @Test
        @DisplayName("both sides generate every type from the start")
        void generatesEveryType() throws Exception {
            JsonNode board = next("state").get("board");
            assertEquals(8, board.get("you").get("sandbox").get("energyTypes").size());
            assertEquals(8, board.get("opponent").get("sandbox").get("energyTypes").size());
        }

        @Test
        @DisplayName("putting Energy in the zone lets it be attached, even after an attach this turn")
        void attachesAgainAfterSettingEnergy() throws Exception {
            next("decision");
            edit("""
                    {"command": "addCard", "opponent": false, "card": "%s", "zone": "ACTIVE"}""".formatted(PIKACHU));
            JsonNode[] charged = edit("""
                    {"command": "setEnergyZone", "opponent": false, "current": "FIRE", "next": null, "types": ["FIRE"]}""");
            JsonNode[] attached = answer(charged[1], option(charged[1], "attach"));

            JsonNode[] again = edit("""
                    {"command": "setEnergyZone", "opponent": false, "current": "WATER", "next": null, "types": ["FIRE"]}""");
            answer(again[1], option(again[1], "attach"));
            assertTrue(attached[1].get("options").valueStream().noneMatch(o -> o.get("kind").asString().equals("attach")));
        }

        @Test
        @DisplayName("Ready to evolve lifts the first-turn rule; without it, nothing evolves on turn 1")
        void readyToEvolve() throws Exception {
            next("decision");
            JsonNode[] active = edit("""
                    {"command": "addCard", "opponent": false, "card": "%s", "zone": "ACTIVE"}""".formatted(PIKACHU));
            JsonNode[] holding = edit("""
                    {"command": "addCard", "opponent": false, "card": "A1-095", "zone": "HAND"}""");
            assertTrue(holding[1].get("options").valueStream().noneMatch(o -> o.get("kind").asString().equals("evolve")));

            JsonNode[] ready = edit("""
                    {"command": "setPlayedThisTurn", "pokemon": %d, "played": false}""".formatted(active[0].get("you").get("active").get("id").asInt()));
            option(ready[1], "evolve");
        }

        @Test
        @DisplayName("the deck is kept in order and a card can be moved within it")
        void deckOrder() throws Exception {
            next("decision");
            edit("""
                    {"command": "addCard", "opponent": false, "card": "%s", "zone": "DECK"}""".formatted(PIKACHU));
            JsonNode[] two = edit("""
                    {"command": "addCard", "opponent": false, "card": "%s", "zone": "DECK", "position": 0}""".formatted(FIRE_ATTACKER));
            JsonNode deck = two[0].get("you").get("sandbox").get("deck");
            assertEquals(FIRE_ATTACKER, deck.get(0).get("card").asString());

            JsonNode[] moved = edit("""
                    {"command": "move", "card": %d, "zone": "DECK"}""".formatted(deck.get(0).get("id").asInt()));
            assertEquals(PIKACHU, moved[0].get("you").get("sandbox").get("deck").get(0).get("card").asString());
        }
    }
}
