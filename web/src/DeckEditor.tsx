import { useEffect, useRef, useState } from 'react'
import edit from './assets/decks/edit.svg'
import plus from './assets/decks/plus.svg'
import back from './assets/editor/back.svg'
import bin from './assets/editor/bin.svg'
import check from './assets/editor/check.svg'
import close from './assets/editor/close.svg'
import down from './assets/editor/down.svg'
import search from './assets/editor/search.svg'
import up from './assets/editor/up.svg'
import chevron from './assets/home/chevron-down.svg'
import { CARD_BASE, COIN_BASE, ENERGY_BASE, PLAYMAT_BASE, SLEEVE_BASE, listArt } from './art'
import { DECK_SIZE } from './Decks'
import { Icon, Shell, type Page } from './Home'

/** Mirrors the server's {@code Cards.Entry}. {@code search} is the attack and ability names. */
type CardEntry = { id: string; name: string; kind: string; types: string[]; search: string }

/** Mirrors the server's {@code Decks.Api.Draft}: what a save sends, and a loaded deck without its id. */
type Draft = {
  name: string
  cards: string[]
  energy: string[]
  focusCard1: string | null
  focusCard2: string | null
  coin: string
  sleeve: string
  playmat: string
}

/** The rules {@code DeckValidator} checks before a game; a draft may fall short of them, never exceed them. */
const MAX_COPIES = 2
const MAX_ENERGY = 3

/** The Energy Zone types: every {@code Type} but Dragon and Colorless, which have no Energy. */
const ENERGY = ['GRASS', 'FIRE', 'WATER', 'LIGHTNING', 'PSYCHIC', 'FIGHTING', 'DARKNESS', 'METAL']

/** How many cards the phone's collapsed deck bar shows before "+N". */
const STRIP = 7

const NEW_DECK: Draft = {
  name: 'New deck',
  cards: [],
  energy: [],
  focusCard1: null,
  focusCard2: null,
  coin: 'Coin_Pokéball.png',
  sleeve: 'Sleeve_Default.png',
  playmat: 'Playmat_Default.png',
}

const COSMETICS = {
  coin: { base: COIN_BASE, title: 'Choose a coin', detail: "" },
  sleeve: { base: SLEEVE_BASE, title: 'Choose a sleeve', detail: "" },
  playmat: { base: PLAYMAT_BASE, title: 'Choose a playmat', detail: '' },
}
type Cosmetic = keyof typeof COSMETICS

/** "LIGHTNING" → "Lightning", "coin" → "Coin". */
const capitalise = (word: string) => word[0].toUpperCase() + word.slice(1).toLowerCase()

/** "Coin_Ho-Oh_Lugia.png" → "Ho-Oh Lugia". */
const cosmeticName = (file: string) => file.replace(/^[^_]+_|\.png$/g, '').replace(/_/g, ' ')

const energyIcon = (type: string) => `${ENERGY_BASE}/${type.toLowerCase()}.png`

/**
 * Builds one deck: the card browser on the left adds cards, the deck panel (a bottom sheet on a phone) removes
 * them and sets the name, energy, cover cards and cosmetics. Nothing is saved until Save; a draft that is not yet
 * a legal deck saves too.
 */
