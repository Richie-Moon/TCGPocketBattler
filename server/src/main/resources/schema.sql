-- Idempotent: runs on every start under the db profile. Statements end with a / line, not ;
-- (spring.sql.init.separator), because the PL/SQL block below has ; inside it.

-- Users sign in with Google (OAuth), so no passwords are stored: (provider, subject) is the identity.
-- display_name is not unique; two players may both be "Ash".
CREATE TABLE IF NOT EXISTS users (
    id           NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    provider     VARCHAR2(20)  NOT NULL,
    subject      VARCHAR2(255) NOT NULL,
    display_name VARCHAR2(100) NOT NULL,
    created_at   TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT users_identity UNIQUE (provider, subject)
)
/

-- Columns added after the table, so databases created before them get them too. Oracle has no
-- ADD IF NOT EXISTS for columns, so ORA-01430 (column already exists) is swallowed instead.
-- profile_icon is the object-storage name "Icon_<name>.png", where <name> is a Pokemon or Trainer.
-- emblem_1..3 are the displayed emblems, "Emblem_<name>.png"; nullable, since most players pick none.
DECLARE
    PROCEDURE add_column(definition VARCHAR2) IS
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE users ADD (' || definition || ')';
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE != -1430 THEN RAISE; END IF;
    END;
BEGIN
    add_column('elo NUMBER(10) DEFAULT 1000 NOT NULL');
    add_column('wins NUMBER(10) DEFAULT 0 NOT NULL');
    add_column('losses NUMBER(10) DEFAULT 0 NOT NULL');
    add_column('profile_icon VARCHAR2(100) DEFAULT ''Icon_Default.png'' NOT NULL'
        || ' CHECK (profile_icon LIKE ''Icon!_%.png'' ESCAPE ''!'')');
    add_column('emblem_1 VARCHAR2(100) CHECK (emblem_1 LIKE ''Emblem!_%.png'' ESCAPE ''!'')');
    add_column('emblem_2 VARCHAR2(100) CHECK (emblem_2 LIKE ''Emblem!_%.png'' ESCAPE ''!'')');
    add_column('emblem_3 VARCHAR2(100) CHECK (emblem_3 LIKE ''Emblem!_%.png'' ESCAPE ''!'')');
END;
/

-- cards is a JSON array of printed ids (["A1-094", ...]) and energy an array of Type names.
-- Legality is DeckValidator's job, not the database's.
CREATE TABLE IF NOT EXISTS decks (
    id         NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id    NUMBER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    name       VARCHAR2(100) NOT NULL,
    cards      JSON NOT NULL,
    energy     JSON NOT NULL,
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL
)
/

CREATE INDEX IF NOT EXISTS decks_user ON decks (user_id)
/

-- Cosmetics are object-storage names, like users.profile_icon: "Coin_<name>.png", and so on.
DECLARE
    PROCEDURE add_column(definition VARCHAR2) IS
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE decks ADD (' || definition || ')';
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE != -1430 THEN RAISE; END IF;
    END;
BEGIN
    add_column('coin VARCHAR2(100) DEFAULT ''Coin_Pokéball.png'' NOT NULL'
        || ' CHECK (coin LIKE ''Coin!_%.png'' ESCAPE ''!'')');
    add_column('sleeve VARCHAR2(100) DEFAULT ''Sleeve_Default.png'' NOT NULL'
        || ' CHECK (sleeve LIKE ''Sleeve!_%.png'' ESCAPE ''!'')');
    add_column('playmat VARCHAR2(100) DEFAULT ''Playmat_Default.png'' NOT NULL'
        || ' CHECK (playmat LIKE ''Playmat!_%.png'' ESCAPE ''!'')');
    -- Printed ids ("A1-094"), like the entries in cards. Nullable: a draft may have none.
    add_column('focus_card_1 VARCHAR2(20)');
    add_column('focus_card_2 VARCHAR2(20)');
END;
/

-- The deck the player has chosen to play with, so at most one per player; deleting it unselects it.
-- Added here, not with the other users columns, because it needs decks to exist.
DECLARE
    PROCEDURE add_column(definition VARCHAR2) IS
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE users ADD (' || definition || ')';
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE != -1430 THEN RAISE; END IF;
    END;
BEGIN
    add_column('selected_deck NUMBER REFERENCES decks (id) ON DELETE SET NULL');
END;
