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
mvn package    # build the jar
```

## Next

- Model all cards in-game. 
- Game client and server to host battles (probably in a separate repository)
- `GameEvent`/`Trigger` → turn engine and `legalActions` →
`RandomPlayer`/`ScriptedPlayer`.