export function DeckEditor({
  id,
  onNavigate,
  onClose,
}: {
  id: number | 'new'
  onNavigate: (page: Page) => void
  onClose: () => void
}) {
  const [catalogue, setCatalogue] = useState<CardEntry[]>([])
  const [deck, setDeck] = useState<Draft | null>(id === 'new' ? NEW_DECK : null)
  const [query, setQuery] = useState('')
  const [kind, setKind] = useState('All')
  const [type, setType] = useState('All')
  const [picking, setPicking] = useState<Cosmetic | 'energy' | null>(null)
  const [open, setOpen] = useState(false)
  const [error, setError] = useState('')
  const [problems, setProblems] = useState<string[]>([])

  // The rules live in DeckValidator, so the server judges the draft. A reply for an older draft is dropped.
  const cards = deck?.cards
  const energy = deck?.energy
  useEffect(() => {
    if (!cards) return
    let stale = false
    fetch('/api/decks/problems', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ cards, energy }),
    })
      .then((response) => (response.ok ? response.json() : []))
      .then((found) => stale || setProblems(found), () => {})
    return () => {
      stale = true
    }
  }, [cards, energy])

  useEffect(() => {
    fetch('/api/cards')
      .then((response) => (response.ok ? response.json() : []))
      .then(setCatalogue, () => {})
  }, [])
  useEffect(() => {
    if (id === 'new') return
    fetch(`/api/decks/${id}`)
      .then((response) => (response.ok ? response.json() : Promise.reject()))
      .then(
        ({ id: _id, ...draft }: Draft & { id: number }) => setDeck(draft),
        () => setError("Couldn't load the deck"),
      )
  }, [id])

  if (!deck) {
    return (
      <Shell page="Decks" onNavigate={onNavigate}>
        <p className="editor-error">{error}</p>
      </Shell>
    )
  }

  const names = new Map(catalogue.map((card) => [card.id, card.name]))
  const wanted = query.trim().toLowerCase()
  const shown = catalogue.filter(
    (card) =>
      (kind === 'All' || card.kind === kind) &&
      (type === 'All' || card.types.includes(type)) &&
      `${card.name} ${card.search}`.toLowerCase().includes(wanted),
  )
  const full = deck.cards.length >= DECK_SIZE
  // The copy limit is by name, as in DeckValidator: two printings of Pikachu are the same card.
  const copies = (name: string) => deck.cards.filter((card) => names.get(card) === name).length
  const focus = [deck.focusCard1, deck.focusCard2].filter((card) => card !== null)
  const change = (changes: Partial<Draft>) => setDeck({ ...deck, ...changes })
  const setCards = (cards: string[], focus: string[]) =>
    change({ cards, focusCard1: focus[0] ?? null, focusCard2: focus[1] ?? null })

  function remove(index: number) {
    const cards = deck!.cards.filter((_, i) => i !== index)
    setCards(cards, focus.filter((card) => cards.includes(card)))
  }

  /** Starring a third card drops the oldest star: the decks page shows two. */
  function star(card: string) {
    setCards(deck!.cards, focus.includes(card) ? focus.filter((starred) => starred !== card) : [...focus, card].slice(-2))
  }

  function toggleEnergy(type: string) {
    const energy = deck!.energy
    if (energy.includes(type)) change({ energy: energy.filter((chosen) => chosen !== type) })
    else if (energy.length < MAX_ENERGY) change({ energy: [...energy, type] })
  }

  function save() {
    const failed = () => setError("Couldn't save the deck")
    fetch(id === 'new' ? '/api/decks' : `/api/decks/${id}`, {
      method: id === 'new' ? 'POST' : 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(deck),
    }).then((response) => (response.ok ? onClose() : failed()), failed)
  }

  const unnamed = !deck.name.trim()

  return (
    <Shell page="Decks" onNavigate={onNavigate} className="content editor">
      <div className="card-browser">
        <header className="editor-head">
          <button type="button" className="secondary back" aria-label="Back to decks" onClick={onClose}>
            <Icon src={back} size={22} />
          </button>
          <div className="title">
            <div className="crumb">Decks /</div>
            <h1>{id === 'new' ? 'New deck' : 'Edit deck'}</h1>
            <div className="subtitle">{deck.name}</div>
          </div>
          <button type="button" className="primary save" disabled={unnamed} onClick={save}>
            Save
          </button>
        </header>

        <div className="filters">
          <label className="search">
            <Icon src={search} size={18} />
            <input
              type="search"
              placeholder="Search any card by name, attack or ability…"
              aria-label="Search cards"
              value={query}
              onChange={(event) => setQuery(event.target.value)}
            />
          </label>
          <Dropdown label="Card type" value={kind} options={catalogue.map((card) => card.kind)} onChange={setKind} />
          <Dropdown label="Type" value={type} options={catalogue.flatMap((card) => card.types)} onChange={setType} />
          <span className="muted showing">
            Showing {shown.length} {shown.length === 1 ? 'card' : 'cards'}
          </span>
        </div>

        <div className="card-grid">
          {shown.map((card) => {
            const count = deck.cards.filter((id) => id === card.id).length
            // The bin is a sibling of the card, not inside it: a button can't hold a button.
            return (
              <div key={card.id} className="browser-item">
                <button
                  type="button"
                  className="browser-card"
                  disabled={full || copies(card.name) >= MAX_COPIES}
                  onClick={() => change({ cards: [...deck.cards, card.id] })}
                >
                  <img src={`${CARD_BASE}/${card.id}.webp`} alt={card.name} loading="lazy" />
                  {count > 0 && <span className="count">×{count}</span>}
                  <span className="add">
                    <Icon src={plus} size={16} />
                    Add
                  </span>
                </button>
                {count > 0 && (
                  <button
                    type="button"
                    className="bin"
                    aria-label={`Remove ${card.name}`}
                    onClick={() => remove(deck.cards.lastIndexOf(card.id))}
                  >
                    <Icon src={bin} size={16} />
                  </button>
                )}
              </div>
            )
          })}
        </div>
      </div>

      <aside className={open ? 'deck-panel open' : 'deck-panel'}>
        <button type="button" className="sheet-toggle" aria-expanded={open} onClick={() => setOpen(!open)}>
          <span className="handle" />
          <span className="sheet-summary">
            {deck.energy.map((type) => (
              <img key={type} className="energy" src={energyIcon(type)} alt={capitalise(type)} />
            ))}
            <span className="sheet-name">{deck.name}</span>
            <b>
              {deck.cards.length} / {DECK_SIZE}
            </b>
            <Icon src={up} size={20} />
          </span>
        </button>

        <label className="deck-title">
          <input
            aria-label="Deck name"
            maxLength={22}
            value={deck.name}
            onChange={(event) => change({ name: event.target.value })}
          />
          <span className="name-count">{deck.name.length} / 22</span>
          <Icon src={edit} size={22} />
        </label>

        <div className="deck-count">
          <span>Cards</span>
          <b>
            {deck.cards.length} / {DECK_SIZE}
          </b>
        </div>
        <div className="progress">
          <div style={{ width: `${(deck.cards.length / DECK_SIZE) * 100}%` }} />
        </div>
        {problems.length > 0 && (
          <ul className="deck-problems">
            {problems.map((problem) => (
              <li key={problem}>{problem}</li>
            ))}
          </ul>
        )}

        <div className="deck-slots">
          {Array.from({ length: DECK_SIZE }, (_, i) => {
            const card = deck.cards[i]
            if (!card) return <div key={i} className="slot empty" />
            return (
              <div key={i} className="slot">
                <button type="button" className="remove" aria-label={`Remove ${names.get(card) ?? card}`} onClick={() => remove(i)}>
                  <img src={`${CARD_BASE}/${card}.webp`} alt="" />
                </button>
                <button
                  type="button"
                  className="star"
                  aria-label={`Show ${names.get(card) ?? card} on the deck's cover`}
                  aria-pressed={focus.includes(card)}
                  onClick={() => star(card)}
                >
                  ★
                </button>
              </div>
            )
          })}
        </div>
        <div className="deck-strip">
          {deck.cards.slice(0, STRIP).map((card, i) => (
            <img key={i} src={`${CARD_BASE}/${card}.webp`} alt="" />
          ))}
          {deck.cards.length > STRIP && <span>+{deck.cards.length - STRIP}</span>}
        </div>

        <div className="deck-energy">
          Deck energy
          {deck.energy.map((type) => (
            <img key={type} className="energy" src={energyIcon(type)} alt={capitalise(type)} title={capitalise(type)} />
          ))}
          <button
            type="button"
            className="add-energy"
            aria-label="Choose deck energy"
            data-opens="energy"
            onClick={() => setPicking('energy')}
          >
            <Icon src={plus} size={14} />
          </button>
        </div>

        <div className="cosmetics">
          <div className="cosmetics-label">Cosmetics</div>
          <div className="pickers">
            {(Object.keys(COSMETICS) as Cosmetic[]).map((cosmetic) => (
              <button
                key={cosmetic}
                type="button"
                className={`picker of-${cosmetic}`}
                data-opens={cosmetic}
                onClick={() => setPicking(cosmetic)}
              >
                <span className="preview">
                  <img src={`${COSMETICS[cosmetic].base}/${deck[cosmetic]}`} alt="" />
                </span>
                <span className="picker-label">{capitalise(cosmetic)}</span>
                <span className="picker-value">
                  <span>{cosmeticName(deck[cosmetic])}</span>
                  <Icon src={down} size={12} />
                </span>
              </button>
            ))}
          </div>
        </div>

        {error && <p className="editor-error">{error}</p>}
        <div className="editor-actions">
          <button type="button" className="secondary" onClick={onClose}>
            Cancel
          </button>
          <button type="button" className="primary" disabled={unnamed} onClick={save}>
            Save deck
          </button>
        </div>
      </aside>

      {picking === 'energy' ? (
        <Picker
          kind="energy"
          title="Choose deck energy"
          detail={`Up to ${MAX_ENERGY} types for this deck's Energy Zone`}
          options={ENERGY.map((type) => ({ value: type, label: capitalise(type), image: energyIcon(type) }))}
          selected={deck.energy}
          onPick={toggleEnergy}
          onClose={() => setPicking(null)}
        />
      ) : (
        picking && (
          <CosmeticPicker
            key={picking}
            cosmetic={picking}
            value={deck[picking]}
            onPick={(file) => change({ [picking]: file })}
            onClose={() => setPicking(null)}
          />
        )
      )}
    </Shell>
  )
}

