# CLAUDE.md

Guidance for Claude Code (claude.ai/code) in this repository.

If you have any questions about the task or request, ask 1-3 clarifying questions. 

## Build & test

Maven, Java 25 (sealed interfaces, records and pattern-matching switch are load-bearing),
`-Xlint:all` on. The root `pom.xml` is the parent; run Maven from the root.

- `engine/` — the rules engine. Free of networking, JSON and threading.
- `server/` — Spring Boot 4 (Jackson 3, so `tools.jackson.*`), imported as a BOM. Depends on
  `engine`, never the reverse.
- `web/` — Vite + React + TypeScript, built with npm, **not** a Maven module. `src/protocol.ts`
  mirrors the server's message records by hand; change both in the same commit. The dev server
  proxies `/play` to Spring on 8080, so there is no CORS setup.

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

**A card's text is data, not code.** An attack is an immutable tree of small composable nodes the
engine evaluates. There is no `case "Pikachu"`, and adding a card must never add a branch to the
engine. If a card can't be expressed with existing nodes, add a node to the right sealed hierarchy
(or document a narrowing) — never special-case.

Read `docs/UML/README.md` (the design document, kept current) before any non-trivial change;
the `.puml` files beside it are the per-hierarchy diagrams.

```
INumber.evaluate(ctx)    -> int                          how much?
ICondition.evaluate(T)   -> boolean                      is it so?
ITarget.resolve(ctx)     -> Optional<PokemonInPlay>      who?
IEffect.apply(ctx)       -> EffectOutcome                do it
```

These compose upward: `IEffect` → `IAttempt` → `IAction` → `ICard.actions()`; `ITrigger` reuses
`IAttempt`. **Only `IEffect` mutates the board**, which is what lets legal-move generation preview
an attack without playing it.

### Invariants

- **Resolve against a `ResolutionContext`, never a bare `Battle`.** It carries `controller` (differs
  from `battle.attacker()` when a defender's trigger fires), `source` (the Pokémon whose text is
  resolving; `Self` resolves to it), the triggering `event`, and a mutable `scope` of coin-flip
  results for one resolution. `withSource`/`withController`/`withEvent` re-point it and keep the
  same scope, so flips stay visible.
- **`NO_OP` vs `FAILED`.** `FAILED` means the effect genuinely could not happen (unresolved target,
  unpayable cost) and aborts the rest of the `Attempt` — the "…**If you do**, …" clause — and must
  leave the board untouched. `NO_OP` (healing a full-HP Pokémon) does not abort.
- **Definitions vs instances.** `ICard` (`PokemonCard`, `ItemCard`, …) is immutable and shared by
  every copy; `CardInstance` / `PokemonInPlay` hold per-copy zone, damage, energy, statuses, tool
  and modifiers. An instance knows its `zone` and `owner`, so `ICondition` takes only its subject.
- **`ICondition<T>` is generic over its subject** (`ResolutionContext`, `PokemonInPlay`,
  `CardInstance`). `For` / `ForAny` lift a Pokémon-level condition to context level by resolving a
  target; the combinators (`And`, `Or`, `Not`, `All`, `Any`, `Always`) compose at any level.
- **Triggers are collected, not registered.** `TriggerDispatcher.collect()` walks the board on every
  dispatch, so stale listeners are impossible. No subscribe/unsubscribe bookkeeping.
- **Targeting is turn-relative** (`AttackerActive`, `OpponentBench`, `Self`, `ChosenFrom`, …) on
  purpose — see the memory note on the controller-relative gap before "fixing" it.
- **A sealed hierarchy's `permits` clause is the inventory**: adding a node means editing the
  interface too. The interface Javadoc carries the *why*; keep that style.

## Adding cards

`engine/src/main/java/com/tcgpocket/pool/A1/` — one class per energy type plus `Trainers`, in
printed set order, assembled by `GeneticApex.CARDS` and indexed by `CardPool` (throws on duplicate
ids at class-load).

- Nothing outside `pool` may name a card class; use `CardPool.get("A1-096")`. The package is a leaf
  so the constants can later be swapped for a data loader by changing `CardPool` alone.
- Build with `PokemonCard.basic(...)` / `.evolution(...)` plus `.withWeakness`, `.withTags`,
  `.withAbility`; Trainers with `ItemCard.of` / `SupporterCard.of` / `ToolCard` (a Tool works
  entirely through `Trigger`s).
- Ids, HP, retreat cost and weakness are hand-transcribed; check them against the printed card.
  Tests assert the *shape* of the card text, not that the numbers are right.
- If a card can't be expressed exactly, implement the nearest correct narrowing with a
  `<b>Narrowed.</b>` Javadoc note naming the node that would fix it (see `Trainers.POTION`).

## Tests

JUnit 5, `@Nested` + `@DisplayName` throughout, so a class reads as a list of rules. Test classes
are named after the hierarchy they cover (`IEffectTest`, `IConditionTest`, `ITriggerTest`, …), not
individual nodes.

- `TestBoard`: `board.you` is the attacker, `board.them` the defender;
  `active/bench/inHand/inDeck/inDiscard/loose` place cards; `contextFor(pokemon)` /
  `contextWithoutSource()` build contexts. Seeded RNG by default.
- `ScriptedRandom.flipping(true, false, …)` scripts coin flips (the last value repeats);
  `ScriptedPlayer` makes player decisions deterministic.

## The engine

`engine.TurnEngine` runs a game: setup, `playTurn`, `legalActions()` (attacker only, since targets
are turn-relative), `checkKnockouts`, and the win check (most win conditions wins; tie after
`MAX_TURNS`). Only it dispatches `TurnStart`, `TurnEnd` and `Knockout`; effects and actions announce
their own events. Knockouts are checked after each action, never inside an effect. There is
deliberately no `Phase` enum.

## The server

Details are in `server/CLAUDE.md`. These rules hold everywhere:

- **The browser only ever sends `{"decision": id, "option": index}`** — an index into a list the
  engine already made legal, plus the decision's id so a stale answer can't land on the next
  question. Never accept a move the client describes itself.
- **`BoardView` is the only board state that leaves the server.** Never serialize `Battle`,
  `Decision` or engine objects: the opponent's hand, deck order and RNG seed are hidden.
- Never commit the DB wallet.
- CSRF tokens are off because the session cookie is `SameSite=Lax`; keep it that way if you add POSTs.

## Not built yet

`CoinFlipped`, and choosing which cards `DiscardFromHand` takes, are designed or marked TODO but
not built. Check the code before assuming a documented piece exists.
