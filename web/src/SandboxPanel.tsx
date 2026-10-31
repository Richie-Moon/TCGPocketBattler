import { createContext, useContext, useEffect, useRef, useState, type FormEvent, type ReactNode } from 'react'
import { ENERGY, ENERGY_BASE } from './art'
import type { BoardView, CardView, DecisionMessage, PokemonView, SandboxCommand, SandboxZone, SideView } from './protocol'

const STATUSES = ['Poison', 'Burn', 'Sleep', 'Paralysis', 'Confusion']

/** Where a card can go, with the deck's ends and the Bench's slots as their own choices. */
const PLACES: { label: string; zone: SandboxZone; position: number | null }[] = [
  { label: 'Hand', zone: 'HAND', position: null },
  { label: 'Active', zone: 'ACTIVE', position: null },
  { label: 'Bench (next free)', zone: 'BENCH', position: null },
  { label: 'Bench 1', zone: 'BENCH', position: 0 },
  { label: 'Bench 2', zone: 'BENCH', position: 1 },
  { label: 'Bench 3', zone: 'BENCH', position: 2 },
  { label: 'Deck top', zone: 'DECK', position: 0 },
  { label: 'Deck bottom', zone: 'DECK', position: null },
  { label: 'Discard', zone: 'DISCARD', position: null },
]

type Edit = (command: SandboxCommand) => void

/** Turns what was typed into a printed id: an id as it is, or a name as its first printing. */
const ResolveCard = createContext<(typed: string) => string>((typed) => typed)

/**
 * The owner's edit panel beside the board: everything on both sides, each change sent as a command. Board edits only
 * apply at a "Your move" question, so the fieldset is disabled during any other; coins can be queued at any time.
 */
export function SandboxPanel({
  board,
  decision,
  edit,
  onReset,
}: {
  board: BoardView | null
  decision: DecisionMessage | null
  edit: Edit
  onReset?: () => void
}) {
  const [cards, setCards] = useState<{ id: string; name: string }[]>([])
  useEffect(() => {
    fetch('/api/cards')
      .then((response) => response.json())
      .then(setCards, () => setCards([]))
  }, [])
  const coins = decision?.coins ?? []
  const resolveCard = (typed: string) => {
    const lower = typed.toLowerCase()
    return (cards.find((card) => card.id.toLowerCase() === lower) ?? cards.find((card) => card.name.toLowerCase() === lower))?.id ?? typed
  }

  return (
    <ResolveCard.Provider value={resolveCard}>
    <aside className="sandbox">
      <div className="sandbox-head">
        <h2>Sandbox</h2>
        <button type="button" onClick={onReset}>
          Reset
        </button>
      </div>
      <datalist id="sandbox-cards">
        {cards.map((card) => (
          <option key={card.id} value={card.id} label={`${card.name} (${card.id})`} />
        ))}
      </datalist>

      <section>
        <h3>Next coins</h3>
        <p>{coins.length ? coins.map((heads) => (heads ? 'H' : 'T')).join(' ') : 'Random'}</p>
        <div className="row">
          <button type="button" disabled={!decision} onClick={() => edit({ command: 'queueCoins', coins: [true] })}>
            + Heads
          </button>
          <button type="button" disabled={!decision} onClick={() => edit({ command: 'queueCoins', coins: [false] })}>
            + Tails
          </button>
          <button type="button" disabled={!decision || !coins.length} onClick={() => edit({ command: 'clearCoins' })}>
            Clear
          </button>
        </div>
      </section>

      {decision && !decision.editable && <p className="muted">Answer the question on the board to edit again.</p>}
      {board && (
        // Not while no question is open: every edit passes through that, and disabling a field drops the cursor from it.
        <fieldset disabled={decision?.editable === false}>
          <section>
            <h3>Game</h3>
            <div className="row">
              <label>
                Turn <Whole value={board.turn} onCommit={(turn) => edit({ command: 'setTurn', turn })} />
              </label>
            </div>
            <CardField
              label="Stadium"
              current={board.stadium?.name}
              onSubmit={(card) => edit({ command: 'setStadium', card })}
              onClear={board.stadium ? () => edit({ command: 'setStadium', card: null }) : undefined}
            />
          </section>
          <SideEditor title="You" side={board.you} opponent={false} edit={edit} />
          <SideEditor title="Opponent" side={board.opponent} opponent edit={edit} />
        </fieldset>
      )}
    </aside>
    </ResolveCard.Provider>
  )
}

