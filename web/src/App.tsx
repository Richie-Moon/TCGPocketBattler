import { useState } from 'react'
import type { CardView, DecisionMessage, PokemonView, SideView } from './protocol'
import { useGame } from './useGame'

function App() {
  const { connection, board, decision, result, error, choose } = useGame()

  let status: string
  if (result) status = `Game over: ${result}`
  else if (connection === 'connecting') status = 'Connecting…'
  else if (connection === 'waiting') status = 'Waiting for an opponent. Open this page in another tab.'
  else if (connection === 'closed') status = 'Disconnected'
  else if (board && !board.you.active)
    status = decision ? 'Set up your board' : 'Waiting for your opponent to confirm their start'
  else if (board) status = `Turn ${board.turn} · ${board.yourTurn ? 'your turn' : "opponent's turn"}`
  else status = 'Starting…'

  return (
    <main>
      <div className="board">
        {board && (
          <>
            <Side title="Opponent" side={board.opponent} />
            <Side title="You" side={board.you} />
          </>
        )}
      </div>
      <aside>
        <p className="status">{status}</p>
        {board?.stadium && (
          <section>
            <h2>Stadium</h2>
            <Card card={board.stadium} />
          </section>
        )}
        {decision?.options[0]?.kind === 'setup' ? (
          <SetupPicker key={decision.id} decision={decision} hand={board?.you.hand ?? []} choose={choose} />
        ) : decision && (
          <section>
            <h2>{decision.prompt}</h2>
            {decision.options.map((option, index) => (
              <button key={index} type="button" className="option" onClick={() => choose(index)}>
                {option.label}
              </button>
            ))}
          </section>
        )}
        {error && <p className="error">{error}</p>}
      </aside>
    </main>
  )
}

function Side({ title, side }: { title: string; side: SideView }) {
  return (
    <section>
      <h2>
        {title}: {side.name} · {side.points} points
      </h2>
      <div className="row">
        {side.active ? <Pokemon pokemon={side.active} active /> : <span className="empty">No Active</span>}
        {side.bench.map((pokemon) => (
          <Pokemon key={pokemon.id} pokemon={pokemon} />
        ))}
      </div>
      <p>Hand ({side.handSize})</p>
      {side.hand.length > 0 && (
        <div className="row">
          {side.hand.map((card) => (
            <Card key={card.id} card={card} />
          ))}
        </div>
      )}
      <p>
        Deck {side.deckSize} · Discard {side.discard.length} · Energy {side.energy ?? '—'} (next{' '}
        {side.nextEnergy ?? '—'})
      </p>
    </section>
  )
}

function Pokemon({ pokemon, active }: { pokemon: PokemonView; active?: boolean }) {
  const energy = Object.entries(pokemon.energy)
    .map(([type, count]) => `${count} ${type}`)
    .join(', ')
  return (
    <figure className={active ? 'pokemon active' : 'pokemon'}>
      <Card card={pokemon} />
      <figcaption>
        {pokemon.hp}/{pokemon.maxHp} HP
        {energy && <div>{energy}</div>}
        {pokemon.statuses.length > 0 && <div>{pokemon.statuses.join(', ')}</div>}
        {pokemon.tool && <div>{pokemon.tool.name}</div>}
      </figcaption>
    </figure>
  )
}

/**
 * The opening board: click a Basic to make it the Active, click more to Bench them, then confirm. The server
 * offers every legal board as an option; Confirm answers with the one matching the clicks. Nothing is placed,
 * or shown to the opponent, until both players have confirmed.
 */
function SetupPicker({
  decision,
  hand,
  choose,
}: {
  decision: DecisionMessage
  hand: CardView[]
  choose: (index: number) => void
}) {
  const [active, setActive] = useState<number | null>(null)
  const [bench, setBench] = useState<number[]>([])
  const basics = hand.filter((card) => decision.options.some((option) => option.card === card.id))
  const benchLimit = Math.max(...decision.options.map((option) => option.bench.length))
  const chosen = decision.options.findIndex(
    (option) =>
      option.card === active && option.bench.length === bench.length && option.bench.every((id) => bench.includes(id)),
  )

  function toggle(id: number) {
    if (id === active) setActive(null)
    else if (bench.includes(id)) setBench(bench.filter((benched) => benched !== id))
    else if (active === null) setActive(id)
    else if (bench.length < benchLimit) setBench([...bench, id])
  }

  return (
    <section>
      <h2>{decision.prompt}</h2>
      <p>Click a Basic to make it your Active, then click any others to Bench them.</p>
      <div className="row">
        {basics.map((card) => {
          const role = card.id === active ? 'Active' : bench.includes(card.id) ? 'Bench' : null
          return (
            <button
              key={card.id}
              type="button"
              className={role ? 'pick picked' : 'pick'}
              aria-pressed={role !== null}
              onClick={() => toggle(card.id)}
            >
              <Card card={card} />
              <span>{role ?? ' '}</span>
            </button>
          )
        })}
      </div>
      <button type="button" className="option" disabled={chosen < 0} onClick={() => choose(chosen)}>
        {active === null ? 'Choose an Active Pokemon' : 'Confirm'}
      </button>
    </section>
  )
}

/** Card art, named by printed id, in a public bucket; VITE_CARD_BASE=/cards uses local files. */
const CARD_BASE = import.meta.env.VITE_CARD_BASE ??
  'https://objectstorage.ap-sydney-1.oraclecloud.com/n/sd3dz8oxtchf/b/assets-bucket/o/cards'

function Card({ card }: { card: Pick<CardView, 'card' | 'name'> }) {
  return <img className="card" src={`${CARD_BASE}/${card.card}.webp`} alt={card.name} title={card.name} />
}

export default App
