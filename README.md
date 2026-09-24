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

## Roadmap

### Engine
- Finish modelling the remaining sets (A1 and P-A base cards are done)
- `CoinFlipped` event and the `EventSource` target
- Player choice for which cards `DiscardFromHand` takes (currently takes them in hand order)
- Controller-relative Pokémon targets (a defender's Tool saying "your Benched Pokémon")
- Match every prompt to the in-game text (e.g. A1-071 Seadra)
- Check whether a coin flip still happens when its effect would do nothing (status already applied)

### Server
- Deck-list validator, then let players bring their own decks instead of the fixed two
- Turn timer that picks a default when a player stalls
- Reconnect to a game in progress
- Game log (seed + deck lists + chosen indices) for replays and bug reports
- Attack index on `OptionView` so the UI can tell attacks apart without the label
- Instance ids for `DistributeEnergy` placements (currently text only)
- Matchmaking beyond first-come pairing (rooms / invite links)

### Web client
- Card images on the board, keyed by `card` id
- Drag and drop: hand → bench/active (play, evolve), energy → Pokémon (attach)
- Tap the Active Pokémon for an attack/ability popover, drawn by the client rather than hotspots on the art
- Clear turn, prompt and coin-flip feedback; game-over screen
- Deck builder, once the server accepts deck lists

### Later
- AI opponent (start from `RandomPlayer`, then something that searches `legalActions`)
- Load card data from files instead of the Java constants in `pool` (only `CardPool` changes)

## Credits

[TCGDex](https://github.com/tcgdex) for card data. 