function SideEditor({ title, side, opponent, edit }: { title: string; side: SideView; opponent: boolean; edit: Edit }) {
  const [place, setPlace] = useState(0)
  const [copies, setCopies] = useState(1)
  const zone =(patch: { current?: string | null; next?: string | null; types?: string[] }) =>
    edit({
      command: 'setEnergyZone',
      opponent,
      current: side.energy,
      next: side.nextEnergy,
      types: side.sandbox?.energyTypes ?? [],
      ...patch,
    })
  const types = side.sandbox?.energyTypes ?? []
  const deck = side.sandbox?.deck ?? []

  return (
    <details open={!opponent}>
      <summary>{title}</summary>
      <div className="row">
        <label>
          Points <Whole value={side.points} onCommit={(points) => edit({ command: 'setPoints', opponent, points })} />
        </label>
        <button type="button" onClick={() => edit({ command: 'resetTurn', opponent })} title="Clear this turn's flags and every lock">
          Reset turn state
        </button>
      </div>

      <h4>Energy Zone</h4>
      <div className="row">
        <EnergySelect label="Now" value={side.energy} onChange={(current) => zone({ current })} />
        <EnergySelect label="Next" value={side.nextEnergy} onChange={(next) => zone({ next })} />
      </div>
      <div className="row energy-types" aria-label="Types generated">
        {ENERGY.map((type) => (
          <label key={type} title={type}>
            <input
              type="checkbox"
              checked={types.includes(type)}
              onChange={(event) => zone({ types: event.target.checked ? [...types, type] : types.filter((t) => t !== type) })}
            />
            <EnergyIcon type={type} />
          </label>
        ))}
      </div>

      <h4>Add a card</h4>
      <CardField
        label="To"
        extra={
          <select aria-label="Where to add it" value={place} onChange={(event) => setPlace(Number(event.target.value))}>
            {PLACES.map((p, i) => (
              <option key={p.label} value={i}>
                {p.label}
              </option>
            ))}
          </select>
        }
        count={
          <label>
            Copies
            <input type="number" min={1} max={20} value={copies} onChange={(event) => setCopies(Math.min(20, Math.max(1, event.target.valueAsNumber || 1)))} />
          </label>
        }
        keep
        onSubmit={(card) => {
          for (let i = 0; i < copies; i++) edit({ command: 'addCard', opponent, card, zone: PLACES[place].zone, position: PLACES[place].position })
        }}
      />

      {side.active && <PokemonEditor title="Active" pokemon={side.active} edit={edit} />}
      {side.bench.map((pokemon, slot) => pokemon && <PokemonEditor key={pokemon.id} title={`Bench ${slot + 1}`} pokemon={pokemon} edit={edit} />)}

      <Pile title="Hand" cards={side.hand} edit={edit} />
      <Pile title="Deck (top first)" cards={deck} edit={edit} ordered />
      {deck.length > 1 && (
        <button type="button" onClick={() => edit({ command: 'shuffle', opponent })}>
          Shuffle deck
        </button>
      )}
      <Pile title="Discard" cards={side.discard} edit={edit} />
    </details>
  )
}

function PokemonEditor({ title, pokemon, edit }: { title: string; pokemon: PokemonView; edit: Edit }) {
  const id = pokemon.id
  return (
    <details className="pokemon-editor">
      <summary>
        {title}: {pokemon.name}
      </summary>
      <div className="row">
        <label>
          Damage <Whole value={pokemon.maxHp - pokemon.hp} onCommit={(damage) => edit({ command: 'setDamage', pokemon: id, damage })} />
        </label>
        <span className="muted">
          HP {pokemon.hp}/{pokemon.maxHp}
        </span>
      </div>
      <div className="energy-counts">
        {ENERGY.map((type) => (
          <label key={type} title={type}>
            <EnergyIcon type={type} />
            <Whole value={pokemon.energy[type] ?? 0} onCommit={(count) => edit({ command: 'setEnergy', pokemon: id, type, count })} />
          </label>
        ))}
      </div>
      <div className="row">
        {STATUSES.map((status) => (
          <label key={status}>
            <input
              type="checkbox"
              checked={pokemon.statuses.includes(status)}
              onChange={(event) => edit({ command: event.target.checked ? 'addStatus' : 'removeStatus', pokemon: id, status })}
            />
            {status}
          </label>
        ))}
      </div>
      <CardField
        label="Tool"
        current={pokemon.tool?.name}
        onSubmit={(card) => edit({ command: 'attachTool', pokemon: id, card })}
        onClear={pokemon.tool ? () => edit({ command: 'removeTool', pokemon: id }) : undefined}
      />
      <CardField label="Evolve into" onSubmit={(card) => edit({ command: 'evolve', pokemon: id, card })} />
      <div className="row">
        <button type="button" onClick={() => edit({ command: 'setPlayedThisTurn', pokemon: id, played: false })}>
          Ready to evolve
        </button>
        <button type="button" onClick={() => edit({ command: 'setPlayedThisTurn', pokemon: id, played: true })}>
          Played this turn
        </button>
      </div>
      <MoveControls card={id} edit={edit} />
    </details>
  )
}

