import { useState, type CSSProperties, type ReactNode } from 'react'
import bot from './assets/home/bot.svg'
import chevronDown from './assets/home/chevron-down.svg'
import chevronRight from './assets/home/chevron-right.svg'
import decksIcon from './assets/home/decks.svg'
import friends from './assets/home/friends.svg'
import homeIcon from './assets/home/home.svg'
import ladder from './assets/home/ladder.svg'
import profile from './assets/home/profile.svg'
import replays from './assets/home/replays.svg'
import tournaments from './assets/home/tournaments.svg'
import users from './assets/home/users.svg'
import { ICON_BASE } from './art'

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
export function Shell({ page, onNavigate, children }: { page: Page; onNavigate: (page: Page) => void; children: ReactNode }) {
  const go = (label: string) => (label === 'Home' || label === 'Decks') && onNavigate(label)
  return (
    <div className="home">
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

      <div className={page === 'Decks' ? 'content decks' : 'content'}>{children}</div>

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

/** The signed-in home screen. Only Queue does anything yet: it starts a game, once a deck is chosen. */
export function Home({
  name,
  profileIcon,
  onQueue,
  onNavigate,
}: {
  name: string
  profileIcon: string
  onQueue: () => void
  onNavigate: (page: Page) => void
}) {
  // ponytail: always null until there is deck building to choose from, so every way to start a game stays disabled.
  const [deck] = useState<{ name: string; cards: number; type: string } | null>(null)
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
        </div>
      </header>

      <div className="columns">
        <div className="column play-column">
          <div className="panel play">
            <h2>Play</h2>
            <div className="label">Deck</div>
            <button type="button" className="deck-select">
              <span className="deck-art" />
              <span className="deck-text">
                <span className="deck-name">{deck ? deck.name : 'No deck selected'}</span>
                <span className="muted">{deck ? `${deck.cards} cards · ${deck.type}` : 'Choose a deck to queue'}</span>
              </span>
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
              <button type="button" className="secondary" disabled={!deck}>
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
              <button type="button" className="link">
                Manage
              </button>
            </div>
            <p className="empty-state">No decks yet</p>
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
