# CLAUDE.md

Guidance for Claude Code in this repository. If a request is unclear, always ask 1-3 clarifying questions.

# Security Guidelines
- Never commit API keys, tokens, or credentials
- Use environment variables for sensitive data
- Review skills that interact with external services
- Test skills in safe environments before sharing
- NEVER annotate git commits with Claude Code as author, or mention Claude Code in ANY way.

## Git

`main` is the release branch. After merging a PR, delete its branch locally (`git branch -d <b>`)
and on GitHub (`git push origin --delete <b>`).

## Build & test

Maven, Java 25, `-Xlint:all`. Run Maven from the root (`pom.xml` is the parent).

- `engine/` - the rules engine. No networking, JSON or threading.
- `server/` - Spring Boot 4 (Jackson 3: `tools.jackson.*`). Depends on `engine`, never the reverse.
- `web/` - Vite + React + TypeScript via npm, **not** a Maven module. `src/protocol.ts` mirrors the
  server's message records by hand; change both in the same commit. The dev server proxies `/play`
  to Spring on 8080 (no CORS setup).

```bash
mvn test                                  # compile + run all tests
mvn -Dtest=IEffectTest test               # one class; IEffectTest$Flips (quote $ in PowerShell) or IEffectTest#method
mvn package && java -jar server/target/server-0.1.0-SNAPSHOT.jar   # serves /play on 8080
cd web && npm install && npm run dev      # localhost:5173 in two tabs (needs the server)
#   ?demo    plays a bot in one tab (server's /play?bot)
#   ?sandbox the owner's test board (needs the db profile and SANDBOX_OWNER_SUBJECT)
cd web && npm run build && npm run lint   # type-check + bundle, oxlint
```

## Project Overview

A Pokémon TCG Pocket battling simulator. 

**A card's text is data, not code.** An attack is an immutable tree of small composable nodes the
engine evaluates. There is no `case "Pikachu"`; adding a card must never add a branch to the engine.
If a card can't be expressed, add a node to the right sealed hierarchy (or document a narrowing).

Read `docs/UML/README.md` (the design document, kept current; per-hierarchy `.puml` beside it)
before any non-trivial change.

```
INumber.evaluate(ctx)    -> int                          how much?
ICondition.evaluate(T)   -> boolean                      is it so?
ITarget.resolve(ctx)     -> Optional<PokemonInPlay>      who?
IEffect.apply(ctx)       -> EffectOutcome                do it
```

These compose upward: `IEffect` → `IAttempt` → `IAction` → `ICard.actions()`; `ITrigger` reuses
`IAttempt`. **Only `IEffect` mutates the board**, so legal-move generation can preview an attack
without playing it.

### Invariants

- **Resolve against a `ResolutionContext`, never a bare `Battle`.** It carries `controller` (differs
  from `battle.attacker()` when a defender's trigger fires), `source` (what `Self` resolves to), the
  triggering `event`, and a mutable `scope` of coin-flip results. `withSource`/`withController`/
  `withEvent` re-point it but share the scope, so flips stay visible.
- **`NO_OP` vs `FAILED`.** `FAILED` (unresolved target, unpayable cost) aborts the rest of the
  `Attempt` — the "…**If you do**, …" clause — and must leave the board untouched. `NO_OP` (healing
  a full-HP Pokémon) does not abort.
- **Definitions vs instances.** `ICard` (`PokemonCard`, `ItemCard`, …) is immutable and shared;
  `CardInstance` / `PokemonInPlay` hold per-copy zone, owner, damage, energy, statuses, tool and
  modifiers, so `ICondition` takes only its subject.
- **`ICondition<T>` is generic over its subject** (`ResolutionContext`, `PokemonInPlay`,
  `CardInstance`). `For` / `ForAny` lift a Pokémon condition to context level via a target;
  `And`, `Or`, `Not`, `All`, `Any`, `Always` compose at any level.
- **Triggers are collected, not registered.** `TriggerDispatcher.collect()` walks the board on every
  dispatch; no subscribe/unsubscribe bookkeeping.
- **Targeting is turn-relative** (`AttackerActive`, `OpponentBench`, `Self`, `ChosenFrom`, …) on
  purpose — read the controller-relative-gap memory note before "fixing" it.
- **A sealed hierarchy's `permits` clause is the inventory**: a new node means editing the interface
  too. The interface Javadoc carries the *why*; keep that style.

## Adding cards

Use the `convert-card` skill to fetch a card's data. Cards live in
`engine/src/main/java/com/tcgpocket/pool/<set>/` (`A1` … `A4a`, plus `PA` promos): one class per
energy type plus `Trainers`, in printed order, assembled by the set's class (`GeneticApex.CARDS`,
`MythicalIsland.CARDS`, …) and indexed by `CardPool`, which throws on duplicate ids at class-load.
A new set must be added to `CardPool`'s list.

