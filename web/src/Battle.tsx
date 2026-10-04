import {
  useEffect,
  useLayoutEffect,
  useRef,
  useState,
  type CSSProperties,
  type DragEvent,
  type MouseEvent,
  type PointerEvent as ReactPointerEvent,
  type ReactNode,
  type RefObject,
} from 'react'
import chat from './assets/battle/chat.svg'
import send from './assets/battle/send.svg'
import { CARD_BASE, COIN_BASE, ENERGY_BASE, ICON_BASE, PLAYMAT_BASE, SLEEVE_BASE } from './art'
import { Icon } from './Home'
import type { BoardView, CardView, OptionView, PokemonView, SideView } from './protocol'
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
  cosmetics: { coin: '', sleeve: '', playmat: '' },
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
 * On your turn you drag what to act with (a card in hand, a Pokemon, the Energy Zone) onto the Pokemon it lands
 * on, or onto the field when it lands on none (a Basic to the Bench, an Item). Tapping it instead lights up those
 * Pokemon to tap, and lists what has no Pokemon to land on (Items). Tapping a Pokemon in play opens its card, with
 * its usable attacks, Ability and Retreat to tap on the art. Any other question lists its options, with its
 * Pokemon tappable too. Long-press (or right-click) any card to see it large. Every gesture
 * still answers with an option index the server offered.
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
  const { connection, board, flips, move, flash, decision, result, error, log, choose } = useGame(bot)
  // Picks are tagged with the decision they were made for, so a new question starts with nothing picked.
  const [pick, setPick] = useState<{ decision: number; source: Source } | null>(null)
  // The Pokemon in play whose card is open to pick an attack, Ability or Retreat from.
  const [preview, setPreview] = useState<{ decision: number; id: number } | null>(null)
  const [setup, setSetup] = useState<{ decision: number; active: number | null; bench: (number | null)[] } | null>(null)
  const [logOpen, setLogOpen] = useState(false)
  const [seen, setSeen] = useState(0)
  // Pokemon gone from play since the last board, still drawn in their old slot until their fade ends (none
  // with reduced motion, where no animation would end to remove them).
  const [leaving, setLeaving] = useState<Leaving[]>([])
  const [last, setLast] = useState(board)
  if (board !== last) {
    setLast(board)
    if (last && board && !calm()) setLeaving((all) => [...all, ...leavers(last, board)])
  }
  const mat = useRef<HTMLDivElement>(null)
  const handRow = useRef<HTMLDivElement>(null)
  const fit = useFit(mat)
  // The opponent's move: its attacker lunges, and whatever else it acts on in play glows.
  useEffect(() => {
    for (const id of move ? [move.card, move.target] : []) {
      const element = id === null ? null : mat.current!.querySelector(`.field [data-id="${id}"]`)
      if (element) animate(element, move!.kind === 'attack' && id === move!.card ? LUNGE : GLOW, { duration: 700, easing: EASE })
    }
  }, [move])

  const you = board?.you ?? NO_SIDE
  const opponent = board?.opponent ?? NO_SIDE
  const options = decision?.options.map((option, index) => ({ option, index })) ?? []
  const isSetup = options[0]?.option.kind === 'setup'
  const isTurn = options.some(({ option }) => option.kind === 'endTurn')
  const selected = pick && pick.decision === decision?.id ? pick.source : null
  const sourced = options.map((o) => ({ ...o, source: isTurn ? sourceOf(o.option, you.active?.id) : null }))
  const sources = new Set(sourced.map((o) => o.source).filter((source) => source !== null))
  const forSelected = sourced.filter((o) => selected !== null && o.source === selected)
  // Your Pokemon with an Ability they can use now.
  const abilities = new Set(sourced.filter((o) => o.option.kind === 'ability').map((o) => o.source))
  const loose = sourced.filter((o) => isTurn && o.source === null && o.option.kind !== 'endTurn')
  // For a picked Basic, each empty Bench slot it can go into and the option that puts it there.
  const benchPlays = new Map(forSelected.flatMap(({ option, index }) => (option.slot === null ? [] : [[option.slot, index] as const])))

  // The Pokemon (and, outside a turn, hand cards) that answer with one click.
  const targets = new Map<number, number>()
  const handPicks = new Map<number, number>()
  for (const { option, index } of isTurn ? forSelected : isSetup ? [] : options) {
    if (option.target !== null && !targets.has(option.target)) targets.set(option.target, index)
    if (!isTurn && option.kind === 'card' && option.card !== null) handPicks.set(option.card, index)
  }

  // Setup: the clicked Basics move to the Active Spot and Bench slots until Confirm.
  const picks = setup && setup.decision === decision?.id ? setup : { active: null, bench: [null, null, null] as (number | null)[] }
  const setupChoice = options.findIndex(
    ({ option }) => option.card === picks.active && option.bench.every((id, slot) => id === picks.bench[slot]),
  )
  /** A tapped Basic: off the board if it is on it, else to the Active Spot, else to the first empty Bench slot. */
  function toggleSetup(id: number) {
    const slot = picks.bench.indexOf(id)
    if (id === picks.active || slot >= 0) placeSetup(id, null)
    else if (picks.active === null) placeSetup(id, 'active')
    else if (picks.bench.includes(null)) placeSetup(id, picks.bench.indexOf(null))
  }
  /** A Basic dropped in setup onto the Active Spot, a Bench slot, or back to hand; whatever was there takes its place. */
  function placeSetup(id: number, where: 'active' | number | null) {
    if (!decision) return
    const board = { active: picks.active, bench: [...picks.bench] }
    const from = id === board.active ? 'active' : board.bench.includes(id) ? board.bench.indexOf(id) : null
    const put = (at: 'active' | number | null, card: number | null) => {
      if (at === 'active') board.active = card
      else if (at !== null) board.bench[at] = card
    }
    const displaced = where === 'active' ? board.active : where === null ? null : board.bench[where]
    put(from, displaced)
    put(where, id)
    setSetup({ decision: decision.id, ...board })
  }
  const setupBasics = new Set(options.map(({ option }) => option.card))
  const byId = new Map(you.hand.map((card) => [card.id, card]))
  const shownActive = isSetup && picks.active !== null ? asPokemon(byId.get(picks.active)) : you.active
  const shownBench = isSetup ? picks.bench.map((id) => (id === null ? null : asPokemon(byId.get(id)))) : you.bench
  // A card you just played leaves your hand while it flashes, before the board that takes it arrives.
  const hand = isSetup
    ? you.hand.filter((card) => card.id !== picks.active && !picks.bench.includes(card.id))
    : you.hand.filter((card) => card.id !== flash?.card)

  function select(source: Source) {
    if (decision && sources.has(source)) setPick(selected === source ? null : { decision: decision.id, source })
  }
  function clickPokemon(id: number) {
    const answer = targets.get(id)
    if (answer !== undefined) choose(answer)
    else if (decision && sources.has(id)) {
      setPick(null)
      setPreview({ decision: decision.id, id })
    }
  }
  const previewed = preview && preview.decision === decision?.id ? [you.active, ...you.bench].find((p) => p?.id === preview.id) : null
  function clickHand(id: number) {
    const answer = handPicks.get(id)
    if (isSetup) toggleSetup(id)
    else if (answer !== undefined) choose(answer)
    else select(id)
  }
  const pokemonState = (id: number) =>
    targets.has(id) ? 'target' : selected === id ? 'selected' : sources.has(id) ? 'can' : undefined

  // Dragging picks the source, so the Pokemon it can land on light up as they do for a tap.
  function dragStart(source: Source) {
    if (decision && isTurn) setPick({ decision: decision.id, source })
  }
  function drop(source: Source, at: Element | null, y: number) {
    const slot = Number(at?.closest('.your-bench [data-slot]')?.getAttribute('data-slot') ?? NaN)
    if (isSetup) {
      if (typeof source === 'number') placeSetup(source, at?.closest('.your-active') ? 'active' : Number.isNaN(slot) ? null : slot)
      return
    }
    const onto = Number(at?.closest('[data-id]')?.getAttribute('data-id') ?? NaN)
    // Only the board above your hand, as the highlight shows: the field runs under the hand's row too.
    const onField = !!at?.closest('.field') && y < handRow.current!.getBoundingClientRect().top
    const hit =
      sourced.find((o) => o.source === source && o.option.target === onto) ??
      sourced.find((o) => o.source === source && o.option.slot === slot) ??
      sourced.find((o) => onField && o.source === source && o.option.kind === 'play' && o.option.target === null && o.option.slot === null)
    if (hit) choose(hit.index)
    else setPick(null)
  }
  const { inspect, setInspect, ghost, gestures } = useGestures(dragStart, drop)
  // Mid-drag, the places a drop would land that aren't a Pokemon (those already show as targets) light up too.
  const setupDrop = !!ghost && isSetup
  const fieldDrop = !!ghost && isTurn && forSelected.some((o) => o.option.kind === 'play' && o.option.target === null && o.option.slot === null)

  let status: string
  if (result) status = result
  else if (connection === 'connecting') status = 'Connecting…'
  else if (connection === 'waiting') status = 'Waiting for an opponent…'
  else if (connection === 'closed') status = 'Disconnected'
  else if (board && !board.you.active) status = decision ? 'Set up your board' : 'Waiting for your opponent'
  else if (board) status = `Turn ${board.turn} · ${board.yourTurn ? 'Your turn' : "Opponent's turn"}`
  else status = 'Starting…'

  let menu: { title: string; note?: string; items: { option: OptionView; index: number }[] } | null = null
  if (isSetup) menu = { title: decision!.prompt, note: 'Drag a Basic to your Active Spot and others to your Bench, or tap them in turn.', items: [] }
  // Options that land on a Pokemon or a Bench slot are answered by dragging onto it or tapping it, and attacks and
  // Abilities from the card preview, so only the rest are listed.
  else if (isTurn && selected !== null)
    menu = {
      title: nameOf(selected),
      note: forSelected.some((o) => o.option.target !== null)
        ? 'Drag onto a highlighted Pokémon, or tap one.'
        : benchPlays.size
          ? 'Drag onto a highlighted Bench slot, or tap one.'
          : undefined,
      items: forSelected.filter(
        (o) => o.option.target === null && o.option.slot === null && o.option.kind !== 'attack' && o.option.kind !== 'ability',
      ),
    }
  else if (isTurn && loose.length) menu = { title: 'More', items: loose }
  else if (decision && !isTurn) menu = { title: decision.prompt, items: options }

  function nameOf(source: Source) {
    if (source === 'energy') return 'Energy'
    const pokemon = [you.active, ...you.bench].find((p) => p?.id === source)
    return pokemon?.name ?? byId.get(source)?.name ?? ''
  }

  const leavingAt = (side: Leaving['side'], slot: Leaving['slot']) =>
    leaving
      .filter((l) => l.side === side && l.slot === slot)
      .map((l) => (
        <div
          key={l.pokemon.id}
          className={l.knockedOut ? 'leaving knocked-out' : 'leaving'}
          inert
          onAnimationEnd={(event) => event.target === event.currentTarget && setLeaving((all) => all.filter((x) => x !== l))}
        >
          <Pokemon pokemon={l.pokemon} onClick={() => {}} />
        </div>
      ))

  const over = result !== null || connection === 'closed'
  // The mat and coin are those of whoever's turn it is; each side's cards keep their own sleeve.
  const current = board?.yourTurn ? you : opponent
  const yourName = `${me?.name ?? (you.name || 'You')}`
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
    <div className={logOpen ? 'battle log-open' : 'battle'} {...gestures}>
      <div
        className="mat"
        ref={mat}
        style={board ? { backgroundImage: `url("${PLAYMAT_BASE}/${current.cosmetics.playmat}")` } : undefined}
      >
        <div className="stage" style={{ zoom: fit.zoom, height: fit.height }}>
          {flips && <Coins key={flips.id} heads={flips.heads} coin={current.cosmetics.coin} />}
          {flash && <Card key={flash.id} card={{ card: flash.shown, name: '' }} className="flash" />}
          <div className={fieldDrop ? 'field drop' : 'field'}>
            <Bench
              className="opp-bench"
              pokemon={opponent.bench}
              state={pokemonState}
              onClick={clickPokemon}
              leaving={(i) => leavingAt('opponent', i)}
            />
            <EnergyZone className="opp-energy" side={opponent} />
            <Slot className="active opp-active">
              {leavingAt('opponent', 'active')}
              {opponent.active && (
                <Pokemon key={opponent.active.id} pokemon={opponent.active} state={pokemonState(opponent.active.id)} onClick={clickPokemon} />
              )}
            </Slot>
            <Zones className="opp-zones" side={opponent} deckFirst={false} />

            <div className="divider">
              <button type="button" className="log-button" aria-label="Battle log" onClick={toggleLog}>
                <Icon src={chat} size={20} />
                {unread > 0 && <span className="badge">{unread}</span>}
              </button>
              <div className="turn" key={status}>
                {board && <img className="coin" src={`${COIN_BASE}/${current.cosmetics.coin}`} alt="" />}
                <span className={board?.yourTurn && !over ? 'dot yours' : 'dot'} />
                {status}
              </div>
              {board?.stadium && <Card key={board.stadium.id} card={board.stadium} className="stadium" />}
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

            <Slot className={setupDrop ? 'active your-active drop' : 'active your-active'}>
              {leavingAt('you', 'active')}
              {shownActive && (
                <Pokemon
                  key={shownActive.id}
                  pokemon={shownActive}
                  state={isSetup ? 'selected' : pokemonState(shownActive.id)}
                  ability={abilities.has(shownActive.id)}
                  onClick={isSetup ? toggleSetup : clickPokemon}
                />
              )}
            </Slot>
            <Zones className="your-zones" side={you} deckFirst />
            <Bench
              className="your-bench"
              pokemon={shownBench}
              state={isSetup ? () => 'selected' : pokemonState}
              onClick={isSetup ? toggleSetup : clickPokemon}
              abilities={abilities}
              open={(i) => (setupDrop && !shownBench[i]) || benchPlays.has(i)}
              onSlot={isSetup ? undefined : (i) => choose(benchPlays.get(i)!)}
              leaving={(i) => leavingAt('you', i)}
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
          <div className="hand" ref={handRow}>
            {hand.map((card, i) => {
              const can = isSetup ? setupBasics.has(card.id) : handPicks.has(card.id) || sources.has(card.id)
              return (
                <button
                  key={card.id}
                  type="button"
                  className={selected === card.id ? 'hand-card selected' : can ? 'hand-card can' : 'hand-card'}
                  style={fan(i, hand.length, 6)}
                  aria-disabled={!can}
                  data-card={card.card}
                  data-drag={can && !handPicks.has(card.id) ? card.id : undefined}
                  onClick={() => can && clickHand(card.id)}
                >
                  <Card card={card} />
                </button>
              )
            })}
          </div>

          <PlayerInfo className="opp-info" name={theirName} points={opponent.points} />
          <PlayerInfo className="your-info" name={yourName} points={you.points} icon={me?.profileIcon} />
        </div>
        {/* Over the mat, not the window, so they centre on the board and leave the log uncovered. */}
        {previewed && (
          <MovePreview
            pokemon={previewed}
            active={previewed.id === you.active?.id}
            options={sourced.filter((o) => o.source === previewed.id)}
            onChoose={(index) => {
              setPreview(null)
              choose(index)
            }}
            onRetreat={() => {
              setPreview(null)
              setPick({ decision: decision!.id, source: previewed.id })
            }}
            onClose={() => setPreview(null)}
          />
        )}
        {inspect && (
          <button type="button" className="inspect" aria-label="Close" onClick={() => setInspect(null)}>
            <img src={`${CARD_BASE}/${inspect}.webp`} alt="" />
          </button>
        )}
      </div>

      <button type="button" className="scrim" aria-label="Close the battle log" onClick={toggleLog} />
      {ghost}
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

