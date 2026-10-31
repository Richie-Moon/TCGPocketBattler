package com.tcgpocket.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import tools.jackson.databind.json.JsonMapper;

import java.util.Optional;

/**
 * Starts the server: two WebSocket endpoints, {@code /play} and the owner's
 * {@code /sandbox}. The page players use is the {@code web/} client.
 */
@SpringBootApplication
@EnableWebSocket
public class ServerApplication implements WebSocketConfigurer {

    private final JsonMapper json;
    private final Optional<Ratings> ratings;
    private final Optional<Decks> decks;
    private final String sandboxOwner;

    public ServerApplication(JsonMapper json, Optional<Ratings> ratings, Optional<Decks> decks,
                             @Value("${SANDBOX_OWNER_SUBJECT:}") String sandboxOwner) {
        this.json = json;
        this.ratings = ratings;
        this.decks = decks;
        this.sandboxOwner = sandboxOwner;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new GameSocket(json, ratings, decks), "/play");
        registry.addHandler(new SandboxSocket(json, sandboxOwner), "/sandbox");
    }
}
