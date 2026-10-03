import { useEffect, useLayoutEffect, useRef, useState, type CSSProperties, type ReactNode, type RefObject } from 'react'
import cardBackDark from './assets/battle/card-back-dark.svg'
import cardBack from './assets/battle/card-back.svg'
import chat from './assets/battle/chat.svg'
import deckBackDark from './assets/battle/deck-back-dark.svg'
import deckBack from './assets/battle/deck-back.svg'
import glowDark from './assets/battle/energy-glow-dark.png'
import glow from './assets/battle/energy-glow.png'
import send from './assets/battle/send.svg'
import { CARD_BASE, ENERGY_BASE, ICON_BASE } from './art'
import { Icon } from './Home'
import type { CardView, OptionView, PokemonView, SideView } from './protocol'
import { useGame, type LogEntry } from './useGame'

/** What a turn option starts from: a card in hand or in play (by instance id), or the Energy Zone. */
type Source = number | 'energy'

const NO_SIDE: SideView = {
  name: '',
  points: 0,
  hand: [],
  handSize: 0,
  deckSize: 0,
  topCards: [],
  discard: [],
  active: null,
  bench: [],
  energy: null,
  nextEnergy: null,
}

function sourceOf(option: OptionView, activeId: number | undefined): Source | null {
  switch (option.kind) {
    case 'play':
    case 'evolve':
    case 'attack':
    case 'ability':
      return option.card
    case 'retreat':
      return activeId ?? null
    case 'attach':
      return 'energy'
    default:
      return null
  }
}

/**
 * The battle page: the mat on the left, the battle log on the right (a bottom sheet on a phone).
 *
 * On your turn you click what to act with (a card in hand, a Pokemon, the Energy Zone); its options are listed
 * and the Pokemon they land on light up, so either answers. Any other question lists its options, with its
 * Pokemon clickable too. Every click still answers with an option index the server offered.
 */