/**
 * Long-press (or right-click) on a `data-card` element to inspect that card, and drag a `data-drag` element
 * (an instance id, or "energy") to drop it wherever the pointer lets go. A press that moves first is a drag;
 * the click a long press or drag would end in is swallowed, so it doesn't also tap.
 */
function useGestures(onStart: (source: Source) => void, onDrop: (source: Source, at: Element | null, y: number) => void) {
  const [inspect, setInspect] = useState<string | null>(null)
  const [drag, setDrag] = useState<{ src: string; x: number; y: number; w: number; h: number } | null>(null)
  const swallow = useRef(false)
  // The latest handlers, so a drag that outlives a render answers the decision open when it ends.
  const latest = useRef({ onStart, onDrop })
  useEffect(() => {
    latest.current = { onStart, onDrop }
  })

  function onPointerDown(event: ReactPointerEvent) {
    swallow.current = false
    if (event.button !== 0) return
    const target = event.target as Element
    const card = target.closest('[data-card]')?.getAttribute('data-card')
    const handle = target.closest('[data-drag]')
    if (!card && !handle) return
    const raw = handle?.getAttribute('data-drag')
    const source: Source = raw === 'energy' ? 'energy' : Number(raw)
    const start = { x: event.clientX, y: event.clientY }
    let dragging = false
    const timer = setTimeout(() => {
      end()
      swallow.current = true
      setInspect(card!)
    }, 450)
    if (!card) clearTimeout(timer)

    function move(e: PointerEvent) {
      if (!dragging && Math.hypot(e.clientX - start.x, e.clientY - start.y) < 8) return
      clearTimeout(timer)
      if (!handle) return end()
      if (!dragging) {
        dragging = true
        latest.current.onStart(source)
      }
      const art = handle.querySelector('img')!
      const { width, height } = art.getBoundingClientRect()
      setDrag({ src: art.src, x: e.clientX, y: e.clientY, w: width, h: height })
    }
    function up(e: PointerEvent) {
      end()
      if (!dragging) return
      swallow.current = true
      latest.current.onDrop(source, document.elementFromPoint(e.clientX, e.clientY), e.clientY)
    }
    function end() {
      clearTimeout(timer)
      setDrag(null)
      removeEventListener('pointermove', move)
      removeEventListener('pointerup', up)
      removeEventListener('pointercancel', end)
    }
    addEventListener('pointermove', move)
    addEventListener('pointerup', up)
    addEventListener('pointercancel', end)
  }

  const ghost = drag && (
    <img
      className="ghost"
      src={drag.src}
      alt=""
      style={{ left: drag.x - drag.w / 2, top: drag.y - drag.h / 2, width: drag.w, height: drag.h }}
    />
  )
  const gestures = {
    onPointerDown,
    onClickCapture(event: MouseEvent) {
      if (!swallow.current) return
      swallow.current = false
      event.stopPropagation()
    },
    onContextMenu(event: MouseEvent) {
      const card = (event.target as Element).closest('[data-card]')?.getAttribute('data-card')
      if (!card) return
      event.preventDefault()
      setInspect(card)
    },
    onDragStart: (event: DragEvent) => event.preventDefault(),
  }
  return { inspect, setInspect, ghost, gestures }
}