function Pile({ title, cards, edit, ordered = false }: { title: string; cards: CardView[]; edit: Edit; ordered?: boolean }) {
  return (
    <>
      <h4>
        {title} · {cards.length}
      </h4>
      <ul className="pile-list">
        {cards.map((card, i) => (
          <li key={card.id}>
            <span title={card.card} data-card={card.card}>
              {card.name}
            </span>
            {ordered && (
              <>
                <button type="button" aria-label={`Move ${card.name} up`} disabled={i === 0} onClick={() => edit({ command: 'move', card: card.id, zone: 'DECK', position: i - 1 })}>
                  ↑
                </button>
                <button
                  type="button"
                  aria-label={`Move ${card.name} down`}
                  disabled={i === cards.length - 1}
                  onClick={() => edit({ command: 'move', card: card.id, zone: 'DECK', position: i + 1 })}
                >
                  ↓
                </button>
              </>
            )}
            <MoveControls card={card.id} edit={edit} />
          </li>
        ))}
      </ul>
    </>
  )
}

function MoveControls({ card, edit }: { card: number; edit: Edit }) {
  return (
    <span className="row">
      <select
        aria-label="Move to"
        value=""
        onChange={(event) => {
          const place = PLACES[Number(event.target.value)]
          edit({ command: 'move', card, zone: place.zone, position: place.position })
        }}
      >
        <option value="" disabled>
          Move to…
        </option>
        {PLACES.map((place, i) => (
          <option key={place.label} value={i}>
            {place.label}
          </option>
        ))}
      </select>
      <button type="button" aria-label="Remove from the game" onClick={() => edit({ command: 'remove', card })}>
        ×
      </button>
    </span>
  )
}

/** A printed card id to submit, with the pool as suggestions by name. */
function CardField({
  label,
  current,
  extra,
  count,
  keep = false,
  onSubmit,
  onClear,
}: {
  label: string
  current?: string
  extra?: ReactNode
  count?: ReactNode
  /** Keep the id after submitting, for adding several copies. */
  keep?: boolean
  onSubmit: (card: string) => void
  onClear?: () => void
}) {
  const [card, setCard] = useState('')
  const resolveCard = useContext(ResolveCard)
  function submit(event: FormEvent) {
    event.preventDefault()
    if (card.trim()) onSubmit(resolveCard(card.trim()))
    if (!keep) setCard('')
  }
  return (
    <form className="row" onSubmit={submit}>
      <label>
        {current ? `${label}: ${current}` : label}
        {extra}
        <input list="sandbox-cards" placeholder="Card name or id" value={card} onChange={(event) => setCard(event.target.value)} />
      </label>
      {count}
      <button type="submit">Set</button>
      {onClear && (
        <button type="button" onClick={onClear}>
          Clear
        </button>
      )}
    </form>
  )
}

function EnergySelect({ label, value, onChange }: { label: string; value: string | null; onChange: (type: string | null) => void }) {
  return (
    <label>
      {label}
      <select value={value ?? ''} onChange={(event) => onChange(event.target.value || null)}>
        <option value="">None</option>
        {ENERGY.map((type) => (
          <option key={type} value={type}>
            {type[0] + type.slice(1).toLowerCase()}
          </option>
        ))}
      </select>
    </label>
  )
}

function EnergyIcon({ type }: { type: string }) {
  return <img src={`${ENERGY_BASE}/${type.toLowerCase()}.png`} alt={type} width={18} height={18} />
}

/** A whole number, sent once when left or on Enter, so typing "120" isn't three edits. A new `value` resets it unless it's being typed in. */
function Whole({ value, onCommit }: { value: number; onCommit: (value: number) => void }) {
  const field = useRef<HTMLInputElement>(null)
  useEffect(() => {
    if (field.current && document.activeElement !== field.current) field.current.value = String(value)
  }, [value])
  function commit(input: HTMLInputElement) {
    const next = input.valueAsNumber
    if (Number.isInteger(next) && next >= 0 && next !== value) onCommit(next)
    else input.value = String(value)
  }
  return (
    <input
      ref={field}
      type="number"
      min={0}
      defaultValue={value}
      onBlur={(event) => commit(event.currentTarget)}
      onKeyDown={(event) => {
        if (event.key === 'Enter') commit(event.currentTarget)
      }}
    />
  )
}
