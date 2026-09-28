package com.tcgpocket.server;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * Rates signed-in players after each game, the way Pokemon Showdown's ladder does: an Elo shown on the
 * ladder, and a Glicko-1 rating and deviation that GXE is worked out from.
 *
 * <p>Real Glicko-1 rates a whole rating period (a day on Showdown) at once. Here every game is its own
 * period, which needs no table of pending results; a day still counts as one period for how fast the
 * deviation grows back while a player is away.
 */
@Component
@Profile("db")
class Ratings {

    record Glicko(double rating, double deviation) {
    }

    private record Player(int elo, Glicko glicko, Timestamp ratedAt) {
    }

    static final Glicko NEW = new Glicko(1500, 350);
    private static final double Q = Math.log(10) / 400;
    /** Deviation regained per idle day: a settled 50 is back to a newcomer's 350 after about 100 days. */
    private static final double C = 34.6;

    private final JdbcClient db;

    Ratings(JdbcClient db) {
        this.db = db;
    }

    /**
     * {@code score} is the first player's: 1 for a win, 0.5 for a tie, 0 for a loss. A game without two
     * different signed-in players is not rated.
     */
    // ponytail: synchronized read-then-write is only safe on one server; use SELECT ... FOR UPDATE if there are more.
    synchronized void record(String first, String second, double score) {
        if (first == null || second == null || first.equals(second)) {
            return;
        }
        Optional<Player> a = load(first);
        Optional<Player> b = load(second);
        if (a.isEmpty() || b.isEmpty()) {
            return;
        }
        Instant now = Instant.now();
        save(first, a.get(), b.get(), score, now);
        save(second, b.get(), a.get(), 1 - score, now);
    }

    private Optional<Player> load(String subject) {
        return db.sql("SELECT elo, glicko, glicko_rd, rated_at FROM users WHERE provider = 'google' AND subject = ?")
                .param(subject)
                .query((row, n) -> new Player(row.getInt(1),
                        new Glicko(row.getDouble(2), row.getDouble(3)), row.getTimestamp(4)))
                .optional();
    }

    private void save(String subject, Player player, Player opponent, double score, Instant now) {
        Glicko rated = glicko(idle(player, now), idle(opponent, now), score);
        db.sql("""
                UPDATE users SET elo = ?, glicko = ?, glicko_rd = ?, rated_at = ?,
                    wins = wins + ?, losses = losses + ?
                WHERE provider = 'google' AND subject = ?""")
                .params(elo(player.elo(), opponent.elo(), score), rated.rating(), rated.deviation(),
                        Timestamp.from(now), score == 1 ? 1 : 0, score == 0 ? 1 : 0, subject)
                .update();
    }

    private static Glicko idle(Player player, Instant now) {
        long days = player.ratedAt() == null ? 0 : Duration.between(player.ratedAt().toInstant(), now).toDays();
        return idle(player.glicko(), days);
    }

    /** Uncertainty grows while a player is away, up to a newcomer's. */
    static Glicko idle(Glicko glicko, long days) {
        double deviation = Math.sqrt(glicko.deviation() * glicko.deviation() + C * C * days);
        return new Glicko(glicko.rating(), Math.min(deviation, NEW.deviation()));
    }

    /** Glicko-1 after one game against {@code opponent} (Glickman, "The Glicko system", step 2). */
    static Glicko glicko(Glicko player, Glicko opponent, double score) {
        double g = 1 / Math.sqrt(1 + 3 * Q * Q * opponent.deviation() * opponent.deviation() / (Math.PI * Math.PI));
        double expected = 1 / (1 + Math.pow(10, -g * (player.rating() - opponent.rating()) / 400));
        double dSquaredInverse = Q * Q * g * g * expected * (1 - expected);
        double precision = 1 / (player.deviation() * player.deviation()) + dSquaredInverse;
        return new Glicko(player.rating() + Q / precision * g * (score - expected), Math.sqrt(1 / precision));
    }

    /**
     * Elo with a K that shrinks as the rating climbs, never below 1000.
     *
     * <p><b>Narrowed.</b> Showdown also lowers K for a loss just above 1000; this keeps only the bands.
     */
    static int elo(int elo, int opponent, double score) {
        int k = elo < 1300 ? 50 : elo < 1600 ? 40 : 32;
        double expected = 1 / (1 + Math.pow(10, (opponent - elo) / 400.0));
        return Math.max(1000, (int) Math.round(elo + k * (score - expected)));
    }
}
