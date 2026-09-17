# TCGPocketBattler

A battle simulator for the Pokémon TCG Pocket.

**Status: design and planning complete (for now). Card implementations are being worked on currently.** 

The core idea is that a card's text is *data* – an immutable tree of small
composable, object-oriented nodes the engine evaluates, rather than per-card code. Start at
[`docs/UML/README.md`](docs/UML/README.md), then
[`docs/UML/Overview.puml`](docs/UML/Overview.puml).

## Build

Java 25 and Maven.

```bash
mvn test       # compile and run tests
mvn package    # build the jars
java -jar server/target/server-0.1.0-SNAPSHOT.jar   # start the game server on port 8080
```

The browser client in `web/` is a separate npm project (Vite + React + TypeScript). With the server
running:

```bash
cd web
npm install
npm run dev    # then open http://localhost:5173 in two tabs
```

## Next

- Model all cards in-game
  - Base cards from A1 and P-A are complete, enough to begin other work
- Game client and server to host battles: `server` plays a game between two browser tabs, and `web/` is the start of the website
- `GameEvent`/`Trigger` → turn engine and `legalActions` →
`RandomPlayer`/`ScriptedPlayer`.

### To Do
- All prompts need to match in game text (e.g. A1-071 Seadra: This attack does 50 damage to 1 of your opponent's Pokémon.)
- Check whether coin flips run when the condition is already met (e.g. coin flip attack applies status to opponent, but the status is already applied. Does the coin flip still happen?)
- Fix "Look at opponents' hand" for Mew and Pokédex after the appropriate engine architecture is implemented

## Credits

[TCGDex](https://github.com/tcgdex) for card data. 