/**
 * A Pokemon's card, grown from where it sits on the board, with its usable attacks, Ability and Retreat
 * lit up on the art to tap. Anywhere else closes it.
 */
function MovePreview({
  pokemon,
  active,
  options,
  onChoose,
  onRetreat,
  onClose,
}: {
  pokemon: PokemonView
  active: boolean
  options: { option: OptionView; index: number }[]
  onChoose: (index: number) => void
  onRetreat: () => void
  onClose: () => void
}) {
  const card = useRef<HTMLDivElement>(null)
  // Centred exactly on the Pokemon it was opened from, and grown out of it.
  useLayoutEffect(() => {
    const element = card.current!
    const from = document.querySelector(`.battle [data-id="${pokemon.id}"]`)?.getBoundingClientRect()
    if (!from) return
    const board = element.parentElement!.getBoundingClientRect()
    // Its layout size, not its box, which a grow already under way would shrink.
    const { offsetWidth: width, offsetHeight: height } = element
    element.style.left = `${from.left + from.width / 2 - board.left - width / 2}px`
    element.style.top = `${from.top + from.height / 2 - board.top - height / 2}px`
    animate(
      element,
      [{ transform: `scale(${from.width / width})`, opacity: 0.6 }, { transform: 'none', opacity: 1 }],
      { duration: 220, easing: EASE },
    )
  }, [pokemon.id])

  const boxes = textBoxes(pokemon)
  const spots: { label: string; box: Box; onClick?: () => void }[] = []
  const answer = (index: number | undefined) => (index === undefined ? undefined : () => onChoose(index))
  if (pokemon.ability && boxes.ability) {
    spots.push({ label: pokemon.ability.name, box: boxes.ability, onClick: answer(options.find((o) => o.option.kind === 'ability')?.index) })
  }
  pokemon.attacks.forEach((attack, i) => {
    const index = options.find((o) => o.option.kind === 'attack' && o.option.label === `Attack: ${attack.name}`)?.index
    spots.push({ label: attack.name, box: boxes.attacks[i], onClick: answer(index) })
  })
  const retreats = options.some((o) => o.option.kind === 'retreat')
  // Only the Active can retreat, so a Benched Pokemon's retreat bar is left as printed.
  if (active) spots.push({ label: 'Retreat', box: RETREAT, onClick: retreats ? onRetreat : undefined })

  return (
    <div className="move-preview" onClick={onClose}>
      <div className="preview-card" ref={card}>
        <img src={`${CARD_BASE}/${pokemon.card}.webp`} alt={pokemon.name} />
        {spots.map(({ label, box, onClick }) => (
          <button
            key={label}
            type="button"
            className={onClick ? 'spot can' : 'spot'}
            aria-label={label}
            disabled={!onClick}
            style={{
              left: `${(box.left / CARD_W) * 100}%`,
              width: `${((box.right - box.left) / CARD_W) * 100}%`,
              top: `${(box.top / CARD_H) * 100}%`,
              height: `${((box.bottom - box.top) / CARD_H) * 100}%`,
            }}
            onClick={(event) => {
              event.stopPropagation()
              onClick?.()
            }}
          />
        ))}
      </div>
    </div>
  )
}

