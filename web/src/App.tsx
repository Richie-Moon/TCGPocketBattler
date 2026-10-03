import { useEffect, useState } from 'react'
import { Battle } from './Battle'
import { DeckEditor } from './DeckEditor'
import { Decks } from './Decks'
import { Home, type Page } from './Home'
import { Landing } from './Landing'

/**
 * Home, then the game once they queue, only for a signed-in user; the landing page otherwise, including when the
 * server has no sign-in (no db profile). `?demo` skips all of that and plays a bot, signed in or not.
 */
function App() {
  const [me, setMe] = useState<{ name: string; elo: number; profileIcon: string; emblems: string[] } | 'signed-out' | null>(null)
  const [playing, setPlaying] = useState<'queue' | 'bot' | null>(
    new URLSearchParams(location.search).has('demo') ? 'bot' : null,
  )
  const [page, setPage] = useState<Page>('Home')
  const [editing, setEditing] = useState<number | 'new' | null>(null)
  useEffect(() => {
    fetch('/api/me')
      .then((response) => (response.ok ? response.json() : 'signed-out'))
      .then(setMe, () => setMe('signed-out'))
  }, [])
  if (playing) {
    const leave = () => {
      history.replaceState(null, '', location.pathname)
      setPlaying(null)
    }
    return <Battle bot={playing === 'bot'} me={me === 'signed-out' ? null : me} onLeave={leave} />
  }
  if (me === null) return null
  if (me === 'signed-out') return <Landing />
  // The sidebar leaves the editor too, dropping its unsaved changes, as Cancel does.
  const navigate = (to: Page) => {
    setEditing(null)
    setPage(to)
  }
  if (page === 'Decks' && editing !== null)
    return <DeckEditor id={editing} onNavigate={navigate} onClose={() => setEditing(null)} />
  return page === 'Decks' ? (
    <Decks name={me.name} profileIcon={me.profileIcon} emblems={me.emblems} onNavigate={navigate} onEdit={setEditing} />
  ) : (
    <Home
      name={me.name}
      profileIcon={me.profileIcon}
      emblems={me.emblems}
      onQueue={() => setPlaying('queue')}
      onPractice={() => setPlaying('bot')}
      onNavigate={navigate}
      onEdit={(id) => {
        setPage('Decks')
        setEditing(id)
      }}
    />
  )
}

export default App