export function Battle({
  bot,
  me,
  onLeave,
}: {
  bot: boolean
  me: { name: string; profileIcon: string } | null
  onLeave: () => void
}) {
  const { connection, board, decision, result, error, log, choose } = useGame(bot)
  // Picks are tagged with the decision they were made for, so a new question starts with nothing picked.
  const [pick, setPick] = useState<{ decision: number; source: Source } | null>(null)
  const [setup, setSetup] = useState<{ decision: number; active: number | null; bench: number[] } | null>(null)
  const [logOpen, setLogOpen] = useState(false)
  const [seen, setSeen] = useState(0)
  const mat = useRef<HTMLDivElement>(null)
  const fit = useFit(mat)

  const you = board?.you ?? NO_SIDE
  const opponent = board?.opponent ?? NO_SIDE
  const options = decision?.options.map((option, index) => ({ option, index })) ?? []
  const isSetup = options[0]?.option.kind === 'setup'
  const isTurn = options.some(({ option }) => option.kind === 'endTurn')
  const selected = pick && pick.decision === decision?.id ? pick.source : null
  const sourced = options.map((o) => ({ ...o, source: isTurn ? sourceOf(o.option, you.active?.id) : null }))
  const sources = new Set(sourced.map((o) => o.source).filter((source) => source !== null))
  const forSelected = sourced.filter((o) => selected !== null && o.source === selected)
  const loose = sourced.filter((o) => isTurn && o.source === null && o.option.kind !== 'endTurn')

  // The Pokemon (and, outside a turn, hand cards) that answer with one click.
  const targets = new Map<number, number>()
  const handPicks = new Map<number, number>()
  for (const { option, index } of isTurn ? forSelected : isSetup ? [] : options) {
    if (option.target !== null && !targets.has(option.target)) targets.set(option.target, index)
    if (!isTurn && option.kind === 'card' && option.card !== null) handPicks.set(option.card, index)
  }

  // Setup: the clicked Basics move to the Active Spot and Bench until Confirm.
  const picks = setup && setup.decision === decision?.id ? setup : { active: null, bench: [] as number[] }
  const benchLimit = isSetup ? Math.max(...options.map(({ option }) => option.bench.length)) : 0
  const setupChoice = options.findIndex(
    ({ option }) =>
      option.card === picks.active &&
      option.bench.length === picks.bench.length &&
      option.bench.every((id) => picks.bench.includes(id)),
  )
  function toggleSetup(id: number) {
    if (!decision) return
    const { active, bench } = picks
    if (id === active) setSetup({ decision: decision.id, active: null, bench })
    else if (bench.includes(id)) setSetup({ decision: decision.id, active, bench: bench.filter((b) => b !== id) })
    else if (active === null) setSetup({ decision: decision.id, active: id, bench })
    else if (bench.length < benchLimit) setSetup({ decision: decision.id, active, bench: [...bench, id] })
  }
  const setupBasics = new Set(options.map(({ option }) => option.card))
  const byId = new Map(you.hand.map((card) => [card.id, card]))
  const shownActive = isSetup && picks.active !== null ? asPokemon(byId.get(picks.active)) : you.active
  const shownBench = isSetup ? picks.bench.map((id) => asPokemon(byId.get(id))!) : you.bench
  const hand = isSetup ? you.hand.filter((card) => card.id !== picks.active && !picks.bench.includes(card.id)) : you.hand

  function select(source: Source) {
    if (decision && sources.has(source)) setPick(selected === source ? null : { decision: decision.id, source })
  }
  function clickPokemon(id: number) {
    const answer = targets.get(id)
    if (answer !== undefined) choose(answer)
    else select(id)
  }
  function clickHand(id: number) {
    const answer = handPicks.get(id)
    if (isSetup) toggleSetup(id)
    else if (answer !== undefined) choose(answer)
    else select(id)
  }
  const pokemonState = (id: number) =>
    targets.has(id) ? 'target' : selected === id ? 'selected' : sources.has(id) ? 'can' : undefined

  let status: string
  if (result) status = result
  else if (connection === 'connecting') status = 'Connecting…'
  else if (connection === 'waiting') status = 'Waiting for an opponent…'
  else if (connection === 'closed') status = 'Disconnected'
  else if (board && !board.you.active) status = decision ? 'Set up your board' : 'Waiting for your opponent'
  else if (board) status = `Turn ${board.turn} · ${board.yourTurn ? 'Your turn' : "Opponent's turn"}`
  else status = 'Starting…'

  let menu: { title: string; note?: string; items: { option: OptionView; index: number }[] } | null = null
  if (isSetup) menu = { title: decision!.prompt, note: 'Tap a Basic to make it your Active, then tap others to Bench them.', items: [] }
  else if (isTurn && selected !== null) menu = { title: nameOf(selected), items: forSelected }
  else if (isTurn && loose.length) menu = { title: 'More', items: loose }
  else if (decision && !isTurn) menu = { title: decision.prompt, items: options }

  function nameOf(source: Source) {
    if (source === 'energy') return 'Energy'
    const pokemon = [you.active, ...you.bench].find((p) => p?.id === source)
    return pokemon?.name ?? byId.get(source)?.name ?? ''
  }

  const over = result !== null || connection === 'closed'
  const yourName = `${me?.name ?? (you.name || 'You')} (you)`
  const theirName = opponent.name || 'Opponent'
  const unread = logOpen ? 0 : log.length - seen
  function toggleLog() {
    setSeen(log.length)
    setLogOpen(!logOpen)
  }
  function concede() {
    if (over || confirm('Concede this game?')) onLeave()
  }

  return (
    <div className={logOpen ? 'battle log-open' : 'battle'}>
      <div className="mat" ref={mat}>
        <div className="stage" style={{ zoom: fit.zoom, height: fit.height }}>
          <div className="field">
            <Bench className="opp-bench" pokemon={opponent.bench} state={pokemonState} onClick={clickPokemon} />
            <EnergyZone className="opp-energy" side={opponent} />
            <Slot className="active opp-active">
              {opponent.active && (
                <Pokemon pokemon={opponent.active} state={pokemonState(opponent.active.id)} onClick={clickPokemon} />
              )}
            </Slot>
            <Zones className="opp-zones" side={opponent} deckFirst={false} />

            <div className="divider">
              <button type="button" className="log-button" aria-label="Battle log" onClick={toggleLog}>
                <Icon src={chat} size={20} />
                {unread > 0 && <span className="badge">{unread}</span>}
              </button>
              <div className="turn">
                <span className={board?.yourTurn && !over ? 'dot yours' : 'dot'} />
                {status}
              </div>
              {board?.stadium && <Card card={board.stadium} className="stadium" />}
              {over ? (
                <button type="button" className="end-turn" onClick={onLeave}>
                  Leave
                </button>
              ) : isSetup ? (
                <button type="button" className="end-turn" disabled={setupChoice < 0} onClick={() => choose(setupChoice)}>
                  Confirm
                </button>
              ) : (
                <button
                  type="button"
                  className="end-turn"
                  disabled={!isTurn}
                  onClick={() => choose(options.find(({ option }) => option.kind === 'endTurn')!.index)}
                >
                  End turn
                </button>
              )}
            </div>

            <Slot className="active your-active">
              {shownActive && (
                <Pokemon pokemon={shownActive} state={isSetup ? 'selected' : pokemonState(shownActive.id)} onClick={isSetup ? toggleSetup : clickPokemon} />
              )}
            </Slot>
            <Zones className="your-zones" side={you} deckFirst />
            <Bench
              className="your-bench"
              pokemon={shownBench}
              state={isSetup ? () => 'selected' : pokemonState}
              onClick={isSetup ? toggleSetup : clickPokemon}
            />
            <EnergyZone
              className="your-energy"
              side={you}
              state={selected === 'energy' ? 'selected' : sources.has('energy') ? 'can' : undefined}
              onClick={() => select('energy')}
            />

            {(menu || error) && (
              <div className="menu">
                {menu && <h3>{menu.title}</h3>}
                {menu?.note && <p className="muted">{menu.note}</p>}
                {menu?.items.map(({ option, index }) => (
                  <button key={index} type="button" className="menu-option" onClick={() => choose(index)}>
                    {option.label}
                  </button>
                ))}
                {isTurn && selected !== null && (
                  <button type="button" className="menu-cancel" onClick={() => setPick(null)}>
                    Cancel
                  </button>
                )}
                {error && <p className="error">{error}</p>}
              </div>
            )}
          </div>

          <OpponentHand side={opponent} />
          <div className="hand">
            {hand.map((card, i) => {
              const can = isSetup ? setupBasics.has(card.id) : handPicks.has(card.id) || sources.has(card.id)
              return (
                <button
                  key={card.id}
                  type="button"
                  className={selected === card.id ? 'hand-card selected' : can ? 'hand-card can' : 'hand-card'}
                  style={fan(i, hand.length, 6)}
                  disabled={!can}
                  onClick={() => clickHand(card.id)}
                >
                  <Card card={card} />
                </button>
              )
            })}
          </div>

          <PlayerInfo className="opp-info" name={theirName} points={opponent.points} />
          <PlayerInfo className="your-info" name={yourName} points={you.points} icon={me?.profileIcon} />
        </div>
      </div>

      <button type="button" className="scrim" aria-label="Close the battle log" onClick={toggleLog} />
      <LogPanel
        log={log}
        subtitle={`Standard · ${me?.name ?? (you.name || 'You')} vs ${theirName}`}
        concedeLabel={over ? 'Leave' : 'Concede'}
        onConcede={concede}
      />
    </div>
  )
}