/** A filter that looks like the design's dropdown and opens the native one: the select lies invisibly over it. */
function Dropdown({
  label,
  value,
  options,
  onChange,
}: {
  label: string
  value: string
  options: string[]
  onChange: (value: string) => void
}) {
  return (
    <label className="dropdown">
      <span>
        {label}: {capitalise(value)}
      </span>
      <Icon src={chevron} size={20} />
      <select aria-label={label} value={value} onChange={(event) => onChange(event.target.value)}>
        {['All', ...new Set(options)].map((option) => (
          <option key={option} value={option}>
            {capitalise(option)}
          </option>
        ))}
      </select>
    </label>
  )
}

/** Every coin, sleeve or playmat in the asset bucket; until the list arrives, just the one the deck has. */
function CosmeticPicker({
  cosmetic,
  value,
  onPick,
  onClose,
}: {
  cosmetic: Cosmetic
  value: string
  onPick: (file: string) => void
  onClose: () => void
}) {
  const { base, title, detail } = COSMETICS[cosmetic]
  const [files, setFiles] = useState([value])
  useEffect(() => {
    // Coin_Tails.png is the back of every coin, not one to choose.
    listArt(base).then((names) => setFiles(names.filter((name) => name !== 'Coin_Tails.png')), () => {})
  }, [base])
  return (
    <Picker
      kind={cosmetic}
      title={title}
      detail={detail}
      options={files.map((file) => ({ value: file, label: cosmeticName(file), image: `${base}/${file}` }))}
      selected={[value]}
      onPick={onPick}
      onClose={onClose}
    />
  )
}