/** Card art is 670x936; boxes on it are in those pixels. */
const CARD_W = 670
const CARD_H = 936
type Box = { left: number; right: number; top: number; bottom: number }
const RETREAT: Box = { left: 345, right: 625, top: 798, bottom: 840 }

/**
 * Where the Ability and each attack are printed, worked out from how long their text is: the Ability sits at the top
 * of the text area with the attacks under it, and without one the attacks are centred in it.
 * ponytail: estimated from the printed layout (55 characters a line), so an unusually worded card can be off by a few
 * pixels; measure boxes per card if that shows.
 */
function textBoxes(pokemon: PokemonView): { ability?: Box; attacks: Box[] } {
  const height = (text: string) => 45 + 32 * Math.ceil(text.length / 55)
  const box = (top: number, h: number): Box => ({ left: 32, right: 638, top: top - 10, bottom: top + h + 10 })
  const heights = pokemon.attacks.map((attack) => height(attack.text))
  let top = 640 - (heights.reduce((sum, h) => sum + h, 0) + 40 * (heights.length - 1)) / 2
  let ability: Box | undefined
  if (pokemon.ability) {
    const h = height(pokemon.ability.text)
    ability = box(495, h)
    top = 495 + h + 40
  }
  const attacks = heights.map((h) => {
    const at = box(top, h)
    top += h + 40
    return at
  })
  return { ability, attacks }
}

