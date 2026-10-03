import { useEffect, useRef, useState, type CSSProperties, type ReactNode } from 'react'
import bot from './assets/home/bot.svg'
import chevronDown from './assets/home/chevron-down.svg'
import chevronRight from './assets/home/chevron-right.svg'
import edit from './assets/decks/edit.svg'
import plus from './assets/decks/plus.svg'
import decksIcon from './assets/home/decks.svg'
import friends from './assets/home/friends.svg'
import homeIcon from './assets/home/home.svg'
import ladder from './assets/home/ladder.svg'
import profile from './assets/home/profile.svg'
import replays from './assets/home/replays.svg'
import tournaments from './assets/home/tournaments.svg'
import users from './assets/home/users.svg'
import { CARD_BASE, EMBLEM_BASE, ENERGY_BASE, ICON_BASE } from './art'
import { DECK_SIZE, type DeckSummary } from './Decks'

export type Page = 'Home' | 'Decks'

const nav = [
  { label: 'Home', icon: homeIcon },
  { label: 'Decks', icon: decksIcon },
  { label: 'Replays', icon: replays },
  { label: 'Profile', icon: profile },
]

const modes = [
  { label: 'Leaderboard', detail: 'View the current rankings', icon: ladder },
  { label: 'Tournaments', detail: 'Join scheduled bracket events', icon: tournaments },
  { label: 'Friends', detail: "See who's online and send challenges", icon: friends },
]

/** A single-colour Figma icon, drawn in the current text colour so it follows the light/dark tokens. */
export function Icon({ src, size }: { src: string; size: number }) {
  return <span className="icon" style={{ '--icon': `url("${src}")`, width: size, height: size } as CSSProperties} />
}

/** The sidebar (the tab bar on a phone) around a page. Only Home and Decks go anywhere yet. */
export function Shell({
  page,
  onNavigate,
  className = page === 'Decks' ? 'content decks' : 'content',
  children,
}: {
  page: Page
  onNavigate: (page: Page) => void
  className?: string
  children: ReactNode
}) {
  const go = (label: string) => (label === 'Home' || label === 'Decks') && onNavigate(label)
  // A removed <img> keeps downloading and holds up the next page's art, so leaving a page drops what it was loading.
  // isConnected skips StrictMode's rehearsal unmount, which would blank a page that is still on screen.
  const root = useRef<HTMLDivElement>(null)
  useEffect(() => {
    const page = root.current!
    return () => {
      if (!page.isConnected) for (const img of page.querySelectorAll('img')) img.src = ''
    }
  }, [])
  return (
    <div className="home" ref={root}>
      <nav className="sidebar">
        <div className="logo">PB</div>
        {nav.map((item) => (
          <button
            key={item.label}
            type="button"
            className={item.label === page ? 'nav-item current' : 'nav-item'}
            onClick={() => go(item.label)}
          >
            <span className="nav-icon">
              <Icon src={item.icon} size={28} />
            </span>
            {item.label}
          </button>
        ))}
      </nav>

      <div className={className}>{children}</div>

      <nav className="tab-bar">
        {nav.map((item) => (
          <button
            key={item.label}
            type="button"
            className={item.label === page ? 'nav-item current' : 'nav-item'}
            onClick={() => go(item.label)}
          >
            <span className="nav-icon">
              <Icon src={item.icon} size={24} />
            </span>
            {item.label}
          </button>
        ))}
      </nav>
    </div>
  )
}

/**
 * The signed-in home screen. Queue starts a game once a deck is chosen; Practice vs bot starts one at once, since
 * games still use fixed decks.
 */
