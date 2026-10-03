import { useEffect, useRef, useState } from 'react'
import type { Answer, BoardView, DecisionMessage, ServerMessage } from './protocol'

/** One line of the battle log. `turn` lines are the dividers; `point` lines are knockouts. */
export interface LogEntry {
  kind: 'turn' | 'you' | 'point' | 'info'
  text: string
}

/**
 * One connection to /play (or /play?bot, which starts at once against a bot): the latest board, the open
 * question if any, a way to answer it, and a log of what this page saw happen.
 */
export function useGame(bot = false) {
  const socket = useRef<WebSocket | null>(null)
  const [connection, setConnection] = useState<'connecting' | 'waiting' | 'playing' | 'closed'>('connecting')
  const [board, setBoard] = useState<BoardView | null>(null)
  const [decision, setDecision] = useState<DecisionMessage | null>(null)
  const [result, setResult] = useState<string | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [log, setLog] = useState<LogEntry[]>([])

  useEffect(() => {
    // Deferred so StrictMode's mount-unmount-mount in dev cancels the timer instead of opening a throwaway
    // socket; the server would pair that socket with a real one, then abandon the game when it closed.
    let ws: WebSocket | undefined
    let last: BoardView | null = null
    const timer = setTimeout(connect, 0)
    return () => {
      clearTimeout(timer)
      ws?.close()
    }

    // ponytail: the server sends no game log, so this only sees turns, points and your own moves; log engine
    // events server-side to show what the opponent did.
    function track(next: BoardView) {
      const entries: LogEntry[] = []
      // A bot's whole turn can pass between two boards, so fill in the turns skipped; turns alternate.
      if (next.you.active)
        for (let turn = last?.you.active ? last.turn + 1 : next.turn; turn <= next.turn; turn++) {
          const yours = next.yourTurn === ((next.turn - turn) % 2 === 0)
          entries.push({ kind: 'turn', text: `Turn ${turn} · ${yours ? 'You' : 'Opponent'}` })
        }
      if (last && next.you.points > last.you.points) entries.push({ kind: 'point', text: 'You took a point.' })
      if (last && next.opponent.points > last.opponent.points)
        entries.push({ kind: 'point', text: 'Opponent took a point.' })
      if (entries.length) setLog((log) => [...log, ...entries])
      last = next
    }

    function connect() {
      ws = new WebSocket(`${location.protocol === 'https:' ? 'wss' : 'ws'}://${location.host}/play${bot ? '?bot' : ''}`)
      socket.current = ws

      ws.onmessage = (event) => {
        const message = JSON.parse(event.data) as ServerMessage
        switch (message.type) {
          case 'waiting':
            setConnection('waiting')
            break
          case 'state':
            setConnection('playing')
            setBoard(message.board)
            setDecision(null)
            track(message.board)
            break
          case 'decision':
            setDecision(message)
            setError(null)
            break
          case 'over':
            setBoard(message.board)
            setDecision(null)
            setResult(message.result)
            track(message.board)
            setLog((log) => [...log, { kind: 'info', text: `Game over: ${message.result}.` }])
            break
          case 'error':
            setError(message.message)
            break
        }
      }
      ws.onclose = () => setConnection('closed')
    }
  }, [bot])

  function choose(option: number) {
    if (!decision) return
    const answer: Answer = { decision: decision.id, option }
    socket.current?.send(JSON.stringify(answer))
    setLog((log) => [...log, { kind: 'you', text: decision.options[option].label }])
    setDecision(null)
  }

  return { connection, board, decision, result, error, log, choose }
}
