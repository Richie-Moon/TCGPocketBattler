# TCGPocketBattler

A battle simulator for the Pokémon TCG Pocket.

**Status: design complete, implementation not started.** The game is fully
modelled in PlantUML and the build is scaffolded; there is no game code yet.

The core idea is that a card's text is *data* — an immutable tree of small
composable nodes the engine evaluates — rather than per-card code. Start at
[`docs/UML/README.md`](docs/UML/README.md), then
[`docs/UML/Overview.puml`](docs/UML/Overview.puml).

## Build

Java 25 and Maven.

```bash
mvn test       # compile and run tests
mvn package    # build the jar
```

Sealed interfaces, records and exhaustive pattern-matching switches are
load-bearing in the model, so `maven.compiler.release` is pinned to 25.
`ToolchainTest` guards that; it can be deleted once real tests exist.

## Next

Implementation, bottom-up — each layer testable before the next:

`Type`/`EnergyCost` → `Battle`/`Side`/`CardInstance`/`ResolutionContext` →
`INumber` → `ICondition` → `ITarget` → `IEffect` → `IAttempt` → `IAction` →
`GameEvent`/`Trigger` → turn engine and `legalActions` →
`RandomPlayer`/`ScriptedPlayer` → a starter set of real cards.