export function Home({
  name,
  profileIcon,
  emblems,
  onQueue,
  onPractice,
  onNavigate,
  onEdit,
}: {
  name: string
  profileIcon: string
  emblems: string[]
  onQueue: () => void
  onPractice: () => void
  onNavigate: (page: Page) => void
  onEdit: (id: number | 'new') => void
}) {
  // The server only lets a legal deck be selected, so having one is enough to queue.
  const [decks, setDecks] = useState<DeckSummary[]>([])
  const load = () =>
    fetch('/api/decks')
      .then((response) => (response.ok ? response.json() : []))
      .then(setDecks)
      .catch(() => {})
  useEffect(() => {
    load()
  }, [])

  function select(d: DeckSummary) {
    if (d.selected) return
    fetch(`/api/decks/${d.id}/selected`, { method: 'PUT' })
      .then((response) => (response.ok ? load() : Promise.reject()))
      .catch(() => alert("Couldn't select the deck"))
  }
  const deck = decks.find((d) => d.selected) ?? null
  const [format, setFormat] = useState<'Standard' | 'Ranked'>('Standard')

  return (
    <Shell page="Home" onNavigate={onNavigate}>
      <header className="top-bar">
        <div className="logo">PB</div>
        <h1>TCG Pocket Battler</h1>
        <div className="player">
          <img className="avatar" src={`${ICON_BASE}/${profileIcon}`} alt="" />
          <div>
            <div className="welcome">Welcome back</div>
            <div className="player-name">
              {name}
              <span className="level-inline"> · Unranked</span>
            </div>
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

      <div className="columns">
        <div className="column play-column">
          <div className="panel play">
            <h2>Play</h2>
            <div className="label">Deck</div>
            <button type="button" className="deck-select" onClick={() => onNavigate('Decks')}>
              {deck ? <DeckLine deck={deck} /> : <DeckLine name="No deck selected" detail="Choose a deck to queue" />}
              <Icon src={chevronDown} size={20} />
            </button>
            <div className="label">Format</div>
            <div className="segmented">
              {(['Standard', 'Ranked'] as const).map((option) => (
                <button
                  key={option}
                  type="button"
                  aria-pressed={format === option}
                  onClick={() => setFormat(option)}
                >
                  {option}
                </button>
              ))}
            </div>
            <button type="button" className="queue" disabled={!deck} onClick={onQueue}>
              Queue
            </button>
            <div className="or">or</div>
            <div className="other-modes">
              <button type="button" className="secondary" disabled={!deck}>
                <Icon src={users} size={18} />
                Play a friend
              </button>
              <button type="button" className="secondary" onClick={onPractice}>
                <Icon src={bot} size={18} />
                <span className="desktop-only">Practice vs bot</span>
                <span className="mobile-only">Vs bot</span>
              </button>
            </div>
            <div className="join-room">
              <input placeholder="Room code" aria-label="Room code" disabled={!deck} />
              <button type="button" className="secondary" disabled={!deck}>
                Join
              </button>
            </div>
          </div>

          <div className="panel your-decks">
            <div className="panel-head">
              <h2>Your decks</h2>
              <button type="button" className="link" onClick={() => onNavigate('Decks')}>
                Manage
              </button>
            </div>
            {/* ponytail: the first three only; Manage shows the rest. */}
            <div className="deck-tiles">
              {decks.slice(0, 3).map((d) => (
                <div key={d.id} className={d.selected ? 'mini-deck selected' : 'mini-deck'}>
                  {/* Only a legal deck can be selected; an incomplete one says why on hover. */}
                  <button
                    type="button"
                    className="pick"
                    disabled={d.problems.length > 0}
                    title={d.problems.map((problem) => `• ${problem}`).join('\n') || undefined}
                    onClick={() => select(d)}
                  >
                    {d.focusCard1 && <img className="art" src={`${CARD_BASE}/${d.focusCard1}.webp`} alt="" />}
                    <span className="caption">
                      <span className="deck-name">{d.name}</span>
                      <span className="meta">
                        {d.energy.map((type) => (
                          <img key={type} className="energy" src={`${ENERGY_BASE}/${type.toLowerCase()}.png`} alt={type} title={type} />
                        ))}
                        {d.cardCount}/{DECK_SIZE}
                      </span>
                    </span>
                  </button>
                  <button type="button" className="edit-deck" aria-label="Edit deck" onClick={() => onEdit(d.id)}>
                    <Icon src={edit} size={16} />
                  </button>
                </div>
              ))}
              {decks.length < 3 && (
                <button type="button" className="add-deck" onClick={() => onEdit('new')}>
                  <Icon src={plus} size={20} />
                  New deck
                </button>
              )}
            </div>
          </div>
        </div>

        <div className="column matches-column">
          <div className="panel recent">
            <div className="panel-head">
              <h2>Recent matches</h2>
              <button type="button" className="link">
                View all
              </button>
            </div>
            <p className="empty-state">No matches yet</p>
          </div>

          <div className="modes">
            {modes.map((mode) => (
              <button key={mode.label} type="button" className="panel mode">
                <span className="mode-icon">
                  <Icon src={mode.icon} size={20} />
                </span>
                <span className="mode-text">
                  <span className="mode-label">{mode.label}</span>
                  <span className="muted">{mode.detail}</span>
                </span>
                <Icon src={chevronRight} size={16} />
              </button>
            ))}
          </div>
        </div>

        <div className="panel news">
          <div className="panel-head">
            <h2>News</h2>
            <button type="button" className="link">
              All posts
            </button>
          </div>
          <p className="empty-state">No news yet</p>
        </div>
      </div>
    </Shell>
  )
}

/** A deck's cover card, name, energy and size, or a placeholder name and detail when there is no deck. */
function DeckLine({ deck, name, detail }: { deck?: DeckSummary; name?: string; detail?: string }) {
  return (
    <>
      {deck?.focusCard1 ? (
        <img className="deck-art" src={`${CARD_BASE}/${deck.focusCard1}.webp`} alt="" />
      ) : (
        <span className="deck-art" />
      )}
      <span className="deck-text">
        <span className="deck-name">{deck ? deck.name : name}</span>
        {deck ? (
          <span className="deck-meta">
            {deck.energy.map((type) => (
              <img key={type} className="energy" src={`${ENERGY_BASE}/${type.toLowerCase()}.png`} alt={type} title={type} />
            ))}
            {deck.cardCount}/{DECK_SIZE} cards
          </span>
        ) : (
          <span className="muted">{detail}</span>
        )}
      </span>
    </>
  )
}