/**
 * The design's coin popup, shared by every choice in the deck panel. A pick applies at once; the close button,
 * Escape, a click outside and (on a phone) Done all just close it. A click outside on another picker's button
 * ({@code data-opens}) also opens that picker, so the player can go straight from one to the next.
 */
function Picker({
  kind,
  title,
  detail,
  options,
  selected,
  onPick,
  onClose,
}: {
  kind: string
  title: string
  detail: string
  options: { value: string; label: string; image: string }[]
  selected: string[]
  onPick: (value: string) => void
  onClose: () => void
}) {
  const dialog = useRef<HTMLDialogElement>(null)
  useEffect(() => {
    if (!dialog.current?.open) dialog.current?.showModal()
  }, [])
  /** Another picker's button under the pointer. The modal page is inert to hit-testing, so it is found by its box. */
  const openerAt = (x: number, y: number) =>
    [...document.querySelectorAll<HTMLElement>('[data-opens]')].find((button) => {
      const box = button.getBoundingClientRect()
      return button.dataset.opens !== kind && x >= box.left && x <= box.right && y >= box.top && y <= box.bottom
    })
  return (
    // The body fills the dialog, so a click that lands on the dialog itself is a click on its backdrop.
    // Escape is caught by onCancel, not onClose: the close event comes late enough to undo the next picker.
    <dialog
      ref={dialog}
      className={`picker-popup of-${kind}`}
      onCancel={onClose}
      onClick={(event) => {
        if (event.target !== event.currentTarget) return
        onClose()
        openerAt(event.clientX, event.clientY)?.click()
      }}
      // The backdrop shows a pointer over another picker's button, as the page would.
      onMouseMove={(event) =>
        event.currentTarget.toggleAttribute(
          'data-over-opener',
          event.target === event.currentTarget && !!openerAt(event.clientX, event.clientY),
        )
      }
    >
      <div className="popup-body">
        <div className="popup-head">
          <div>
            <div className="popup-title">{title}</div>
            <div className="muted">{detail}</div>
          </div>
          <button type="button" className="close" aria-label="Close" onClick={onClose}>
            <Icon src={close} size={16} />
          </button>
        </div>
        <div className="popup-grid">
          {options.map((option) => (
            <button
              key={option.value}
              type="button"
              aria-pressed={selected.includes(option.value)}
              onClick={() => onPick(option.value)}
            >
              <span className="option-art">
                <img src={option.image} alt="" loading="lazy" />
                <span className="check">
                  <Icon src={check} size={12} />
                </span>
              </span>
              {option.label}
            </button>
          ))}
        </div>
        <button type="button" className="primary done" onClick={onClose}>
          Done
        </button>
      </div>
    </dialog>
  )
}