/**
 * Scales the mat's 1200x900 design (390x844 on a phone) down to fit, and stretches its height to fill, so
 * the board keeps the design's proportions at any window size.
 */
function useFit(mat: RefObject<HTMLDivElement | null>) {
  const [fit, setFit] = useState({ zoom: 1, height: 900 })
  useLayoutEffect(() => {
    const element = mat.current!
    const observer = new ResizeObserver(() => {
      const [width, height] = matchMedia('(max-width: 600px)').matches ? [390, 844] : [1200, 900]
      const zoom = Math.min(1, element.clientWidth / width, element.clientHeight / height)
      setFit({ zoom, height: element.clientHeight / zoom })
    })
    observer.observe(element)
    return () => observer.disconnect()
  }, [mat])
  return fit
}

/** Where card `i` of `n` sits in a fan drawn for `designed` cards; wider hands squeeze in. */
function fan(i: number, n: number, designed: number) {
  const d = i - (n - 1) / 2
  return { '--d': d, '--ad': Math.abs(d), '--f': Math.min(1, (designed - 1) / Math.max(1, n - 1)) } as CSSProperties
}

/** A hand card picked during setup, drawn in play; its HP isn't sent until it is placed, so none is shown. */
function asPokemon(card: CardView | undefined): PokemonView | null {
  return card ? { ...card, hp: 0, maxHp: 0, energy: {}, statuses: [], tool: null } : null
}

