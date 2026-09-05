# TCGPocketBattler

A battle simulator for the Pokémon TCG Pocket.

**Status: design phase.** The game is fully modelled in PlantUML; no Java yet.

The core idea is that a card's text is *data* — an immutable tree of small
composable nodes the engine evaluates — rather than per-card code. Start at
[`docs/UML/README.md`](docs/UML/README.md), then
[`docs/UML/Overview.puml`](docs/UML/Overview.puml).

## Next

Implementation, bottom-up (Maven + JUnit 5, Java 25): `Type`/`EnergyCost` →
`Battle`/`Side`/`CardInstance`/`ResolutionContext` → `INumber` → `ICondition`
→ `ITarget` → `IEffect` → `IAttempt` → `IAction` → `GameEvent`/`Trigger` →
turn engine and `legalActions` → `RandomPlayer`/`ScriptedPlayer` → a starter
set of real cards.