/**
 * The engine's coin flips, spun one after another with the coin of whoever's turn it is and then faded: only a show of results
 * already decided. useGame holds the board they led to until the last coin lands.
 */
function Coins({ heads, coin }: { heads: boolean[]; coin: string }) {
  const [shown, setShown] = useState(true)
  // Kept from the flip: an attack's board, arriving once the coins land, may already be the other player's turn.
  const [face] = useState(coin)
  // When the CSS fade ends: each coin starts 0.3s after the last and spins for 0.6s, then 0.5s rest and 0.3s fade.
  useEffect(() => {
    const timer = setTimeout(() => setShown(false), heads.length * 300 + 1100)
    return () => clearTimeout(timer)
  }, [heads])
  if (!shown) return null
  return (
    <div
      className="coins"
      role="img"
      aria-label={heads.map((h) => (h ? 'Heads' : 'Tails')).join(', ')}
      style={{ '--n': heads.length } as CSSProperties}
    >
      {heads.map((h, i) => (
        <div key={i} className={h ? 'coin-flip' : 'coin-flip tails'} style={{ '--i': i } as CSSProperties}>
          <img src={`${COIN_BASE}/${face}`} alt="" />
          <img className="back" src={`${COIN_BASE}/Coin_Tails.png`} alt="" />
        </div>
      ))}
    </div>
  )
}

