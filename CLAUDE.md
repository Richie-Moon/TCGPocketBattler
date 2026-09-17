# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & test

Maven, Java 25 (`maven.compiler.release` is 25 — sealed interfaces, records and pattern-matching
switch are load-bearing, not incidental). `-Xlint:all` is on.

Multi-module: the root `pom.xml` is the parent (Java version, JUnit, compiler/surefire config are
set there and inherited). Run Maven from the root.

- `engine/` — the rules engine; everything below except "The server" is about it. It stays free
  of networking, JSON and threading.
- `server/` — Spring Boot 4 (Jackson 3, so `tools.jackson.*`), depends on `engine`, never the
  reverse. Spring Boot is imported as a BOM because the root pom is already the parent.
- `web/` — the browser client: Vite + React + TypeScript, built with npm, **not** a Maven module.
  `src/protocol.ts` mirrors the server's message records by hand; change both in the same commit.
  The Vite dev server proxies `/play` to Spring on 8080, so there is no CORS setup.

```bash
mvn test                                  # compile + run all tests
mvn package                               # build the jar
mvn -Dtest=IEffectTest test               # one test class
mvn -Dtest=IEffectTest$Flips test         # one @Nested class (quote the $ in PowerShell)
mvn -Dtest=IEffectTest#unresolvedTargetFails test   # one method
java -jar server/target/server-0.1.0-SNAPSHOT.jar   # after mvn package; serves /play on 8080
cd web && npm install && npm run dev      # play at localhost:5173 in two tabs (needs the server)
cd web && npm run build && npm run lint   # type-check + bundle, oxlint
```

## The core idea

**A card's text is data, not code.** An attack is not a Java method; it is an immutable tree of
small composable nodes the engine evaluates. There is no `case "Pikachu"` anywhere, and adding a
card must never mean adding a branch to the engine. If a card cannot be expressed with the existing
nodes, the fix is a new node in the right sealed hierarchy (or a documented narrowing), not
special-casing.

`docs/UML/README.md` is the design document and is kept current — read it before any non-trivial
change. The `.puml` files under `docs/UML/` are the per-hierarchy diagrams.

### The four evaluators

```
INumber.evaluate(ctx)    -> int                          how much?
ICondition.evaluate(T)   -> boolean                      is it so?
ITarget.resolve(ctx)     -> Optional<PokemonInPlay>      who?
IEffect.apply(ctx)       -> EffectOutcome                do it
```

They compose upward: `IEffect` → `IAttempt` → `IAction` → `ICard.actions()`, and `ITrigger` reuses
`IAttempt` too. **Only `IEffect` mutates the board** — that read/write boundary is what lets legal-move
generation preview an attack without playing it.

### Invariants worth knowing before editing

