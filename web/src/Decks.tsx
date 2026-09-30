import { useEffect, useState } from 'react'
import edit from './assets/decks/edit.svg'
import more from './assets/decks/more.svg'
import plus from './assets/decks/plus.svg'
import sleeve from './assets/decks/sleeve.svg'
import { CARD_BASE, COIN_BASE, EMBLEM_BASE, ENERGY_BASE, ICON_BASE } from './art'
import { Icon, Shell, type Page } from './Home'

/** Mirrors the server's {@code Decks.Summary}: no card list, so the page stays light. Energy is {@code Type} names. */
type DeckSummary = {
  id: number
  name: string
  cardCount: number
  energy: string[]
  focusCard1: string | null
  focusCard2: string | null
  coin: string
  sleeve: string
  playmat: string
}

const DECK_SIZE = 20

/**
 * The signed-in player's saved decks, from /api/decks. New, Edit and the menu do nothing until there is a deck editor.
 * ponytail: every deck shows the design's placeholder sleeve and playmat until those assets exist.
 */
export function Decks({
  name,
  profileIcon,
  emblems,
  onNavigate,
}: {
  name: string
  profileIcon: string
  emblems: string[]
  onNavigate: (page: Page) => void
}) {
  const [decks, setDecks] = useState<DeckSummary[] | null>(null)
  useEffect(() => {
    fetch('/api/decks')
      .then((response) => (response.ok ? response.json() : []))
      .then(setDecks, () => setDecks([]))
  }, [])

  return (
    <Shell page="Decks" onNavigate={onNavigate}>
      <header className="top-bar decks-head">
        <div className="title">
          <h1>Decks</h1>
          <div className="muted">{decks && `${decks.length} ${decks.length === 1 ? 'deck' : 'decks'}`}</div>
        </div>
        <button type="button" className="new-deck" aria-label="New deck">
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
            <DeckTile key={deck.id} deck={deck} />
          ))}
          <button type="button" className="deck-tile new-tile">
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

function DeckTile({ deck }: { deck: DeckSummary }) {
  const incomplete = deck.cardCount < DECK_SIZE && <span className="badge">Incomplete</span>
  return (
    <div className="deck-tile">
      <div className="cover">
        <span className="playmat" />
        <img className="sleeve" src={sleeve} alt="" />
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
        <button type="button" className="edit-square" aria-label="Edit deck">
          <Icon src={edit} size={20} />
        </button>
      </div>
      <div className="deck-actions">
        <button type="button" className="secondary">
          <Icon src={edit} size={20} />
          Edit deck
        </button>
        <button type="button" className="secondary" aria-label="More">
          <Icon src={more} size={22} />
        </button>
      </div>
    </div>
  )
}

/** A focus card's art, or a grey placeholder when the deck has none chosen. */
function CoverCard({ id, className }: { id: string | null; className: string }) {
  return id ? (
    <img className={`cover-card ${className}`} src={`${CARD_BASE}/${id}.webp`} alt="" />
  ) : (
    <span className={`cover-card placeholder ${className}`} />
  )
}