const EASE = 'cubic-bezier(0.2, 0.8, 0.2, 1)'
const LUNGE: Keyframe[] = [{}, { translate: '0 32px', scale: 1.08, offset: 0.35 }, {}]
const GLOW: Keyframe[] = [{}, { filter: 'brightness(1.25) drop-shadow(0 0 10px var(--color-glow))', scale: 1.06, offset: 0.4 }, {}]
const SHAKE: Keyframe[] = [{ translate: '0' }, { translate: '-5px' }, { translate: '4px' }, { translate: '-2px' }, { translate: '0' }]

/** Whether the system asks for reduced motion; the CSS animations stop there too. */
const calm = () => matchMedia('(prefers-reduced-motion: reduce)').matches

function animate(element: Element, keyframes: Keyframe[], options: KeyframeAnimationOptions) {
  if (!calm()) element.animate(keyframes, options)
}

/** A Pokemon that left play, and the slot it left: the Active Spot or a Bench index. */
type Leaving = { side: 'you' | 'opponent'; slot: number | 'active'; pokemon: PokemonView; knockedOut: boolean }

/**
 * The Pokemon in play on `before` that are in play nowhere on `after` (a retreat or bench shuffle only moves them).
 * One that went to the discard pile was Knocked Out, so it takes the hit it was removed before showing, at 0 HP.
 */
