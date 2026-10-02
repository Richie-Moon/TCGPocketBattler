import { useEffect, useRef, useState } from 'react'
import edit from './assets/decks/edit.svg'
import more from './assets/decks/more.svg'
import plus from './assets/decks/plus.svg'
import close from './assets/editor/close.svg'
import { CARD_BASE, COIN_BASE, EMBLEM_BASE, ENERGY_BASE, ICON_BASE, PLAYMAT_BASE, SLEEVE_BASE } from './art'
import { Icon, Shell, type Page } from './Home'

/**
 * Mirrors the server's {@code Decks.Summary}: no card list, so the page stays light. {@code legal} is
 * {@code DeckValidator}'s verdict, which the page could not reach without the cards. Energy is {@code Type} names.
 */
type DeckSummary = {
  id: number
  name: string
  cardCount: number
  legal: boolean
  energy: string[]
  focusCard1: string | null
  focusCard2: string | null
  coin: string
  sleeve: string
  playmat: string
}

export const DECK_SIZE = 20

/** An empty list when signed out or offline, so the page still offers New deck. */
const loadDecks = (): Promise<DeckSummary[]> =>
  fetch('/api/decks')
    .then((response) => (response.ok ? response.json() : []))
    .catch(() => [])

const getJson = (url: string) => fetch(url).then((response) => (response.ok ? response.json() : Promise.reject()))

/**
 * The QR code Pokémon TCG Pocket scans to import the deck, as an image URL. The code holds Pocket's own card ids,
 * which tcgp-deck-qr looks up by set and collector number in the community card database it fetches from GitHub.
 * Rejects when a request fails or a card is not in that database. Loaded on demand: only sharing needs the library.
 */
async function deckQr(id: number): Promise<string> {
  const { loadCardDatabase, resolveDeck, encodePayload, createQrSvg } = await import('tcgp-deck-qr')
  const [database, deck, catalogue] = await Promise.all([
    loadCardDatabase(),
    getJson(`/api/decks/${id}`) as Promise<{ cards: string[]; energy: string[] }>,
    getJson('/api/cards') as Promise<{ id: string; kind: string }[]>,
  ])
  const pokemon = new Set(catalogue.filter((card) => card.kind === 'Pokémon').map((card) => card.id))
  // "A1-094" is A1 number 94; our promo set "P-A" is the database's "PROMO-A".
  const entry = (card: string) => {
    const cut = card.lastIndexOf('-')
    return { set: card.slice(0, cut).replace(/^P-/, 'PROMO-'), number: Number(card.slice(cut + 1)) }
  }
  const resolved = resolveDeck(
    {
      pokemon: deck.cards.filter((card) => pokemon.has(card)).map(entry),
      trainers: deck.cards.filter((card) => !pokemon.has(card)).map(entry),
      energy: deck.energy,
    },
    database,
  )
  return `data:image/svg+xml,${encodeURIComponent(createQrSvg(encodePayload(resolved)))}`
}

/** The signed-in player's saved decks, from /api/decks. New and Edit open the deck editor; More shares, duplicates or deletes. */
export function Decks({
  name,
  profileIcon,
  emblems,
  onNavigate,
  onEdit,
}: {
  name: string
  profileIcon: string
  emblems: string[]
  onNavigate: (page: Page) => void
  onEdit: (deck: number | 'new') => void
}) {
  const [decks, setDecks] = useState<DeckSummary[] | null>(null)
  useEffect(() => {
    loadDecks().then(setDecks)
  }, [])

  return (
    <Shell page="Decks" onNavigate={onNavigate}>
      <header className="top-bar decks-head">
        <div className="title">
          <h1>Decks</h1>
          <div className="muted">{decks && `${decks.length} ${decks.length === 1 ? 'deck' : 'decks'}`}</div>
        </div>
        <button type="button" className="new-deck" aria-label="New deck" onClick={() => onEdit('new')}>
          <Icon src={plus} size={20} />
          <span>New deck</span>
        </button>
        <div className="player">
          <img className="avatar" src={`${ICON_BASE}/${profileIcon}`} alt="" />
          <div>
            <div className="player-name">{name}</div>
            <div className="level">Unranked</div>
          </div>
          {emblems.length > 0 && (
            <div className="emblems">
              {emblems.slice(0, 3).map((emblem, i) => (
                <img key={i} src={`${EMBLEM_BASE}/${emblem}`} alt="" />
              ))}
            </div>
          )}
        </div>
      </header>

      {decks && (
        <div className="deck-grid">
          {decks.map((deck) => (
            <DeckTile
              key={deck.id}
              deck={deck}
              onEdit={() => onEdit(deck.id)}
              onChanged={() => loadDecks().then(setDecks)}
            />
          ))}
          <button type="button" className="deck-tile new-tile" onClick={() => onEdit('new')}>
            <span className="plus-box">
              <Icon src={plus} size={32} />
            </span>
            Create a new deck
          </button>
        </div>
      )}
    </Shell>
  )
}

