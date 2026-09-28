package com.tcgpocket.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import tools.jackson.databind.json.JsonMapper;

import java.util.Optional;

/**
 * Starts the server: one WebSocket endpoint, {@code /play}. The page players
 * use is the {@code web/} client.
 */
@SpringBootApplication
@EnableWebSocket
public class ServerApplication implements WebSocketConfigurer {

    private final JsonMapper json;
    private final Optional<Ratings> ratings;

    public ServerApplication(JsonMapper json, Optional<Ratings> ratings) {
        this.json = json;
        this.ratings = ratings;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new GameSocket(json, ratings), "/play");
    }
}
