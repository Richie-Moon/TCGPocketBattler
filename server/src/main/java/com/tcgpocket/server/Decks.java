package com.tcgpocket.server;

import com.tcgpocket.energy.Type;
import com.tcgpocket.engine.DeckValidator;
import com.tcgpocket.pool.CardPool;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.json.JsonMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A signed-in player's saved decks. Every statement is scoped to the owner's Google {@code subject},
 * so one player can never read, change or delete another's deck by guessing its id.
 *
 * <p>Legality is not checked here: {@code DeckValidator} is the caller's job, so a half-built deck can
 * still be saved.
 */
@Component
@Profile("db")
class Decks {

    /** {@code cards} are printed ids ({@code "A1-094"}), in the order the player listed them. */
    record Deck(long id, String name, List<String> cards, Set<Type> energy) {
    }

    private static final String OWNER = "(SELECT id FROM users WHERE provider = 'google' AND subject = ?)";

    private final JdbcClient db;
    private final JsonMapper json;

    Decks(JdbcClient db, JsonMapper json) {
        this.db = db;
        this.json = json;
    }

    /** Most recently edited first. */
    List<Deck> all(String subject) {
        return db.sql("SELECT id, name, JSON_SERIALIZE(cards), JSON_SERIALIZE(energy) FROM decks"
                        + " WHERE user_id = " + OWNER + " ORDER BY updated_at DESC")
                .param(subject)
                .query((row, n) -> deck(row))
                .list();
    }

    Optional<Deck> find(String subject, long id) {
        return db.sql("SELECT id, name, JSON_SERIALIZE(cards), JSON_SERIALIZE(energy) FROM decks"
                        + " WHERE id = ? AND user_id = " + OWNER)
                .params(id, subject)
                .query((row, n) -> deck(row))
                .optional();
    }

    /** The new deck's id. */
    long create(String subject, String name, List<String> cards, Set<Type> energy) {
        KeyHolder key = new GeneratedKeyHolder();
        db.sql("INSERT INTO decks (user_id, name, cards, energy) VALUES (" + OWNER + ", ?, JSON(?), JSON(?))")
                .params(subject, name, json.writeValueAsString(cards), json.writeValueAsString(energy))
                .update(key, "id");
        return key.getKeyAs(Number.class).longValue();
    }

    /** False when there is no such deck, or it is someone else's. */
    boolean update(String subject, long id, String name, List<String> cards, Set<Type> energy) {
        return db.sql("UPDATE decks SET name = ?, cards = JSON(?), energy = JSON(?), updated_at = SYSTIMESTAMP"
                        + " WHERE id = ? AND user_id = " + OWNER)
                .params(name, json.writeValueAsString(cards), json.writeValueAsString(energy), id, subject)
                .update() == 1;
    }

    /** False when there is no such deck, or it is someone else's. */
    boolean delete(String subject, long id) {
        return db.sql("DELETE FROM decks WHERE id = ? AND user_id = " + OWNER)
                .params(id, subject)
                .update() == 1;
    }

    private Deck deck(ResultSet row) throws SQLException {
        return new Deck(row.getLong(1), row.getString(2),
                List.of(json.readValue(row.getString(3), String[].class)),
                Set.of(json.readValue(row.getString(4), Type[].class)));
    }

    /**
     * {@code /api/decks}, for the signed-in player only: 401 when signed out, 404 for a deck that is
     * missing or someone else's. Drafts are allowed, so a deck is only checked for being well-formed
     * (known card ids, no more than a full deck), never for legality.
     */
    @RestController
    @RequestMapping("/api/decks")
    @Profile("db")
    static class Api {

        /** What the browser sends to create or replace a deck. */
        record Draft(String name, List<String> cards, Set<Type> energy) {
        }

        private final Decks decks;

        Api(Decks decks) {
            this.decks = decks;
        }

        @GetMapping
        List<Deck> all(@AuthenticationPrincipal OidcUser user) {
            return decks.all(subject(user));
        }

        @GetMapping("/{id}")
        Deck find(@AuthenticationPrincipal OidcUser user, @PathVariable long id) {
            return decks.find(subject(user), id).orElseThrow(Api::notFound);
        }

        @PostMapping
        ResponseEntity<Deck> create(@AuthenticationPrincipal OidcUser user, @RequestBody Draft draft) {
            String subject = subject(user);
            check(draft);
            long id = decks.create(subject, draft.name().strip(), draft.cards(), draft.energy());
            return ResponseEntity.status(HttpStatus.CREATED).body(decks.find(subject, id).orElseThrow());
        }

        @PutMapping("/{id}")
        Deck update(@AuthenticationPrincipal OidcUser user, @PathVariable long id, @RequestBody Draft draft) {
            String subject = subject(user);
            check(draft);
            if (!decks.update(subject, id, draft.name().strip(), draft.cards(), draft.energy())) {
                throw notFound();
            }
            return decks.find(subject, id).orElseThrow();
        }

        @DeleteMapping("/{id}")
        ResponseEntity<Void> delete(@AuthenticationPrincipal OidcUser user, @PathVariable long id) {
            if (!decks.delete(subject(user), id)) {
                throw notFound();
            }
            return ResponseEntity.noContent().build();
        }

        private static String subject(OidcUser user) {
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            return user.getSubject();
        }

        /** Well-formed, not legal: a half-built deck is fine, a made-up card id is not. */
        static void check(Draft draft) {
            if (draft.name() == null || draft.name().isBlank() || draft.name().strip().length() > 100) {
                throw badRequest("A deck needs a name of 1 to 100 characters");
            }
            if (draft.cards() == null || draft.cards().size() > DeckValidator.DECK_SIZE) {
                throw badRequest("A deck holds at most " + DeckValidator.DECK_SIZE + " cards");
            }
            draft.cards().stream().filter(id -> id == null || CardPool.find(id).isEmpty()).findFirst()
                    .ifPresent(id -> {
                        throw badRequest("No card has the id " + id);
                    });
            if (draft.energy() == null || draft.energy().stream().anyMatch(Objects::isNull)
                    || draft.energy().size() > DeckValidator.MAX_ENERGY_TYPES) {
                throw badRequest("Choose at most " + DeckValidator.MAX_ENERGY_TYPES + " energy types");
            }
        }

        private static ResponseStatusException notFound() {
            return new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        private static ResponseStatusException badRequest(String reason) {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        }
    }
}