function DeckTile({ deck, onEdit, onChanged }: { deck: DeckSummary; onEdit: () => void; onChanged: () => void }) {
  const incomplete = deck.cardCount < DECK_SIZE && <span className="badge">Incomplete</span>
  const menu = `deck-menu-${deck.id}`
  const [sharing, setSharing] = useState(false)
  const done = (response: Response) => (response.ok ? onChanged() : Promise.reject())

  /** The summary has no card list, so the copy is made from the full deck. The name is cut to stay within the server's 100. */
  function duplicate() {
    getJson(`/api/decks/${deck.id}`)
      .then(({ id: _id, ...draft }) =>
        fetch('/api/decks', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ ...draft, name: `${draft.name.slice(0, 95)} copy` }),
        }),
      )
      .then(done)
      .catch(() => alert("Couldn't duplicate the deck"))
  }

  function remove() {
    if (!confirm(`Delete "${deck.name}"? This can't be undone.`)) return
    fetch(`/api/decks/${deck.id}`, { method: 'DELETE' })
      .then(done)
      .catch(() => alert("Couldn't delete the deck"))
  }

  return (
    <div className="deck-tile">
      <div className="cover">
        <img className="playmat" src={`${PLAYMAT_BASE}/${deck.playmat}`} alt="" />
        <img className="sleeve" src={`${SLEEVE_BASE}/${deck.sleeve}`} alt="" />
        <CoverCard id={deck.focusCard2} className="left" />
        <CoverCard id={deck.focusCard1} className="front" />
        {incomplete}
        <img className="coin" src={`${COIN_BASE}/${deck.coin}`} alt="" />
      </div>
      <div className="deck-info">
        <div className="deck-body">
          <div className="tile-name">{deck.name}</div>
          <div className="deck-meta">
            {deck.energy.map((type) => (
              <img key={type} className="energy" src={`${ENERGY_BASE}/${type.toLowerCase()}.png`} alt={type} title={type} />
            ))}
            {deck.cardCount}/{DECK_SIZE} cards
          </div>
          {incomplete}
        </div>
        <button type="button" className="edit-square" aria-label="Edit deck" onClick={onEdit}>
          <Icon src={edit} size={20} />
        </button>
      </div>
      <div className="deck-actions">
        <button type="button" className="secondary" onClick={onEdit}>
          <Icon src={edit} size={20} />
          Edit deck
        </button>
        <button type="button" className="secondary" aria-label="More" popoverTarget={menu}>
          <Icon src={more} size={22} />
        </button>
        {/* A native popover: Escape and a click outside close it, and each item closes it as it acts. */}
        <div id={menu} popover="auto" className="deck-menu">
          <button
            type="button"
            popoverTarget={menu}
            popoverTargetAction="hide"
            disabled={!deck.legal}
            title={deck.legal ? undefined : `Needs ${DECK_SIZE} cards, a Basic Pokémon and 1 to 3 energy types`}
            onClick={() => setSharing(true)}
          >
            Share
          </button>
          <button type="button" popoverTarget={menu} popoverTargetAction="hide" onClick={duplicate}>
            Duplicate
          </button>
          <button type="button" className="danger" popoverTarget={menu} popoverTargetAction="hide" onClick={remove}>
            Delete
          </button>
        </div>
      </div>
      {sharing && <ShareDialog deck={deck} onClose={() => setSharing(false)} />}
    </div>
  )
}

/** The deck's QR code, made as the dialog opens. The close button, Escape and a click outside close it. */
function ShareDialog({ deck, onClose }: { deck: DeckSummary; onClose: () => void }) {
  const dialog = useRef<HTMLDialogElement>(null)
  const [qr, setQr] = useState('')
  const [failed, setFailed] = useState(false)
  useEffect(() => {
    if (!dialog.current?.open) dialog.current?.showModal()
    deckQr(deck.id).then(setQr, () => setFailed(true))
  }, [deck.id])
  return (
    // The body fills the dialog, so a click that lands on the dialog itself is a click on its backdrop.
    <dialog
      ref={dialog}
      className="picker-popup share-popup"
      onClose={onClose}
      onClick={(event) => event.target === event.currentTarget && onClose()}
    >
      <div className="popup-body">
        <div className="popup-head">
          <div>
            <div className="popup-title">Share {deck.name}</div>
            <div className="muted">Scan in Pokémon TCG Pocket to import this deck</div>
          </div>
          <button type="button" className="close" aria-label="Close" onClick={onClose}>
            <Icon src={close} size={16} />
          </button>
        </div>
        {qr ? (
          <img className="qr" src={qr} alt={`QR code for ${deck.name}`} />
        ) : (
          <div className="qr muted" role="status">
            {failed ? "Couldn't make the QR code" : 'Making the QR code…'}
          </div>
        )}
      </div>
    </dialog>
  )
}

/** A focus card's art, or nothing when the deck has none chosen. */
function CoverCard({ id, className }: { id: string | null; className: string }) {
  return id && <img className={`cover-card ${className}`} src={`${CARD_BASE}/${id}.webp`} alt="" />
}