function Card({ card, className = 'art' }: { card: Pick<CardView, 'card' | 'name'>; className?: string }) {
  return <img className={className} src={`${CARD_BASE}/${card.card}.webp`} alt={card.name} title={card.name} />
}

/** The design's card back, in its dark variant on a dark theme. */
function Back({ deck, className }: { deck?: boolean; className?: string }) {
  return (
    <picture className={className}>
      <source srcSet={deck ? deckBackDark : cardBackDark} media="(prefers-color-scheme: dark)" />
      <img src={deck ? deckBack : cardBack} alt="" />
    </picture>
  )
}

function Slot({ className, title, children }: { className: string; title?: string; children?: ReactNode }) {
  return (
    <div className={`slot ${className}`} title={title}>
      {children}
    </div>
  )
}

function Pokemon({
  pokemon,
  state,
  onClick,
}: {
  pokemon: PokemonView
  state?: string
  onClick: (id: number) => void
}) {
  const energy = Object.entries(pokemon.energy).flatMap(([type, count]) => Array<string>(count).fill(type))
  const tags = [...pokemon.statuses, ...(pokemon.tool ? [pokemon.tool.name] : [])]
  return (
    <button
      type="button"
      className={state ? `pokemon ${state}` : 'pokemon'}
      disabled={!state}
      onClick={() => onClick(pokemon.id)}
    >
      <Card card={pokemon} />
      {pokemon.maxHp > 0 && <span className="hp">{pokemon.hp}</span>}
      {energy.length > 0 && (
        <span className="attached">
          {energy.map((type, i) => (
            <img key={i} src={`${ENERGY_BASE}/${type.toLowerCase()}.png`} alt={type} title={type} />
          ))}
        </span>
      )}
      {tags.length > 0 && <span className="tags">{tags.join(' · ')}</span>}
    </button>
  )
}

function Bench({
  className,
  pokemon,
  state,
  onClick,
}: {
  className: string
  pokemon: PokemonView[]
  state: (id: number) => string | undefined
  onClick: (id: number) => void
}) {
  return (
    <div className={`bench ${className}`}>
      {[0, 1, 2].map((i) => (
        <Slot key={i} className="benched">
          {pokemon[i] && <Pokemon pokemon={pokemon[i]} state={state(pokemon[i].id)} onClick={onClick} />}
        </Slot>
      ))}
    </div>
  )
}

/** The discard pile (its top card) and the deck (face down while it has cards). */
function Zones({ className, side, deckFirst }: { className: string; side: SideView; deckFirst: boolean }) {
  const top = side.discard.at(-1)
  const discard = (
    <Slot key="discard" className="pile">
      {top && <Card card={top} />}
    </Slot>
  )
  const deck = (
    <Slot key="deck" className="pile deck" title={`${side.deckSize} cards in deck`}>
      {side.deckSize > 0 && <Back deck className="art" />}
    </Slot>
  )
  return <div className={`zones ${className}`}>{deckFirst ? [deck, discard] : [discard, deck]}</div>
}