function leavers(before: BoardView, after: BoardView): Leaving[] {
  const inPlay = new Set([after.you, after.opponent].flatMap((side) => [side.active, ...side.bench]).map((p) => p?.id))
  return (['you', 'opponent'] as const).flatMap((side) => {
    const discarded = new Set(after[side].discard.map((card) => card.id))
    const slots: (readonly [Leaving['slot'], PokemonView | null])[] = [['active', before[side].active], ...before[side].bench.map((p, i) => [i, p] as const)]
    return slots.flatMap(([slot, pokemon]) => {
      if (!pokemon || inPlay.has(pokemon.id)) return []
      const knockedOut = discarded.has(pokemon.id)
      return [{ side, slot, pokemon: knockedOut ? { ...pokemon, hp: 0 } : pokemon, knockedOut }]
    })
  })
}

/** Where card `i` of `n` sits in a fan drawn for `designed` cards; wider hands squeeze in. */
function fan(i: number, n: number, designed: number) {
  const d = i - (n - 1) / 2
  return { '--d': d, '--ad': Math.abs(d), '--f': Math.min(1, (designed - 1) / Math.max(1, n - 1)) } as CSSProperties
}

/** A hand card picked during setup, drawn in play; its HP isn't sent until it is placed, so none is shown. */
function asPokemon(card: CardView | undefined): PokemonView | null {
  return card ? { ...card, hp: 0, maxHp: 0, energy: {}, statuses: [], tool: null, attacks: [], ability: null } : null
}

function Card({ card, className = 'art' }: { card: Pick<CardView, 'card' | 'name'>; className?: string }) {
  return <img className={className} src={`${CARD_BASE}/${card.card}.webp`} alt={card.name} title={card.name} data-card={card.card} />
}

/** A face-down card, in its side's sleeve. */
function Back({ sleeve }: { sleeve: string }) {
  return <img className="art" src={`${SLEEVE_BASE}/${sleeve}`} alt="" />
}

function Slot({ className, title, slot, children }: { className: string; title?: string; slot?: number; children?: ReactNode }) {
  return (
    <div className={`slot ${className}`} title={title} data-slot={slot}>
      {children}
    </div>
  )
}

function Pokemon({
  pokemon,
  state,
  ability = false,
  onClick,
}: {
  pokemon: PokemonView
  state?: string
  /** Whether it has an Ability it can use now. */
  ability?: boolean
  onClick: (id: number) => void
}) {
  const self = useRef<HTMLButtonElement>(null)
  const was = useRef(pokemon)
  // Entering play is a CSS animation on mount; what changes on a Pokemon already in play is compared here.
  useEffect(() => {
    const before = was.current
    was.current = pokemon
    // A Basic placed in setup has no HP yet, so its first real board is no change.
    if (before.maxHp === 0) return
    const element = self.current!
    const hp = element.querySelector('.hp')
    if (pokemon.card !== before.card) {
      animate(element, [{ filter: 'brightness(1.8)', scale: 1.06 }, {}], { duration: 450, easing: EASE })
    } else if (pokemon.hp < before.hp) {
      animate(element, SHAKE, { duration: 350, easing: 'ease-out' })
      if (hp) animate(hp, [{ background: 'var(--color-danger)', color: '#fff' }, {}], { duration: 600 })
    } else if (pokemon.hp > before.hp && hp) {
      animate(hp, [{ background: 'var(--heal)', color: '#fff' }, {}], { duration: 600 })
    }
  }, [pokemon])

  const types = Object.entries(pokemon.energy)
  // From 5 Energy on, each type shows once with its count, so the row stays on the card.
  const condensed = types.reduce((sum, [, count]) => sum + count, 0) >= 5
  const energy = condensed ? types : types.flatMap(([type, count]) => Array.from({ length: count }, () => [type, 1] as const))
  const tags = [...pokemon.statuses, ...(pokemon.tool ? [pokemon.tool.name] : [])]
  return (
    <button
      ref={self}
      type="button"
      className={['pokemon', state, ability && 'ability'].filter(Boolean).join(' ')}
      aria-disabled={!state}
      data-id={pokemon.id}
      data-card={pokemon.card}
      data-drag={state === 'can' || state === 'selected' ? pokemon.id : undefined}
      onClick={() => state && onClick(pokemon.id)}
    >
      <Card card={pokemon} />
      {pokemon.maxHp > 0 && <span className="hp">{pokemon.hp}</span>}
      {energy.length > 0 && (
        <span className="attached">
          {energy.map(([type, count], i) => (
            <span key={i} className="energy">
              <img src={`${ENERGY_BASE}/${type.toLowerCase()}.png`} alt={type} title={count > 1 ? `${type} ×${count}` : type} />
              {count > 1 && <span className="count">{count}</span>}
            </span>
          ))}
        </span>
      )}
      {tags.length > 0 && <span key={tags.join()} className="tags">{tags.join(' · ')}</span>}
    </button>
  )
}

