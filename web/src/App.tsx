import type { PokemonView, SideView } from './protocol'
import { useGame } from './useGame'

function App() {
  const { connection, board, decision, result, error, choose } = useGame()

  let status: string
  if (result) status = `Game over: ${result}`
  else if (connection === 'connecting') status = 'Connecting…'
  else if (connection === 'waiting') status = 'Waiting for an opponent. Open this page in another tab.'
  else if (connection === 'closed') status = 'Disconnected'
  else if (board) status = `Turn ${board.turn} · ${board.yourTurn ? 'your turn' : "opponent's turn"}`
  else status = 'Starting…'

  return (
    <main>
      <p className="status">{status}</p>
      {board && (
        <>
          {board.stadium && <p>Stadium: {board.stadium.name}</p>}
          <Side title="Opponent" side={board.opponent} />
          <Side title="You" side={board.you} />
        </>
      )}
      {decision && (
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
    </main>
  )
}

function Side({ title, side }: { title: string; side: SideView }) {
  return (
    <section>
      <h2>
        {title}: {side.name} · {side.points} points
      </h2>
      <p>Active: {side.active ? <Pokemon pokemon={side.active} /> : '—'}</p>
      <p>
        Bench:{' '}
        {side.bench.length
          ? side.bench.map((pokemon, index) => (
              <span key={index}>
                {index > 0 && ' | '}
                <Pokemon pokemon={pokemon} />
              </span>
            ))
          : '—'}
      </p>
      <p>
        Hand ({side.handSize}): {side.hand.length ? side.hand.map((card) => card.name).join(', ') : '—'}
      </p>
      <p>
        Deck {side.deckSize} · Discard {side.discard.length} · Energy {side.energy ?? '—'} (next{' '}
        {side.nextEnergy ?? '—'})
      </p>
    </section>
  )
}

function Pokemon({ pokemon }: { pokemon: PokemonView }) {
  const energy = Object.entries(pokemon.energy)
    .map(([type, count]) => `${count} ${type}`)
    .join(', ')
  return (
    <span>
      {pokemon.name} {pokemon.hp}/{pokemon.maxHp} HP
      {energy && ` · ${energy}`}
      {pokemon.statuses.length > 0 && ` · ${pokemon.statuses.join(', ')}`}
      {pokemon.tool && ` · ${pokemon.tool.name}`}
    </span>
  )
}

export default App
