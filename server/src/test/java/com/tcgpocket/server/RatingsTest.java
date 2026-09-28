package com.tcgpocket.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Ratings")
class RatingsTest {

    @Test
    @DisplayName("two newcomers: the winner gains what the loser drops, and both grow more certain")
    void newcomers() {
        Ratings.Glicko winner = Ratings.glicko(Ratings.NEW, Ratings.NEW, 1);
        Ratings.Glicko loser = Ratings.glicko(Ratings.NEW, Ratings.NEW, 0);
        assertEquals(1662.3, winner.rating(), 0.1);
        assertEquals(1337.7, loser.rating(), 0.1);
        assertEquals(290.2, winner.deviation(), 0.1);
        assertEquals(winner.deviation(), loser.deviation(), 1e-9);
        assertEquals(1500, Ratings.glicko(Ratings.NEW, Ratings.NEW, 0.5).rating(), 1e-9);
    }

    @Test
    @DisplayName("deviation grows while idle, but never past a newcomer's")
    void idle() {
        assertEquals(50, Ratings.idle(new Ratings.Glicko(1700, 50), 0).deviation(), 1e-9);
        assertEquals(Math.sqrt(50 * 50 + 34.6 * 34.6 * 10), Ratings.idle(new Ratings.Glicko(1700, 50), 10).deviation(), 1e-9);
        assertEquals(350, Ratings.idle(new Ratings.Glicko(1700, 50), 1000).deviation(), 1e-9);
    }

    @Test
    @DisplayName("Elo: K is 50 below 1300 and 32 from 1600, and never drops below 1000")
    void elo() {
        assertEquals(1025, Ratings.elo(1000, 1000, 1));
        assertEquals(1000, Ratings.elo(1000, 1000, 0));
        assertEquals(1684, Ratings.elo(1700, 1700, 0));
    }
}