- **Everything resolves against a `ResolutionContext`, never a bare `Battle`.** It carries
  `controller` (which differs from `battle.attacker()` whenever a defender's trigger fires),
  `source` (the Pokémon whose text is resolving — `Self` resolves to it), the `event` that fired a
  trigger, and a mutable `scope` holding coin-flip results for one resolution. Use
  `withSource`/`withController`/`withEvent` to re-point it; they keep the same scope so flips stay
  visible.
- **`NO_OP` vs `FAILED` is the whole point of `EffectOutcome`.** `FAILED` means the effect genuinely
  could not happen (unresolved target, unpayable cost) and aborts the rest of the `Attempt` — that is
  the "…**If you do**, …" clause. `NO_OP` (healing a full-HP Pokémon) does not abort. An effect that
  reports `FAILED` must leave the board untouched.
- **Definitions vs instances.** `ICard` (`PokemonCard`, `ItemCard`, …) is immutable and shared by
  every copy; `CardInstance` / `PokemonInPlay` hold per-copy zone, damage, energy, statuses, tool and
  modifiers. Because an instance knows its own `zone` and `owner`, `ICondition` takes only its
  subject — no context parameter.
- **`ICondition<T>` is generic over its subject** (`ResolutionContext`, `PokemonInPlay`,
  `CardInstance`). `For` / `ForAny` lift a Pokémon-level condition to a context-level one by resolving
  a target; combinators (`And`, `Or`, `Not`, `All`, `Any`, `Always`) are generic and compose at any
  level.
- **Triggers are collected, not registered.** `TriggerDispatcher.collect()` walks the board on every
  dispatch, so stale listeners are impossible. Do not add subscribe/unsubscribe bookkeeping.
- **Targeting is turn-relative** (`AttackerActive`, `OpponentBench`, `Self`, `ChosenFrom`, …), and
  that is deliberate — see the memory note on the controller-relative gap before "fixing" it.

Every sealed hierarchy's `permits` clause is the inventory — adding a node means editing the
interface too. The interface Javadoc carries the *why*; keep that style when adding nodes.

## Adding cards

`engine/src/main/java/com/tcgpocket/pool/A1/` — one class per energy type plus `Trainers`, in printed set
order, assembled by `GeneticApex.CARDS` and indexed by `CardPool` (which throws on duplicate ids at
class-load).

- Nothing outside `pool` may name a card class: use `CardPool.get("A1-096")`. The package is a leaf
  so the Java constants can later be swapped for a data loader by changing `CardPool` alone.
- Cards are built with `PokemonCard.basic(...)` / `.evolution(...)` plus `.withWeakness`, `.withTags`,
  `.withAbility`; Trainers with `ItemCard.of` / `SupporterCard.of` / `ToolCard` (a Tool does its work
  entirely through `Trigger`s).
- Ids, HP, retreat cost and weakness are transcribed by hand and need checking against the printed
  card. Tests assert the *shape* of the card text, which is a different claim from the numbers being
  right.
- When a card cannot be expressed exactly, implement the nearest correct narrowing and say so in a
  `<b>Narrowed.</b>` Javadoc note naming the node that would fix it (see `Trainers.POTION`).

## Tests

JUnit 5, `@Nested` + `@DisplayName` throughout, so a class reads as a list of rules.

- `TestBoard` builds a two-sided board in a line or two: `board.you` is the attacker, `board.them` the
  defender; `active/bench/inHand/inDeck/inDiscard/loose` place cards; `contextFor(pokemon)` /
  `contextWithoutSource()` build contexts. Seeded RNG by default.
- `ScriptedRandom.flipping(true, false, …)` scripts coin flips instead of hunting for a seed; the last
  value repeats once the script runs out. `ScriptedPlayer` makes player decisions deterministic.
- Test classes are named after the hierarchy they cover (`IEffectTest`, `IConditionTest`,
  `ITriggerTest`, …), not after individual node classes.

## The engine

`engine.TurnEngine` runs a game: setup, `playTurn`, `legalActions()` (attacker only, since targets are
turn-relative), `checkKnockouts`, and the win check (most win conditions wins; tie after
`MAX_TURNS`). It is the only thing that dispatches `TurnStart`, `TurnEnd` and `Knockout`. Effects and
actions announce their own events. Knockouts are checked after each action, never inside an effect.
There is deliberately no `Phase` enum.

## The server

`server.GameSocket` is one WebSocket endpoint (`/play`) that pairs connections first come, first
served. Each game runs `TurnEngine.playGame()` on its own virtual thread; `RemotePlayer` is the
`IPlayer` that blocks in `choose` until the browser answers. The engine was not rewritten for this.

- **The browser only ever sends `{"decision": id, "option": index}`.** An index into a list the
  engine already made legal, plus the decision's id so a stale answer can't land on the next
  question. Never accept a move the client describes itself.
- **`BoardView` is the only board state that leaves the server.** Never serialize `Battle`,
  `Decision` or engine objects: the opponent's hand, deck order and RNG seed are hidden. Options go
  out as text from `Labels`, and only to the player being asked. `GameSocketTest` fails if an
  opponent's hand is sent.
- Decks are fixed (`Game.LIGHTNING_DECK` / `FIRE_DECK`) until there is a deck-list validator. There
  is no turn timer, no reconnect, and no game log yet (seed + deck lists + chosen indices would
  replay a game exactly).

## Not built yet

`CoinFlipped`, `EventSource`, and choosing which cards
`DiscardFromHand` takes are designed or marked TODO but not built. Check the code before assuming a
documented piece exists.
