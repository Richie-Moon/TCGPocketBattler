-- Idempotent (Oracle 23ai IF NOT EXISTS): runs on every start under the db profile.

-- Users sign in with Google (OAuth), so no passwords are stored: (provider, subject) is the identity.
-- display_name is not unique; two players may both be "Ash".
CREATE TABLE IF NOT EXISTS users (
    id           NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    provider     VARCHAR2(20)  NOT NULL,
    subject      VARCHAR2(255) NOT NULL,
    display_name VARCHAR2(100) NOT NULL,
    created_at   TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT users_identity UNIQUE (provider, subject)
);

-- cards is a JSON array of printed ids (["A1-094", ...]) and energy an array of Type names.
-- Legality is DeckValidator's job, not the database's.
CREATE TABLE IF NOT EXISTS decks (
    id         NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id    NUMBER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    name       VARCHAR2(100) NOT NULL,
    cards      JSON NOT NULL,
    energy     JSON NOT NULL,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS decks_user ON decks (user_id);
