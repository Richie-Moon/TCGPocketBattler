# The model

A Pokémon TCG Pocket battle simulator, modelled before it is built.

The organising idea is that **a card's text is data, not code**. An attack is
not a Java method; it is an immutable tree of small composable nodes that the
engine evaluates. "Discard an Energy from this Pokémon. If you do, this attack
does 40 more damage" is a value you can build, print, compare, serialize and
test — not a `case "Pikachu"` branch somewhere.

Everything else follows from that.

## The layers

Read `Overview.puml` first; it is this list as a picture.

| Layer | What it is | Mutates? |
|---|---|---|
| **Card text** | `INumber`, `ICondition`, `ITarget`, `IEffect`, `IAttempt`, `IAction`, `ITrigger` — sealed hierarchies of immutable records | only `IEffect` |
| **Cards** | `ICard` definitions (immutable, one per printed card) and `CardInstance` / `PokemonInPlay` (one per physical copy in a game) | instances only |
| **Resolution** | `ResolutionContext` — the ambient argument every node is evaluated against | its `scope` only |
| **Runtime state** | `Battle`, `Side` | yes — this is the game |
| **Engine** | `TurnEngine`, `GameEvent`, `DamageCalculator` | drives everything |
| **Agents** | `IPlayer` — makes every decision the rules leave to a player | no |

The four evaluators are the backbone. Each answers one question about the game:

```
INumber.evaluate(ctx)    -> int              how much?
ICondition.evaluate(t)   -> boolean          is it so?
ITarget.resolve(ctx)     -> PokemonInPlay    who?
IEffect.apply(ctx)       -> EffectOutcome    do it
```

and they compose upward:

```
IEffect  ── grouped by ──>  IAttempt  ── offered as ──>  IAction  ──>  ICard.actions
                                 ^
                                 └── also reused by ──  ITrigger
```

## Files

| File | Contents |
|---|---|
| `Overview.puml` | **start here** — the layer stack and how it composes |
| `Resolution.puml` | `ResolutionContext`, `ResolutionScope`, `FlipResult`, `RandomSource` |
| `Battle_Side.puml` | the mutable game state |
| `CardInstance.puml` | definition vs. instance, and `PokemonInPlay`'s battle state |
| `ICard.puml` | card definitions — Pokémon, Item, Supporter, Tool, Stadium, Fossil |
| `Energy.puml` | `Type`, `EnergyCost`, the Energy Zone |
| `IAbility.puml` | activated and passive abilities |
| `INumber.puml` | arithmetic expression tree |
| `ICondition.puml` | boolean expression tree, generic over its subject |
| `ITarget.puml` | single, multi and side targeting |
| `IEffect.puml` | every board mutation |
| `IAttempt.puml` | ordered effects that can fail |
| `IAction.puml` | attacks, trainer actions, and engine-generated moves |
| `ITrigger.puml` | `GameEvent` hierarchy and trigger dispatch |
| `IStatus.puml` | special conditions and how they stack |
| `Damage.puml` | the damage pipeline and its ordering |
| `GameFlow.puml` | phases, one turn end to end, win condition |
| `IPlayer.puml` | the decision-making seam |

## Five decisions worth knowing about

**Everything is evaluated against a `ResolutionContext`, not a bare `Battle`.**
A Tool's trigger means "the Pokémon I am attached to", which no turn-relative
target (`AttackerActive`, …) can express. The context carries `source` (so
`Self` works), the `event` that fired a trigger (so a card can retaliate
against what hit it), and a per-resolution `scope` holding coin-flip results.

**Definitions are separate from instances.** A deck holds two copies of
Pikachu: same definition, independent damage. `ICard` is immutable and shared;
`PokemonInPlay` holds the damage, energy, statuses, tool and modifiers.
Because an instance knows its own `zone` and `owner`, card-level conditions
like `IsActive` need no context — which is why `ICondition` keeps the simple
`evaluate(T)` signature.

**An `IAttempt` can fail, and failure short-circuits.** Effects return
`APPLIED` / `NO_OP` / `FAILED`; an `Attempt` stops at the first `FAILED`. That
is exactly the "…**If you do**, …" clause that runs through real card text.
`NO_OP` (healing something already at full HP) does *not* abort — the
distinction is the whole point of the enum.

**Triggers are collected, not registered.** `TriggerDispatcher.collect()` walks
the board on every dispatch instead of subscribing and unsubscribing as cards
come and go. A board is ~10 cards, so the cost is nil, and it makes stale
listeners impossible: a discarded Tool cannot fire, and a Pokémon that just
evolved brings its new ability along for free.

**A turn is `Decision<IAction>`.** `Battle.legalActions(Side)` builds every
legal move — attack, retreat, evolve, play, attach, ability, end turn — and
`IPlayer.choose` picks one. The same seam serves every mid-resolution choice.
This is what makes the thing a simulator rather than a card renderer, and it
is what lets a `ScriptedPlayer` make rule tests deterministic.

## Rendering

IntelliJ's PlantUML plugin previews these inline. From the command line:

```bash
java -jar plantuml.jar -tsvg -o ./out 'docs/UML/*.puml'   # render
java -jar plantuml.jar -checkonly -failfast2 'docs/UML/*.puml'   # syntax only
```

`Damage.puml` and `GameFlow.puml` each hold two diagrams, so they render two
files apiece.

## Conventions

- `<<sealed>>` marks a closed hierarchy, `<<record>>` an immutable leaf — the
  intended Java 25 shape, stated in the model rather than left implicit.
- `/' ... '/` comments carry the *why*, especially where a modelling choice
  looks arbitrary.
- Fields are drawn with `-` throughout, including on interfaces, as shorthand
  for "this hierarchy has this property".
- Cross-file references (`Type`, `Phase`, `PokemonInPlay`) are left undeclared
  in the files that use them; PlantUML renders them as bare boxes and each
  file stays independently renderable.
