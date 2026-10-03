# server/CLAUDE.md

The never-rules for the server live in the root `CLAUDE.md` and apply here too.

`server.GameSocket` is one WebSocket endpoint (`/play`) that pairs connections first come, first
served, or against the engine's `RandomPlayer` at once for `/play?bot` (unrated; the web client's
`?demo` and Practice vs bot). Each game runs `TurnEngine.playGame()` on its own virtual thread; `RemotePlayer` is the
`IPlayer` that blocks in `choose` until the browser answers. The engine was not rewritten for this.

- Options go out as `OptionView` (a label plus the instance ids it acts on), and only to the player
  being asked. `id`s in `BoardView` and `OptionView` are `CardInstance.instanceId()`, so a
  drag-and-drop client maps a gesture to an option index. `GameSocketTest` fails if an opponent's
  hand is sent or an option names an id that isn't on the board.
- Setup is the one simultaneous step: `Game` deals both hands, asks both players for their
  `TurnEngine.openingDecision` at once (each on its own virtual thread), and places neither board
  until both have confirmed. Only the game thread changes the board.
- Each player plays their selected deck (`users.selected_deck`, re-checked with `Decks.problems`);
  signed out, without the `db` profile, or with none selected they get `Game.Deal.LIGHTNING` (first
  seat) or `FIRE` (second seat, and always the bot). `engine.DeckValidator` (run by `TurnEngine.dealOpeningHands` for both sides) is what a deck
  builder should call too. There is no turn timer, no reconnect, and no game log yet (seed + deck
  lists + chosen indices would replay a game exactly). Saved decks live behind `/api/decks`
  (`Decks`, `db` profile): drafts are allowed, so saving checks only that a deck is well-formed,
  and only the selected deck is played. The deck editor browses `/api/cards` (`Cards`): printed
  text only, straight from `CardPool`, so it needs no sign-in or database.
- Persistence is an OCI Always Free Autonomous Database (23ai), off by default.
  `--spring.profiles.active=db` turns on the datasource (`application-db.properties`, wallet folder
  in `DB_WALLET`, password in `DB_PASSWORD`) and runs the idempotent `schema.sql`. Keep it free:
  `--is-free-tier true`. Users sign in with Google only (OAuth; identity is `provider` +
  `subject`). No passwords are stored, and `display_name` is deliberately not unique. `Accounts`
  wires it: the server keeps every endpoint open, and without the `db` profile there is no sign-in
  at all. The web client is what gates play: it shows the landing page unless `/api/me` answers
  200, so without the `db` profile the browser never reaches the game.