function EnergyZone({
  className,
  side,
  state,
  onClick,
}: {
  className: string
  side: SideView
  state?: 'can' | 'selected'
  onClick?: () => void
}) {
  return (
    <button type="button" className={`energy-zone ${className} ${state ?? ''}`} disabled={!state} onClick={onClick}>
      <picture className="glow">
        <source srcSet={glowDark} media="(prefers-color-scheme: dark)" />
        <img src={glow} alt="" />
      </picture>
      {side.energy && (
        <img className="current" src={`${ENERGY_BASE}/${side.energy.toLowerCase()}.png`} alt={side.energy} title={`Energy: ${side.energy}`} />
      )}
      {side.nextEnergy && (
        <img className="next" src={`${ENERGY_BASE}/${side.nextEnergy.toLowerCase()}.png`} alt={side.nextEnergy} title={`Next: ${side.nextEnergy}`} />
      )}
      <span className="label">Energy</span>
    </button>
  )
}

/** Face down, except any cards the opponent has had to reveal. */
function OpponentHand({ side }: { side: SideView }) {
  return (
    <div className="opp-hand" aria-label={`Opponent's hand: ${side.handSize} cards`}>
      {Array.from({ length: side.handSize }, (_, i) => (
        <span key={i} className="opp-card" style={fan(i, side.handSize, 5)}>
          {i < side.hand.length ? <Card card={side.hand[i]} /> : <Back />}
        </span>
      ))}
    </div>
  )
}

function PlayerInfo({ className, name, points, icon }: { className: string; name: string; points: number; icon?: string }) {
  return (
    <div className={`player-info ${className}`}>
      {icon ? <img className="avatar" src={`${ICON_BASE}/${icon}`} alt="" /> : <span className="avatar" />}
      <div className="who">
        <span className="name">{name}</span>
        <span className="points" aria-label={`${points} points`}>
          {[0, 1, 2].map((i) => (
            <span key={i} className={i < points ? 'point won' : 'point'} />
          ))}
        </span>
      </div>
    </div>
  )
}

function LogPanel({
  log,
  subtitle,
  concedeLabel,
  onConcede,
}: {
  log: LogEntry[]
  subtitle: string
  concedeLabel: string
  onConcede: () => void
}) {
  const [filter, setFilter] = useState<'All' | 'Log' | 'Chat'>('All')
  const feed = useRef<HTMLDivElement>(null)
  useEffect(() => {
    feed.current?.scrollTo({ top: feed.current.scrollHeight })
  }, [log.length, filter])

  return (
    <aside className="log">
      <span className="grabber" />
      <header className="log-head">
        <div className="log-title">
          <h2>Battle log</h2>
          <p className="muted">{subtitle}</p>
        </div>
        <button type="button" className="concede" onClick={onConcede}>
          {concedeLabel}
        </button>
      </header>
      <div className="log-filter">
        <div className="segmented">
          {(['All', 'Log', 'Chat'] as const).map((option) => (
            <button key={option} type="button" aria-pressed={filter === option} onClick={() => setFilter(option)}>
              {option}
            </button>
          ))}
        </div>
      </div>
      <div className="feed" ref={feed}>
        {/* ponytail: there is no chat yet, so Chat is empty and All is the log. */}
        {filter === 'Chat' ? (
          <p className="muted">No chat yet</p>
        ) : (
          log.map((entry, i) =>
            entry.kind === 'turn' ? (
              <div key={i} className="log-turn">
                {entry.text}
              </div>
            ) : (
              <div key={i} className={`log-line ${entry.kind}`}>
                <span className="dot" />
                {entry.text}
              </div>
            ),
          )
        )}
      </div>
      <form className="say" onSubmit={(event) => event.preventDefault()}>
        <input placeholder="Say something…" aria-label="Chat message" disabled />
        <button type="submit" aria-label="Send" disabled>
          <Icon src={send} size={22} />
        </button>
      </form>
    </aside>
  )
}