function Bench({
  className,
  pokemon,
  state,
  onClick,
  abilities,
  open = () => false,
  onSlot,
  leaving,
}: {
  className: string
  /** By slot, null where it is empty. */
  pokemon: (PokemonView | null)[]
  state: (id: number) => string | undefined
  onClick: (id: number) => void
  abilities?: Set<Source | null>
  /** Whether a card could go into empty slot `i`, by a drop or (with `onSlot`) a tap. */
  open?: (i: number) => boolean
  onSlot?: (i: number) => void
  /** What is fading out of slot `i`. */
  leaving: (i: number) => ReactNode
}) {
  return (
    <div className={`bench ${className}`}>
      {[0, 1, 2].map((i) => {
        const p = pokemon[i]
        const lit = !p && open(i)
        return (
          <Slot key={i} className={lit ? 'benched drop' : 'benched'} slot={i}>
            {leaving(i)}
            {p && <Pokemon key={p.id} pokemon={p} state={state(p.id)} ability={abilities?.has(p.id)} onClick={onClick} />}
            {lit && onSlot && <button type="button" className="slot-pick" aria-label={`Bench slot ${i + 1}`} onClick={() => onSlot(i)} />}
          </Slot>
        )
      })}
    </div>
  )
}

/** The discard pile (its top card) and the deck (face down while it has cards). */
function Zones({ className, side, deckFirst }: { className: string; side: SideView; deckFirst: boolean }) {
  const top = side.discard.at(-1)
  const discard = (
    <Slot key="discard" className="pile">
      {top && <Card key={top.id} card={top} />}
    </Slot>
  )
  const deck = (
    <Slot key="deck" className="pile deck" title={`${side.deckSize} cards in deck`}>
      {side.deckSize > 0 && <Back sleeve={side.cosmetics.sleeve} />}
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
    <button
      type="button"
      className={`energy-zone ${className} ${state ?? ''}`}
      aria-disabled={!state}
      data-drag={state ? 'energy' : undefined}
      onClick={() => state && onClick?.()}
    >
      <span className="glow" />
      {side.energy && (
        <img className="current" src={`${ENERGY_BASE}/${side.energy.toLowerCase()}.png`} alt={side.energy} title={`Energy: ${side.energy}`} />
      )}
      {side.nextEnergy && (
        <img className="next" src={`${ENERGY_BASE}/${side.nextEnergy.toLowerCase()}.png`} alt={side.nextEnergy} title={`Next: ${side.nextEnergy}`} />
      )}
    </button>
  )
}

/** Face down, except any cards the opponent has had to reveal. */
function OpponentHand({ side }: { side: SideView }) {
  return (
    <div className="opp-hand" aria-label={`Opponent's hand: ${side.handSize} cards`}>
      {Array.from({ length: side.handSize }, (_, i) => (
        <span key={i} className="opp-card" style={fan(i, side.handSize, 5)}>
          {i < side.hand.length ? <Card card={side.hand[i]} /> : <Back sleeve={side.cosmetics.sleeve} />}
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
