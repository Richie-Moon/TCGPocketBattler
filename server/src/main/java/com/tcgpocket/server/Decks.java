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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

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

    /**
     * What the editor needs. {@code cards} are printed ids ({@code "A1-094"}), in the order the player listed
     * them; the focus cards are printed ids too, or null. Cosmetics are object-storage names ({@code "Coin_<name>.png"}).
     */
    record Deck(long id, String name, List<String> cards, Set<Type> energy,
                String focusCard1, String focusCard2, String coin, String sleeve, String playmat) {
    }

    /**
     * What the decks page needs: a {@link Deck} with its card list reduced to a count and to its
     * {@link #problems}; a deck with none is legal, which is what lets it be shared. {@code selected} marks the
     * one deck the player has chosen to play with.
     */
    record Summary(long id, String name, int cardCount, List<String> problems, Set<Type> energy,
                   String focusCard1, String focusCard2, String coin, String sleeve, String playmat,
                   boolean selected) {
    }

    /** Shared by both reads so {@link #deck} and {@link #summary} agree on column positions. */
    private static final String COLUMNS =
            "id, name, JSON_SERIALIZE(energy), focus_card_1, focus_card_2, coin, sleeve, playmat, JSON_SERIALIZE(cards)";

    private static final String OWNER = "(SELECT id FROM users WHERE provider = 'google' AND subject = ?)";

    private final JdbcClient db;
    private final JsonMapper json;

    Decks(JdbcClient db, JsonMapper json) {
        this.db = db;
        this.json = json;
    }

    /**
     * In the order they were created, oldest first, so editing a deck never moves it: the identity
     * {@code id} only grows.
     */
    List<Summary> all(String subject) {
        return db.sql("SELECT " + COLUMNS + ", CASE WHEN id = (SELECT selected_deck FROM users"
                        + " WHERE provider = 'google' AND subject = ?) THEN 1 ELSE 0 END"
                        + " FROM decks WHERE user_id = " + OWNER + " ORDER BY id")
                .params(subject, subject)
                .query((row, n) -> summary(row))
                .list();
    }

    Optional<Deck> find(String subject, long id) {
        return db.sql("SELECT " + COLUMNS + " FROM decks WHERE id = ? AND user_id = " + OWNER)
                .params(id, subject)
                .query((row, n) -> deck(row))
                .optional();
    }

    /** The new deck's id. */
    long create(String subject, Api.Draft draft) {
        KeyHolder key = new GeneratedKeyHolder();
        db.sql("INSERT INTO decks (user_id, name, cards, energy, focus_card_1, focus_card_2, coin, sleeve, playmat)"
                        + " VALUES (" + OWNER + ", ?, JSON(?), JSON(?), ?, ?, ?, ?, ?)")
                .param(subject).params(values(draft))
                .update(key, "id");
        return key.getKeyAs(Number.class).longValue();
    }

    /** False when there is no such deck, or it is someone else's. */
    boolean update(String subject, long id, Api.Draft draft) {
        return db.sql("UPDATE decks SET name = ?, cards = JSON(?), energy = JSON(?), focus_card_1 = ?, focus_card_2 = ?,"
                        + " coin = ?, sleeve = ?, playmat = ?, updated_at = SYSTIMESTAMP"
                        + " WHERE id = ? AND user_id = " + OWNER)
                .params(values(draft)).params(id, subject)
                .update() == 1;
    }

    /** A draft's columns, in the order both writes bind them. */
    private List<Object> values(Api.Draft draft) {
        return Arrays.asList(draft.name().strip(), json.writeValueAsString(draft.cards()),
                json.writeValueAsString(draft.energy()), draft.focusCard1(), draft.focusCard2(),
                draft.coin(), draft.sleeve(), draft.playmat());
    }

    /** Unselects the deck, if it was anyone's selection. */
    void unselect(long id) {
        db.sql("UPDATE users SET selected_deck = NULL WHERE selected_deck = ?").param(id).update();
    }

    /** False when there is no such deck, or it is someone else's. */
    boolean select(String subject, long id) {
        return db.sql("UPDATE users SET selected_deck = ? WHERE provider = 'google' AND subject = ?"
                        + " AND EXISTS (SELECT 1 FROM decks WHERE id = ? AND user_id = users.id)")
                .params(id, subject, id)
                .update() == 1;
    }

    /** False when there is no such deck, or it is someone else's. */
    boolean delete(String subject, long id) {
        return db.sql("DELETE FROM decks WHERE id = ? AND user_id = " + OWNER)
                .params(id, subject)
                .update() == 1;
    }

    private Deck deck(ResultSet row) throws SQLException {
        return new Deck(row.getLong(1), row.getString(2), List.of(json.readValue(row.getString(9), String[].class)),
                energy(row), row.getString(4), row.getString(5), row.getString(6), row.getString(7), row.getString(8));
    }

    private Summary summary(ResultSet row) throws SQLException {
        Deck deck = deck(row);
        return new Summary(deck.id(), deck.name(), deck.cards().size(), problems(deck.cards(), deck.energy()),
                deck.energy(), deck.focusCard1(), deck.focusCard2(), deck.coin(), deck.sleeve(), deck.playmat(),
                row.getBoolean(10));
    }

    /**
     * Why {@code DeckValidator} would keep the deck out of a game, as sentences for the player; empty when it
     * is legal. A card that has left the pool is a problem too, and the only one reported: the rest cannot be
     * checked without it.
     */
    static List<String> problems(List<String> cards, Set<Type> energy) {
        List<String> gone = cards.stream().filter(id -> CardPool.find(id).isEmpty()).distinct()
                .map(id -> id + " is no longer a card").toList();
        return gone.isEmpty() ? DeckValidator.problems(cards.stream().map(CardPool::get).toList(), energy) : gone;
    }

    private Set<Type> energy(ResultSet row) throws SQLException {
        return Set.of(json.readValue(row.getString(3), Type[].class));
    }

    /**
     * {@code /api/decks}, for the signed-in player only: 401 when signed out, 404 for a deck that is
     * missing or someone else's. The list returns {@link Summary summaries}; only {@code /{id}} carries the cards.
     * Drafts are allowed, so a deck is only checked for being well-formed
     * (known card ids, no more than a full deck, focus cards that are in it, cosmetics named like the
     * object-storage files), never for legality.
     */
    @RestController
    @RequestMapping("/api/decks")
    @Profile("db")
    static class Api {

        /** What the browser sends to create or replace a deck: a {@link Deck} without its id. */
        record Draft(String name, List<String> cards, Set<Type> energy,
                     String focusCard1, String focusCard2, String coin, String sleeve, String playmat) {
        }

        private final Decks decks;

        Api(Decks decks) {
            this.decks = decks;
        }

        @GetMapping
        List<Summary> all(@AuthenticationPrincipal OidcUser user) {
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
            long id = decks.create(subject, draft);
            return ResponseEntity.status(HttpStatus.CREATED).body(decks.find(subject, id).orElseThrow());
        }

        @PutMapping("/{id}")
        Deck update(@AuthenticationPrincipal OidcUser user, @PathVariable long id, @RequestBody Draft draft) {
            String subject = subject(user);
            check(draft);
            if (!decks.update(subject, id, draft)) {
                throw notFound();
            }
            // Only a legal deck may stay selected.
            if (!Decks.problems(draft.cards(), draft.energy()).isEmpty()) {
                decks.unselect(id);
            }
            return decks.find(subject, id).orElseThrow();
        }

        /** {@link Decks#problems} for a draft the editor has not saved, so it needs no deck id and stores nothing. */
        @PostMapping("/problems")
        List<String> problems(@RequestBody Draft draft) {
            if (draft.cards() == null || draft.cards().contains(null) || draft.energy() == null) {
                throw badRequest("A deck needs cards and energy");
            }
            return Decks.problems(draft.cards(), draft.energy());
        }

        /** Makes this the deck the player plays with, replacing any other. Only a legal deck may be selected. */
        @PutMapping("/{id}/selected")
        ResponseEntity<Void> select(@AuthenticationPrincipal OidcUser user, @PathVariable long id) {
            String subject = subject(user);
            Deck deck = decks.find(subject, id).orElseThrow(Api::notFound);
            if (!Decks.problems(deck.cards(), deck.energy()).isEmpty()) {
                throw badRequest("Only a complete deck can be selected");
            }
            if (!decks.select(subject, id)) {
                throw notFound();
            }
            return ResponseEntity.noContent().build();
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
            if (draft.name() == null || draft.name().isBlank() || draft.name().strip().length() > 22) {
                throw badRequest("A deck needs a name of 1 to 22 characters");
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
            if (Stream.of(draft.focusCard1(), draft.focusCard2())
                    .anyMatch(id -> id != null && !draft.cards().contains(id))) {
                throw badRequest("A focus card must be one of the deck's cards");
            }
            // Coin_Tails.png is the back of every coin, not one to choose.
            if (!cosmetic("Coin", draft.coin()) || draft.coin().equals("Coin_Tails.png")
                    || !cosmetic("Sleeve", draft.sleeve())
                    || !cosmetic("Playmat", draft.playmat())) {
                throw badRequest("Choose a coin, a sleeve and a playmat");
            }
        }

        /** The name ends up in an image URL, so it may only be a file name: letters, digits, {@code _ . -}. */
        private static boolean cosmetic(String kind, String name) {
            return name != null && name.length() <= 100 && name.matches(kind + "_[\\p{L}\\p{N}_.-]+\\.png");
        }

        private static ResponseStatusException notFound() {
            return new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        private static ResponseStatusException badRequest(String reason) {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        }
    }
}