- Nothing outside `pool` may name a card class; use `CardPool.get("A1-096")`. The package is a leaf
  so it can later become a data loader by changing `CardPool` alone.
- Build with `PokemonCard.basic(...)` / `.evolution(...)` plus `.withWeakness`, `.withTags`,
  `.withAbility`; Trainers with `ItemCard.of` / `SupporterCard.of` / `ToolCard.of` /
  `StadiumCard.of` (Tools and Stadiums work entirely through `Trigger`s). Text shared within a set
  goes in a package-private holder (e.g. `A4a.LegendaryPulse`).
- Ids, HP, retreat cost and weakness are hand-transcribed; check them against the printed card.
  Tests (one per set, e.g. `GeneticApexTest`) assert the *shape* of the text, not the numbers.
- If a card can't be expressed exactly, implement the nearest correct narrowing with a
  `<b>Narrowed.</b>` Javadoc note naming the node that would fix it.

## Tests

JUnit 5, `@Nested` + `@DisplayName`, so a class reads as a list of rules. Classes are named after
the hierarchy (`IEffectTest`, `IConditionTest`, `ITriggerTest`, …) or set they cover, not nodes.

- `TestBoard`: `board.you` attacks, `board.them` defends; `active/bench/inHand/inDeck/inDiscard/loose`
  place cards; `contextFor(pokemon)` / `contextWithoutSource()` build contexts. Seeded RNG.
- `ScriptedRandom.flipping(true, false, …)` scripts coin flips (the last value repeats);
  `player.ScriptedPlayer` makes decisions deterministic.

## The engine

`engine.TurnEngine` runs a game: `dealOpeningHands`/`openingDecision` setup, `playTurn`,
`legalActions()` (attacker only, since targets are turn-relative), `checkKnockouts`, and the win
check (most win conditions; tie after `MAX_TURNS`). `TurnEngine.endless` is the sandbox's variant.
Only it dispatches `TurnStart`, `TurnEnd` and `Knockout`; effects and actions announce their own
events. Knockouts are checked after each action, never inside an effect. There
is deliberately no `Phase` enum.

## The server

Details in `server/CLAUDE.md`. These rules hold everywhere:

- **The browser only ever sends `{"decision": id, "option": index}`** — an index into a list the
  engine already made legal, plus the decision's id so a stale answer can't land on the next
  question. Never accept a move the client describes itself. The one exception is the owner-only
  `/sandbox` socket, which also takes edits; it must never share code paths that would let them
  reach `/play`.
- **`BoardView` is the only board state that leaves the server.** Never serialize `Battle`,
  `Decision` or engine objects: the opponent's hand, deck order and RNG seed are hidden. Only
  `BoardView.sandbox` shows them, to the sandbox's owner.
- Never commit the DB wallet.
- CSRF tokens are off because the session cookie is `SameSite=Lax`; keep it that way if you add POSTs.

## Not Built Yet

`CoinFlipped` is not built yet. Do not assume that they documentation is correct. Always check the codebase itself. 
